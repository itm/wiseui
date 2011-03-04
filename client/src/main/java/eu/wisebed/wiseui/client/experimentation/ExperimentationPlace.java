package eu.wisebed.wiseui.client.experimentation;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class ExperimentationPlace extends Place {

    public static class Tokenizer implements
            PlaceTokenizer<ExperimentationPlace> {

        public String getToken(final ExperimentationPlace place) {
            return "";
        }

        public ExperimentationPlace getPlace(final String token) {
            return new ExperimentationPlace();
        }
    }
}
