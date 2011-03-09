package eu.wisebed.wiseui.client.testbedselection.view;

import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.IsWidget;

public interface RawWisemlView extends IsWidget {

	HasHTML getXmlHasHTML();
	
	void setPresenter(Presenter presenter);
	
	public interface Presenter {
		
	}
}
