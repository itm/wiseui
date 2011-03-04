package eu.wisebed.wiseui.client.administration;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class AdministrationPlace extends Place {

    public static class Tokenizer implements
            PlaceTokenizer<AdministrationPlace> {

        public String getToken(final AdministrationPlace place) {
            return "";
        }

        public AdministrationPlace getPlace(final String token) {
            return new AdministrationPlace();
        }
    }
}
