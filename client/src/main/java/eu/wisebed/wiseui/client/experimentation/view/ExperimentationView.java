package eu.wisebed.wiseui.client.experimentation.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

import eu.wisebed.wiseui.widgets.loading.HasLoadingIndicator;

@ImplementedBy(ExperimentationViewImpl.class)
public interface ExperimentationView extends IsWidget {
	
	void setPresenter(Presenter presenter);
	
	Presenter getPresenter();
	
    HasLoadingIndicator getLoadingIndicator();
    
    void renderUserReservations();

	public interface Presenter {
		void getUserReservations();
	}
}


