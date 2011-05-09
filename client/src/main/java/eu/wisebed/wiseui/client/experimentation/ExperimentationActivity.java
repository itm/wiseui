package eu.wisebed.wiseui.client.experimentation;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import eu.wisebed.wiseui.client.WiseUiGinjector;
import eu.wisebed.wiseui.client.experimentation.presenter.ExperimentationPresenter;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentationView;
import eu.wisebed.wiseui.client.main.WiseUiPlace;
import eu.wisebed.wiseui.client.util.AuthenticationManager;
import eu.wisebed.wiseui.widgets.messagebox.MessageBox;

public class ExperimentationActivity extends AbstractActivity {

    private final WiseUiGinjector injector;
    private ExperimentationPresenter presenter;
    private WiseUiPlace place;
    private final PlaceController placeController;
    private EventBus eventBus;
    private ExperimentationView view;

    @Inject
    public ExperimentationActivity(final WiseUiGinjector injector,
    		final ExperimentationPresenter presenter,
    		final PlaceController placeController) {
    	this.presenter = presenter;
        this.injector = injector;
        this.placeController = placeController;
        this.view = presenter.getView();
    }

    public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
//    	// check if user authenticated
//    	final AuthenticationManager authenticationManager = 
//			injector.getAuthenticationManager();
//    	if (authenticationManager.getSecretAuthenticationKeys().isEmpty()) {
//        	// an ugly way to show that authentication is required
//        	MessageBox.error("Authentication Required", "Authentication is required in order to proceed.", null, null);
//        	return;
//        }
    	
    	// set event bus
    	this.eventBus = eventBus;
    	
    	// in the start of this activity get all reservations of the user
        presenter.getUserReservations();
        
        // fill experimentation view with experiment views
        panel.setWidget(view);
        
        // bind events
        bind();
    }
    
    public void setPlace(final WiseUiPlace place) {
    	this.place = place;
    }
    
    private void bind() {
    	//eventBus.bind(...)
    }
}
