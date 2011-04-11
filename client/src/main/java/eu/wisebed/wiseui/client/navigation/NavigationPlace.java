package eu.wisebed.wiseui.client.navigation;

import eu.wisebed.wiseui.client.util.KeyValuePlace;

public class NavigationPlace extends KeyValuePlace {

	private static final String INDEX_STRING = "index";
	
	private static final Integer DEFAULT_INDEX = 0;
	
	public NavigationPlace() {
		set(INDEX_STRING, DEFAULT_INDEX.toString());
	}
	
	public NavigationPlace(final Integer index) {
		set(INDEX_STRING, index.toString());
	}
	
	public Integer getIndex() {
		return Integer.valueOf(get(INDEX_STRING));
	}
}
