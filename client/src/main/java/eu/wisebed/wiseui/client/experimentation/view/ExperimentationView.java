package eu.wisebed.wiseui.client.experimentation.view;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

import eu.wisebed.wiseui.client.experimentation.view.ExperimentView;

@ImplementedBy(ExperimentationViewImpl.class)
public interface ExperimentationView extends IsWidget {
	
	void setPresenter(Presenter presenter);
	Presenter getPresenter();
	void initView(List<ExperimentView> panels);
	void resetExperimentContainer();


	public interface Presenter {
		void getUserReservations();
	}
}


