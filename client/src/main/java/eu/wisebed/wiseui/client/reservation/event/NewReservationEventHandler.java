package eu.wisebed.wiseui.client.reservation.event;

import com.google.gwt.event.shared.EventHandler;

public interface NewReservationEventHandler extends EventHandler{
	void onNewReservation(NewReservationEvent event);
}
