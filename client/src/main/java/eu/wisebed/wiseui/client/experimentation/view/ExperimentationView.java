package eu.wisebed.wiseui.client.experimentation.view;

import java.util.Date;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

import eu.wisebed.wiseui.widgets.loading.HasLoadingIndicator;

@ImplementedBy(ExperimentationViewImpl.class)
public interface ExperimentationView extends IsWidget {
	
	void setPresenter(Presenter presenter);
	
	Presenter getPresenter();
	
    HasLoadingIndicator getLoadingIndicator();
    
    void addExperimentPanel(ExperimentView experiment);
    
	void clearExperimentationPanel();
	
	Date getFromDate();
	
	Date getToDate();

	public interface Presenter {
		void getUserReservations();
		void refreshUserExperiments();
	}
}


