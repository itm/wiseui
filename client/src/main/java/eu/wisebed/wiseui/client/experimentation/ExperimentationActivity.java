package eu.wisebed.wiseui.client.experimentation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import eu.wisebed.wiseui.client.WiseUiGinjector;
import eu.wisebed.wiseui.client.experimentation.Experiment.Button;
import eu.wisebed.wiseui.client.experimentation.Experiment.Callback;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentView;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentationView;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentationView.Presenter;

public class ExperimentationActivity extends AbstractActivity implements
        Presenter {

    private final WiseUiGinjector injector;
    private ExperimentationView view;
    private List<ExperimentView> experiments;

    @Inject
    public ExperimentationActivity(final WiseUiGinjector injector) {
        this.injector = injector;
        experiments = new ArrayList<ExperimentView>();
    }

    public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
        
    	// init view
    	view = injector.getExperimentationView();
        view.setPresenter(this);
        
        // get reservations of a user
        getUserReservations();
        
        view.initView(experiments);
        panel.setWidget(view);
    }
    
	@Override
	public void getUserReservations() {
		// TODO After RPC some reservations might have arrived
		// those reservations fill the experiments list and then added to the view
        
		// fake data -->
        Date startDate = new Date();
        Date stopDate = new Date();
        stopDate.setMinutes(startDate.getMinutes() + 1);
        List<String> urns = new ArrayList<String>();
        urns.add("node1");
        urns.add("node2");
        urns.add("node3");
        final Experiment experiment = injector.getExperiment();
        experiment.initialize(1, startDate, stopDate, urns, 
        	"uploadedsampleimage1.bin",new Callback() {

				@Override
				public void onButtonClicked(Button button) {
					GWT.log(button.getValue() + " pressed! for experiment with ID :" + experiment.getReservationID());
				} 
        });
        experiments.add(experiment.getView());
        // --> end of fake data
	}
}
