package eu.wisebed.wiseui.client.reservation.view;

import java.util.ArrayList;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

import eu.wisebed.wiseui.shared.SensorDetails;

public interface ReservationView extends IsWidget {

    void setPresenter(Presenter presenter);
    void renderNodes(ArrayList<SensorDetails> nodeList);
    void initRsView();
	
	public interface Presenter{
		void goTo(Place place);
		void makeReservation();
		boolean checkRsDetails();
	}
}
