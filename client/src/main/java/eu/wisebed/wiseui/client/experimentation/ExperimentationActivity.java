package eu.wisebed.wiseui.client.experimentation;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import eu.wisebed.wiseui.client.WiseUiGinjector;
import eu.wisebed.wiseui.client.experimentation.presenter.ExperimentationPresenter;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentationView;
import eu.wisebed.wiseui.client.main.WiseUiPlace;

public class ExperimentationActivity extends AbstractActivity {

    private final WiseUiGinjector injector;
    private WiseUiPlace place;
    
    @Inject
    public ExperimentationActivity(final WiseUiGinjector injector){
    	this.injector = injector;
    }

    public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
    	    	
    	// initialize experimentation panel 
    	initExperimentationPanel(panel);
        
    }
    
    private void initExperimentationPanel(final AcceptsOneWidget panel) {
    	GWT.log("Init Experimentation Panel");
    	
    	final ExperimentationPresenter presenter = injector.getExperimentationPresenter();
    	final ExperimentationView view = injector.getExperimentationView();
    	
    	presenter.setPlace(place);
    	view.setPresenter(presenter);
    	panel.setWidget(view);
    	
    }
    
    public void setPlace(final WiseUiPlace place) {
    	this.place = place;
    }
}
