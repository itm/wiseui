package eu.wisebed.wiseui.widgets.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * Resources used by calendar widget.
 */
public interface TimeSelectorResources extends ClientBundle {
  
	public static final TimeSelectorResources INSTANCE =  
		GWT.create(TimeSelectorResources.class);
	
	@Source("eu/wisebed/wiseui/widgets/css/TimeSelector.css")
	public Style style();
	
	public interface Style extends CssResource {
	}
}