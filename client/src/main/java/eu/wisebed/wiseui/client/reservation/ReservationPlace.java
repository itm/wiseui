package eu.wisebed.wiseui.client.reservation;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class ReservationPlace extends Place {

    public ReservationPlace() {
    }

    public static class Tokenizer implements PlaceTokenizer<ReservationPlace> {

        public String getToken(final ReservationPlace place) {
            return "";
        }

        public ReservationPlace getPlace(final String token) {
            return new ReservationPlace();
        }
    }
}
