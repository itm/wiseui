package eu.wisebed.wiseui.client.reservation.presenter;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.inject.Inject;

import eu.wisebed.wiseui.api.ReservationService;
import eu.wisebed.wiseui.api.ReservationServiceAsync;
import eu.wisebed.wiseui.api.TestbedConfigurationService;
import eu.wisebed.wiseui.api.TestbedConfigurationServiceAsync;
import eu.wisebed.wiseui.client.WiseUiGinjector;
import eu.wisebed.wiseui.client.reservation.event.NewReservationEvent;
import eu.wisebed.wiseui.client.reservation.event.ReservationFailedEvent;
import eu.wisebed.wiseui.client.reservation.event.ReservationSuccessEvent;
import eu.wisebed.wiseui.client.reservation.view.ReservationView;
import eu.wisebed.wiseui.client.reservation.view.ReservationView.Presenter;
import eu.wisebed.wiseui.client.util.AuthenticationManager;
import eu.wisebed.wiseui.shared.ReservationDetails;
import eu.wisebed.wiseui.shared.SensorDetails;
import eu.wisebed.wiseui.shared.TestbedConfiguration;
import eu.wisebed.wiseui.shared.exception.AuthenticationException;
import eu.wisebed.wiseui.shared.exception.ReservationConflictException;
import eu.wisebed.wiseui.shared.exception.ReservationException;
import eu.wisebed.wiseui.shared.wiseml.SecretAuthenticationKey;

public class ReservationPresenter implements Presenter{

	private final ReservationView view;
	private ReservationServiceAsync service;
	private PlaceController placeController;
	private EventBus eventBus;
	private SingleSelectionModel<TestbedConfiguration> testbedSelectionModel;
	private WiseUiGinjector injector;
	private final ListDataProvider<TestbedConfiguration> dataProvider = 
		new ListDataProvider<TestbedConfiguration>();

	@Inject
	public ReservationPresenter(final WiseUiGinjector injector, final ReservationView view, 
			final EventBus eventBus){
		this.view = view;
		this.eventBus = eventBus;
		this.injector = injector;
		dataProvider.addDataDisplay(view.getTestbedList());
		
        // Init selection model
        testbedSelectionModel = new SingleSelectionModel<TestbedConfiguration>();
        view.setTestbedSelectionModel(testbedSelectionModel);
	}

	public void bindEvents() {
		testbedSelectionModel.addSelectionChangeHandler(new Handler() {
			public void onSelectionChange(final SelectionChangeEvent event) {
				onTestbedSelectionChange(event);
			}
		});
	}
	
	/**
	 * Send the urn prefix to the server to identify the testbed the user
	 * has previously logged in.
	 */
	public void getTestbedLoggedIn(final List<String> urnPrefix){
		// TODO: DI
		final TestbedConfigurationServiceAsync service = 
			GWT.create(TestbedConfigurationService.class);
		service.getTestbedLoggedIn(urnPrefix, 
				new AsyncCallback<List<TestbedConfiguration>>(){
			public void onFailure(Throwable caught){
				GWT.log("Failed rpc");
			}
			public void onSuccess(List<TestbedConfiguration> testbeds){
				if(testbeds == null){
					Window.alert("No testbeds identified");
				}else{
					GWT.log("Number of testbeds logged in:" + testbeds.size());
					for(TestbedConfiguration bed: testbeds){
						dataProvider.getList().add(bed);
					}
					testbedSelectionModel.setSelected(testbeds.get(0), true);
				}
			}
		});
	}

	/**
	 * Makes an Asynchronous callback to the server 'asking' for the 
	 * nodes located in the network. Result consists of an array with all
	 * sensors' useful information
	 */
	public void getNetwork(final String sessionManagementEndpointUrl) {
		GWT.log("Getting infrastructure for:" + sessionManagementEndpointUrl);
		final ReservationServiceAsync service = 
			GWT.create(ReservationService.class);
		service.getNodeList(sessionManagementEndpointUrl,
				new AsyncCallback<ArrayList<SensorDetails>>(){
			public void onFailure(Throwable caught){
				GWT.log("Failed rpc");
			}
			public void onSuccess(ArrayList<SensorDetails> nodeList){
				if(nodeList==null){
					Window.alert("No nodes returned to client");
					// TODO SNO We can use the MessageBox here
				}else{
					view.renderNodes(nodeList);
				}
			}
		});
	}

	public void initRsView() {
		view.initRsView();
	}

	/**
	 * Render login required information
	 */
	public void loginRequired() {
		view.loginRequiredPanel(true);
		// FIXME: This shouldn't be here, but i will come back for the view
		//		  later
		view.reserveButton(false);
	}

	public void renderTestbeds(List<TestbedConfiguration> testbeds) {
		view.renderTestbeds(testbeds);
	}
	
	/**
	 * Check if user has set all required reservation details
	 */
	public boolean checkReservationDetails() {
		
		// Temporarily checking only for the image
		if (!view.checkReservationDetails()){
			eventBus.fireEvent(new ReservationFailedEvent());
			return false;
		}
		return true;
	}

	/**
	 * Sends reservation details to the server after collecting time, date and
	 * sensors as information about the experiment to be scheduled.
	 */
	public void makeReservation(final String rsEndpointUrl, final String urn){
		final ReservationServiceAsync service = 
			GWT.create(ReservationService.class);
		final ReservationDetails data = view.getReservationDetails();	
		AuthenticationManager authenticationManager = injector.getAuthenticationManager();
		SecretAuthenticationKey secretAuthenticationKey = 
			authenticationManager.getKeyHash().get(urn);
		GWT.log("Key:" + secretAuthenticationKey.getSecretAuthenticationKey());
		eventBus.fireEvent(new NewReservationEvent()); // New reservation occurred
		service.makeReservation(secretAuthenticationKey, rsEndpointUrl, data, 
				new AsyncCallback<String>(){
				public void onFailure(Throwable caught){
					if(caught instanceof AuthenticationException){
						GWT.log("User not authorized to make reservations");
					}else if(caught instanceof ReservationException){
						GWT.log("RS system failed");
					}else if(caught instanceof ReservationConflictException){
						GWT.log("Cannot make reservations some nodes are " +
								"already reserved");
					}else{
						GWT.log("Error occured while contacting the server");
					}
					eventBus.fireEvent(new ReservationFailedEvent());
				}
				public void onSuccess(String result) {
					eventBus.fireEvent(new ReservationSuccessEvent());
				}
			});
	}

	public SingleSelectionModel<TestbedConfiguration> getTestbedSelectionModel(){
		return testbedSelectionModel;
	}
	
	private void onTestbedSelectionChange(final SelectionChangeEvent event){
		TestbedConfiguration bed = testbedSelectionModel.getSelectedObject();
		getNetwork(bed.getSessionmanagementEndpointUrl());
	}

	/**
	 * Navigate to a new Place in the browser
	 */
	public void goTo(Place place) {
		placeController.goTo(place);
	}
}
