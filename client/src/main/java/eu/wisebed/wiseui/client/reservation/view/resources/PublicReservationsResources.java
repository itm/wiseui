package eu.wisebed.wiseui.client.reservation.view.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * Resources used by calendar widget.
 */
public interface PublicReservationsResources extends ClientBundle {

	@Source("eu/wisebed/wiseui/client/reservation/css/publicReservations.css")
	public Style style();

	public interface Style extends CssResource {
		String center();
	}
}