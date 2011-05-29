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
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import eu.wisebed.testbed.api.wsn.WSNServiceHelper;
import eu.wisebed.testbed.api.wsn.v22.SessionManagement;
import eu.wisebed.wiseui.shared.dto.SecretReservationKey;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import de.uniluebeck.itm.wisebed.cmdlineclient.jobs.AsyncJobObserver;

import eu.wisebed.wiseui.api.ExperimentationService;
import eu.wisebed.wiseui.server.WiseUiGuiceModule;
import eu.wisebed.wiseui.server.controller.ExperimentController;
import eu.wisebed.wiseui.shared.exception.ExperimentationException;
import static eu.wisebed.wiseui.server.util.URLUtil.getPort;
import static eu.wisebed.wiseui.server.util.URLUtil.getRandomURLSuffix;

@Singleton
public class ExperimentationServiceImpl extends RemoteServiceServlet
implements ExperimentationService {

	private static final long serialVersionUID = -6301493806193636782L;
	private static final Logger LOGGER = 
		LoggerFactory.getLogger(ExperimentationServiceImpl.class.getName());
	private List<ExperimentController> experimentControllers;
	private AsyncJobObserver jobs;
	private final Mapper mapper;
	private Injector injector;
	private SessionManagement sessionManagmentService;


	@Inject
	ExperimentationServiceImpl(final Mapper mapper,final AsyncJobObserver jobs, 
			final List<ExperimentController> experimentControllers){

		this.mapper = mapper;
		this.jobs = new AsyncJobObserver(1, TimeUnit.MINUTES);
		this.experimentControllers = experimentControllers;
		this.injector = Guice.createInjector(new WiseUiGuiceModule());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startExperimentController(
			final String sessionManagementUrl,
			final List<SecretReservationKey> secretReservationKeys)
	throws ExperimentationException {

		// format local end point url
		SecretReservationKey key = secretReservationKeys.get(0);
		String localEndpointUrl = null;
		try{
			localEndpointUrl = "http://" +
			InetAddress.getLocalHost().getCanonicalHostName() +
			":" + getPort() + "/controller"
			+ getRandomURLSuffix(key.getSecretReservationKey());
		} catch (UnknownHostException cause) {
			LOGGER.error(cause.getMessage(),cause);
			throw new ExperimentationException("Could not publish local " +
					"controller on" + localEndpointUrl);
		}

		// Map local transport objects to remote objects
		List<eu.wisebed.testbed.api.rs.v1.SecretReservationKey> rsSecretReservationKeys
		= new ArrayList<eu.wisebed.testbed.api.rs.v1.SecretReservationKey>(
				Lists.transform(secretReservationKeys,
						new Function<SecretReservationKey, eu.wisebed.testbed.api.rs.v1.SecretReservationKey>() {
					@Override
					public eu.wisebed.testbed.api.rs.v1.SecretReservationKey apply(
							final SecretReservationKey s) {
						return mapper.map(s, eu.wisebed.testbed.api.rs.v1.SecretReservationKey.class);
					}
				}));


		// setup experiment controller
		ExperimentController controller = injector.getInstance(ExperimentController.class);
		controller.setEndPointURL(localEndpointUrl);
		controller.setSecretReservationKeys(rsSecretReservationKeys);
		sessionManagmentService =
			WSNServiceHelper.getSessionManagementService(sessionManagementUrl); 
		controller.setSessionManagement(sessionManagmentService);

		// publish controller
		try{
			controller.publish();
		} catch (MalformedURLException cause) {
			LOGGER.error(cause.getMessage(), cause);
			throw new ExperimentationException(
					"Could not public local controller on "
					+ controller.getEndPointURL() + " (" + cause.getMessage() + ")");

		}
		LOGGER.info("Local controller published on url: " + controller.getEndPointURL());		

		// start session management
		controller.startSessionManagement();

		// add controller to the list of controllers
		experimentControllers.add(controller);

	}

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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stopExperimentController(final List<SecretReservationKey> secretReservationKeys)
	throws ExperimentationException {


		// Map local transport objects to remote objects
		List<eu.wisebed.testbed.api.rs.v1.SecretReservationKey> rsSecretReservationKeys
		= new ArrayList<eu.wisebed.testbed.api.rs.v1.SecretReservationKey>(
				Lists.transform(secretReservationKeys,
						new Function<SecretReservationKey, eu.wisebed.testbed.api.rs.v1.SecretReservationKey>() {
					@Override
					public eu.wisebed.testbed.api.rs.v1.SecretReservationKey apply(
							final SecretReservationKey s) {
						return mapper.map(s, eu.wisebed.testbed.api.rs.v1.SecretReservationKey.class);
					}
				}));

		// find experiment controller from secret reservation key
		ExperimentController controller 
		=	 findExperimentControllerBySecretReservationKey(rsSecretReservationKeys);

		// if controller not found
		if(controller == null){
			throw new ExperimentationException("Unexpected. Controller not " +
			"properly set on the server");
		}

		// try to free session management
		controller.freeSessionManagement();
		
		LOGGER.info("Terminating controller with Secret reservation key = " 
				+ secretReservationKeys.get(0).getSecretReservationKey());

		// remove the selected controller 
		experimentControllers.remove(controller);
	}
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
	
	/**
	 * Finds an experiment controller by iterating for it's secret reservation key in the controllers list.
	 * @param
	 * @return
	 */
	private ExperimentController findExperimentControllerBySecretReservationKey(
			final List<eu.wisebed.testbed.api.rs.v1.SecretReservationKey> secretReservationKeys){

		// iterate and compare secret reservation keys
		for(ExperimentController controller : experimentControllers) {
			String searchingKey = secretReservationKeys.get(0).getSecretReservationKey() ;
			String controllerKey = controller.getSecretReservationKeys().get(0).getSecretReservationKey();
			if(searchingKey.equals(controllerKey)){
				return controller;
			}
		}

		// if not found return null
		return null;
	}
}
