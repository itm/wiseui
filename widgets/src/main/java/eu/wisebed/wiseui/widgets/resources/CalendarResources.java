package eu.wisebed.wiseui.widgets.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * Resources used by calendar widget.
 */
public interface CalendarResources extends ClientBundle {
  
	public static final CalendarResources INSTANCE =  
		GWT.create(CalendarResources.class);
	
	@Source("eu/wisebed/wiseui/widgets/css/Calendar.css")
	public Style style();
	
	public interface Style extends CssResource {
		String label();
	}
}