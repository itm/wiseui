package eu.wisebed.wiseui.client.reservation.gin;

import com.google.gwt.inject.client.Ginjector;
import eu.wisebed.wiseui.client.reservation.ReservationActivity;
import eu.wisebed.wiseui.client.reservation.view.ReservationView;

public interface ReservationGinjector extends Ginjector {

    ReservationActivity getReservationActivity();

    ReservationView getReservationView();
}
