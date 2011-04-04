package eu.wisebed.wiseui.client.reservation.event;

import com.google.gwt.event.shared.GwtEvent;

public class ReservationFailedEvent extends GwtEvent<ReservationFailedEventHandler>{
	public static Type<ReservationFailedEventHandler> TYPE = 
		new Type<ReservationFailedEventHandler>();

	@Override
	public Type<ReservationFailedEventHandler> getAssociatedType(){
		return TYPE;
	}
	
	@Override
	protected void dispatch(ReservationFailedEventHandler handler){
		handler.onReservationFailed(this);
	}
}
