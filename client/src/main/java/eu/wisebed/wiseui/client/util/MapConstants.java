package eu.wisebed.wiseui.client.util;

import com.google.gwt.i18n.client.Constants;

public interface MapConstants extends Constants {

	@DefaultStringValue("")
	String key();
	
	@DefaultStringValue("2")
	String version();
	
	@DefaultBooleanValue(false)
	boolean useSensors();
}
