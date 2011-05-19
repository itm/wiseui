package eu.wisebed.wiseui.client.experimentation.view;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

import eu.wisebed.wiseui.shared.dto.ReservationDetails;
import eu.wisebed.wiseui.widgets.loading.HasLoadingIndicator;

@ImplementedBy(ExperimentationViewImpl.class)
public interface ExperimentationView extends IsWidget {
	
	void setPresenter(Presenter presenter);
	
	Presenter getPresenter();
	
    HasLoadingIndicator getLoadingIndicator();
    
    void renderUserReservations(List<ReservationDetails> reservations);

	public interface Presenter {
		void getUserReservations(List<String> urnPrefixList,String rsEndpointUrl);
		void refreshUserExperiments();
	}
}


