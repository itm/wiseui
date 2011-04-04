package eu.wisebed.wiseui.client.reservation.event;

import com.google.gwt.event.shared.GwtEvent;

public class MissingReservationParametersEvent extends GwtEvent<
	MissingReservationParametersEventHandler>{
	
	public static Type<MissingReservationParametersEventHandler> TYPE = 
		new Type<MissingReservationParametersEventHandler>();

	@Override
	public Type<MissingReservationParametersEventHandler> getAssociatedType(){
		return TYPE;
	}
	
	@Override
	protected void dispatch(MissingReservationParametersEventHandler handler){
		handler.onMissingReservationParameters(this);
	}
}
