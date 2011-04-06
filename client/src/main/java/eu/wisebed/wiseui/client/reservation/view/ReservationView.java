package eu.wisebed.wiseui.client.reservation.view;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

import eu.wisebed.wiseui.client.reservation.ReservationPlace;

@ImplementedBy(ReservationViewImpl.class)
public interface ReservationView extends IsWidget {

    void setPresenter(Presenter presenter);
    void setSubview(String view);
    void reserveButton(boolean status);
    void newReservationToggleButton(boolean status);
    AcceptsOneWidget getTestbedsPanel();
    AcceptsOneWidget getParametersPanel();

	
	public interface Presenter{
		void setPlace(ReservationPlace place);
		void gotoSubview(String view);
		void makeReservation();
		ReservationPlace getPlace();
	}
}