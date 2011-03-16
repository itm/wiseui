package eu.wisebed.wiseui.client.experimentation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import eu.wisebed.wiseui.client.WiseUiGinjector;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentationView;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentationView.Presenter;
import eu.wisebed.wiseui.widgets.experimentpanel.ExperimentPanel;
import eu.wisebed.wiseui.widgets.experimentpanel.ExperimentPanel.Button;
import eu.wisebed.wiseui.widgets.experimentpanel.ExperimentPanel.Callback;
import eu.wisebed.wiseui.widgets.experimentpanel.ExperimentPanelView;
import eu.wisebed.wiseui.widgets.gin.WidgetsGinjector;

public class ExperimentationActivity extends AbstractActivity implements
        Presenter {

    private final WiseUiGinjector injector;
    private final WidgetsGinjector widgetsInjector;
    private ExperimentationView view;
    private List<ExperimentPanelView> experiments;

    @Inject
    public ExperimentationActivity(final WiseUiGinjector injector, final WidgetsGinjector widgetsInjector) {
        this.injector = injector;
        this.widgetsInjector = widgetsInjector;
        experiments = new ArrayList<ExperimentPanelView>();
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
        final ExperimentPanel panel1 = widgetsInjector.getExperimentPanel();
        panel1.initPanel(1, startDate, stopDate, urns, 
        	"uploadedsampleimage1.bin",new Callback() {

				@Override
				public void onButtonClicked(Button button) {
					GWT.log(button.getValue() + " pressed! for experiment with ID :" + panel1.getReservationID());
				}
        	
        });
        experiments.add(panel1.getView());
        // --> end of fake data
	}
}
