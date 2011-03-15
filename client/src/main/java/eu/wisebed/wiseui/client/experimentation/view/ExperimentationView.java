package eu.wisebed.wiseui.client.experimentation.view;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;

import eu.wisebed.wiseui.widgets.experimentpanel.ExperimentPanelView;

public interface ExperimentationView extends IsWidget{
	
	void setPresenter(Presenter presenter);
	Presenter getPresenter();
	void initView(List<ExperimentPanelView> panels);

	public interface Presenter{
		void getUserReservations();
	}
}


