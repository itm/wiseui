package eu.wisebed.wiseui.server.controller;

import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executors;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;

import eu.wisebed.wiseui.shared.dto.ExperimentMessage;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

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
import eu.wisebed.wiseui.server.util.APIKeysUtil;
import eu.wisebed.wiseui.server.util.URLUtil;
import eu.wisebed.wiseui.shared.exception.ExperimentationException;

@WebService(serviceName = "ControllerService", 
		targetNamespace = Constants.NAMESPACE_CONTROLLER_SERVICE, 
		portName = "ControllerPort", 
		endpointInterface = Constants.ENDPOINT_INTERFACE_CONTROLLER_SERVICE)
public class ExperimentController implements Controller {
	
	private final Logger LOGGER 
		= Logger.getLogger(ExperimentController.class.getName());
	private String endPointURL;
	private int reservationID;
	private WSN wsn;
	private List<SecretReservationKey> keys;
	private SessionManagement sessionManagement = null;
	private final Queue<ExperimentMessage> undelivered = new LinkedList<ExperimentMessage>();
	
	public ExperimentController(){}
	
	/**
	 * Setups and initiates session management for this controller.
	 * @throws ExpException, Generic exception related with exceptions found 
	 * in Experiment monitoring
	 */
	public final void startSessionManagement() throws ExperimentationException{
		LOGGER.log(Level.DEBUG,"Getting an WSN instance ... ");
		
		// check session management if  exists here
		if(sessionManagement == null){
			throw new ExperimentationException("Unintialized session management");
		}
		
		// get controller instance from session management
		String wsnEndpointURL=null;
		try {
			wsnEndpointURL = sessionManagement.getInstance(
					APIKeysUtil.copyRsToWsn(keys),endPointURL);
		} catch (ExperimentNotRunningException_Exception e) {
			LOGGER.log(Level.FATAL, e);
			throw new ExperimentationException("Experiment not running on " 
					+ endPointURL);
		} catch (UnknownReservationIdException_Exception e) {
			LOGGER.log(Level.FATAL, e);
			throw new ExperimentationException("Unknown reservation ID");
		}
		
		// if all went well set
		wsn = WSNServiceHelper.getWSNService(wsnEndpointURL);
		
		LOGGER.log(Level.DEBUG,"Got an WSN instance URL, endpoint is: " + 
				wsnEndpointURL);
	}

	/**
	 * Stops & frees session management for this controller.
	 * @throws ExpException, Generic exception related with exceptions found 
	 * in Experiment monitoring
	 */
	public void freeSessionManagement() throws ExperimentationException{
		
		LOGGER.log(Level.INFO,"Freeing session management on ("
				+ endPointURL +")");
		// check session management if  exists here
		if(sessionManagement == null){
			throw new ExperimentationException("Unintialized session management");
		}
		
		// trying to free session management
		try {
			sessionManagement.free(APIKeysUtil.copyRsToWsn(keys));
		} catch (ExperimentNotRunningException_Exception e) {
			LOGGER.log(Level.FATAL, e);
			throw new ExperimentationException("Experiment is not running");
		} catch (UnknownReservationIdException_Exception e) {
			LOGGER.log(Level.FATAL, e);
			throw new ExperimentationException("Unknown reservation ID provided"); 
		}
		LOGGER.log(Level.INFO,"Session management on ("
				+ endPointURL +") is now free");
	}

	/**
	 * Publishes this controller on a specific URL.
	 * @param endpointUrl, the URL for the controller to be published.
	 * @throws MalformedURLException
	 */
	public void publish() throws MalformedURLException {
		
		if(endPointURL.isEmpty() || endPointURL == null)
			throw new MalformedURLException("Empty or null URL string");
		
		String bindAllInterfacesUrl = 
			URLUtil.convertHostToZeros(endPointURL);

		LOGGER.log(Level.INFO,"Experiment controller (#" + reservationID +")");
		LOGGER.log(Level.INFO,"Endpoint URL: " + endPointURL);
		LOGGER.log(Level.INFO,"Binding  URL: " + bindAllInterfacesUrl);

		Endpoint endpoint = Endpoint.publish(bindAllInterfacesUrl, this);
		endpoint.setExecutor(Executors.newCachedThreadPool());

		LOGGER.log(Level.INFO,"Succesfully binded experiment controller (#" + 
				reservationID +")" + " at " + bindAllInterfacesUrl);
	}
	
	@Override
	public void experimentEnded() {
		LOGGER.log(Level.INFO, "Experiment ended");
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
			
			undelivered.add(experimentMessage);
		}
	}

	@Override
	public void receiveNotification(List<String> notifications) {
		for(String notification : notifications) {
			
			ExperimentMessage experimentMessage = new ExperimentMessage();
			experimentMessage.setupAsNotification(notification);
			
			LOGGER.log(Level.INFO,"Received notification : " + notification);
			
			undelivered.add(experimentMessage);
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
				
				LOGGER.log(Level.INFO,"Received requestStatus id " 
						+ requestID);
				LOGGER.log(Level.INFO,"status msg : " + msg);
				LOGGER.log(Level.INFO,"node ID : " + nodeID);
				LOGGER.log(Level.INFO,"value : " + value);
				
				undelivered.add(experimentMessage);
			}
		}
	}

	public String getEndPointURL() {
		return endPointURL;
	}

	public void setEndPointURL(String endPointURL) {
		this.endPointURL = endPointURL;
	}

	public int getReservationID() {
		return reservationID;
	}

	public void setReservationID(int reservationID) {
		this.reservationID = reservationID;
	}

	public WSN getWsn() {
		return wsn;
	}

	public void setWsn(WSN wsn) {
		this.wsn = wsn;
	}

	public List<SecretReservationKey> getKeys() {
		return keys;
	}

	public void setKeys(List<SecretReservationKey> keys) {
		this.keys = keys;
	}

	public SessionManagement getSessionManagement() {
		return sessionManagement;
	}

	public void setSessionManagement(final SessionManagement sessionManagement) {
		this.sessionManagement = sessionManagement;
	}

	public Queue<ExperimentMessage> getUndelivered() {
		return undelivered;
	}
}

