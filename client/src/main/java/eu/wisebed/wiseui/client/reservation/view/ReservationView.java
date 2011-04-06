package eu.wisebed.wiseui.client.reservation.view;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

@ImplementedBy(ReservationViewImpl.class)
public interface ReservationView extends IsWidget {

    void setPresenter(Presenter presenter);
    void setSubview(String view);
    void reserveButton(boolean status);
    AcceptsOneWidget getTestbedsPanel();
    AcceptsOneWidget getParametersPanel();

	
	public interface Presenter{
		void gotoSubview(String view);
		void makeReservation();
		boolean checkReservationDetails();
	}
}