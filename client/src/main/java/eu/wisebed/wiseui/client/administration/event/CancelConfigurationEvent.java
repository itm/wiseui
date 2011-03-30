package eu.wisebed.wiseui.client.administration.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import eu.wisebed.wiseui.client.administration.event.CancelConfigurationEvent.CancelConfigurationHandler;

public class CancelConfigurationEvent extends GwtEvent<CancelConfigurationHandler> {

	public interface CancelConfigurationHandler extends EventHandler {
		void onCancelConfiguration(CancelConfigurationEvent event);
	}
	
	public static final Type<CancelConfigurationHandler> TYPE = new Type<CancelConfigurationHandler>();

	@Override
	public Type<CancelConfigurationHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(CancelConfigurationHandler handler) {
		handler.onCancelConfiguration(this);
	}
}
