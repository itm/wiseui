package eu.wisebed.wiseui.client.reservation;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import eu.wisebed.wiseui.api.ReservationServiceAsync;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import eu.wisebed.wiseui.api.ReservationService;
import eu.wisebed.wiseui.client.reservation.view.ReservationView;
import eu.wisebed.wiseui.client.reservation.view.ReservationView.Presenter;
import eu.wisebed.wiseui.shared.SensorDetails;

public class ReservationActivity extends AbstractActivity implements Presenter {

	private static final Logger LOGGER =
		Logger.getLogger(ReservationActivity.class.getName());

    private ReservationView view;
    private PlaceController placeController;
    private ReservationServiceAsync service;
    
    @Inject
    public ReservationActivity(final ReservationView view, 
    		final EventBus eventBus) {
        this.view = view;
    }

    public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
        view.setPresenter(this);
        view.initRsView();
		panel.setWidget(view.asWidget());
		service = GWT.create(ReservationService.class);
        panel.setWidget(view);
        getNetwork();
    }
    
	/**
	 * Makes an Asynchronous callback to the server 'asking' for the 
	 * nodes located in the network. Result consists of an array with all
	 * sensors' useful information
	 */
	public void getNetwork() {
		service.getNodeList(new AsyncCallback<ArrayList<SensorDetails>>(){
			public void onFailure(Throwable caught){
				LOGGER.log(Level.INFO,"Failed rpc");
			}
			public void onSuccess(ArrayList<SensorDetails> nodeList){
				if(nodeList==null){
					Window.alert("No nodes returned to client");
                    // TODO SNO We can use the MessageBox here
				}else{
					view.renderNodes(nodeList);
					LOGGER.log(Level.INFO,"Network nodes:" + nodeList);
				}
			}
		});
	}
	
	/**
	 * Sends reservation details to the server after collecting time, date and
	 * sensors as information about the experiment to be scheduled.
	 */
	public void makeReservation() {
		// TODO: Add functionality while integrating
	}
	
	/**
	 * Check if user has set all required reservation details
	 */
	public boolean checkRsDetails() {
		// TODO: Add functionality while integrating
		return false;
	}

	/**
	 * Navigate to a new Place in the browser
	 */
	public void goTo(Place place) {
		placeController.goTo(place);
	}
}
