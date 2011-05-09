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

import java.util.Date;
import java.util.List;

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

import eu.wisebed.wiseui.api.ReservationService;
import eu.wisebed.wiseui.api.ReservationServiceAsync;
import eu.wisebed.wiseui.api.CalendarServiceAsync;
import eu.wisebed.wiseui.client.reservation.view.PublicReservationsView;
import eu.wisebed.wiseui.client.testbedlist.event.TestbedSelectedEvent;
import eu.wisebed.wiseui.client.testbedselection.event.ThrowableEvent;
import eu.wisebed.wiseui.client.util.EventBusManager;
import eu.wisebed.wiseui.shared.dto.PublicReservationData;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;
import eu.wisebed.wiseui.widgets.messagebox.MessageBox;

/**
 * @author John I. Gakos
 * @author Soenke Nommensen
 */
public class PublicReservationsPresenter implements PublicReservationsView.Presenter,
        TestbedSelectedEvent.ConfigurationSelectedHandler,
        ThrowableEvent.ThrowableHandler, PlaceChangeEvent.Handler {

    private final EventBusManager eventBus;
    private final PublicReservationsView view;
    private final ReservationServiceAsync service;
    private final CalendarServiceAsync calendarService;
    private TestbedConfiguration testbedConfiguration;

    @Inject
    public PublicReservationsPresenter(final EventBus eventBus,
                                       final PublicReservationsView view,
                                       final ReservationServiceAsync service,
                                       final CalendarServiceAsync calendarService) {
        this.eventBus = new EventBusManager(eventBus);
        this.view = view;
        this.service = service;
        this.calendarService = calendarService;
        bind();
    }

    public void bind() {
        eventBus.addHandler(PlaceChangeEvent.TYPE, this);
        eventBus.addHandler(TestbedSelectedEvent.TYPE, this);
        eventBus.addHandler(ThrowableEvent.TYPE, this);
        view.getCalendar().addOpenHandler(new OpenHandler<Appointment>() {
            @Override
            public void onOpen(OpenEvent<Appointment> event) {
                view.showReservationDetails(event.getTarget());
            }
        });
        // TODO Check for authorization, apply changes in reservation system
        view.getCalendar().addDeleteHandler(new DeleteHandler<Appointment>() {
            @Override
            public void onDelete(DeleteEvent<Appointment> event) {
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
            public void onUpdate(UpdateEvent<Appointment> event) {
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
        view.getCalendar().addTimeBlockClickHandler(new TimeBlockClickHandler<Date>() {
            @Override
            public void onTimeBlockClick(TimeBlockClickEvent<Date> event) {
                Date startDate = event.getTarget();
                // TODO Show popup to create a new reservation an add to the calendar.
                // This new reservation has to be sent back to the reservation system.
                GWT.log("Time block clicked");
            }
        });
        view.getDatePicker().addValueChangeHandler(new ValueChangeHandler<Date>() {
            @Override
            public void onValueChange(ValueChangeEvent<Date> event) {
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
        String testbedName = "<unknown>";
        if (testbedConfiguration != null) testbedName = testbedConfiguration.getName();
        GWT.log("Loading public reservations for Testbed '" + testbedName + "'");
        if (testbedConfiguration == null) {
            final String errorMessage = "Reservation URL not found for Testbed '" + testbedName + "'";
            GWT.log(errorMessage);
            MessageBox.warning("Configuration could not be loaded!",
                    errorMessage,
                    null);
        }

        // Remove all current calendar entries
        view.removeAllReservations();

        ReservationService.Range range;
        if (view.getCalendar().getDays() == ONE_DAY) {
            range = ReservationService.Range.ONE_DAY;
        } else if (view.getCalendar().getDays() == WEEK) {
            range = ReservationService.Range.WEEK;
        } else {
            range = ReservationService.Range.MONTH;
        }

        // Make the service call
        service.getPublicReservations(testbedConfiguration.getRsEndpointUrl(),
                current, range, new AsyncCallback<List<PublicReservationData>>() {

                    public void onFailure(Throwable caught) {
                        GWT.log("Error fetching reservation data!\n" + caught.getMessage());
                        eventBus.fireEvent(new ThrowableEvent(caught));
                    }

                    public void onSuccess(final List<PublicReservationData> publicReservations) {
                        if (publicReservations == null || publicReservations.isEmpty()) {
                            final String errorMessage = "No public reservation data found.";
                            GWT.log(errorMessage);
                            MessageBox.warning("Error fetching reservation data!", errorMessage, null);
                        } else {
                            // Render the {@link eu.wisebed.wiseui.shared.dto.ReservationDetails}
                            view.renderPublicReservations(publicReservations);
                        }
                    }
                });

        view.getLoadingIndicator().hideLoading();
    }

    public void handleBackClicked() {
        final Date current = view.getCalendar().getDate();
        if (view.getCalendar().getDays() == ONE_DAY) {
            subtractDays(current, ONE_DAY);
        } else if (view.getCalendar().getDays() == WEEK) {
            subtractDays(current, WEEK);
        } else {
            subtractMonth(current);
        }
    }

    public void handleForwardClicked() {
        final Date current = view.getCalendar().getDate();
        if (view.getCalendar().getDays() == ONE_DAY) {
            addDays(current, ONE_DAY);
        } else if (view.getCalendar().getDays() == WEEK) {
            addDays(current, WEEK);
        } else {
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
}
