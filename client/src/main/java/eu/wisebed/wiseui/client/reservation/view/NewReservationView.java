package eu.wisebed.wiseui.client.reservation.view;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

import eu.wisebed.wiseui.shared.ReservationDetails;
import eu.wisebed.wiseui.shared.SensorDetails;

@ImplementedBy(NewReservationViewImpl.class)
public interface NewReservationView extends IsWidget {

	void setPresenter(Presenter presenter);
    void renderNodes(ArrayList<SensorDetails> nodeList);
    void enableNewReservationView();
	boolean checkReservationDetails();
    ReservationDetails getReservationDetails();
	Date getDateValue();
	Integer[] getStartTime();
	Integer[] getStopTime();
	ArrayList<SensorDetails> getNodesSelected();
	String getImageFileName();
	String getImageFileNameField();

	
	public interface Presenter{
		boolean checkReservationDetails();
	}
}
