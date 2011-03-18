package eu.wisebed.wiseui.client.reservation;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import eu.wisebed.wiseui.client.WiseUiGinjector;
import eu.wisebed.wiseui.client.reservation.presenter.ReservationPresenter;
import eu.wisebed.wiseui.client.reservation.view.ReservationView;
import eu.wisebed.wiseui.client.util.AuthenticationManager;

public class ReservationActivity extends AbstractActivity{

    private WiseUiGinjector injector;
    
    @Inject
    public ReservationActivity(final WiseUiGinjector injector, 
    		final EventBus eventBus) {
    	this.injector = injector;
    }

    public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
    	final ReservationPresenter presenter = injector.getReservationPresenter();
    	final ReservationView view = injector.getReservationView();
    	final AuthenticationManager authenticationManager = 
    		injector.getAuthenticationManager();
        view.setPresenter(presenter);
        panel.setWidget(view);
        if (authenticationManager.getSecretAuthenticationKeys().isEmpty()) { // User has not logged in to a testbed
        	GWT.log("Cookies" + Cookies.getCookieNames());
        	presenter.loginRequired();
        }else{
            presenter.initRsView();
	        presenter.getNetwork();
        }
    }
}
