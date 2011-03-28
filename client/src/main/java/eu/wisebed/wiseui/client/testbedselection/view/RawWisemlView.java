package eu.wisebed.wiseui.client.testbedselection.view;

import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

import eu.wisebed.wiseui.widgets.loading.HasLoadingIndicator;

@ImplementedBy(RawWisemlViewImpl.class)
public interface RawWisemlView extends IsWidget {

	HasHTML getXmlHasHTML();
	
	void setPresenter(Presenter presenter);
	
	HasLoadingIndicator getLoadingIndicator();
	
	public interface Presenter {
		
	}
}
