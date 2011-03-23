package eu.wisebed.wiseui.client.reservation.presenter;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import eu.wisebed.wiseui.api.ReservationService;
import eu.wisebed.wiseui.api.ReservationServiceAsync;
import eu.wisebed.wiseui.client.reservation.ReservationActivity;
import eu.wisebed.wiseui.client.reservation.view.ReservationView;
import eu.wisebed.wiseui.client.reservation.view.ReservationView.Presenter;
import eu.wisebed.wiseui.shared.dto.SensorDetails;
import eu.wisebed.wiseui.shared.dto.SensorDetails;

public class ReservationPresenter implements Presenter{
	
	private final ReservationView view;
    private ReservationServiceAsync service;
	private PlaceController placeController;
    
	@Inject
	public ReservationPresenter(final ReservationView view){
		this.view = view;
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
					GWT.log("Network nodes:" + nodeList);
				}
			}
		});
	}
	
	public void initRsView() {
		view.initRsView();
	}
	public void loginRequired() {
    	view.loginRequiredPanel(true);
    	view.reserveButton(false);
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
