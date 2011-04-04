package eu.wisebed.wiseui.client.reservation.event;

import com.google.gwt.event.shared.EventHandler;

public interface MissingReservationParametersEventHandler extends EventHandler{
	void onMissingReservationParameters(MissingReservationParametersEvent event);
}
