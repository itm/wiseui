package eu.wisebed.wiseui.client.experimentation.view;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;

import eu.wisebed.wiseui.client.experimentation.view.ExperimentView;

public interface ExperimentationView extends IsWidget {
	
	void setPresenter(Presenter presenter);
	Presenter getPresenter();
	void initView(List<ExperimentView> panels);

	public interface Presenter {
		void getUserReservations();
	}
}


