package eu.wisebed.wiseui.client.testbedlist.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;

public class EditTestbedEvent extends GwtEvent<EditTestbedEvent.Handler> {

	public interface Handler extends EventHandler {
		
		void onEditTestbed(EditTestbedEvent event);
	}
	
	public static final Type<Handler> TYPE = new Type<Handler>();

	private final TestbedConfiguration configuration;
	
	public EditTestbedEvent() {
		configuration = null;
	}
	
	public EditTestbedEvent(final TestbedConfiguration configuration) {
		this.configuration = configuration;
	}
	
	public TestbedConfiguration getConfiguration() {
		return configuration;
	}
	
	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(final Handler handler) {
		handler.onEditTestbed(this);
	}
}
