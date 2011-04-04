package eu.wisebed.wiseui.client.reservation.view;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.ImplementedBy;

import eu.wisebed.wiseui.client.reservation.ReservationPlace;
import eu.wisebed.wiseui.shared.TestbedConfiguration;

@ImplementedBy(TestbedsLoggedInViewImpl.class)
public interface TestbedsLoggedInView extends IsWidget{

	void renderTestbeds(List<TestbedConfiguration> testbeds);
	void setTestbedSelectionModel(
    		SelectionModel<TestbedConfiguration> selectionModel);
    HasData<TestbedConfiguration> getTestbedList();
    
	public interface Presenter{
		void setPlace(ReservationPlace place);
		void getTestbedsLoggedIn(List<String> urnPrefix);
		SingleSelectionModel<TestbedConfiguration> getTestbedSelectionModel();
	}
}