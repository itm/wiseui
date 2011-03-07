package eu.wisebed.wiseui.client.testbedselection.view;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;

import eu.wisebed.wiseui.client.testbedselection.TestbedSelectionPlace;
import eu.wisebed.wiseui.shared.wiseml.Coordinate;

public interface MapView extends IsWidget {

	void setPresenter(Presenter presenter);
	
	void setTestbedCoordinate(Coordinate coordinate, String title, String description);
	
	void setTestbedShape(List<Coordinate> coordinates);
	
	public interface Presenter {
		void setPlace(TestbedSelectionPlace place);
	}
}
