package eu.wisebed.wiseui.client.reservation.presenter;

import com.google.inject.Inject;

import eu.wisebed.wiseui.client.reservation.view.NewReservationView;
import eu.wisebed.wiseui.client.reservation.view.NewReservationView.Presenter;

public class NewReservationPresenter implements Presenter{

	private final NewReservationView view;

	@Inject
	public NewReservationPresenter(final NewReservationView view){
		this.view = view;
		enableNewReservationView();
	}

	public void enableNewReservationView() {
		view.enableNewReservationView();
	}
	
	/**
	 * Check if user has set all required reservation details
	 */
	public boolean checkReservationDetails() {
		// Temporarily checking only for the image
		if (!view.checkReservationDetails()){
			return false;
		}
		return true;
	}
}
