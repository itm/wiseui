package eu.wisebed.wiseui.client.reservation;

import eu.wisebed.wiseui.client.reservation.common.Parameters;
import eu.wisebed.wiseui.client.util.KeyValuePlace;
import eu.wisebed.wiseui.client.util.Objects2;

public class ReservationPlace extends KeyValuePlace {

    public ReservationPlace() {
    	this(Parameters.ALL_VIEW);
    }
    
    public ReservationPlace(final String view) {
        set(Parameters.VIEW_KEY, view);
    }
   
    public String getView() {
		return Objects2.nullOrToString(get(Parameters.VIEW_KEY));
	}
}
