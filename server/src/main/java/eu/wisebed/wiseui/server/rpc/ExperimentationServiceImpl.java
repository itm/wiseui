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
package eu.wisebed.wiseui.server.rpc;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import eu.wisebed.testbed.api.wsn.WSNServiceHelper;
import eu.wisebed.testbed.api.wsn.v22.Program;
import eu.wisebed.testbed.api.wsn.v22.ProgramMetaData;
import eu.wisebed.testbed.api.wsn.v22.SessionManagement;
import eu.wisebed.wiseui.persistence.PersistenceServiceProvider;
import eu.wisebed.wiseui.shared.dto.BinaryImage;
import eu.wisebed.wiseui.shared.dto.ExperimentMessage;
import eu.wisebed.wiseui.shared.dto.SecretReservationKey;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import de.uniluebeck.itm.wisebed.cmdlineclient.jobs.AsyncJobObserver;
import de.uniluebeck.itm.wisebed.cmdlineclient.jobs.Job;

import eu.wisebed.wiseui.api.ExperimentationService;
import eu.wisebed.wiseui.api.PersistenceService;
import eu.wisebed.wiseui.server.WiseUiGuiceModule;
import eu.wisebed.wiseui.server.controller.ExperimentController;
import eu.wisebed.wiseui.shared.exception.ExperimentationException;
import static eu.wisebed.wiseui.server.util.URLUtil.getPort;
import static eu.wisebed.wiseui.server.util.URLUtil.getRandomURLSuffix;
import static eu.wisebed.wiseui.shared.common.Checks.ifNull;

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
	private final PersistenceService persistenceService = PersistenceServiceProvider.newPersistenceService();



	@Inject
	ExperimentationServiceImpl(final Mapper mapper,final AsyncJobObserver jobs, 
			final List<ExperimentController> experimentControllers){

		this.mapper = mapper;
		this.jobs = jobs;
		this.experimentControllers = experimentControllers;
		this.injector = Guice.createInjector(new WiseUiGuiceModule());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startExperimentController(
			final String sessionManagementUrl,
			final List<SecretReservationKey> secretReservationKeys,
			final List<String> nodeUrns)
	throws ExperimentationException {

		// format local end point url
		SecretReservationKey key = secretReservationKeys.get(0);
		String localEndpointUrl = null;
		
		localEndpointUrl = "http://94.64.211.238:"+getPort()+"/controller" + getRandomURLSuffix(key.getSecretReservationKey());
		
//		try{
//			localEndpointUrl = "http://" +
//			InetAddress.getLocalHost().getCanonicalHostName() +
//			":" + getPort() + "/controller"
//			+ getRandomURLSuffix(key.getSecretReservationKey());
//		} catch (UnknownHostException cause) {
//			LOGGER.error(cause.getMessage(),cause);
//			throw new ExperimentationException("Could not publish local " +
//					"controller on" + localEndpointUrl);
//		}

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
		controller.setLocalEndpointUrl(localEndpointUrl);
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
					+ controller.getLocalEndpointUrl() + " (" + cause.getMessage() + ")");

		}
		LOGGER.info("Local controller published on url: " + controller.getLocalEndpointUrl());		

		// start session management
		controller.startSessionManagement();
		
		// format node list and add it to the controller setup
		controller.prepareWiseMlSetup(nodeUrns);
		
		// add controller to the list of controllers
		experimentControllers.add(controller);
	}

	@Override
	@SuppressWarnings("unchecked")
	/**
	 * {@inheritDoc}
	 */
	public void flashExperimentImage(
			final List<SecretReservationKey> secretReservationKeys,
			final Integer imageId,final List<String> nodeUrns)
			throws ExperimentationException {
		
		// retrieve image
		BinaryImage image = persistenceService.loadBinaryImage(imageId);
		
		LOGGER.info("Flashing image : " + image.getId() + "." + image.getFileName());
		LOGGER.info("Flashing nodes " + nodeUrns.toString());
		LOGGER.info("For reservation with keys :" + secretReservationKeys.get(0).getSecretReservationKey());
		
		// make program indices list
		@SuppressWarnings("rawtypes")
		List programIndicesList = new ArrayList();
		for(int i=0;i<nodeUrns.size();i++) {
			programIndicesList.add(0);
		}
		
		// make program list
		List<Program> programList = new ArrayList<Program>();
		try{
			Program program = new Program();
			ProgramMetaData value = new ProgramMetaData();
			value.setName(image.getFileName());
			value.setOther(""); // TODO these values ???
			value.setPlatform("");
			value.setVersion("");
			program.setMetaData(value);
			program.setProgram(image.getContent());
			programList.add(program);
		} catch (Exception cause) {
			LOGGER.error(cause.getMessage());
			throw new ExperimentationException(cause.getMessage());
		}
				
		// get experiment controller
		ExperimentController controller = findExperimentControllerBySecretReservationKey(secretReservationKeys);
		// if controller not found or if it has not queue
		try{
			ifNull(controller,"Unexpected. Controller not " +
			"properly set on the server");
			ifNull(controller.getMessageQueue(),"Unexpected. Message queue not " +
			"properly set on the controller");
		}catch(RuntimeException cause){
			throw new ExperimentationException(cause.getMessage());

		}
		
		// submit job to job observer
		jobs.submit(new Job(
				"(flash nodes)("+secretReservationKeys.get(0).getSecretReservationKey() + ")",
				controller.getWsn().flashPrograms(nodeUrns, programIndicesList, programList),
				nodeUrns,Job.JobType.flashPrograms));
		
		jobs.join();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stopExperimentController(final List<SecretReservationKey> secretReservationKeys)
	throws ExperimentationException {

		// find experiment controller from secret reservation key
		ExperimentController controller 
		=	 findExperimentControllerBySecretReservationKey(secretReservationKeys);

		// if controller not found
		try{
			ifNull(controller,"Unexpected. Controller not " +
			"properly set on the server");
		}catch(RuntimeException cause){
			throw new ExperimentationException(cause.getMessage());

		}

		// try to free session management
		controller.freeSessionManagement();

		LOGGER.info("Terminating controller with Secret reservation key = " 
				+ secretReservationKeys.get(0).getSecretReservationKey());

		// remove the selected controller 
		experimentControllers.remove(controller);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExperimentMessage returnExperimentMessage(
			List<SecretReservationKey> secretReservationKeys)
	throws ExperimentationException{

		// get experiment controller
		ExperimentController controller = findExperimentControllerBySecretReservationKey(secretReservationKeys);
		// if controller not found or if it has not queue
		try{
			ifNull(controller,"Unexpected. Controller not " +
				"properly set on the server");
			ifNull(controller.getMessageQueue(),"Unexpected. Message queue not " +
				"properly set on the controller");
		}catch(RuntimeException cause){
			throw new ExperimentationException(cause.getMessage());
		}

		// setup an experiment message
		ExperimentMessage message = controller.getMessageQueue().poll();
		if(message != null) {
			String value = secretReservationKeys.get(0).getSecretReservationKey();
			message.setSecretReservationKeyValue(value);
		}

		return message;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<BinaryImage> returnUploadedExperimentImages()
	throws ExperimentationException {
		
		List<BinaryImage> availableImages = new ArrayList<BinaryImage>();
		for(BinaryImage image : persistenceService.loadAllBinaryImages()) {
			// empty the content is not needed and it's a download overhead 
			image.setContent(null);
			availableImages.add(image);
		}
		LOGGER.info("Persistence holds "+ availableImages.size() + " images");
		return availableImages;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String returnExperimentWiseMLReport(
			List<SecretReservationKey> secretReservationKeys)
			throws ExperimentationException {
		
		// get experiment controller
		ExperimentController controller = findExperimentControllerBySecretReservationKey(secretReservationKeys);
		// if controller not found or if it has not queue
		try{
			ifNull(controller,"Unexpected. Controller not " +
			"properly set on the server");
			ifNull(controller.getMessageQueue(),"Unexpected. Message queue not " +
			"properly set on the controller");
		}catch(RuntimeException cause){
			throw new ExperimentationException(cause.getMessage());

		}
		
		// get wiseml string
		String wisemlString;
		try{
			wisemlString = controller.getWiseMLasString();
		}catch(Exception cause){
			LOGGER.error(cause.getMessage());
			throw new ExperimentationException(cause.getMessage());
		}
		LOGGER.info(wisemlString);
		
		return wisemlString;
	}

	/**
	 * Finds an experiment controller by iterating for it's secret reservation key in the controllers list.
	 * @param secretReservationKeys List of <code>SecretReservationKey</code>
	 * @return ExperimentController instance
	 */
	private ExperimentController findExperimentControllerBySecretReservationKey(
			final List<SecretReservationKey> secretReservationKeys){
		
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

		// iterate and compare secret reservation keys
		for(ExperimentController controller : experimentControllers) {
			String searchingKey = rsSecretReservationKeys.get(0).getSecretReservationKey() ;
			String controllerKey = controller.getSecretReservationKeys().get(0).getSecretReservationKey();
			if(searchingKey.equals(controllerKey)){
				return controller;
			}
		}

		// if not found return null
		return null;
	}
}
