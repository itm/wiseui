/*
 * Copyright 2011 Universität zu Lübeck, Institut für Telematik (ITM),
 *              Research Academic Computer Technology Institute (RACTI)
 *
 * ITM and RACTI license this file under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.wisebed.wiseui.server.rpc;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import eu.wisebed.wiseui.shared.dto.ExperimentMessage;
import eu.wisebed.wiseui.shared.dto.SecretReservationKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.uniluebeck.itm.wisebed.cmdlineclient.jobs.AsyncJobObserver;

import eu.wisebed.wiseui.api.ExperimentationService;
import eu.wisebed.wiseui.server.controller.ExperimentController;
import eu.wisebed.wiseui.server.util.URLUtil;
import eu.wisebed.wiseui.shared.exception.ExperimentationException;
import eu.wisebed.wiseui.shared.exception.ReservationException;

@Singleton
public class ExperimentationServiceImpl extends RemoteServiceServlet
	implements ExperimentationService {

	private static final long serialVersionUID = -6301493806193636782L;
	private static final Logger LOGGER = 
		LoggerFactory.getLogger(ExperimentationServiceImpl.class.getName());
	private List<ExperimentController> experimentControllers;
	private AsyncJobObserver jobs; 
//	private SessionManagement sessionManagement;


	@Inject
	ExperimentationServiceImpl(){
		experimentControllers = new ArrayList<ExperimentController>();
		jobs = new AsyncJobObserver(1, TimeUnit.MINUTES);
	}


	@Override
	public void startExperimentController(
			List<SecretReservationKey> secretReservationKeys)
			throws ReservationException, ExperimentationException {

		// format local end point url
		SecretReservationKey key = secretReservationKeys.get(0);
		String localEndpointUrl = null;
		try{
			localEndpointUrl = "http://" +
			InetAddress.getLocalHost().getCanonicalHostName() +
			":" + URLUtil.getPort() + "/controller"
			+ URLUtil.getRandomURLSuffix(key.getSecretReservationKey());
		} catch (UnknownHostException cause) {
			LOGGER.error(cause.getMessage(),cause);
			throw new ExperimentationException("Could not publish local " +
					"controller on" + localEndpointUrl);
		}
		
		// setup experiment controller
		ExperimentController controller = new ExperimentController();
		

	}
		
		
		////
////		// setup experiment controller
////		ExperimentController controller = new ExperimentController();
////		controller.setEndPointURL(endPointURL);
////		controller.setReservationID(reservationID);
////		List<SecretReservationKey> keys = new ArrayList<SecretReservationKey>();
////		keys.add(key);
////		controller.setKeys(keys);
////		List<String> urnPrefixList = new ArrayList<String>();
////		urnPrefixList.add(reservation.getUrnPrefix());
////		List<TestbedConfiguration> testbed =
////			TestbedConfigurationManager.fetchTestbedByUrn(urnPrefixList);
////		String sessionManagmentURL =
////			testbed.get(0).getSessionmanagementEndpointUrl();
////		sessionManagement =
////			WSNServiceHelper.getSessionManagementService(sessionManagmentURL);
////		controller.setSessionManagement(sessionManagement);
////
////		// publish controller
////		try{
////			controller.publish();
////		} catch (MalformedURLException e) {
////			LOGGER.log(Level.FATAL, e);
////			throw new ExperimentationException(
////					"Could not public local controller on "
////					+ controller.getEndPointURL() + " (" + e.getMessage() + ")");
////
////		}
////		LOGGER.log(Level.INFO,"Local controller published on url: " +
////				controller.getEndPointURL());
////
////		// start session management
////
////		// controller found
////		controller.startSessionManagement();
////
////		// add controller to the controllers list
////		experimentControllers.add(controller);
//	}
//	
//	/**
//	 *  This method loads an experiment image on the web services.
//	 *  @param <code>reservationID</code>, a reservation ID.
//	 */
//	@Override
//	public void flashExperimentImage(final int reservationID)
//			throws ReservationException, ExperimentationException {
//		
//		LOGGER.info("Flashing image for controller with id = " + reservationID);
//
////		// get reservation
////		ReservationDetails reservation =
////			ReservationServiceManager.fetchReservation(reservationID);
////
////		//	get image related file name from reservation
////		final String filename = reservation.getImageFileName();
////		final Image image =
////			ImageServiceManager.fetchImageByFilename(filename);
////
////		LOGGER.log(Level.INFO, "Image filename \"" + filename +
////				"\" for reservation (" + reservationID +")");
////
////		// Setup for flashing an image
////		// form a node list
////		List<String> nodeURNs = new ArrayList<String>();
////		for(SensorDetails sensor : reservation.getSensors()){
////			nodeURNs.add(sensor.getUrn());
////		}
////
////		LOGGER.log(Level.INFO, "Fetched " + nodeURNs.size() + " node URNs");
////		@SuppressWarnings("rawtypes")
////		List programIndices = new ArrayList();
////		for(int i= 0;i < nodeURNs.size();i++){
////			LOGGER.log(Level.INFO,"Node URN fetched :" + nodeURNs.get(i));
////			programIndices.add(0);
////		}
////
////		// setup image to flash
////		List<Program> programs = new ArrayList<Program>();
////               try {
////			programs.add(ImageUtil.readImage(image,
////			        "iSerial",
////			        "",
////			        "iSense",
////			        "1.0"
////			));
////		} catch (Exception e) {
////			LOGGER.log(Level.FATAL, e);
////			throw new ExperimentationException();
////		}
////
////		ExperimentController controller =
////			findExperimentControllerByID(reservationID);
////
////        jobs.submit(new Job(
////        		"flash nodes",
////                controller.getWsn().flashPrograms(
////                		nodeURNs, programIndices, programs),
////                nodeURNs,
////                Job.JobType.flashPrograms
////            ));
////        jobs.join();
//	}
//
//	@Override
//	public void terminateExperiment(final int reservationID)
//			throws ExperimentationException {
//		
//		LOGGER.info("Terminating controller with id = " + reservationID);
//
//		// find output controller
//		ExperimentController controller 
//			= findExperimentControllerByID(reservationID);
//		
//		if(controller == null){
//			throw new ExperimentationException("Unexpected. Controller not " +
//					"properly set on the server");
//		}
//
//		// try to free session management
//		controller.freeSessionManagement();
//
//		// remove the selected controller 
//		experimentControllers.remove(controller);		
//	}
//	
//	/**
//	 * Returns an undeliverd message back to the client
//	 * @param <code>reservationID</code>, a reservation ID for an experiment
//	 */
//	@Override
//	public ExperimentMessage getNextUndeliveredMessage(final int reservationID)
//		throws ExperimentationException {
//		
//		ExperimentController controller = findExperimentControllerByID(reservationID);
//		
//		if(controller == null){
//			throw new ExperimentationException("Unexpected. Controller not " +
//					"properly set on the server");
//		}
//		
//		if(controller.getUndelivered() == null){
//			throw new ExperimentationException("Unexpected. Message queue not " +
//						"properly set on the controller with id #" + reservationID);
//		}	
//		ExperimentMessage message = controller.getUndelivered().poll();
//		if(message != null)
//			message.setReservationID(reservationID);
//		
//		return message;
//	}
//	
//	/**
//	 * Finds an outputcontroller by looking for it's ID in the controllers list. 
//	 * @param <code>ID</code>, id of a controller.
//	 * @return an <code>OutputController</code> object 
//	 * or <code>null</code> if controller not found. 
//	 */
//	private ExperimentController findExperimentControllerByID(final int reservationID){
//		for(ExperimentController controller : experimentControllers){
//			if(reservationID == controller.getReservationID()){
//				return controller;
//			}
//		}
//		return null;
//	}
//
//	public void setExperimentControllers(
//			List<ExperimentController> experimentControllers) {
//		this.experimentControllers = experimentControllers;
//	}
//
//	public List<ExperimentController> getExperimentControllers() {
//		return experimentControllers;
//	}
}
