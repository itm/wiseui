package eu.wisebed.wiseui.client.experimentation.presenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;

import eu.wisebed.wiseui.client.WiseUiGinjector;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentView;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentationView;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentationView.Presenter;
import eu.wisebed.wiseui.client.experimentation.presenter.ExperimentPresenter.Button;
import eu.wisebed.wiseui.client.experimentation.presenter.ExperimentPresenter.Callback;

public class ExperimentationPresenter implements Presenter{
	
    private final WiseUiGinjector injector;
    private ExperimentationView view;
	private EventBus eventBus;



    @Inject
    public ExperimentationPresenter(final WiseUiGinjector injector,
    		final ExperimentationView view,final EventBus eventBus){
    	this.injector = injector;
    	this.view = view;
    	this.eventBus = eventBus;
        view.setPresenter(this);
    }
    
	public void setView(ExperimentationView view) {
		this.view = view;
	}

	public ExperimentationView getView() {
		return view;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void getUserReservations() {
		// TODO After RPC some reservations might have arrived
		// those reservations fill the experiments list and then added to the view
        GWT.log("Get Reservations/Experiments");
        List<ExperimentView> experimentViews = new ArrayList<ExperimentView>();
		
        // fake data -->
        Date startDate = new Date();
        Date stopDate = new Date();
        stopDate.setMinutes(startDate.getMinutes() + 1);
        List<String> urns = new ArrayList<String>();
        urns.add("node1");
        urns.add("node2");
        urns.add("node3");
        final ExperimentPresenter experiment = injector.getExperimentPresenter();
        experiment.initialize(1, startDate, stopDate, urns, 
        	"uploadedsampleimage1.bin",new Callback() { // TODO GINject

				@Override
				public void onButtonClicked(Button button) {
					switch(button){
						case START:
							experiment.setAsRunningExperiment();
							break;
						case STOP:
							experiment.setAsTerminatedExperiment();
							break;
						case SHOWHIDE:
							experiment.getView().showHideNodeOutput();
							break;
						case CANCEL:
							experiment.setAsCancelledExperiment();
							break;
					}
				} 
        });
        experimentViews.add(experiment.getView());
        // --> end of fake data

        // fill view with these views
        view.initView(experimentViews);
	}
}
