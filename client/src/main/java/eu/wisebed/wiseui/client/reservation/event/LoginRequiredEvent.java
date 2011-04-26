package eu.wisebed.wiseui.client.reservation.event;

import com.google.gwt.event.shared.GwtEvent;

public class LoginRequiredEvent extends GwtEvent<LoginRequiredEventHandler>{
	public static Type<LoginRequiredEventHandler> TYPE = new Type<LoginRequiredEventHandler>();

	@Override
	public Type<LoginRequiredEventHandler> getAssociatedType(){
		return TYPE;
	}
	
	@Override
	protected void dispatch(LoginRequiredEventHandler handler){
		handler.onLoginRequired(this);
	}
}
