/**
 * Copyright (C) 2011 Universität zu Lübeck, Institut für Telematik (ITM), Research Academic Computer
 *                             Technology Institute (RACTI)
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
package eu.wisebed.wiseui.client.reservation.presenter;

import com.google.common.base.Objects;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import eu.wisebed.wiseui.api.ReservationServiceAsync;
import eu.wisebed.wiseui.client.WiseUiGinjector;
import eu.wisebed.wiseui.client.reservation.event.EditReservationEvent;
import eu.wisebed.wiseui.client.reservation.event.ReservationFailedEvent;
import eu.wisebed.wiseui.client.reservation.event.ReservationSuccessEvent;
import eu.wisebed.wiseui.client.reservation.i18n.ReservationMessages;
import eu.wisebed.wiseui.client.reservation.view.ReservationEditView;
import eu.wisebed.wiseui.client.reservation.view.ReservationEditView.Presenter;
import eu.wisebed.wiseui.client.testbedlist.event.TestbedSelectedEvent;
import eu.wisebed.wiseui.client.testbedlist.event.TestbedSelectedEvent.ConfigurationSelectedHandler;
import eu.wisebed.wiseui.client.testbedselection.common.TestbedTreeViewModel;
import eu.wisebed.wiseui.client.util.AuthenticationManager;
import eu.wisebed.wiseui.client.util.EventBusManager;
import eu.wisebed.wiseui.shared.dto.ConfidentialReservationData;
import eu.wisebed.wiseui.shared.dto.Data;
import eu.wisebed.wiseui.shared.dto.Node;
import eu.wisebed.wiseui.shared.dto.SecretAuthenticationKey;
import eu.wisebed.wiseui.shared.dto.SecretReservationKey;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;
import eu.wisebed.wiseui.shared.exception.AuthenticationException;
import eu.wisebed.wiseui.shared.exception.ReservationException;
import eu.wisebed.wiseui.widgets.messagebox.MessageBox;
import eu.wisebed.wiseui.widgets.messagebox.MessageBox.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Soenke Nommensen
 * @author John I. Gakos
 */
public class ReservationEditPresenter implements Presenter, EditReservationEvent.Handler, ConfigurationSelectedHandler,
        PlaceChangeEvent.Handler, ReservationSuccessEvent.Handler, ReservationFailedEvent.Handler {

    private static final String DEFAULT_NEW_TITLE = "New Reservation";

    private WiseUiGinjector injector;
    private final EventBusManager eventBus;
    private final ReservationEditView view;
    private TestbedConfiguration selectedConfiguration;
    private ReservationServiceAsync reservationService;
    private String title = DEFAULT_NEW_TITLE;
    private List<String> selectedNodes = new ArrayList<String>();
    private ReservationMessages messages;

    @Inject
    public ReservationEditPresenter(final WiseUiGinjector injector,
                                    final EventBus eventBus,
                                    final ReservationEditView view,
                                    final ReservationServiceAsync service,
                                    final ReservationMessages messages) {
        this.injector = injector;
        this.eventBus = new EventBusManager(eventBus);
        this.view = view;
        this.reservationService = service;
        this.messages = messages;

        bind();
    }

    private void bind() {
        eventBus.addHandler(EditReservationEvent.TYPE, this);
        eventBus.addHandler(TestbedSelectedEvent.TYPE, this);
        eventBus.addHandler(PlaceChangeEvent.TYPE, this);
        eventBus.addHandler(ReservationSuccessEvent.TYPE, this);
        eventBus.addHandler(ReservationFailedEvent.TYPE, this);
    }

    /**
     * Sends reservation details to server and books a new reservation.
     */
    @Override
    public void submit() {
        if (selectedNodes == null || selectedNodes.isEmpty()) {
            // TODO: Add a 'suggestion' type message box
            final String suggestion = "Please select at least one node to submit a reservation!";
            MessageBox.info("No nodes selected", suggestion, null);
            return;
        }

        final String rsEndpointUrl = selectedConfiguration.getRsEndpointUrl();

        final ConfidentialReservationData reservationData = new ConfidentialReservationData();
        reservationData.setFrom(view.getStartDateBox().getValue());
        reservationData.setTo(view.getEndDateBox().getValue());
        reservationData.setUserData(view.getWhoTextBox().getText());
        reservationData.setNodeURNs(selectedNodes);

        final AuthenticationManager authenticationManager = injector.getAuthenticationManager();
        final List<SecretAuthenticationKey> authenticationKeys = new ArrayList<SecretAuthenticationKey>();

        for (String urnPrefix : selectedConfiguration.getUrnPrefixList()) {
            SecretAuthenticationKey secretAuthenticationKey = authenticationManager.getMap().get(urnPrefix);
            authenticationKeys.add(secretAuthenticationKey);
            // Set up data object for confidential reservation data
            final Data data = new Data();
            data.setUrnPrefix(secretAuthenticationKey.getUrnPrefix());
            data.setUsername(secretAuthenticationKey.getUsername());
            data.setSecretReservationKey(secretAuthenticationKey.getSecretAuthenticationKey());
            reservationData.getData().add(data);
        }

        view.hide();

        reservationService.makeReservation(rsEndpointUrl, authenticationKeys, reservationData, new AsyncCallback<List<SecretReservationKey>>() {
            public void onFailure(Throwable caught) {
                eventBus.fireEvent(new ReservationFailedEvent(caught));
            }

            public void onSuccess(List<SecretReservationKey> result) {
                eventBus.fireEvent(new ReservationSuccessEvent());
            }
        });
    }

    @Override
    public void cancel() {
        MessageBox.warning(title, "Do you want to discard your changes?", new MessageBox.Callback() {

            @Override
            public void onButtonClicked(final Button button) {
                if (Button.OK.equals(button)) {
                    view.hide();
                }
            }
        });
    }

    @Override
    public void onTestbedSelected(final TestbedSelectedEvent event) {
        selectedConfiguration = event.getConfiguration();
    }

    private void setNodesSelected(final Set<Node> nodesSelected) {
        selectedNodes.clear();
        List<Node> nodes = new ArrayList<Node>(nodesSelected.size());
        for (Node node : nodesSelected) {
            selectedNodes.add(node.getId());
            nodes.add(node);
        }
        view.setTreeViewModel(new TestbedTreeViewModel(selectedConfiguration, nodes, null));
    }

    public void onPlaceChange(final PlaceChangeEvent event) {
        eventBus.removeAll();
    }

    @Override
    public void onEditReservation(final EditReservationEvent event) {
        if (selectedConfiguration == null) {
            final String suggestion = "Please select at least one testbed to make a new reservation";
            MessageBox.info("No testbed selected", suggestion, null);
            return;
        }
        final String title = Objects.firstNonNull(selectedConfiguration.getName(), DEFAULT_NEW_TITLE);
        final AuthenticationManager authenticationManager = injector.getAuthenticationManager();
        //view.getWhoTextBox().setText(authenticationManager.getSecretAuthenticationKeys().get(0).getUsername());
        view.getWhoTextBox().setText(event.getAppointment().getCreatedBy());
        view.getStartDateBox().setValue(event.getAppointment().getStart());
        view.getEndDateBox().setValue(event.getAppointment().getEnd());
        view.show(title);
        if (event.getNodes() != null) {
            setNodesSelected(event.getNodes());
        }
    }

    public void onReservationSuccess() {
        MessageBox.success(messages.reservationSuccessTitle(), messages.reservationSuccess(), null);
    }

    @Override
    public void onReservationFailed(final ReservationFailedEvent event) {
        final String errorMsgTitle;
        final String errorMsg;
        final Throwable caught = event.getThrowable();
        // TODO: Get exception's message for more descriptive error handling
        if (caught instanceof AuthenticationException) {
            errorMsgTitle = messages.authenticationKeysExpiredTitle();
            errorMsg = messages.authenticationKeysExpired();
            GWT.log(errorMsg);
        } else if (caught instanceof ReservationException) {
            errorMsgTitle = messages.faultyReservationParametersTitle();
            errorMsg = messages.faultyReservationParameters();
            GWT.log(errorMsg);
        } else {
            errorMsgTitle = messages.rsServiceErrorTitle();
            errorMsg = messages.rsServiceError();
            GWT.log(errorMsg);
        }
        MessageBox.error(errorMsgTitle, errorMsg, caught, null);
    }
}