package eu.wisebed.wiseui.client.testbedlist.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class RefreshTestbedListEvent extends GwtEvent<RefreshTestbedListEvent.Handler> {

	public interface Handler extends EventHandler {
		
		void onRefreshTestbedList(RefreshTestbedListEvent event);
	}
	
	public static final Type<RefreshTestbedListEvent.Handler> TYPE = new Type<RefreshTestbedListEvent.Handler>();

	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onRefreshTestbedList(this);
	}
}
