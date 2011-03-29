package eu.wisebed.wiseui.client.experimentation;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import eu.wisebed.wiseui.client.WiseUiGinjector;
import eu.wisebed.wiseui.client.experimentation.presenter.ExperimentationPresenter;
import eu.wisebed.wiseui.client.util.AuthenticationManager;
import eu.wisebed.wiseui.widgets.messagebox.MessageBox;

public class ExperimentationActivity extends AbstractActivity {

    private final WiseUiGinjector injector;
    private ExperimentationPresenter presenter;

    @Inject
    public ExperimentationActivity(final WiseUiGinjector injector,
    		final EventBus eventBus) {
        this.injector = injector;
    }

    public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
    	// check if user authenticated
    	final AuthenticationManager authenticationManager = 
			injector.getAuthenticationManager();
    	if (authenticationManager.getSecretAuthenticationKeys().isEmpty()) {
        	// an ugly way to show that authentication is required
        	MessageBox.error("Authentication Required", "Authentication is required in order to proceed.", null, null);
        	return;
        }
    	
    	// get presenter
        presenter = injector.getExperimentationPresenter();
        
        // in the start of this activity get all reservations of the user
        presenter.getUserReservations();
        
        // fill experimentation view with experiment views
        panel.setWidget(presenter.getView());
    }
}
