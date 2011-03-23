package eu.wisebed.wiseui.client.reservation.view;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionModel;

import eu.wisebed.wiseui.shared.SensorDetails;
import eu.wisebed.wiseui.shared.TestbedConfiguration;

public interface ReservationView extends IsWidget {

    void setPresenter(Presenter presenter);
    void renderNodes(ArrayList<SensorDetails> nodeList);
    void initRsView();
    void reserveButton(boolean visible);
    void loginRequiredPanel(boolean visible);
    void renderTestbeds(List<TestbedConfiguration> testbeds);
    void setTestbedSelectionModel(SelectionModel<TestbedConfiguration> selectionModel);
    HasData<TestbedConfiguration> getTestbedList();
	
	public interface Presenter{
		void goTo(Place place);
		void makeReservation();
		boolean checkRsDetails();
	}
}
