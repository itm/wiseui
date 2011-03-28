package eu.wisebed.wiseui.client.reservation.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.ImplementedBy;

import eu.wisebed.wiseui.shared.ReservationDetails;
import eu.wisebed.wiseui.shared.SensorDetails;
import eu.wisebed.wiseui.shared.TestbedConfiguration;

@ImplementedBy(ReservationViewImpl.class)
public interface ReservationView extends IsWidget {

    void setPresenter(Presenter presenter);
    void renderNodes(ArrayList<SensorDetails> nodeList);
    void initRsView();
    void reserveButton(boolean visible);
    void loginRequiredPanel(boolean visible);
    void renderTestbeds(List<TestbedConfiguration> testbeds);
    void setTestbedSelectionModel(SelectionModel<TestbedConfiguration> selectionModel);
	boolean checkReservationDetails();
    ReservationDetails getReservationDetails();
	Date getDateValue();
	Integer[] getStartTime();
	Integer[] getStopTime();
	ArrayList<SensorDetails> getNodesSelected();
    HasData<TestbedConfiguration> getTestbedList();
	String getImageFileName();
	String getImageFileNameField();

	
	public interface Presenter{
		void goTo(Place place);
		void makeReservation(String rsEndpointUrl, String urn);
		SingleSelectionModel<TestbedConfiguration> getTestbedSelectionModel();
		boolean checkReservationDetails();
	}
}
