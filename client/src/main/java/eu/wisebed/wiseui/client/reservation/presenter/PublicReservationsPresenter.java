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

import com.bradrydzewski.gwt.calendar.client.Appointment;
import com.bradrydzewski.gwt.calendar.client.event.DeleteEvent;
import com.bradrydzewski.gwt.calendar.client.event.DeleteHandler;
import com.bradrydzewski.gwt.calendar.client.event.TimeBlockClickEvent;
import com.bradrydzewski.gwt.calendar.client.event.TimeBlockClickHandler;
import com.bradrydzewski.gwt.calendar.client.event.UpdateEvent;
import com.bradrydzewski.gwt.calendar.client.event.UpdateHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
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
import eu.wisebed.wiseui.client.reservation.event.EditReservationEvent;
import eu.wisebed.wiseui.client.reservation.event.ReservationSuccessEvent;
import eu.wisebed.wiseui.client.reservation.event.UpdateNodesSelectedEvent;
import eu.wisebed.wiseui.client.reservation.view.PublicReservationsView;
import eu.wisebed.wiseui.client.testbedlist.event.TestbedSelectedEvent;
import eu.wisebed.wiseui.client.testbedselection.event.ThrowableEvent;
import eu.wisebed.wiseui.client.util.EventBusManager;
import eu.wisebed.wiseui.shared.dto.Node;
import eu.wisebed.wiseui.shared.dto.PublicReservationData;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;
import eu.wisebed.wiseui.widgets.messagebox.MessageBox;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author John I. Gakos
 * @author Soenke Nommensen
 */
public class PublicReservationsPresenter implements PublicReservationsView.Presenter,
        TestbedSelectedEvent.ConfigurationSelectedHandler, UpdateNodesSelectedEvent.Handler,
        ReservationSuccessEvent.Handler, ThrowableEvent.ThrowableHandler, PlaceChangeEvent.Handler {

    private final EventBusManager eventBus;
    private final PublicReservationsView view;
    private final ReservationServiceAsync reservationService;
    private final CalendarServiceAsync calendarService;
    private TestbedConfiguration testbedConfiguration;
    private Set<Node> nodes;

    @Inject
    public PublicReservationsPresenter(final EventBus eventBus,
                                       final PublicReservationsView view,
                                       final ReservationServiceAsync service,
                                       final CalendarServiceAsync calendarService) {
        this.eventBus = new EventBusManager(eventBus);
        this.view = view;
        this.reservationService = service;
        this.calendarService = calendarService;
        bind();
    }

    private void bind() {
        eventBus.addHandler(PlaceChangeEvent.TYPE, this);
        eventBus.addHandler(TestbedSelectedEvent.TYPE, this);
        eventBus.addHandler(ThrowableEvent.TYPE, this);
        eventBus.addHandler(UpdateNodesSelectedEvent.TYPE, this);
        eventBus.addHandler(ReservationSuccessEvent.TYPE, this);
        
        view.getCalendar().addOpenHandler(new OpenHandler<Appointment>() {
            @Override
            public void onOpen(final OpenEvent<Appointment> event) {
                view.showReservationDetails(event.getTarget());
            }
        });
        // TODO Check for authorization, apply changes in reservation system
        view.getCalendar().addDeleteHandler(new DeleteHandler<Appointment>() {
            @Override
            public void onDelete(final DeleteEvent<Appointment> event) {
                boolean commit = Window
                        .confirm(
                                "Are you sure you want to delete appointment \""
                                        + event.getTarget().getTitle() + "\"");
                if (!commit) {
                    event.setCancelled(true);
                    GWT.log("Cancelled Appointment deletion");
                }
            }
        });
        // TODO Check for authorization, apply changes in reservation system
        view.getCalendar().addUpdateHandler(new UpdateHandler<Appointment>() {
            @Override
            public void onUpdate(final UpdateEvent<Appointment> event) {
                boolean commit = Window
                        .confirm(
                                "Are you sure you want to update the appointment \""
                                        + event.getTarget().getTitle() + "\"");
                if (!commit) {
                    event.setCancelled(true);
                    GWT.log("Cancelled Appointment update");
                }
            }
        });
        // TODO Check for authorization, apply changes in reservation system
        view.getCalendar().addTimeBlockClickHandler(new TimeBlockClickHandler<Date>() {
            @Override
            public void onTimeBlockClick(final TimeBlockClickEvent<Date> event) {
            	final Date startDate = event.getTarget();
            	Appointment reservation = new Appointment();
            	reservation.setStart(startDate);
                showEditReservationDialog(reservation, nodes);
            }
        });
        // TODO Check for authorization, apply changes in reservation system
        view.getDatePicker().addValueChangeHandler(new ValueChangeHandler<Date>() {
            @Override
            public void onValueChange(final ValueChangeEvent<Date> event) {
                view.getCalendar().setDate(event.getValue());
                GWT.log("onValueChange() => loadPublicReservations()");
                loadPublicReservations(view.getCalendar().getDate());
            }
        });
    }

    @Override
    public void onTestbedSelected(final TestbedSelectedEvent event) {
        this.testbedConfiguration = event.getConfiguration();
        loadPublicReservations(view.getCalendar().getDate());
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
        if (testbedConfiguration != null && testbedConfiguration.getName() != null) {
            MessageBox.error("Error fetching reservation data for testbed '"
                    + testbedConfiguration.getName()
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
     */
    @Override
    public void loadPublicReservations(final Date current) {
        view.getLoadingIndicator().showLoading("Reservations");

        // Logging
        String testbedName = "<Unknown testbed>";
        if (testbedConfiguration != null) {
            testbedName = testbedConfiguration.getName();
        }
        GWT.log("Loading public reservations for Testbed '" + testbedName + "'");

        if (testbedConfiguration == null) {
            final String errorMessage = "Reservation URL not found for Testbed '" + testbedName + "'";
            GWT.log(errorMessage);
            MessageBox.warning("Configuration could not be loaded!",
                    errorMessage,
                    null);
        }

        // Remove all current calendar entries
        view.removeAllAppointments();

        // Determine the date range, which shall be passed to the remote call
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

        // Make the service call
        reservationService.getPublicReservations(testbedConfiguration.getRsEndpointUrl(),
                current, range, new AsyncCallback<List<PublicReservationData>>() {

                    public void onFailure(Throwable caught) {
                        view.getLoadingIndicator().hideLoading();
                        GWT.log("Error fetching reservation data!\n" + caught.getMessage());
                        eventBus.fireEvent(new ThrowableEvent(caught));
                    }

                    public void onSuccess(final List<PublicReservationData> publicReservations) {
                        if (publicReservations == null || publicReservations.isEmpty()) {
                            GWT.log("No public reservation data found.");
                        } else {
                            // Render the {@link eu.wisebed.wiseui.shared.dto.ReservationDetails}
                            view.renderPublicReservations(publicReservations);
                        }
                        view.getLoadingIndicator().hideLoading();
                    }
                });
    }

    public void handleBackClicked() {
        final Date current = view.getCalendar().getDate();
        switch (view.getCalendar().getDays()) {
            case ONE_DAY:
                subtractDays(current, ONE_DAY);
                break;
            case WEEK:
                subtractDays(current, WEEK);
                break;
            default:
                subtractMonth(current);
        }
    }

    public void handleForwardClicked() {
        final Date current = view.getCalendar().getDate();
        switch (view.getCalendar().getDays()) {
            case ONE_DAY:
                addDays(current, ONE_DAY);
                break;
            case WEEK:
                addDays(current, WEEK);
                break;
            default:
                addMonth(current);
        }
    }

    public void handleTodayClicked() {
        final Date today = new Date();
        view.getCalendar().setDate(today);
        view.getDatePicker().setValue(today);
        loadPublicReservations(today);
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
                loadPublicReservations(result);
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
                loadPublicReservations(result);
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
                loadPublicReservations(result);
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
                loadPublicReservations(result);
            }
        });
    }
    
    public void onUpdateNodesSelected(final UpdateNodesSelectedEvent event){
    	this.nodes = event.getNodes();
    }

    public void onReservationSuccess(){
    	loadPublicReservations(view.getCalendar().getDate());
    }

    public void showEditReservationDialog(final Appointment reservation, final Set<Node> nodes){
        eventBus.fireEventFromSource(new EditReservationEvent(reservation, nodes), this);
    }
}
