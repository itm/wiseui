package eu.wisebed.wiseui.widgets.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * Resources used by calendar widget.
 */
public interface ImageUploadResources extends ClientBundle {
  
	public static final ImageUploadResources INSTANCE =  
		GWT.create(ImageUploadResources.class);
	
	@Source("eu/wisebed/wiseui/widgets/css/ImageUpload.css")
	public Style style();
	
	public interface Style extends CssResource {
	}
}