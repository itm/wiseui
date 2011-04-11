package eu.wisebed.wiseui.client.reservation;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import eu.wisebed.wiseui.client.WiseUiGinjector;
import eu.wisebed.wiseui.client.main.WiseUiPlace;
import eu.wisebed.wiseui.client.reservation.presenter.PublicReservationsPresenter;
import eu.wisebed.wiseui.client.reservation.presenter.ReservationPresenter;
import eu.wisebed.wiseui.client.reservation.view.PublicReservationsView;
import eu.wisebed.wiseui.client.reservation.view.ReservationView;

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
		final ReservationPresenter reservationPresenter = injector.getReservationPresenter();
		final ReservationView reservationView = injector.getReservationView();
		reservationPresenter.setPlace(place);
		reservationView.setPresenter(reservationPresenter);
		panel.setWidget(reservationView.asWidget());
		initPublicReservationsPanel(reservationView);
		reservationPresenter.bindEnabledViewEvents();
	}
	
	private void initPublicReservationsPanel(final ReservationView reservationView){
		final PublicReservationsPresenter publicReservationsPresenter = injector.getAllReservationsPresenter();
		final PublicReservationsView publicReservationsView = injector.getAllReservationsView();
		publicReservationsView.setPresenter(publicReservationsPresenter);
		reservationView.getParametersPanel().setWidget(publicReservationsView);
	}
	
	public void setPlace(final WiseUiPlace place){
		this.place = place;
	}
}
