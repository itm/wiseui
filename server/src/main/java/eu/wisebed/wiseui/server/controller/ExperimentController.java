package eu.wisebed.wiseui.server.controller;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executors;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

//import de.uniluebeck.itm.wisebed.cmdlineclient.jobs.AsyncJobObserver;

import eu.wisebed.testbed.api.rs.v1.SecretReservationKey;
import eu.wisebed.testbed.api.wsn.Constants;
import eu.wisebed.testbed.api.wsn.WSNServiceHelper;
import eu.wisebed.testbed.api.wsn.v211.Controller;
import eu.wisebed.testbed.api.wsn.v211.ExperimentNotRunningException_Exception;
import eu.wisebed.testbed.api.wsn.v211.Message;
import eu.wisebed.testbed.api.wsn.v211.RequestStatus;
import eu.wisebed.testbed.api.wsn.v211.UnknownReservationIdException_Exception;
import eu.wisebed.testbed.api.wsn.v211.WSN;
import eu.wisebed.testbed.api.wsn.v211.SessionManagement;
import eu.wisebed.wiseui.server.util.APIKeysUtil;
import eu.wisebed.wiseui.server.util.URLUtil;
import eu.wisebed.wiseui.shared.ExperimentMessage;
import eu.wisebed.wiseui.shared.exception.ExperimentationException;

@WebService(serviceName = "ControllerService", 
		targetNamespace = Constants.NAMESPACE_CONTROLLER_SERVICE, 
		portName = "ControllerPort", 
		endpointInterface = Constants.ENDPOINT_INTERFACE_CONTROLLER_SERVICE)
public class ExperimentController implements Controller {
	
	private final Logger LOGGER 
		= Logger.getLogger(ExperimentController.class.getName());
	private Queue<ExperimentMessage> undeliveredMessagesQueue;
	private String endPointURL;
	private int reservationID;
	private WSN wsn;
	//private AsyncJobObserver jobs;	 TODO a dependency to be added
	private List<SecretReservationKey> keys;
	private SessionManagement sessionManagement;
	
	public ExperimentController(final String endPointURL,
			final int reservationID,final SessionManagement sessionManagement,
			final Queue<ExperimentMessage> undeliveredMessagesQueue,
			final List<SecretReservationKey> keys){
		this.undeliveredMessagesQueue = undeliveredMessagesQueue;
		this.endPointURL = endPointURL;
		this.reservationID = reservationID;
		this.sessionManagement = sessionManagement;
		this.keys = keys;
	}
	
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
	public void publish(final String endpointUrl) throws MalformedURLException {
		String bindAllInterfacesUrl = 
			URLUtil.convertHostToZeros(endpointUrl);

		LOGGER.log(Level.INFO,"Experiment controller (#" + reservationID +")");
		LOGGER.log(Level.INFO,"Endpoint URL: " + endpointUrl);
		LOGGER.log(Level.INFO,"Binding  URL: " + bindAllInterfacesUrl);

		Endpoint endpoint = Endpoint.publish(bindAllInterfacesUrl, this);
		endpoint.setExecutor(Executors.newCachedThreadPool());

		LOGGER.log(Level.INFO,"Succesfully binded experiment controller (#" + 
				reservationID +")" + " at " + bindAllInterfacesUrl);
	}
	
	@Override
	public void receive(Message msg) {
		// TODO implement after resolving asyncontrollerobserver issure
		
	}

	@Override
	public void receiveStatus(RequestStatus status) {
		// TODO implement after resolving asyncontrollerobserver issure
		
	}
}

