package eu.wisebed.wiseui.client.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Constants;


/**
 * Constants that are used for loading the map api from google.
 * 
 * @author Malte Legenhausen
 */
public interface MapConstants extends Constants {
	
    public static final MapConstants INSTANCE = GWT.create(MapConstants.class);

	@DefaultStringValue("ABQIAAAAJF12r4xVlog3DZkEwDC09BRisPSeHzj7Yhj17FYCkK1ytSRbxBQV16SxQgD_zuTEDGaTRK9sHFtMDQ")
	String key();
	
	@DefaultStringValue("2")
	String version();
	
	@DefaultBooleanValue(false)
	boolean useSensors();
}
