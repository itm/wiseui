package eu.wisebed.wiseui.client.reservation.event;

import com.google.gwt.event.shared.EventHandler;

public interface ReservationFailedEventHandler extends EventHandler{
	void onRsFailed(ReservationFailedEvent event);
}
