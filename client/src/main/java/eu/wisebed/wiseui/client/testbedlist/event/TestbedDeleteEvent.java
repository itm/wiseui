package eu.wisebed.wiseui.client.testbedlist.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;

public class TestbedDeleteEvent extends GwtEvent<TestbedDeleteEvent.Handler> {

	public interface Handler extends EventHandler {
		
		void onDeleteTestbed(TestbedDeleteEvent event);
	}
	
	public static final Type<Handler> TYPE = new Type<Handler>();

	private final TestbedConfiguration configuration;
	
	public TestbedDeleteEvent(final TestbedConfiguration configuration) {
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
	protected void dispatch(Handler handler) {
		handler.onDeleteTestbed(this);
	}
	
	
}
