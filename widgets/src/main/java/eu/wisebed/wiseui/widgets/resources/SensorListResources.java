package eu.wisebed.wiseui.widgets.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * Resources used by calendar widget.
 */
public interface SensorListResources extends ClientBundle {
  
	public static final SensorListResources INSTANCE =  
		GWT.create(SensorListResources.class);
	
	@Source("eu/wisebed/wiseui/widgets/css/SensorList.css")
	public Style style();
	
	public interface Style extends CssResource {
		String sensorPanel();
		String scrollable();
	}
}