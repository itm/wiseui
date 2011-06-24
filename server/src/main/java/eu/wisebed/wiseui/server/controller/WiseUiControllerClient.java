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
package eu.wisebed.wiseui.server.controller;

import com.google.inject.Inject;
import de.uniluebeck.itm.wisebed.cmdlineclient.BeanShellHelper;
import de.uniluebeck.itm.wisebed.cmdlineclient.protobuf.ProtobufControllerClient;
import de.uniluebeck.itm.wisebed.cmdlineclient.wrapper.WSNAsyncWrapper;
import eu.wisebed.api.sm.ExperimentNotRunningException_Exception;
import eu.wisebed.api.sm.SecretReservationKey;
import eu.wisebed.api.sm.SessionManagement;
import eu.wisebed.api.sm.UnknownReservationIdException_Exception;
import eu.wisebed.api.wsn.WSN;
import eu.wisebed.testbed.api.wsn.WSNServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Soenke Nommensen
 */
public class WiseUiControllerClient {

    private static final Logger LOG = LoggerFactory.getLogger(WiseUiControllerClient.class);

    private String sessionManagementEndpoint;

    private List<SecretReservationKey> secretReservationKeys = new ArrayList<SecretReservationKey>();

    private WiseUiListener listener;

    private WSNAsyncWrapper wsn;

    private String portalServer;

    @Inject
    public WiseUiControllerClient(final WiseUiListener listener) {
        this.listener = listener;
    }

    public void setupProtobufClient() {
        // Retrieve Java proxies of the endpoint URLs above
        final SessionManagement sessionManagement = WSNServiceHelper.getSessionManagementService(sessionManagementEndpoint);

        String wsnEndpointURL = null;
        try {
            wsnEndpointURL = sessionManagement.getInstance(secretReservationKeys, "NONE");
        } catch (final UnknownReservationIdException_Exception e) {
            LOG.warn("There was not reservation found with the given secret reservation key. Exiting.");
            System.exit(1);
        } catch (final ExperimentNotRunningException_Exception e) {
            LOG.error(e.getMessage(), e);
        }

        LOG.info("Got a WSN instance URL, endpoint is: {}", wsnEndpointURL);
        WSN wsnService = WSNServiceHelper.getWSNService(wsnEndpointURL);
        wsn = WSNAsyncWrapper.of(wsnService);
        ProtobufControllerClient pcc = ProtobufControllerClient.create(
                portalServer,
                8885, secretReservationKeys);
        if (pcc == null) return;
        pcc.addListener(listener);
        pcc.connect();
    }

    public String getSessionManagementEndpoint() {
        return sessionManagementEndpoint;
    }

    public void setSessionManagementEndpoint(String sessionManagementEndpoint) {
        this.sessionManagementEndpoint = sessionManagementEndpoint;
    }

    public List<SecretReservationKey> getSecretReservationKeys() {
        return secretReservationKeys;
    }

    public void setSecretReservationKeys(List<SecretReservationKey> secretReservationKeys) {
        this.secretReservationKeys = secretReservationKeys;
    }

    public WiseUiListener getListener() {
        return listener;
    }

    public WSNAsyncWrapper getWsn() {
        return wsn;
    }

    public void setPortalServer(String portalServer) {
        this.portalServer = portalServer;
    }
}
