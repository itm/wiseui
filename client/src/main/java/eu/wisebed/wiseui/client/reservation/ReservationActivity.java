package eu.wisebed.wiseui.client.reservation;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import eu.wisebed.wiseui.client.WiseUiGinjector;
import eu.wisebed.wiseui.client.main.WiseUiPlace;
import eu.wisebed.wiseui.client.reservation.common.Parameters;
import eu.wisebed.wiseui.client.reservation.presenter.AllReservationsPresenter;
import eu.wisebed.wiseui.client.reservation.presenter.NewReservationPresenter;
import eu.wisebed.wiseui.client.reservation.presenter.ReservationPresenter;
import eu.wisebed.wiseui.client.reservation.presenter.TestbedsLoggedInPresenter;
import eu.wisebed.wiseui.client.reservation.view.AllReservationsView;
import eu.wisebed.wiseui.client.reservation.view.NewReservationView;
import eu.wisebed.wiseui.client.reservation.view.ReservationView;
import eu.wisebed.wiseui.client.reservation.view.TestbedsLoggedInView;
import eu.wisebed.wiseui.client.util.AuthenticationManager;

public class ReservationActivity extends AbstractActivity{

	private WiseUiGinjector injector;
	private WiseUiPlace place;

	@Inject
	public ReservationActivity(final WiseUiGinjector injector) {
		this.injector = injector;
	}

	public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
		initReservationPanel(panel);
	}
	
	private void initReservationPanel(final AcceptsOneWidget panel){
		final ReservationPresenter reservationPresenter = 
			injector.getReservationPresenter();
		final ReservationView reservationView = injector.getReservationView();
		final AuthenticationManager authenticationManager = 
			injector.getAuthenticationManager();
		reservationPresenter.setPlace(place);
		reservationView.setPresenter(reservationPresenter);
		panel.setWidget(reservationView.asWidget());
		reservationPresenter.bindDefaultViewEvents();
		if (authenticationManager.getSecretAuthenticationKeys().isEmpty()) { 
			// User has not logged in to a testbed
			
			// FIXME: The issue is that the first time calling start() the event
			// bus does not listen to events. Strange, i will come back later on 
			// this.
			reservationPresenter.pleaseLogin();
			reservationPresenter.disableReservation();
			return;
		}
		reservationPresenter.bindEnabledViewEvents();
		initTestbedsLoggedInPanel(reservationView, authenticationManager);
		initRestPanels(reservationView);
	}

	private void initTestbedsLoggedInPanel(final ReservationView reservationView,
			final AuthenticationManager authenticationManager){
		
		final TestbedsLoggedInPresenter testbedsLoggedInPresenter = 
			injector.getTestbedsLoggedInPresenter();
		testbedsLoggedInPresenter.getTestbedsLoggedIn(
				authenticationManager.getUrnPrefixByCookie());
		testbedsLoggedInPresenter.setPlace(place);
		final TestbedsLoggedInView testbedsLoggedInView = 
			injector.getTestbedsLoggedInView();
		reservationView.getTestbedsPanel().setWidget(testbedsLoggedInView);
	}
	
	private void initRestPanels(final ReservationView reservationView){
		final ReservationPlace reservationPlace = (ReservationPlace) place.get(ReservationPlace.class);
		if (reservationPlace.getView()==Parameters.NEW_VIEW)
			initNewReservationPanel(reservationView);
		else if (reservationPlace.getView()==Parameters.ALL_VIEW)
			initAllReservationsPanel(reservationView);
	}

	private void initNewReservationPanel(final ReservationView reservationView){
		final NewReservationPresenter newReservationViewPresenter =
			injector.getNewReservationPresenter();
		final NewReservationView newReservationView = injector.getNewReservationView();
		newReservationView.setPresenter(newReservationViewPresenter);
		reservationView.getParametersPanel().setWidget(newReservationView);
	}
	
	private void initAllReservationsPanel(final ReservationView reservationView){
		final AllReservationsPresenter allReservationsPresenter = 
			injector.getAllReservationsPresenter();
		final AllReservationsView allReservationsView = 
			injector.getAllReservationsView();
		allReservationsView.setPresenter(allReservationsPresenter);
		reservationView.getParametersPanel().setWidget(allReservationsView);
	}
	
	public void setPlace(final WiseUiPlace place){
		this.place = place;
	}
}
