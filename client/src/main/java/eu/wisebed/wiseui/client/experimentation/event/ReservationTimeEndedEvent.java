package eu.wisebed.wiseui.client.experimentation.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;


public class ReservationTimeEndedEvent extends GwtEvent<ReservationTimeEndedEvent.Handler> {

	public interface Handler extends EventHandler {
		void onReservationTimeEnded(ReservationTimeEndedEvent event);
	}
	
	public static final Type<ReservationTimeEndedEvent.Handler> TYPE = new Type<ReservationTimeEndedEvent.Handler>();

	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onReservationTimeEnded(this);
	}
}