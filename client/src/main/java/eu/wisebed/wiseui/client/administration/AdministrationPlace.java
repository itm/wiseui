  package eu.wisebed.wiseui.client.administration;

import eu.wisebed.wiseui.client.util.KeyValuePlace;

/**
 * Place for the administration view.
 * 
 * @author Malte Legenhausen
 */
public class AdministrationPlace extends KeyValuePlace {
	
	public AdministrationPlace() {
		
	}
	
	public AdministrationPlace(final String token) {
		parse(token);
	}
}
