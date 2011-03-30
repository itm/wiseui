package eu.wisebed.wiseui.client.administration.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import eu.wisebed.wiseui.client.administration.event.SaveConfigurationEvent.SaveConfigurationHandler;

public class SaveConfigurationEvent extends GwtEvent<SaveConfigurationHandler> {
	
	public interface SaveConfigurationHandler extends EventHandler {
		void onSaveConfiguration(SaveConfigurationEvent event);
	}
	
	public static final Type<SaveConfigurationHandler> TYPE = new Type<SaveConfigurationHandler>();

	@Override
	public Type<SaveConfigurationHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(final SaveConfigurationHandler handler) {
		handler.onSaveConfiguration(this);
	}
}
