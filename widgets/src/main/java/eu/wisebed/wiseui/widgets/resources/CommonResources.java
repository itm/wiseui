package eu.wisebed.wiseui.widgets.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * Resources used by calendar widget.
 */
public interface CommonResources extends ClientBundle {
  
	public static final CommonResources INSTANCE =  
		GWT.create(CommonResources.class);
	
	@Source("eu/wisebed/wiseui/widgets/css/Common.css")
	public Style style();
	
	public interface Style extends CssResource {
		String label();
	}
}