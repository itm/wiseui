package eu.wisebed.wiseui.client.administration;

import com.google.gwt.place.shared.PlaceTokenizer;

import eu.wisebed.wiseui.client.util.Ints2;
import eu.wisebed.wiseui.client.util.KeyValuePlace;
import eu.wisebed.wiseui.client.util.Objects2;

/**
 * Place for the administration view.
 * 
 * @author Malte Legenhausen
 */
public class AdministrationPlace extends KeyValuePlace {
	
	public AdministrationPlace() {
		set("selection", null);
	}
	
	public AdministrationPlace(final Integer selection) {
		set("selection", Objects2.nullOrToString(selection));
	}
	
	public AdministrationPlace(final String token) {
		this();
		parse(token);
	}
	
	public Integer getSelection() {
		return Ints2.nullOrValueOf(get("selection"));
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
