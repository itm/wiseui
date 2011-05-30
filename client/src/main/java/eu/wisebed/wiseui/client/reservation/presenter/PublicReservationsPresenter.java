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
import com.google.gwt.core.client.Scheduler;
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
import eu.wisebed.wiseui.client.WiseUiGinjector;
import eu.wisebed.wiseui.client.reservation.event.EditReservationEvent;
import eu.wisebed.wiseui.client.reservation.event.ReservationDeleteFailedEvent;
import eu.wisebed.wiseui.client.reservation.event.ReservationDeleteSuccessEvent;
import eu.wisebed.wiseui.client.reservation.event.ReservationSuccessEvent;
import eu.wisebed.wiseui.client.reservation.event.UpdateNodesSelectedEvent;
import eu.wisebed.wiseui.client.reservation.i18n.ReservationMessages;
import eu.wisebed.wiseui.client.reservation.view.PublicReservationsView;
import eu.wisebed.wiseui.client.testbedlist.event.TestbedSelectedEvent;
import eu.wisebed.wiseui.client.testbedselection.event.ThrowableEvent;
import eu.wisebed.wiseui.client.util.EventBusManager;
import eu.wisebed.wiseui.shared.dto.ConfidentialReservationData;
import eu.wisebed.wiseui.shared.dto.Node;
import eu.wisebed.wiseui.shared.dto.PublicReservationData;
import eu.wisebed.wiseui.shared.dto.SecretAuthenticationKey;
import eu.wisebed.wiseui.shared.dto.SecretReservationKey;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;
import eu.wisebed.wiseui.widgets.messagebox.MessageBox;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author John I. Gakos
 * @author Soenke Nommensen
 */
public class PublicReservationsPresenter implements PublicReservationsView.Presenter,
        TestbedSelectedEvent.ConfigurationSelectedHandler, UpdateNodesSelectedEvent.Handler,
        ReservationSuccessEvent.Handler, ThrowableEvent.ThrowableHandler, PlaceChangeEvent.Handler,
        ReservationDeleteSuccessEvent.Handler, ReservationDeleteFailedEvent.Handler{

	private WiseUiGinjector injector;
    private final EventBusManager eventBus;
    private final PublicReservationsView view;
    private final ReservationServiceAsync reservationService;
    private final CalendarServiceAsync calendarService;
    private TestbedConfiguration testbedConfiguration;
    private Set<Node> nodes;
    private ReservationMessages messages;

    @Inject
    public PublicReservationsPresenter(final WiseUiGinjector injector,
    								   final EventBus eventBus,
                                       final PublicReservationsView view,
                                       final ReservationServiceAsync reservationService,
                                       final CalendarServiceAsync calendarService,
                                       final ReservationMessages messages) {
    	this.injector = injector;
        this.eventBus = new EventBusManager(eventBus);
        this.view = view;
        this.reservationService = reservationService;
        this.calendarService = calendarService;
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
        
        view.getCalendar().addOpenHandler(new OpenHandler<Appointment>() {
            @Override
            public void onOpen(final OpenEvent<Appointment> event) {
            	if (!isAuthenticated()) return;	
                view.showReservationDetails(event.getTarget());
            }
        });
        view.getCalendar().addUpdateHandler(new UpdateHandler<Appointment>() {
            @Override
            public void onUpdate(final UpdateEvent<Appointment> event) {
            	if (!isAuthenticated()) return;
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
            public void onTimeBlockClick(final TimeBlockClickEvent<Date> event) {
            	if (!isAuthenticated()) return;
            	final Date startDate = event.getTarget();
            	Appointment reservation = new Appointment();
            	reservation.setStart(startDate);
                showEditReservationDialog(reservation, nodes);
            }
        });
        view.getDatePicker().addValueChangeHandler(new ValueChangeHandler<Date>() {
            @Override
            public void onValueChange(final ValueChangeEvent<Date> event) {
            	if (!isAuthenticated()) return;
                view.getCalendar().setDate(event.getValue());
                GWT.log("onValueChange() => loadReservations()");
                reloadCalendar(testbedConfiguration, event.getValue());
            }
        });
    }

    @Override
    public void onTestbedSelected(final TestbedSelectedEvent event) {
        testbedConfiguration = event.getConfiguration();
        reloadCalendar(testbedConfiguration, view.getCalendar().getDate());
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
        final ReservationService.Range range = calcRange();

        // Make the service call
        GWT.log("Loading public reservations for Testbed '" + testbedName + "'");
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
                    
                    // If user is authenticated remove from calendar all reservations that
                    // belong to him and re-render them with secret reservation keys
                    if (isAuthenticated()) view.removeUsersReservations(
                    		injector.getAuthenticationManager().getMap().get(
                    				testbedConfiguration.getUrnPrefixList().get(0)).getUsername());
                }
                view.getLoadingIndicator().hideLoading();
            }
        });
    }

    /**
     * Call GWT-RPC getPrivateReservations(...) from {@link eu.wisebed.wiseui.api.ReservationService}.
     * The resulting {@link eu.wisebed.wiseui.shared.dto.ConfidentialReservationData} are rendered in the calendar widget.
     */
    public void loadPrivateReservations(final Date current){    	
        final String urnPrefix = testbedConfiguration.getUrnPrefixList().get(0);
    	final String rsEndpointUrl = testbedConfiguration.getRsEndpointUrl();
    	final List<SecretAuthenticationKey> snaaKeys = injector.getAuthenticationManager().getSecretAuthenticationKeys();
    	final ReservationService.Range range = calcRange();
    	
        GWT.log("Loading private reservations for Testbed '" + testbedConfiguration.getName() + "'");
    	reservationService.getPrivateReservations(rsEndpointUrl, snaaKeys, current, range,
    			new AsyncCallback<List<ConfidentialReservationData>>(){

    		public void onFailure(final Throwable caught){
                GWT.log("Error fetching private reservation data!\n" + caught.getMessage());
                eventBus.fireEvent(new ThrowableEvent(caught));
    		}
    		
    		public void onSuccess(final List<ConfidentialReservationData> privateReservations){
    			for (ConfidentialReservationData reservation : privateReservations){
        			final Appointment privateReservationRendered = view.renderPrivateReservation(reservation);
    				SecretReservationKey rsKey = new SecretReservationKey();
    				rsKey.setUrnPrefix(reservation.getData().get(0).getUrnPrefix());
    				rsKey.setSecretReservationKey(reservation.getData().get(0).getSecretReservationKey());
    				injector.getReservationManager().addReservation(privateReservationRendered, rsKey);
    			}
    		}
    	});
    	
    }
 
    /**
     * Call GWT-RPC deleteReservation(...) from {@link eu.wisebed.wiseui.api.ReservationService}.
     * Deleting reservation from RS service. This action cannot be undone!
     */
    public void removeReservation(final Appointment reservation){
    	final String urnPrefix = testbedConfiguration.getUrnPrefixList().get(0);
    	final String rsEndpointUrl = testbedConfiguration.getRsEndpointUrl();
    	final List<SecretAuthenticationKey> snaaKeys = injector.getAuthenticationManager().getSecretAuthenticationKeys();
    	final List<SecretReservationKey> rsKeys = new ArrayList<SecretReservationKey>();
    	rsKeys.add(injector.getReservationManager().getPrivateReservationsMap().get(reservation));
    	
    	GWT.log("Removing reservation...");
    	reservationService.deleteReservation(rsEndpointUrl, snaaKeys, rsKeys, new AsyncCallback<String>() {
    		public void onFailure(final Throwable caught){
    			eventBus.fireEvent(new ReservationDeleteFailedEvent(caught));
    		}
    		public void onSuccess(final String result){
    			// TODO: Better server response handling
    			eventBus.fireEvent(new ReservationDeleteSuccessEvent(reservation));
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
        reloadCalendar(testbedConfiguration, today);
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
                reloadCalendar(testbedConfiguration, result);
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
                reloadCalendar(testbedConfiguration, result);
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
                reloadCalendar(testbedConfiguration, result);
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
                reloadCalendar(testbedConfiguration, result);
            }
        });
    }
    
    /**
     * Reload reservations on calendar. If the user is authenticated to at least one testbed
     * reload calendar by fetching private reservations in order to be aware of the secret
     * reservation keys that belong to this client.
     * @param testbedConfiguration
     * @param date
     */
    private void reloadCalendar(final TestbedConfiguration testbedConfiguration, final Date date){
        loadPublicReservations(date);
        if (isAuthenticated()){ 
        	loadPrivateReservations(date);
        }
    }
    
    private ReservationService.Range calcRange(){
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
    
    public void onUpdateNodesSelected(final UpdateNodesSelectedEvent event){
    	this.nodes = event.getNodes();
    }

    public void onReservationSuccess(){
    	reloadCalendar(testbedConfiguration, view.getCalendar().getDate());
    }

    public void showEditReservationDialog(final Appointment reservation, final Set<Node> nodes){
        eventBus.fireEventFromSource(new EditReservationEvent(reservation, nodes), this);
    }
    
    public void onReservationDeleteFailed(final ReservationDeleteFailedEvent event){
    	MessageBox.error(messages.reservationDeleteFailedTitle(), messages.reservationDeleteFailed(), null, null);
    }
    
    public void onReservationDeleteSuccess(final ReservationDeleteSuccessEvent event){
    	MessageBox.success(messages.reservationDeleteSuccessTitle(), messages.reservationDeleteSuccess(), null);
    	view.getCalendar().removeAppointment(event.getReservation());
    }
    
    @Override
    public boolean isAuthenticated(){
    	final boolean status = injector.getAuthenticationManager().isAuthenticated(testbedConfiguration);
    	if (!status) MessageBox.error(messages.loginRequiredTitle(), messages.loginRequired(), null, null);
    	return status;
    }
}
