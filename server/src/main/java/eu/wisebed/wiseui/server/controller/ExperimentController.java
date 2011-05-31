/**
 * Copyright (C) 2011 Universität zu Lübeck, Institut für Telematik (ITM), Research Academic Computer Technology Institute (RACTI)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.wisebed.wiseui.server.controller;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executors;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;

import eu.wisebed.wiseui.shared.dto.ExperimentMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.uniluebeck.itm.tr.util.StringUtils;

import eu.wisebed.testbed.api.rs.v1.SecretReservationKey;
import eu.wisebed.testbed.api.wsn.Constants;
import eu.wisebed.testbed.api.wsn.WSNServiceHelper;
import eu.wisebed.testbed.api.wsn.v22.Controller;
import eu.wisebed.testbed.api.wsn.v22.ExperimentNotRunningException_Exception;
import eu.wisebed.testbed.api.wsn.v22.Message;
import eu.wisebed.testbed.api.wsn.v22.RequestStatus;
import eu.wisebed.testbed.api.wsn.v22.UnknownReservationIdException_Exception;
import eu.wisebed.testbed.api.wsn.v22.WSN;
import eu.wisebed.testbed.api.wsn.v22.SessionManagement;
import eu.wisebed.wiseui.shared.exception.ExperimentationException;

import static eu.wisebed.wiseui.server.util.APIKeysUtil.copyRsToWsn;
import static eu.wisebed.wiseui.server.util.URLUtil.convertHostToZeros;

import static eu.wisebed.wiseui.shared.common.Checks.ifNull;
import static eu.wisebed.wiseui.shared.common.Checks.ifNullOrEmpty;

@WebService(serviceName = "ControllerService", 
		targetNamespace = Constants.NAMESPACE_CONTROLLER_SERVICE, 
		portName = "ControllerPort", 
		endpointInterface = Constants.ENDPOINT_INTERFACE_CONTROLLER_SERVICE)
public class ExperimentController implements Controller {
	
	private final Logger LOGGER 
		= LoggerFactory.getLogger(ExperimentController.class.getName());
	private final Queue<ExperimentMessage> experimentMessagesQueue;
	private String localEndpointUrl;
	private WSN wsn;
	private List<SecretReservationKey> secretReservationKeys;
	private SessionManagement sessionManagement;
	
	@Inject
	public ExperimentController(final Queue<ExperimentMessage> experimentMessagesQueue){
		this.experimentMessagesQueue = experimentMessagesQueue;
	}
	
	/**
	 * Setups and initiates session management for this controller.
	 * @throws ExpException, Generic exception related with exceptions found 
	 * in Experiment monitoring
	 */
	public final void startSessionManagement() throws ExperimentationException{
		LOGGER.debug("Getting an WSN instance on "
				+ localEndpointUrl +" ...");
		
		// check session management if  exists here
		try{
			ifNull(sessionManagement,"Session management service is null");
		}catch(RuntimeException cause){
			throw new ExperimentationException(cause.getMessage());
		}
		
		// get controller instance from session management
		String wsnEndpointURL=null;
		try {
			wsnEndpointURL = sessionManagement.getInstance(
				copyRsToWsn(secretReservationKeys),localEndpointUrl);
		} catch (ExperimentNotRunningException_Exception cause) {
			LOGGER.error(cause.getMessage(),cause);
			throw new ExperimentationException("Experiment not running on " 
					+ localEndpointUrl);
		} catch (UnknownReservationIdException_Exception cause) {
			LOGGER.error(cause.getMessage(),cause);
			throw new ExperimentationException("Unknown reservation Id");
		}
		
		// if all went well set
		wsn = WSNServiceHelper.getWSNService(wsnEndpointURL);
		
		LOGGER.info("Got an WSN instance URL, endpoint is: " + wsnEndpointURL);
	}

	/**
	 * Stops & frees session management for this controller.
	 * @throws ExpException, Generic exception related with exceptions found 
	 * in Experiment monitoring
	 */
	public void freeSessionManagement() throws ExperimentationException{
		
		LOGGER.info("Freeing session management on "
				+ localEndpointUrl +" ...");
		
		// check session management if  exists here
		try{
			ifNull(sessionManagement,"Session management service is null");
		}catch(RuntimeException cause){
			throw new ExperimentationException(cause.getMessage());
		}
		
		// trying to free session management
		try {
			sessionManagement.free(copyRsToWsn(secretReservationKeys));
		} catch (ExperimentNotRunningException_Exception cause) {
			LOGGER.error(cause.getMessage(),cause);
			throw new ExperimentationException("Experiment is not running");
		} catch (UnknownReservationIdException_Exception cause) {
			LOGGER.error(cause.getMessage(),cause);
			throw new ExperimentationException("Unknown reservation ID provided"); 
		}
		
		LOGGER.info("Session management on ("+ localEndpointUrl +") is now free");
	}

	/**
	 * Publishes this controller on a specific URL.
	 * @param endpointUrl, the URL for the controller to be published.
	 * @throws MalformedURLException
	 */
	public void publish() throws MalformedURLException {
		
		try{
			ifNullOrEmpty(localEndpointUrl,"Empty or null URL string");
		}catch(RuntimeException cause){
			throw new MalformedURLException(cause.getMessage());
		}
					
		String bindAllInterfacesUrl = 
			convertHostToZeros(localEndpointUrl);
		String key = secretReservationKeys.get(0).getSecretReservationKey();
		LOGGER.info("Experiment controller (" + key +")");
		LOGGER.info("Endpoint URL: " + localEndpointUrl);
		LOGGER.info("Binding  URL: " + bindAllInterfacesUrl);

		Endpoint endpoint = Endpoint.publish(bindAllInterfacesUrl, this);
		endpoint.setExecutor(Executors.newCachedThreadPool());

		LOGGER.info("Succesfully binded experiment controller ("+ key + ")" + " at " + bindAllInterfacesUrl);
	}
	
	@Override
	public void experimentEnded() {
		LOGGER.info("Experiment ended");
	}

	@Override
	public void receive(List<Message> msgs) {
		for(Message msg : msgs) {
			final String source = msg.getSourceNodeId();
			final String timeStamp = msg.getTimestamp().toXMLFormat();
			final String data = StringUtils.toHexString(msg.getBinaryData());
			final String level = msg.getBinaryData()[1] == 0x00 ? "DEBUG" : "FATAL";
						
			ExperimentMessage experimentMessage = new ExperimentMessage();
			experimentMessage.setupAsMessage(source, level, data, timeStamp);
			
			experimentMessagesQueue.add(experimentMessage);
		}
	}

	@Override
	public void receiveNotification(List<String> notifications) {
		for(String notification : notifications) {
			
			ExperimentMessage experimentMessage = new ExperimentMessage();
			experimentMessage.setupAsNotification(notification);
			
			LOGGER.info("Received notification : " + notification);
			
			experimentMessagesQueue.add(experimentMessage);
		}
	}

	@Override
	public void receiveStatus(List<RequestStatus> requestStati) {
		for(RequestStatus requestStatus : requestStati) {
			final String requestID = requestStatus.getRequestId();
			
			for(int i=0;i<requestStatus.getStatus().size();i++){
				final String msg = requestStatus.getStatus().get(i).getMsg();
				final String nodeID = requestStatus.getStatus().get(i).getNodeId();
				final String value = requestStatus.getStatus().get(i).getValue().toString();
					
				ExperimentMessage experimentMessage = new ExperimentMessage();
				experimentMessage.setupAsRequestStatus(requestID, nodeID, msg, value);
				
				LOGGER.info("Received requestStatus id " 
						+ requestID);
				LOGGER.info("status msg : " + msg);
				LOGGER.info("node ID : " + nodeID);
				LOGGER.info("value : " + value);
				
				experimentMessagesQueue.add(experimentMessage);
			}
		}
	}

	public String getEndPointURL() {
		return localEndpointUrl;
	}

	public void setEndPointURL(final String localEndPointUrl) {
		this.localEndpointUrl = localEndPointUrl;
	}

	public WSN getWsn() {
		return wsn;
	}

	public void setWsn(WSN wsn) {
		this.wsn = wsn;
	}

	public List<SecretReservationKey> getSecretReservationKeys() {
		return secretReservationKeys;
	}

	public void setSecretReservationKeys(
			List<SecretReservationKey> secretReservationKeys) {
		this.secretReservationKeys = secretReservationKeys;
	}


	public SessionManagement getSessionManagement() {
		return sessionManagement;
	}

	public void setSessionManagement(final SessionManagement sessionManagement) {
		this.sessionManagement = sessionManagement;
	}

	public Queue<ExperimentMessage> getMessageQueue() {
		return experimentMessagesQueue;
	}
}

