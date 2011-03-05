package eu.wisebed.wiseui.client.testbedselection.view;

import com.google.gwt.user.client.ui.IsWidget;

import eu.wisebed.wiseui.client.testbedselection.TestbedSelectionPlace;

public interface NodeDetailView extends IsWidget {

	void setPresenter(Presenter presenter);
	
	public interface Presenter {
		
		void setPlace(TestbedSelectionPlace place);
	}
}
