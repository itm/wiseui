package eu.wisebed.wiseui.client.experimentation;

import com.google.gwt.place.shared.PlaceTokenizer;

import eu.wisebed.wiseui.client.WiseUiPlace;

public class ExperimentationPlace extends WiseUiPlace {

	public ExperimentationPlace() {
		
	}
	
	public ExperimentationPlace(String token) {
		super(token);
	}
	
	public ExperimentationPlace(Integer testbedId) {
		super(testbedId);
	}
	
	@Override
	public WiseUiPlace copy(Integer testbedId) {
		return new ExperimentationPlace(testbedId);
	}
	
    public static class Tokenizer implements
            PlaceTokenizer<ExperimentationPlace> {
    	
        public String getToken(final ExperimentationPlace place) {
            return place.toString();
        }

        public ExperimentationPlace getPlace(final String token) {
            return new ExperimentationPlace(token);
        }
    }
}
