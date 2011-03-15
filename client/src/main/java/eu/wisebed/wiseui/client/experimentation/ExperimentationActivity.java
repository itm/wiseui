package eu.wisebed.wiseui.client.experimentation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import eu.wisebed.wiseui.client.WiseUiGinjector;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentationView;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentationView.Presenter;
import eu.wisebed.wiseui.widgets.experimentpanel.ExperimentPanel;
import eu.wisebed.wiseui.widgets.experimentpanel.ExperimentPanelView;
import eu.wisebed.wiseui.widgets.gin.WidgetsGinjector;

public class ExperimentationActivity extends AbstractActivity implements
        Presenter {

    private final WiseUiGinjector injector;
    private final WidgetsGinjector widgetsInjector;
    private ExperimentationView view;

    @Inject
    public ExperimentationActivity(final WiseUiGinjector injector, final WidgetsGinjector widgetsInjector) {
        this.injector = injector;
        this.widgetsInjector = widgetsInjector;
    }

    public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
        
    	view = injector.getExperimentationView();
        
        view.setPresenter(this);
        
        List<ExperimentPanelView> panels = 
        	new ArrayList<ExperimentPanelView>();
        
        // fake data -->
        Date startDate = new Date(2011-1900,10,3);
        Date stopDate  = new Date(2011-1900,10,3);
        List<String> urns = new ArrayList<String>();
        urns.add("node1");
        urns.add("node2");
        urns.add("node3");
        
        ExperimentPanel panel1 = widgetsInjector.getExperimentPanel();
        panel1.initPanel(1, startDate, stopDate, urns, "uploadedsampleimage1.bin");
        panel1.setAsPendingExperiment();
        panels.add(panel1.getView());
        
        ExperimentPanel panel2 = widgetsInjector.getExperimentPanel();
        panel2.initPanel(2, startDate, stopDate, urns, "uploadedsampleimage2.bin");
        panel2.setAsReadyExperiment();
        panels.add(panel2.getView());

        
        ExperimentPanel panel3 = widgetsInjector.getExperimentPanel();
        panel3.initPanel(3, startDate, stopDate, urns, "uploadedsampleimage3.bin");
        panel3.setAsRunningExperiment();
        panels.add(panel3.getView());

        
        ExperimentPanel panel4 = widgetsInjector.getExperimentPanel();
        panel4.initPanel(4, startDate, stopDate, urns, "uploadedsampleimage4.bin");
        panel4.setAsCancelledExperiment();
        panels.add(panel4.getView());

        
        ExperimentPanel panel5 = widgetsInjector.getExperimentPanel();
        panel5.initPanel(5, startDate, stopDate, urns, "uploadedsampleimage5.bin");
        panel4.setAsTimedoutExperiment();
        panels.add(panel5.getView());


        // --> end of fake data
        view.initView(panels);
        panel.setWidget(view);
    }
    
	@Override
	public void getUserReservations() {
		// TODO Auto-generated method stub
	}
}
