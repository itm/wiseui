/**
 * Copyright (C) 2011 Universität zu Lübeck, Institut für Telematik (ITM),
 *                             Research Academic Computer Technology Institute (RACTI)
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

import com.google.inject.Inject;
import de.uniluebeck.itm.tr.util.StringUtils;
import eu.wisebed.api.controller.Controller;
import eu.wisebed.api.rs.SecretReservationKey;
import eu.wisebed.api.sm.ExperimentNotRunningException_Exception;
import eu.wisebed.api.sm.SessionManagement;
import eu.wisebed.api.sm.UnknownReservationIdException_Exception;
import eu.wisebed.api.wsn.WSN;
import eu.wisebed.testbed.api.wsn.Constants;
import eu.wisebed.testbed.api.wsn.WSNServiceHelper;
import eu.wisebed.wiseml.controller.WiseMLController;
import eu.wisebed.wiseml.model.WiseML;
import eu.wisebed.wiseml.model.setup.Node;
import eu.wisebed.wiseml.model.setup.Setup;
import eu.wisebed.wiseml.model.trace.Trace;
import eu.wisebed.wiseui.shared.dto.Message;
import eu.wisebed.wiseui.shared.dto.RequestStatus;
import eu.wisebed.wiseui.shared.dto.Status;
import eu.wisebed.wiseui.shared.exception.ExperimentationException;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executors;

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
    private Mapper mapper;
	private Queue<Message> messageQueue;
	private Queue<RequestStatus> requestStatusQueue;
	private Queue<String> notificationQueue;
	private String localEndpointUrl;
	private WSN wsn;
	private WiseML wiseml;
	private Setup wisemlSetup;
	private Trace wisemlTrace;
	private WiseMLController wisemlController;
	private List<SecretReservationKey> secretReservationKeys;
	private SessionManagement sessionManagement;
	private List<String> nodeUrns;
	
	@Inject
	public ExperimentController(
			final Mapper mapper,
			final WiseML wiseml,
			final Setup wisemlSetup,
			final Trace wisemlTrace,
			final WiseMLController wisemlController,
			final Queue<RequestStatus> requestStatusQueue,
			final Queue<Message> messageQueue,
			final Queue<String> notificationQueue) {
		this.setMapper(mapper);
		this.setWiseml(wiseml);
		this.setWisemlSetup(wisemlSetup);
		this.setWisemlTrace(wisemlTrace);
		this.setWisemlController(wisemlController);
		this.setMessageQueue(messageQueue);
		this.setNodeUrns(new ArrayList<String>());		
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

	@SuppressWarnings("unchecked")
	@Override
	public void receive(List<eu.wisebed.api.common.Message> msgs) {
		for(eu.wisebed.api.common.Message msg : msgs) {
			
			// set experiment message
			final String source = msg.getSourceNodeId();
			final String timeStamp = msg.getTimestamp().toXMLFormat();
			final String data = StringUtils.toHexString(msg.getBinaryData());
			final String level = msg.getBinaryData()[1] == 0x00 ? "DEBUG" : "FATAL";
			LOGGER.info("Received Message :");
			LOGGER.info("[" +source+"][" +timeStamp+"][" + level +"][" + data +"]");

						
			// map message and put it in queue
			Message message = mapper.map(msg, Message.class);
			message.setTimestamp(
					msg.getTimestamp().toGregorianCalendar().getTime());
			messageQueue.add(message);
						
			// set Wiseml message
			eu.wisebed.wiseml.model.trace.Message traceMessage = new 
				eu.wisebed.wiseml.model.trace.Message();
			traceMessage.setTimestamp((long)msg.getTimestamp().getMillisecond());
			traceMessage.setData("[" + level +"][" + data +"]");
			traceMessage.setId(source);
			
			// add it to Wiseml trace
			wisemlTrace.getChildren().add(message);
			
			LOGGER.info("Added to WiseML trace");
		}
	}

	@Override
	public void receiveNotification(List<String> notifications) {
		for(String notification : notifications) {
			LOGGER.info("[Notification][" + notification +"]");
			notificationQueue.add(notification);
		}
	}

	@Override
	public void receiveStatus(List<eu.wisebed.api.controller.RequestStatus> requestStati) {
		for(eu.wisebed.api.controller.RequestStatus requestStatus : requestStati) {

			RequestStatus reqStatusDto = mapper.map(requestStatus, RequestStatus.class);
			
			for(int i=0;i<requestStatus.getStatus().size();i++){
				eu.wisebed.api.controller.Status status =
					requestStatus.getStatus().get(i);
				final String requestID = requestStatus.getRequestId();
				final String msg = status.getMsg();
				final String nodeID = status.getNodeId();
				final String value = status.getValue().toString();
				
				LOGGER.info("[RequestStatus][" + requestID + "]" +
						"[status msg][" + msg + "]" + 
						"[node][" + nodeID + "]" + 
						"[value][" + value + "]");
			
				reqStatusDto.getStatus().add(mapper.map(status, Status.class)); 
			}
			requestStatusQueue.add(reqStatusDto);
		}
	}
	
	public String getWiseMlSerialiazed() {

		// prepare wiseml setup
		List<Node> nodeList = new ArrayList<Node>();
		for(String nodeUrn : nodeUrns) {
			Node node = new Node();
			node.setId(nodeUrn);
			nodeList.add(node);
		}
		wisemlSetup.setNodes(nodeList);
		wiseml.setSetup(wisemlSetup);
		LOGGER.info("WiseML Setup is now prepared");
		
		// prepare wiseml trace
		wiseml.setTrace(wisemlTrace);

		// return serialiased wiseml
		return wisemlController.writeWiseMLAsSTring(wiseml);
	}
	
	public Mapper getMapper() {
		return mapper;
	}

	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
	}
	
	public Queue<Message> getMessageQueue() {
		return messageQueue;
	}
	
	public void setMessageQueue(Queue<Message> messageQueue) {
		this.messageQueue = messageQueue;
	}

	public Queue<RequestStatus> getRequestStatusQueue() {
		return requestStatusQueue;
	}

	public void setRequestStatusQueue(Queue<RequestStatus> requestStatusQueue) {
		this.requestStatusQueue = requestStatusQueue;
	}

	public Queue<String> getNotificationQueue() {
		return notificationQueue;
	}

	public void setNotificationQueue(Queue<String> notificationQueue) {
		this.notificationQueue = notificationQueue;
	}
	
	public List<String> getNodeUrns() {
		return nodeUrns;
	}

	public void setNodeUrns(List<String> nodeUrns) {
		this.nodeUrns = nodeUrns;
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


	public void setWisemlTrace(Trace wisemlTrace) {
		this.wisemlTrace = wisemlTrace;
	}

	public Trace getWisemlTrace() {
		return wisemlTrace;
	}

	public void setWisemlSetup(Setup wisemlSetup) {
		this.wisemlSetup = wisemlSetup;
	}
	
	public Setup getWisemlSetup() {
		return wisemlSetup;
	}

	public void setWiseml(WiseML wiseml) {
		this.wiseml = wiseml;
	}

	public WiseML getWiseml() {
		return wiseml;
	}
	
	public String getLocalEndpointUrl() {
		return localEndpointUrl;
	}

	public void setLocalEndpointUrl(String localEndpointUrl) {
		this.localEndpointUrl = localEndpointUrl;
	}

	public WiseMLController getWisemlController() {
		return wisemlController;
	}

	public void setWisemlController(WiseMLController wisemlController) {
		this.wisemlController = wisemlController;
	}
}

