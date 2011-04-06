package eu.wisebed.wiseui.client.reservation;

import com.google.gwt.place.shared.PlaceTokenizer;

import eu.wisebed.wiseui.client.WiseUiPlace;
import eu.wisebed.wiseui.client.reservation.common.Parameters;
import eu.wisebed.wiseui.client.util.Ints2;
import eu.wisebed.wiseui.client.util.KeyValuePlace;
import eu.wisebed.wiseui.client.util.Objects2;

public class ReservationPlace extends WiseUiPlace {

    public ReservationPlace() {
    	this(null, Parameters.ALL_VIEW);
    }
    
    public ReservationPlace(final Integer selection, final String view) {
    	super(selection);
        set(Parameters.VIEW_KEY, view);
    }
   
    public String getView() {
		return Objects2.nullOrToString(get(Parameters.VIEW_KEY));
	}
    
    @Override
    public WiseUiPlace copy(Integer testbedId) {
    	return new ReservationPlace(testbedId, getView());
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
