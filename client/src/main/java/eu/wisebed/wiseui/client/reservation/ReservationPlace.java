package eu.wisebed.wiseui.client.reservation;

import com.google.gwt.place.shared.PlaceTokenizer;

import eu.wisebed.wiseui.client.reservation.common.Parameters;
import eu.wisebed.wiseui.client.util.Ints2;
import eu.wisebed.wiseui.client.util.KeyValuePlace;
import eu.wisebed.wiseui.client.util.Objects2;

public class ReservationPlace extends KeyValuePlace {

    public ReservationPlace() {
    	this(null, Parameters.NEW_VIEW);
    }
    
    public ReservationPlace(final Integer selection, final String view) {
    	set(Parameters.SELECTION_KEY, Objects2.nullOrToString(selection));
        set(Parameters.VIEW_KEY, view);
    }

    public Integer getSelection() {
        return Ints2.nullOrValueOf(get(Parameters.SELECTION_KEY));
    }
   
    public String getView() {
		return Objects2.nullOrToString(get(Parameters.VIEW_KEY));
	}

    public static class Tokenizer implements PlaceTokenizer<ReservationPlace> {

        public String getToken(final ReservationPlace place) {
            return place.toString();
        }

        public ReservationPlace getPlace(final String token) {
            return new ReservationPlace();
        }
    }
}
