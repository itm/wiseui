package eu.wisebed.wiseui.client.reservation.presenter;

import com.google.inject.Inject;

import eu.wisebed.wiseui.client.reservation.view.AllReservationsView;
import eu.wisebed.wiseui.client.reservation.view.AllReservationsView.Presenter;


public class AllReservationsPresenter implements Presenter{

	private final AllReservationsView view;

	@Inject
	public AllReservationsPresenter(final AllReservationsView view){
		this.view = view;
	
	}
}
