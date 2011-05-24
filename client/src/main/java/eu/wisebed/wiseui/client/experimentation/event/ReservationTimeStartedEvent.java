package eu.wisebed.wiseui.client.experimentation.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;


public class ReservationTimeStartedEvent extends GwtEvent<ReservationTimeStartedEvent.Handler>{

	public interface Handler extends EventHandler {
		void onReservationTimeStarted(ReservationTimeStartedEvent event);
	}
	
	public static final Type<ReservationTimeStartedEvent.Handler> TYPE = new Type<ReservationTimeStartedEvent.Handler>();

	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onReservationTimeStarted(this);
	}
}