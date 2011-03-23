package eu.wisebed.wiseui.client.reservation.presenter;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;

import eu.wisebed.wiseui.api.ReservationService;
import eu.wisebed.wiseui.api.ReservationServiceAsync;
import eu.wisebed.wiseui.api.TestbedConfigurationService;
import eu.wisebed.wiseui.api.TestbedConfigurationServiceAsync;
import eu.wisebed.wiseui.client.reservation.ReservationActivity;
import eu.wisebed.wiseui.client.reservation.view.ReservationView;
import eu.wisebed.wiseui.client.reservation.view.ReservationView.Presenter;
import eu.wisebed.wiseui.client.testbedselection.common.UrnPrefixInfo;
import eu.wisebed.wiseui.shared.SensorDetails;
import eu.wisebed.wiseui.shared.TestbedConfiguration;

public class ReservationPresenter implements Presenter{

	private final ReservationView view;
	private ReservationServiceAsync service;
	private PlaceController placeController;
	private SingleSelectionModel<TestbedConfiguration> testbedSelectionModel;
	private final ListDataProvider<TestbedConfiguration> dataProvider = 
		new ListDataProvider<TestbedConfiguration>();

	@Inject
	public ReservationPresenter(final ReservationView view){
		this.view = view;
		dataProvider.addDataDisplay(view.getTestbedList());
		
        // Init selection model
        testbedSelectionModel = new SingleSelectionModel<TestbedConfiguration>();
        view.setTestbedSelectionModel(testbedSelectionModel);
	}

	/**
	 * Send the urn prefix to the server to identify the testbed the user
	 * has previously logged in.
	 */
	public void getTestbedLoggedIn(final List<String> urnPrefix){
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
					for(int i=0; i<testbeds.size(); i++){
						GWT.log("Logged in to: " + testbeds.get(i).getName());
						dataProvider.getList().add(testbeds.get(i));
					}
				}
			}
		});
	}

	/**
	 * Makes an Asynchronous callback to the server 'asking' for the 
	 * nodes located in the network. Result consists of an array with all
	 * sensors' useful information
	 */
	public void getNetwork() {
		final ReservationServiceAsync service = GWT.create(ReservationService.class);
		service.getNodeList(new AsyncCallback<ArrayList<SensorDetails>>(){
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
	public boolean checkRsDetails() {
		// TODO: Add functionality while integrating
		return false;
	}

	/**
	 * Sends reservation details to the server after collecting time, date and
	 * sensors as information about the experiment to be scheduled.
	 */
	public void makeReservation() {
		// TODO: Add functionality while integrating
	}

	/**
	 * Navigate to a new Place in the browser
	 */
	public void goTo(Place place) {
		placeController.goTo(place);
	}
}
