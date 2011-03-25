package eu.wisebed.wiseui.client.reservation.event;

import com.google.gwt.event.shared.GwtEvent;

public class ReservationSuccessEvent extends GwtEvent<ReservationSuccessEventHandler>{
	public static Type<ReservationSuccessEventHandler> TYPE = 
		new Type<ReservationSuccessEventHandler>();

	@Override
	public Type<ReservationSuccessEventHandler> getAssociatedType(){
		return TYPE;
	}
	
	@Override
	protected void dispatch(ReservationSuccessEventHandler handler){
		handler.onRsSuccess(this);
	}
}
