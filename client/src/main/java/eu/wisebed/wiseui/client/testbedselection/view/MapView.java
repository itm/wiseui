package eu.wisebed.wiseui.client.testbedselection.view;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

import eu.wisebed.wiseui.client.testbedselection.TestbedSelectionPlace;
import eu.wisebed.wiseui.shared.dto.Coordinate;
import eu.wisebed.wiseui.widgets.loading.HasLoadingIndicator;

@ImplementedBy(MapViewImpl.class)
public interface MapView extends IsWidget {

	void setPresenter(Presenter presenter);
	
	void setTestbedCoordinate(Coordinate coordinate, String title, String description);
	
	void setTestbedShape(List<Coordinate> coordinates);
	
	HasLoadingIndicator getLoadingIndicator();
	
	public interface Presenter {
		
	}
}
