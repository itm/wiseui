package eu.wisebed.wiseui.client.reservation.event;

import com.google.gwt.event.shared.GwtEvent;

public class NewReservationEvent extends GwtEvent<NewReservationEventHandler>{
	public static Type<NewReservationEventHandler> TYPE = 
		new Type<NewReservationEventHandler>();

	@Override
	public Type<NewReservationEventHandler> getAssociatedType(){
		return TYPE;
	}
	
	@Override
	protected void dispatch(NewReservationEventHandler handler){
		handler.onNewReservation(this);
	}
}
