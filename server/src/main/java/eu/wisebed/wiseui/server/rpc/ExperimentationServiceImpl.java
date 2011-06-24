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
import de.uniluebeck.itm.wisebed.cmdlineclient.jobs.JobResult;
import eu.wisebed.api.sm.SessionManagement;
import eu.wisebed.api.wsn.Program;
import eu.wisebed.api.wsn.ProgramMetaData;
import eu.wisebed.wiseui.api.ExperimentationService;
import eu.wisebed.wiseui.api.PersistenceService;
import eu.wisebed.wiseui.server.WiseUiGuiceModule;
import eu.wisebed.wiseui.server.controller.WiseUiControllerClient;
import eu.wisebed.wiseui.shared.dto.BinaryImage;
import eu.wisebed.wiseui.shared.dto.Message;
import eu.wisebed.wiseui.shared.dto.SecretReservationKey;
import eu.wisebed.wiseui.shared.exception.ExperimentationException;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Singleton
public class ExperimentationServiceImpl extends RemoteServiceServlet
        implements ExperimentationService {

    private static final Logger LOG = LoggerFactory.getLogger(ExperimentationServiceImpl.class);

    private List<WiseUiControllerClient> experimentControllers;

    private Mapper mapper;

    private SessionManagement sessionManagmentService;

    private PersistenceService persistenceService;

    private Injector injector = Guice.createInjector(new WiseUiGuiceModule());

    @Inject
    ExperimentationServiceImpl(final Mapper mapper,
                               final PersistenceService persistenceService) {

        this.mapper = mapper;
        this.experimentControllers = new ArrayList<WiseUiControllerClient>();
        this.persistenceService = persistenceService;
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

        // Map local transport objects to remote objects
        List<eu.wisebed.api.sm.SecretReservationKey> rsSecretReservationKeys
                = new ArrayList<eu.wisebed.api.sm.SecretReservationKey>(
                Lists.transform(secretReservationKeys,
                        new Function<SecretReservationKey, eu.wisebed.api.sm.SecretReservationKey>() {
                            @Override
                            public eu.wisebed.api.sm.SecretReservationKey apply(
                                    final SecretReservationKey s) {
                                return mapper.map(s, eu.wisebed.api.sm.SecretReservationKey.class);
                            }
                        }));

        // setup experiment controller
        final WiseUiControllerClient controller = injector.getInstance(WiseUiControllerClient.class);
        controller.setSecretReservationKeys(rsSecretReservationKeys);
        controller.setSessionManagementEndpoint(sessionManagementUrl);

        String portalServer = null;
        try {
            final URL sessionUrl = new URL(sessionManagementUrl);
            portalServer = sessionUrl.getHost();
        } catch (final MalformedURLException e) {
            LOG.error(e.getMessage(), e);
            throw new ExperimentationException("Could not setup Protobuf host!", e);
        }

        controller.setPortalServer(portalServer);
        controller.setupProtobufClient();

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

        LOG.info("Flashing image : " + image.getId() + "." + image.getFileName());
        LOG.info("Flashing nodes " + nodeUrns.toString());
        LOG.info("For reservation with keys :" + secretReservationKeys.get(0).getSecretReservationKey());

        // make program indices list
        @SuppressWarnings("rawtypes")
        List programIndices = new ArrayList();
        for (String nodeUrn : nodeUrns) {
            programIndices.add(0);
        }

        // make program list
        List<Program> programs = new ArrayList<Program>();
        try {
            final Program program = new Program();
            final ProgramMetaData value = new ProgramMetaData();
            value.setName(image.getFileName());
            value.setOther("other"); // TODO these values ???
            value.setPlatform("platform");
            value.setVersion("1.0");
            program.setMetaData(value);
            program.setProgram(image.getContent());
            programs.add(program);
        } catch (Exception cause) {
            LOG.error(cause.getMessage());
            throw new ExperimentationException(cause.getMessage());
        }

        // get experiment controller
        final WiseUiControllerClient controller = findExperimentControllerBySecretReservationKey(secretReservationKeys);

        Future flashFuture = controller.getWsn().flashPrograms(nodeUrns, programIndices, programs, 3, TimeUnit.MINUTES);
        JobResult flashJobResult = null;
        try {
            flashJobResult = (JobResult) flashFuture.get();
        } catch (final InterruptedException e) {
            LOG.error(e.getMessage(), e);
        } catch (final ExecutionException e) {
            LOG.error(e.getMessage(), e);
        }
        LOG.info("{}", flashJobResult);
        if (flashJobResult != null) {
            if (flashJobResult.getSuccessPercent() < 100) {
                throw new ExperimentationException("Not all nodes could be flashed.");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetExperimentNodes(
            List<SecretReservationKey> secretReservationKeys,
            List<String> nodeUrns) throws ExperimentationException {

        LOG.info("Reseting nodes : " + nodeUrns.toString());

        // get experiment controller
        final WiseUiControllerClient controller = findExperimentControllerBySecretReservationKey(secretReservationKeys);

        Future resetFuture = controller.getWsn().resetNodes(nodeUrns, 10, TimeUnit.SECONDS);
        JobResult resetJobResult = null;
        try {
            resetJobResult = (JobResult) resetFuture.get();
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
        } catch (ExecutionException e) {
            LOG.error(e.getMessage(), e);
        }
        LOG.info("{}", resetJobResult);
        if (resetJobResult != null) {
            if (resetJobResult.getSuccessPercent() < 100) {
                throw new ExperimentationException("Not all nodes could be reset. Exiting");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stopExperimentController(final List<SecretReservationKey> secretReservationKeys)
            throws ExperimentationException {

        // find experiment controller from secret reservation key
        WiseUiControllerClient controller
                = findExperimentControllerBySecretReservationKey(secretReservationKeys);

        LOG.info("Terminating controller with Secret reservation key = "
                + secretReservationKeys.get(0).getSecretReservationKey());

        // remove the selected controller
        experimentControllers.remove(controller);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Message returnExperimentMessage(final List<SecretReservationKey> secretReservationKeys)
            throws ExperimentationException {

        // get experiment controller
        WiseUiControllerClient controller = findExperimentControllerBySecretReservationKey(secretReservationKeys);
        if (controller == null || controller.getListener() == null) return null;
        // setup an experiment message
        Message message = controller.getListener().getMessageQueue().poll();
        if (message != null) {
            String value = secretReservationKeys.get(0).getSecretReservationKey();
            message.setSecretRecretReservationKey(value);
            LOG.debug("message=" + message);
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
        LOG.info("Persistence holds " + availableImages.size() + " images");
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
        WiseUiControllerClient controller = findExperimentControllerBySecretReservationKey(secretReservationKeys);

        // get wise ml string
        String wisemlString;
        try {
            wisemlString = controller.getListener().getSerializedWiseMl();
        } catch (Exception cause) {
            LOG.error(cause.getMessage());
            throw new ExperimentationException(cause.getMessage());
        }
        LOG.info(wisemlString);

        return wisemlString;
    }

    /**
     * Finds an experiment controller by iterating for it's secret reservation key in the controllers list.
     *
     * @param rsSecretReservationKeys List of {@link eu.wisebed.wiseui.shared.dto.SecretReservationKey}
     * @return ExperimentController instance
     */
    private WiseUiControllerClient findExperimentControllerBySecretReservationKey(
            final List<SecretReservationKey> rsSecretReservationKeys) {

        WiseUiControllerClient experimentController = null;

        // iterate and compare secret reservation keys
        for (WiseUiControllerClient controller : experimentControllers) {
            String searchingKey = rsSecretReservationKeys.get(0).getSecretReservationKey();
            String controllerKey = controller.getSecretReservationKeys().get(0).getSecretReservationKey();
            if (searchingKey.equals(controllerKey)) {
                experimentController = controller;
            }
        }

        return experimentController;
    }
}
