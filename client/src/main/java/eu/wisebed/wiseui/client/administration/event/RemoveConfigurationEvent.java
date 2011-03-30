package eu.wisebed.wiseui.client.administration.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import eu.wisebed.wiseui.client.administration.event.RemoveConfigurationEvent.RemoveConfigurationHandler;
import eu.wisebed.wiseui.shared.TestbedConfiguration;

public class RemoveConfigurationEvent extends GwtEvent<RemoveConfigurationHandler> {

	public interface RemoveConfigurationHandler extends EventHandler {
		void onRemoveConfiguration(RemoveConfigurationEvent event);
	}
	
	public static final Type<RemoveConfigurationHandler> TYPE = new Type<RemoveConfigurationHandler>();
	
	private final TestbedConfiguration configuration;
	
	public RemoveConfigurationEvent(final TestbedConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	public Type<RemoveConfigurationHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(RemoveConfigurationHandler handler) {
		handler.onRemoveConfiguration(this);
	}
	
	public TestbedConfiguration getConfiguration() {
		return configuration;
	}
}
