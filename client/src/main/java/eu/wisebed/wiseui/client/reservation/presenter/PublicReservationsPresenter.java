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
import com.bradrydzewski.gwt.calendar.client.event.TimeBlockClickEvent;
import com.bradrydzewski.gwt.calendar.client.event.TimeBlockClickHandler;
import com.bradrydzewski.gwt.calendar.client.event.UpdateEvent;
import com.bradrydzewski.gwt.calendar.client.event.UpdateHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import eu.wisebed.wiseui.api.CalendarServiceAsync;
import eu.wisebed.wiseui.api.ReservationService;
import eu.wisebed.wiseui.api.ReservationServiceAsync;
import eu.wisebed.wiseui.client.WiseUiGinjector;
import eu.wisebed.wiseui.client.reservation.event.CreateReservationEvent;
import eu.wisebed.wiseui.client.reservation.event.EditReservationEvent;
import eu.wisebed.wiseui.client.reservation.event.ReservationDeleteFailedEvent;
import eu.wisebed.wiseui.client.reservation.event.ReservationDeleteSuccessEvent;
import eu.wisebed.wiseui.client.reservation.event.ReservationSuccessEvent;
import eu.wisebed.wiseui.client.reservation.event.UpdateNodesSelectedEvent;
import eu.wisebed.wiseui.client.reservation.i18n.ReservationMessages;
import eu.wisebed.wiseui.client.reservation.view.PublicReservationsView;
import eu.wisebed.wiseui.client.testbedlist.event.TestbedSelectedEvent;
import eu.wisebed.wiseui.client.testbedselection.event.ThrowableEvent;
import eu.wisebed.wiseui.client.testbedselection.event.WisemlLoadedEvent;
import eu.wisebed.wiseui.client.util.AuthenticationManager;
import eu.wisebed.wiseui.client.util.EventBusManager;
import eu.wisebed.wiseui.client.util.ReservationManager;
import eu.wisebed.wiseui.shared.dto.ConfidentialReservationData;
import eu.wisebed.wiseui.shared.dto.Node;
import eu.wisebed.wiseui.shared.dto.PublicReservationData;
import eu.wisebed.wiseui.shared.dto.SecretAuthenticationKey;
import eu.wisebed.wiseui.shared.dto.Setup;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;
import eu.wisebed.wiseui.widgets.messagebox.MessageBox;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author John I. Gakos
 * @author Soenke Nommensen
 * @author Philipp Abraham
 */
public class PublicReservationsPresenter implements PublicReservationsView.Presenter,
        TestbedSelectedEvent.ConfigurationSelectedHandler, UpdateNodesSelectedEvent.Handler,
        ReservationSuccessEvent.Handler, ThrowableEvent.ThrowableHandler, PlaceChangeEvent.Handler,
        ReservationDeleteSuccessEvent.Handler, ReservationDeleteFailedEvent.Handler,
        WisemlLoadedEvent.WisemlLoadedHandler {

    private WiseUiGinjector injector;
    private final EventBusManager eventBus;
    private final PublicReservationsView view;
    private final ReservationServiceAsync reservationService;
    private final CalendarServiceAsync calendarService;
    private TestbedConfiguration selectedTestbedConfiguration;
    private ReservationManager reservationManager;
    private Set<Node> selectedNodes = new HashSet<Node>();
    private List<Node> setupNodes = new ArrayList<Node>();
    private ReservationMessages messages;

    @Inject
    public PublicReservationsPresenter(final WiseUiGinjector injector,
                                       final EventBus eventBus,
                                       final PublicReservationsView view,
                                       final ReservationServiceAsync reservationService,
                                       final CalendarServiceAsync calendarService,
                                       final ReservationManager reservationManager,
                                       final ReservationMessages messages) {
        this.injector = injector;
        this.eventBus = new EventBusManager(eventBus);
        this.view = view;
        this.reservationService = reservationService;
        this.calendarService = calendarService;
        this.reservationManager = reservationManager;
        this.messages = messages;
        bind();
    }

    private void bind() {
        eventBus.addHandler(PlaceChangeEvent.TYPE, this);
        eventBus.addHandler(TestbedSelectedEvent.TYPE, this);
        eventBus.addHandler(ThrowableEvent.TYPE, this);
        eventBus.addHandler(UpdateNodesSelectedEvent.TYPE, this);
        eventBus.addHandler(ReservationSuccessEvent.TYPE, this);
        eventBus.addHandler(ReservationDeleteSuccessEvent.TYPE, this);
        eventBus.addHandler(ReservationDeleteFailedEvent.TYPE, this);
        eventBus.addHandler(WisemlLoadedEvent.TYPE, this);

        view.getCalendar().addSelectionHandler(new SelectionHandler<Appointment>() {

            @Override
            public void onSelection(SelectionEvent<Appointment> event) {
                GWT.log(event.getSelectedItem().getTitle() + " selected");
            }
        });

        view.getCalendar().addOpenHandler(new OpenHandler<Appointment>() {
            @Override
            public void onOpen(final OpenEvent<Appointment> event) {
                // Default read only status is true
                boolean readOnly = true;

                final Appointment appointment = event.getTarget();

                final HashSet<Node> filteredNodes = new HashSet<Node>();
                final PublicReservationData publicReservationData
                        = reservationManager.getPublicReservations().get(appointment.getId());

                // TODO Reduce computational complexity!
                if (publicReservationData != null) {
                    GWT.log("Found public reservation: " + publicReservationData);
                    for (String urnPrefix : publicReservationData.getNodeURNs()) {
                        for (Node node : setupNodes) {
                            if (node.getId().equals(urnPrefix)) {
                                filteredNodes.add(node);
                            }
                        }
                    }
                }

                // Authenticated users can edit their own reservations
                if (isAuthenticated()
                        && reservationManager.getConfidentialReservations().containsKey(appointment.getId())) {
                    readOnly = false;

                    final PublicReservationData confidentialReservationData
                            = reservationManager.getConfidentialReservations().get(appointment.getId());

                    if (confidentialReservationData != null) {
                        GWT.log("Found confidential reservation: " + confidentialReservationData);
                        for (String urnPrefix : confidentialReservationData.getNodeURNs()) {
                            for (Node node : setupNodes) {
                                if (node.getId().equals(urnPrefix)) {
                                    if (!filteredNodes.contains(node)) {
                                        filteredNodes.add(node);
                                    }
                                }
                            }
                        }
                    }
                }

                eventBus.fireEventFromSource(new EditReservationEvent(appointment, filteredNodes, readOnly), this);
            }
        });
        view.getCalendar().addUpdateHandler(new UpdateHandler<Appointment>() {
            @Override
            public void onUpdate(final UpdateEvent<Appointment> event) {
                if (!isAuthenticated()) {
                    MessageBox.warning(messages.loginRequiredTitle(), messages.loginRequired(), null);
                    return;
                }
                // TODO moving appointments should update reservation

                boolean commit = Window
                        .confirm(
                                "Are you sure you want to update the appointment \""
                                        + event.getTarget().getTitle() + "\"");
                if (!commit) {
                    event.setCancelled(true);
                }
            }
        });
        view.getCalendar().addTimeBlockClickHandler(new TimeBlockClickHandler<Date>() {
            @Override
            public void onTimeBlockClick(final TimeBlockClickEvent<Date> event) {
                final Date startDate = event.getTarget();
                final Appointment reservation = new Appointment();

                if (!isAuthenticated()) {
                    MessageBox.warning(messages.loginRequiredTitle(), messages.loginRequired(), null);
                    return;
                }

                reservation.setStart(startDate);
                reservation.setEnd(startDate);

                eventBus.fireEventFromSource(new CreateReservationEvent(reservation, selectedNodes), this);
                //showEditReservationDialog(reservation, selectedNodes, false);
            }
        });
        view.getDatePicker().addValueChangeHandler(new ValueChangeHandler<Date>() {
            @Override
            public void onValueChange(final ValueChangeEvent<Date> event) {
                final Date newDate = event.getValue();
                view.getCalendar().setDate(newDate);
                loadReservations(newDate);
            }
        });
    }

    @Override
    public void onTestbedSelected(final TestbedSelectedEvent event) {
        selectedTestbedConfiguration = event.getConfiguration();
        loadReservations(view.getCalendar().getDate());
    }

    @Override
    public void onPlaceChange(final PlaceChangeEvent event) {
        view.getLoadingIndicator().hideLoading();
        eventBus.removeAll();
    }

    @Override
    public void onThrowable(final ThrowableEvent event) {
        view.getLoadingIndicator().hideLoading();
        if (event.getThrowable() == null) return;
        if (selectedTestbedConfiguration != null && selectedTestbedConfiguration.getName() != null) {
            MessageBox.error("Error fetching reservation data for testbed '"
                    + selectedTestbedConfiguration.getName()
                    + "'!",
                    event.getThrowable().getMessage(),
                    event.getThrowable(), null);
        } else {
            MessageBox.error("Error fetching reservation data!",
                    event.getThrowable().getMessage(),
                    event.getThrowable(), null);
        }
    }

    /**
     * Call GWT-RPC getPublicReservations(...) from {@link eu.wisebed.wiseui.api.ReservationService}.
     * The resulting {@link eu.wisebed.wiseui.shared.dto.ReservationDetails} are rendered in the calendar widget.
     *
     * @param pivotDate Anchor date for fetching reservation data
     */
    private void loadPublicAndMaybeConfidentialReservations(final Date pivotDate) {
        view.getLoadingIndicator().showLoading("Reservations");

        // Logging
        String testbedName = "<Unknown testbed>";
        if (selectedTestbedConfiguration != null) {
            testbedName = selectedTestbedConfiguration.getName();
        }

        if (selectedTestbedConfiguration == null) {
            final String errorMessage = "Reservation URL not found for Testbed '" + testbedName + "'";
            GWT.log(errorMessage);
            MessageBox.warning("Configuration could not be loaded!",
                    errorMessage,
                    null);
        }

        // Remove all pivotDate calendar entries
        view.removeAllAppointments();

        // Remove all registered reservations from the ReservationManager
        reservationManager.getPublicReservations().clear();
        reservationManager.getConfidentialReservations().clear();

        // Determine the date range, which shall be passed to the remote call
        final ReservationService.Range range = calcRange();

        // Make the service call
        GWT.log("Loading public reservations for Testbed '" + testbedName + "'");
        reservationService.getPublicReservations(selectedTestbedConfiguration.getRsEndpointUrl(),
                pivotDate, range, new AsyncCallback<List<PublicReservationData>>() {
            public void onFailure(Throwable caught) {
                view.getLoadingIndicator().hideLoading();
                GWT.log("Error fetching reservation data!\n" + caught.getMessage());
                eventBus.fireEvent(new ThrowableEvent(caught));
            }

            public void onSuccess(final List<PublicReservationData> publicReservations) {
                if (publicReservations != null && !publicReservations.isEmpty()) {
                    // Render the {@link eu.wisebed.wiseui.shared.dto.ReservationDetails}
                    view.renderPublicReservations(publicReservations);

                    // If user is authenticated remove from calendar all reservations that
                    // belong to him and re-render them with secret reservation keys
                    boolean loadConfidentialReservations = false;
                    if (isAuthenticated()) {
                        final List<Appointment> toBeDeleted = new ArrayList<Appointment>();
                        for (Appointment appointment : view.getCalendar().getAppointments()) {
                            for (PublicReservationData publicReservationData : publicReservations) {
                                final String reservationId =
                                        publicReservationData.getUserData() + publicReservationData.hashCode();
                                final String authenticatedUserName
                                        = injector.getAuthenticationManager()
                                                  .getAuthenticatedUserName(selectedTestbedConfiguration);
                                GWT.log("authenticatedUserName: " + authenticatedUserName);
                                GWT.log("appointment.getCreatedBy: " + appointment.getCreatedBy());
                                if (reservationId.equals(appointment.getId())
                                        // TODO SNO
                                        && injector.getAuthenticationManager()
                                                   .getAuthenticatedUserName(selectedTestbedConfiguration)
                                                   .equals(appointment.getCreatedBy())
                                        ) {
                                    toBeDeleted.add(appointment);
                                    GWT.log("loadPublicAndMaybeConfidentialReservations(): about to delete reservation made by "
                                            + appointment.getCreatedBy());
                                    loadConfidentialReservations = true;
                                }
                            }
                        }
                        for (Appointment appointment : toBeDeleted) {
                            view.getCalendar().removeAppointment(appointment);
                            reservationManager.getPublicReservations().remove(appointment.getId());
                        }

                        // The user is authenticated => load confidential reservations
                        if (loadConfidentialReservations) {
                            loadConfidentialReservations(pivotDate);
                        } else {
                            view.getLoadingIndicator().hideLoading();
                        }
                    } else {
                        view.getLoadingIndicator().hideLoading();
                    }
                } else {
                    GWT.log("No public reservation data found.");
                    view.getLoadingIndicator().hideLoading();
                }
            }
        });
    }

    /**
     * Call GWT-RPC getConfidentialReservations(...) from {@link eu.wisebed.wiseui.api.ReservationService}.
     * The resulting {@link eu.wisebed.wiseui.shared.dto.ConfidentialReservationData} are rendered in
     * the calendar widget.
     *
     * @param pivotDate Anchor date for fetching reservation data
     */
    private void loadConfidentialReservations(final Date pivotDate) {
        final String rsEndpointUrl = selectedTestbedConfiguration.getRsEndpointUrl();
        final List<SecretAuthenticationKey> secretAuthenticationKeys = new ArrayList<SecretAuthenticationKey>();
        final AuthenticationManager authenticationManager = injector.getAuthenticationManager();
        for (String urnPrefix : selectedTestbedConfiguration.getUrnPrefixList()) {
            secretAuthenticationKeys.add(authenticationManager.getMap().get(urnPrefix));
        }

        final ReservationService.Range range = calcRange();
        GWT.log("Loading private reservations for Testbed '" + selectedTestbedConfiguration.getName() + "'");

        reservationService.getConfidentialReservations(rsEndpointUrl, secretAuthenticationKeys, pivotDate, range,
                new AsyncCallback<List<ConfidentialReservationData>>() {

                    public void onFailure(final Throwable caught) {
                        GWT.log("Error fetching private reservation data!\n" + caught.getMessage());
                        eventBus.fireEvent(new ThrowableEvent(caught));
                    }

                    public void onSuccess(final List<ConfidentialReservationData> confidentialReservations) {
                        List<ConfidentialReservationData> filteredConfidentialReservations
                                = new ArrayList<ConfidentialReservationData>();
                        for (ConfidentialReservationData c : confidentialReservations) {
                            if ((c.getData() == null) || (c.getData().isEmpty())) {
                                continue;
                            }
                            final String userName = c.getData().get(0).getUsername();
                            final String urnPrefix = c.getData().get(0).getUrnPrefix();
                            // TODO
                            final String authenticatedUserName
                                    = injector.getAuthenticationManager()
                                              .getAuthenticatedUserName(selectedTestbedConfiguration);
                            if (authenticatedUserName.equals(userName)) {
                                filteredConfidentialReservations.add(c);
                            }
                        }
                        view.renderConfidentialReservations(filteredConfidentialReservations);
                        view.getLoadingIndicator().hideLoading();
                    }
                });
    }

    @Override
    public void registerPublicReservation(final String appointmentId,
                                          final PublicReservationData publicReservationData) {
        reservationManager.getPublicReservations().put(appointmentId, publicReservationData);
    }

    @Override
    public void registerConfidentialReservation(final String appointmentId,
                                                final ConfidentialReservationData confidentialReservationData) {
        reservationManager.getConfidentialReservations().put(appointmentId, confidentialReservationData);
    }

    @Override
    public void handleBackClicked() {
        final Date selectedDate = view.getCalendar().getDate();
        switch (view.getCalendar().getDays()) {
            case ONE_DAY:
                subtractDays(selectedDate, ONE_DAY);
                break;
            case WEEK:
                subtractDays(selectedDate, WEEK);
                break;
            default:
                subtractMonth(selectedDate);
        }
    }

    @Override
    public void handleForwardClicked() {
        final Date selectedDate = view.getCalendar().getDate();
        switch (view.getCalendar().getDays()) {
            case ONE_DAY:
                addDays(selectedDate, ONE_DAY);
                break;
            case WEEK:
                addDays(selectedDate, WEEK);
                break;
            default:
                addMonth(selectedDate);
        }
    }

    @Override
    public void handleTodayClicked() {
        final Date now = new Date();
        view.getCalendar().setDate(now);
        view.getDatePicker().setValue(now);
        loadReservations(now);
    }

    private void addDays(final Date current, final int days) {
        calendarService.addDays(current, days, new AsyncCallback<Date>() {

            public void onFailure(final Throwable caught) {
                GWT.log("Error in calendar service!\n" + caught.getMessage());
                eventBus.fireEvent(new ThrowableEvent(caught));
            }

            public void onSuccess(final Date result) {
                view.getCalendar().setDate(result, days);
                view.getDatePicker().setValue(result);
                loadReservations(result);
            }
        });
    }

    private void subtractDays(final Date current, final int days) {
        calendarService.subtractDays(current, days, new AsyncCallback<Date>() {

            public void onFailure(final Throwable caught) {
                GWT.log("Error in calendar service!\n" + caught.getMessage());
                eventBus.fireEvent(new ThrowableEvent(caught));
            }

            public void onSuccess(final Date result) {
                view.getCalendar().setDate(result, days);
                view.getDatePicker().setValue(result);
                loadReservations(result);
            }
        });
    }

    private void addMonth(final Date current) {
        calendarService.addMonth(current, new AsyncCallback<Date>() {

            public void onFailure(final Throwable caught) {
                GWT.log("Error in calendar service!\n" + caught.getMessage());
                eventBus.fireEvent(new ThrowableEvent(caught));
            }

            public void onSuccess(final Date result) {
                view.getCalendar().setDate(result);
                view.getDatePicker().setValue(result);
                loadReservations(result);
            }
        });
    }

    private void subtractMonth(final Date current) {
        calendarService.subtractMonth(current, new AsyncCallback<Date>() {

            public void onFailure(final Throwable caught) {
                GWT.log("Error in calendar service!\n" + caught.getMessage());
                eventBus.fireEvent(new ThrowableEvent(caught));
            }

            public void onSuccess(final Date result) {
                view.getCalendar().setDate(result);
                view.getDatePicker().setValue(result);
                loadReservations(result);
            }
        });
    }

    /**
     * Reload reservations on calendar. If the user is authenticated to at least one testbed
     * reload calendar by fetching private reservations in order to be aware of the secret
     * reservation keys that belong to this client.
     *
     * @param date Date selected in calendar widget
     */
    @Override
    public void loadReservations(final Date date) {
        loadPublicAndMaybeConfidentialReservations(date);
    }

    private ReservationService.Range calcRange() {
        ReservationService.Range range;
        switch (view.getCalendar().getDays()) {
            case ONE_DAY:
                range = ReservationService.Range.ONE_DAY;
                break;
            case WEEK:
                range = ReservationService.Range.WEEK;
                break;
            default:
                range = ReservationService.Range.MONTH;
        }
        return range;
    }

    @Override
    public void onUpdateNodesSelected(final UpdateNodesSelectedEvent event) {
        this.selectedNodes = event.getNodes();
    }

    @Override
    public void onReservationSuccess(final ReservationSuccessEvent event) {
        loadReservations(view.getCalendar().getDate());
    }

    @Override
    public void onReservationDeleteFailed(final ReservationDeleteFailedEvent event) {
        String errorMessage = messages.reservationDeleteFailed();
        Throwable throwable = null;
        if (event != null && event.getThrowable() != null) {
            throwable = event.getThrowable();
            errorMessage = messages.reservationDeleteFailed()
                    + "\n"
                    + throwable.getMessage();
        }
        MessageBox.error(messages.reservationDeleteFailedTitle(),
                errorMessage,
                throwable,
                null);
    }

    @Override
    public void onReservationDeleteSuccess(final ReservationDeleteSuccessEvent event) {
        MessageBox.success(messages.reservationDeleteSuccessTitle(), messages.reservationDeleteSuccess(), null);
        view.getCalendar().removeAppointment(event.getReservation());
    }

    @Override
    public boolean isAuthenticated() {
        boolean result = false;
        final AuthenticationManager authenticationManager = injector.getAuthenticationManager();
        if (selectedTestbedConfiguration != null) {
            result = authenticationManager.isAuthenticated(selectedTestbedConfiguration);
        }
        return result;
    }

    @Override
    public void onWisemlLoaded(final WisemlLoadedEvent event) {
        final Setup setup = event.getWiseml().getSetup();
        if (setup != null) {
            setupNodes = setup.getNode();
        }
    }
}