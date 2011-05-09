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
package eu.wisebed.wiseui.client.reservation.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import eu.wisebed.wiseui.api.ReservationServiceAsync;
import eu.wisebed.wiseui.client.WiseUiGinjector;
import eu.wisebed.wiseui.client.main.WiseUiPlace;
import eu.wisebed.wiseui.client.reservation.event.MissingReservationParametersEvent;
import eu.wisebed.wiseui.client.reservation.event.MissingReservationParametersEventHandler;
import eu.wisebed.wiseui.client.reservation.event.ReservationFailedEvent;
import eu.wisebed.wiseui.client.reservation.event.ReservationFailedEventHandler;
import eu.wisebed.wiseui.client.reservation.event.ReservationSuccessEvent;
import eu.wisebed.wiseui.client.reservation.event.ReservationSuccessEventHandler;
import eu.wisebed.wiseui.client.reservation.i18n.ReservationMessages;
import eu.wisebed.wiseui.client.reservation.view.ReservationView;
import eu.wisebed.wiseui.client.reservation.view.ReservationView.Presenter;
import eu.wisebed.wiseui.client.util.AuthenticationManager;
import eu.wisebed.wiseui.shared.dto.Node;
import eu.wisebed.wiseui.shared.dto.ReservationDetails;
import eu.wisebed.wiseui.shared.dto.SecretAuthenticationKey;
import eu.wisebed.wiseui.shared.dto.SecretReservationKey;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;
import eu.wisebed.wiseui.shared.exception.AuthenticationException;
import eu.wisebed.wiseui.shared.exception.ReservationConflictException;
import eu.wisebed.wiseui.shared.exception.ReservationException;
import eu.wisebed.wiseui.widgets.messagebox.MessageBox;

import java.util.List;

public class ReservationPresenter implements Presenter, MissingReservationParametersEventHandler,
        ReservationSuccessEventHandler, ReservationFailedEventHandler {

    private final ReservationView view;
    private WiseUiPlace place;
    private ReservationServiceAsync reservationService;
    private PlaceController placeController;
    private EventBus eventBus;
    private WiseUiGinjector injector;
    private ReservationMessages messages;

    @Inject
    public ReservationPresenter(final WiseUiGinjector injector,
                                final ReservationServiceAsync reservationService,
                                final ReservationView view,
                                final PlaceController placeController,
                                final EventBus eventBus,
                                final ReservationMessages messages) {
        this.injector = injector;
        this.reservationService = reservationService;
        this.view = view;
        this.placeController = placeController;
        this.eventBus = eventBus;
        this.messages = messages;
    }

    public void setPlace(final WiseUiPlace place) {
        this.place = place;
    }

    public void bindEnabledViewEvents() {
        eventBus.addHandler(MissingReservationParametersEvent.TYPE, this);
        eventBus.addHandler(ReservationSuccessEvent.TYPE, this);
        eventBus.addHandler(ReservationFailedEvent.TYPE, this);
    }

    /**
     * Makes an Asynchronous callback to server asking for the nodes located in
     * the network. Result consists of an array with all sensors' useful
     * information.
     */
    public void getNetwork(final String sessionManagementEndpointUrl) {
        reservationService.getNodeList(sessionManagementEndpointUrl, new AsyncCallback<List<Node>>() {
            public void onFailure(Throwable caught) {
                GWT.log("Failed rpc");
            }

            public void onSuccess(List<Node> nodeList) {
                if (nodeList == null) {
                    MessageBox.error(messages.noNodesReturnedTitle(),
                            messages.noNodesReturned(), null, null);
                } else {
                    // TODO FIXME!
                    //injector.getNewReservationView().renderNodes(nodeList);
                }
            }
        });
    }

    /**
     * Sends reservation details to server and books a new reservation.
     */
    public void makeReservation() {
        // FIXME: Get testbed selected from testbed panel
        TestbedConfiguration bed = new TestbedConfiguration();
        final String rsEndpointUrl = bed.getRsEndpointUrl();
        final String urnPrefix = bed.getUrnPrefixList().get(0);
        final AuthenticationManager auth = injector.getAuthenticationManager();
        SecretAuthenticationKey secretAuthKey = auth.getKeyHash().get(urnPrefix);
        // FIXME: Retrieve reservation details from google cal;
        final ReservationDetails data = null;
        reservationService.makeReservation(secretAuthKey, rsEndpointUrl, data, new AsyncCallback<SecretReservationKey>() {
            public void onFailure(Throwable caught) {
                if (caught instanceof AuthenticationException) {
                    GWT.log("User not authorized to make reservations");
                } else if (caught instanceof ReservationException) {
                    GWT.log("RS system failed");
                } else if (caught instanceof ReservationConflictException) {
                    GWT.log("Cannot make reservations some nodes are " +
                            "already reserved");
                } else {
                    GWT.log("Error occured while contacting the server");
                }
                eventBus.fireEvent(new ReservationFailedEvent());
            }

            public void onSuccess(SecretReservationKey result) {
                eventBus.fireEvent(new ReservationSuccessEvent());
            }
        });
    }

    public void onMissingReservationParameters(final MissingReservationParametersEvent event) {
        MessageBox.error(messages.missingReservationParametersTitle(), messages.missingReservationParameters(), null,
                null);
    }

    public void onReservationSuccess(final ReservationSuccessEvent event) {
        MessageBox.success(messages.reservationSuccessTitle(), messages.reservationSuccess(), null);
    }

    public void onReservationFailed(final ReservationFailedEvent event) {
        MessageBox.error(messages.reservationFailedTitle(), messages.reservationFailed(), null, null);
    }
}