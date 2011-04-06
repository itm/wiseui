package eu.wisebed.wiseui.client.administration;

import com.google.gwt.place.shared.PlaceTokenizer;

import eu.wisebed.wiseui.client.WiseUiPlace;

/**
 * Place for the administration view.
 * 
 * @author Malte Legenhausen
 */
public class AdministrationPlace extends WiseUiPlace {
	
	public AdministrationPlace() {
		super();
	}
	
	public AdministrationPlace(final Integer selection) {
		super(selection);
	}
	
	public AdministrationPlace(final String token) {
		this();
		parse(token);
	}
	
	@Override
	public WiseUiPlace copy(Integer testbedId) {
		return new AdministrationPlace(testbedId);
	}

	/**
	 * Tokenizer for the AdministrationPlace.
	 * 
	 * @author Malte Legenhausen
	 */
	public static class Tokenizer implements
			PlaceTokenizer<AdministrationPlace> {

		public String getToken(final AdministrationPlace place) {
			return place.toString();
		}

		public AdministrationPlace getPlace(final String token) {
			return new AdministrationPlace(token);
		}
	}
}
