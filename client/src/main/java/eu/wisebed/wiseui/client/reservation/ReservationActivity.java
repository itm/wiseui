package eu.wisebed.wiseui.client.reservation;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import eu.wisebed.wiseui.client.WiseUiGinjector;
import eu.wisebed.wiseui.client.reservation.common.Parameters;
import eu.wisebed.wiseui.client.reservation.presenter.NewReservationPresenter;
import eu.wisebed.wiseui.client.reservation.presenter.PublicReservationsPresenter;
import eu.wisebed.wiseui.client.reservation.presenter.ReservationPresenter;
import eu.wisebed.wiseui.client.reservation.view.NewReservationView;
import eu.wisebed.wiseui.client.reservation.view.PublicReservationsView;
import eu.wisebed.wiseui.client.reservation.view.ReservationView;
import eu.wisebed.wiseui.client.util.AuthenticationManager;

public class ReservationActivity extends AbstractActivity{

	private WiseUiGinjector injector;
	private ReservationPlace place;

	@Inject
	public ReservationActivity(final WiseUiGinjector injector) {
		this.injector = injector;
	}

	public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
		initReservationPanel(panel);
	}
	
	private void initReservationPanel(final AcceptsOneWidget panel){
		final ReservationPresenter reservationPresenter = injector.getReservationPresenter();
		final ReservationView reservationView = injector.getReservationView();
		final AuthenticationManager authenticationManager = injector.getAuthenticationManager();
		reservationPresenter.setPlace(place);
		reservationView.setPresenter(reservationPresenter);
		panel.setWidget(reservationView.asWidget());
		if (authenticationManager.getSecretAuthenticationKeys().isEmpty()) { 
//			// User has not logged in to a testbed. Render public view
			initPublicReservationsPanel(reservationView);
			reservationPresenter.disableReservation();
			return;
		}
		reservationPresenter.bindEnabledViewEvents();
		initRestPanels(reservationView);
	}

	private void initRestPanels(final ReservationView reservationView){
		if (place.getView()==Parameters.ALL_VIEW)
			initPublicReservationsPanel(reservationView);
		else if (place.getView()==Parameters.NEW_VIEW)
			initNewReservationPanel(reservationView);
	}

	private void initNewReservationPanel(final ReservationView reservationView){
		final NewReservationPresenter newReservationViewPresenter = injector.getNewReservationPresenter();
		final NewReservationView newReservationView = injector.getNewReservationView();
		newReservationView.setPresenter(newReservationViewPresenter);
		reservationView.getParametersPanel().setWidget(newReservationView);
	}
	
	private void initPublicReservationsPanel(final ReservationView reservationView){
		final PublicReservationsPresenter publicReservationsPresenter = injector.getAllReservationsPresenter();
		final PublicReservationsView publicReservationsView = injector.getAllReservationsView();
		publicReservationsView.setPresenter(publicReservationsPresenter);
		reservationView.getParametersPanel().setWidget(publicReservationsView);
	}
	
	public void setPlace(final ReservationPlace place){
		this.place = place;
	}
}
