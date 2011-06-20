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
package eu.wisebed.wiseui.client.reservation.presenter;

import com.bradrydzewski.gwt.calendar.client.Appointment;
import com.google.common.base.Objects;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import eu.wisebed.wiseui.api.ReservationServiceAsync;
import eu.wisebed.wiseui.client.WiseUiGinjector;
import eu.wisebed.wiseui.client.reservation.event.CreateReservationEvent;
import eu.wisebed.wiseui.client.reservation.event.EditReservationEvent;
import eu.wisebed.wiseui.client.reservation.event.ReservationDeleteFailedEvent;
import eu.wisebed.wiseui.client.reservation.event.ReservationDeleteSuccessEvent;
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
import eu.wisebed.wiseui.client.util.ReservationManager;
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
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Soenke Nommensen
 * @author Philipp Abraham
 * @author John I. Gakos
 */
public class ReservationEditPresenter implements Presenter, CreateReservationEvent.Handler, EditReservationEvent.Handler, ConfigurationSelectedHandler,
        PlaceChangeEvent.Handler, ReservationSuccessEvent.Handler, ReservationFailedEvent.Handler {

    private static final String DEFAULT_NEW_TITLE = "New Reservation";

    private WiseUiGinjector injector;
    private final EventBusManager eventBus;
    private final ReservationEditView view;
    private TestbedConfiguration selectedConfiguration;
    private ReservationServiceAsync reservationService;
    private ReservationManager reservationManager;
    private String title = DEFAULT_NEW_TITLE;
    private List<String> selectedNodes = new ArrayList<String>();
    private ReservationMessages messages;

    private boolean readOnly = false;
    private Appointment appointment = null;

    @Inject
    public ReservationEditPresenter(final WiseUiGinjector injector,
                                    final EventBus eventBus,
                                    final ReservationEditView view,
                                    final ReservationServiceAsync service,
                                    final ReservationManager reservationManager,
                                    final ReservationMessages messages) {
        this.injector = injector;
        this.eventBus = new EventBusManager(eventBus);
        this.view = view;
        this.reservationService = service;
        this.reservationManager = reservationManager;
        this.messages = messages;

        bind();
    }

    private void bind() {
        eventBus.addHandler(EditReservationEvent.TYPE, this);
        eventBus.addHandler(CreateReservationEvent.TYPE, this);
        eventBus.addHandler(TestbedSelectedEvent.TYPE, this);
        eventBus.addHandler(PlaceChangeEvent.TYPE, this);
        eventBus.addHandler(ReservationSuccessEvent.TYPE, this);
        eventBus.addHandler(ReservationFailedEvent.TYPE, this);
    }

    /**
     * Sends reservation details to server and books a new reservation.
     */
    @Override
    public void create() {
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
            data.setUrnPrefix(urnPrefix); // or secretAuthenticationKey.getUrnPrefix() ?
            data.setUrnPrefix(urnPrefix); // or secretAuthenticationKey.geIdtUrnPrefix() ?
            data.setUsername(secretAuthenticationKey.getUsername());
            data.setSecretReservationKey(secretAuthenticationKey.getSecretAuthenticationKey());
            reservationData.getData().add(data);
        }

        view.hide();

        reservationService.makeReservation(rsEndpointUrl,
                                           authenticationKeys,
                                           reservationData,
                                           new AsyncCallback<List<SecretReservationKey>>() {
            public void onFailure(Throwable caught) {
                eventBus.fireEvent(new ReservationFailedEvent(caught));
            }

            public void onSuccess(List<SecretReservationKey> result) {
                eventBus.fireEvent(new ReservationSuccessEvent());
            }
        });
    }
    
    /**
     * Deletes and Creates the Reservation due to lack of Updatefunction on the server
     */
    @Override
    public void submit() {
    	// delete from data in this.appointment
    	deleteReservation(this.appointment, new AsyncCallback<Void>() {
    		@Override
    		public void onFailure(Throwable caught) {
    			eventBus.fireEvent(new ReservationDeleteFailedEvent(caught));
    		}
    		
    		@Override
    		public void onSuccess(Void arg0) {
    			// create from textboxes
    	    	create();
    		}
		});
    	
    	
        
    }

    /**
     * Call GWT-RPC deleteReservation(...) from {@link eu.wisebed.wiseui.api.ReservationService}.
     * Deleting reservation from RS service. This action cannot be undone!
     *
     * @param appointment Appointment to be deleted
     */
    public void deleteReservation(final Appointment appointment, AsyncCallback<Void> callback) {
        // Get RS endpoint URL
        final String rsEndpointUrl = selectedConfiguration.getRsEndpointUrl();

        // Get secret authentication keys
        final List<SecretAuthenticationKey> secretAuthenticationKeys
                = injector.getAuthenticationManager().getSecretAuthenticationKeys();

        // Get secret reservation keys
        final ConfidentialReservationData confidentialReservationData
                = reservationManager.getConfidentialReservations().get(appointment.getId());
        final List<SecretReservationKey> secretReservationKeys
                = new ArrayList<SecretReservationKey>(confidentialReservationData.getData().size());
        for (Data data : confidentialReservationData.getData()) {
            final SecretReservationKey secretReservationKey =
                    new SecretReservationKey(data.getUrnPrefix(), data.getSecretReservationKey());
            secretReservationKeys.add(secretReservationKey);
        }

        // Make the RPC call and handle the result via events
        GWT.log("Delete reservation: " + confidentialReservationData);
        reservationService.deleteReservation(rsEndpointUrl, secretAuthenticationKeys, secretReservationKeys, callback);
    }

    @Override
    public void cancel() {
        if (readOnly) {
            view.hide();
        } else {
            MessageBox.warning(title, "Do you want to discard your changes?", new MessageBox.Callback() {

                @Override
                public void onButtonClicked(final Button button) {
                    if (Button.OK.equals(button)) {
                        view.hide();
                    }
                }
            });
        }
    }

    @Override
    public void delete() {
        if (appointment == null) {
            return;
        }
        MessageBox.warning(title, "Do you want to delete the reservation?", new MessageBox.Callback() {

            @Override
            public void onButtonClicked(final Button button) {
                if (Button.OK.equals(button)) {
                    deleteReservation(appointment, new AsyncCallback<Void>() {
                        public void onFailure(final Throwable caught) {
                        	eventBus.fireEvent(new ReservationDeleteFailedEvent(caught));
                        }

                        public void onSuccess(final Void result) {
                        	eventBus.fireEvent(new ReservationDeleteSuccessEvent(appointment));
                        }
                    });
                    GWT.log("Deleting reservation made by: " + appointment.getCreatedBy());
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

    @Override
    public void onEditReservation(final EditReservationEvent event) {
        prepareDialog(event.getAppointment(), event.isReadOnly(), event.getNodes());
        view.setUpdate();
    }
    
	@Override
	public void onCreateReservation(CreateReservationEvent event) {
		prepareDialog(event.getAppointment(), false, event.getNodes());
		view.setCreate();
	}
	
	private void prepareDialog(final Appointment appointment, final boolean readOnly, final Set<Node> nodes) {
		
		if (selectedConfiguration == null) {
            final String suggestion = "Please select at least one testbed to make a new reservation";
            MessageBox.warning("No testbed selected", suggestion, null);
            return;
        }
		
		// Set appointment
        this.appointment = appointment;

        this.readOnly = readOnly;
        view.setReadOnly(readOnly);

        // Fill in default values
        view.getWhoTextBox().setText("");
        view.getReservationKeyBox().setText("");
        view.getStartDateBox().setValue(new Date());
        view.getEndDateBox().setValue(new Date());

        // Fill in real values
        final String title = Objects.firstNonNull(selectedConfiguration.getName(), DEFAULT_NEW_TITLE);
        final String createdBy = appointment.getCreatedBy();
        view.getWhoTextBox().setText(createdBy != null ? createdBy : getAuthenticatedUserName());
        final Date start = appointment.getStart();
        view.getStartDateBox().setValue(start);
        final Date end = appointment.getEnd();
        view.getEndDateBox().setValue(end != null ? end : start);

        // TODO SNO Refactor. This code looks too complicated and ugly.
        // Fill in secret reservation key for authenticated users in confidential reservations
        if (!readOnly) {
            final ConfidentialReservationData confidentialReservationData
                    = injector.getReservationManager().getConfidentialReservations().get(appointment.getId());
            if (confidentialReservationData != null) {
                final List<Data> dataList = confidentialReservationData.getData();
                if (dataList != null && !dataList.isEmpty()) {
                    final Data data = dataList.get(0);
                    if (data != null) {
                        final String text = data.getUrnPrefix() + "," + data.getSecretReservationKey();
                        view.getReservationKeyBox().setText(text);
                    }
                }
            }
        }
        view.show(title);
        if (nodes != null) {
            setNodesSelected(nodes);
        }
		
	}

    // Quick Hack
    private String getAuthenticatedUserName() {
        final AuthenticationManager authenticationManager = injector.getAuthenticationManager();
        String userName = "-1"; // TODO Is this really what we want (use an empty string...)?
        if (authenticationManager.isAuthenticated(selectedConfiguration)) {
            final String firstUrnPrefix = selectedConfiguration.getUrnPrefixList().get(0);
            final SecretAuthenticationKey secretAuthenticationKey = authenticationManager.getMap().get(firstUrnPrefix);
            if (secretAuthenticationKey != null) {
                userName = secretAuthenticationKey.getUsername();
                GWT.log("getAuthenticatedUserName: " + userName + " for prefix: " + firstUrnPrefix);
            }
        }
        return userName;
    }
}