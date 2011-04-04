package eu.wisebed.wiseui.client.reservation.view.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

/**
 * Resources used by calendar widget.
 */
public interface AllReservationsResources extends ClientBundle {
  
	@Source("eu/wisebed/wiseui/client/reservation/static/underconstruction.png")
	ImageResource underConstruction();

	@Source("eu/wisebed/wiseui/client/reservation/css/allReservations.css")
	public Style style();

	public interface Style extends CssResource {
		String center();
	}
}