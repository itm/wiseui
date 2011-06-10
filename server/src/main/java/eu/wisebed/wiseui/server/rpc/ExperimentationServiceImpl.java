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
package eu.wisebed.wiseui.server.rpc;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import de.uniluebeck.itm.wisebed.cmdlineclient.jobs.AsyncJobObserver;
import de.uniluebeck.itm.wisebed.cmdlineclient.jobs.Job;
import eu.wisebed.api.sm.SessionManagement;
import eu.wisebed.api.wsn.Program;
import eu.wisebed.api.wsn.ProgramMetaData;
import eu.wisebed.testbed.api.wsn.WSNServiceHelper;
import eu.wisebed.wiseui.api.ExperimentationService;
import eu.wisebed.wiseui.api.PersistenceService;
import eu.wisebed.wiseui.server.WiseUiGuiceModule;
import eu.wisebed.wiseui.server.controller.ExperimentController;
import eu.wisebed.wiseui.shared.dto.BinaryImage;
import eu.wisebed.wiseui.shared.dto.Message;
import eu.wisebed.wiseui.shared.dto.SecretReservationKey;
import eu.wisebed.wiseui.shared.exception.ExperimentationException;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

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
    private final PersistenceService persistenceService;

    @Inject
    ExperimentationServiceImpl(final Mapper mapper,
                               final AsyncJobObserver jobs,
                               final List<ExperimentController> experimentControllers,
                               final PersistenceService persistenceService) {

        this.mapper = mapper;
        this.jobs = jobs;
        this.experimentControllers = experimentControllers;
        this.persistenceService = persistenceService;
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

        try {
            localEndpointUrl = "http://" +
                    InetAddress.getLocalHost().getCanonicalHostName() +
                    ":" + getPort() + "/controller"
                    + getRandomURLSuffix(key.getSecretReservationKey());
        } catch (UnknownHostException cause) {
            LOGGER.error(cause.getMessage(), cause);
            throw new ExperimentationException("Could not publish local " +
                    "controller on" + localEndpointUrl);
        }

        // Map local transport objects to remote objects
        List<eu.wisebed.api.rs.SecretReservationKey> rsSecretReservationKeys
                = new ArrayList<eu.wisebed.api.rs.SecretReservationKey>(
                Lists.transform(secretReservationKeys,
                        new Function<SecretReservationKey, eu.wisebed.api.rs.SecretReservationKey>() {
                            @Override
                            public eu.wisebed.api.rs.SecretReservationKey apply(
                                    final SecretReservationKey s) {
                                return mapper.map(s, eu.wisebed.api.rs.SecretReservationKey.class);
                            }
                        }));

        // setup experiment controller
        ExperimentController controller = injector.getInstance(ExperimentController.class);
        controller.setLocalEndpointUrl(localEndpointUrl);
        controller.setSecretReservationKeys(rsSecretReservationKeys);
        controller.setNodeUrns(nodeUrns);
        sessionManagmentService =
                WSNServiceHelper.getSessionManagementService(sessionManagementUrl);
        controller.setSessionManagement(sessionManagmentService);

        // publish controller
        try {
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
            final Integer imageId, final List<String> nodeUrns)
            throws ExperimentationException {

        // retrieve image
        BinaryImage image = persistenceService.loadBinaryImage(imageId);

        LOGGER.info("Flashing image : " + image.getId() + "." + image.getFileName());
        LOGGER.info("Flashing nodes " + nodeUrns.toString());
        LOGGER.info("For reservation with keys :" + secretReservationKeys.get(0).getSecretReservationKey());

        // make program indices list
        @SuppressWarnings("rawtypes")
        List programIndicesList = new ArrayList();
        for (int i = 0; i < nodeUrns.size(); i++) {
            programIndicesList.add(0);
        }

        // make program list
        List<Program> programList = new ArrayList<Program>();
        try {
            Program program = new Program();
            ProgramMetaData value = new ProgramMetaData();
            value.setName(image.getFileName());
            value.setOther("other"); // TODO these values ???
            value.setPlatform("platform");
            value.setVersion("1.0");
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
        try {
            ifNull(controller, "Unexpected. Controller not " +
            	"properly set on the server");
            ifNull(controller.getMessageQueue(), "Unexpected. Message queue not " +
            	"properly set on the controller.");
            ifNull(controller.getRequestStatusQueue(), "Unexpected. Message queue" +
            	" not properly set on the controller." );
            ifNull(controller.getNotificationQueue(), "Unexpected. Notification" +
            	" queue not properly set on the controller.");
        } catch (RuntimeException cause) {
            throw new ExperimentationException(cause.getMessage());

        }

        // submit job to job observer
        jobs.submit(new Job(
                "(flash nodes)(" + secretReservationKeys.get(0).getSecretReservationKey() + ")",
                controller.getWsn().flashPrograms(nodeUrns, programIndicesList, programList),
                nodeUrns, Job.JobType.flashPrograms));

        jobs.join();
    }
    
    /**
     * {@inheritDoc}
     */
	@Override
	public void resetExperimentNodes(
			List<SecretReservationKey> secretReservationKeys,
			List<String> nodeUrns) throws ExperimentationException {

		LOGGER.info("Reseting nodes : " + nodeUrns.toString());
		
		// get experiment controller
        ExperimentController controller = findExperimentControllerBySecretReservationKey(secretReservationKeys);
        // if controller not found or if it has not queue
        try {
            ifNull(controller, "Unexpected. Controller not " +
            	"properly set on the server");
            ifNull(controller.getMessageQueue(), "Unexpected. Message queue not " +
            	"properly set on the controller.");
            ifNull(controller.getRequestStatusQueue(), "Unexpected. Message queue" +
            	" not properly set on the controller." );
            ifNull(controller.getNotificationQueue(), "Unexpected. Notification" +
            	" queue not properly set on the controller.");
        } catch (RuntimeException cause) {
            throw new ExperimentationException(cause.getMessage());

        }
        
        jobs.submit(new Job(
                "(reset nodes)(" + secretReservationKeys.get(0).getSecretReservationKey() + ")",
                controller.getWsn().resetNodes(nodeUrns),
                nodeUrns,Job.JobType.resetNodes
        		));
        
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
                = findExperimentControllerBySecretReservationKey(secretReservationKeys);

        // if controller not found
        try {
            ifNull(controller, "Unexpected. Controller not " +
                    "properly set on the server");
        } catch (RuntimeException cause) {
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
    public Message returnExperimentMessage(
            List<SecretReservationKey> secretReservationKeys)
            throws ExperimentationException {

        // get experiment controller
        ExperimentController controller = findExperimentControllerBySecretReservationKey(secretReservationKeys);
        // if controller not found or if it has not queue
        try {
            ifNull(controller, "Unexpected. Controller not " +
            	"properly set on the server");
            ifNull(controller.getMessageQueue(), "Unexpected. Message queue not " +
            	"properly set on the controller.");
            ifNull(controller.getRequestStatusQueue(), "Unexpected. Message queue" +
            	" not properly set on the controller." );
            ifNull(controller.getNotificationQueue(), "Unexpected. Notification" +
            	" queue not properly set on the controller.");
        } catch (RuntimeException cause) {
            throw new ExperimentationException(cause.getMessage());
        }

        // setup an experiment message
        Message message = controller.getMessageQueue().poll();
        if (message != null) {
            String value = secretReservationKeys.get(0).getSecretReservationKey();
            message.setSecretRecretReservationKey(value);
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
        for (BinaryImage image : persistenceService.loadAllBinaryImages()) {
            // empty the content is not needed and it's a download overhead
            image.setContent(null);
            availableImages.add(image);
        }
        LOGGER.info("Persistence holds " + availableImages.size() + " images");
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
        try {
            ifNull(controller, "Unexpected. Controller not " +
            	"properly set on the server");
            ifNull(controller.getMessageQueue(), "Unexpected. Message queue not " +
      			"properly set on the controller.");
			ifNull(controller.getRequestStatusQueue(), "Unexpected. Message queue" +
				" not properly set on the controller." );
			ifNull(controller.getNotificationQueue(), "Unexpected. Notification" +
				" queue not properly set on the controller.");
        } catch (RuntimeException cause) {
            throw new ExperimentationException(cause.getMessage());

        }

        // get wise ml string
        String wisemlString;
        try {
            wisemlString = controller.getWiseMlSerialiazed();
        } catch (Exception cause) {
            LOGGER.error(cause.getMessage());
            throw new ExperimentationException(cause.getMessage());
        }
        LOGGER.info(wisemlString);

        return wisemlString;
    }

    /**
     * Finds an experiment controller by iterating for it's secret reservation key in the controllers list.
     *
     * @param rsSecretReservationKeys List of {@link eu.wisebed.wiseui.shared.dto.SecretReservationKey}
     * @return ExperimentController instance
     */
    private ExperimentController findExperimentControllerBySecretReservationKey(
            final List<SecretReservationKey> rsSecretReservationKeys) {

        ExperimentController experimentController = null;

        // iterate and compare secret reservation keys
        for (ExperimentController controller : experimentControllers) {
            String searchingKey = rsSecretReservationKeys.get(0).getSecretReservationKey();
            String controllerKey = controller.getSecretReservationKeys().get(0).getSecretReservationKey();
            if (searchingKey.equals(controllerKey)) {
                experimentController = controller;
            }
        }

        // if not found return null
        return experimentController;
    }
}
