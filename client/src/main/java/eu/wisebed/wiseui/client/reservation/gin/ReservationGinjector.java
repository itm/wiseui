package eu.wisebed.wiseui.client.reservation.gin;

import com.google.gwt.inject.client.Ginjector;
import eu.wisebed.wiseui.client.reservation.ReservationActivity;
import eu.wisebed.wiseui.client.reservation.presenter.AllReservationsPresenter;
import eu.wisebed.wiseui.client.reservation.presenter.NewReservationPresenter;
import eu.wisebed.wiseui.client.reservation.presenter.ReservationPresenter;
import eu.wisebed.wiseui.client.reservation.presenter.TestbedsLoggedInPresenter;
import eu.wisebed.wiseui.client.reservation.view.AllReservationsView;
import eu.wisebed.wiseui.client.reservation.view.NewReservationView;
import eu.wisebed.wiseui.client.reservation.view.ReservationView;
import eu.wisebed.wiseui.client.reservation.view.TestbedsLoggedInView;

public interface ReservationGinjector extends Ginjector {

    ReservationActivity getReservationActivity();
    
    ReservationPresenter getReservationPresenter();

    ReservationView getReservationView();
    
    TestbedsLoggedInPresenter getTestbedsLoggedInPresenter();
    
    TestbedsLoggedInView getTestbedsLoggedInView();
    
    NewReservationPresenter getNewReservationPresenter();

    NewReservationView getNewReservationView();
    
    AllReservationsView getAllReservationsView();
    
    AllReservationsPresenter getAllReservationsPresenter();
    
}
