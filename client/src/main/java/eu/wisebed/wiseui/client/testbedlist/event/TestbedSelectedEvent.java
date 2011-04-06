package eu.wisebed.wiseui.client.testbedlist.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import eu.wisebed.wiseui.client.testbedlist.event.TestbedSelectedEvent.ConfigurationSelectedHandler;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;

public class TestbedSelectedEvent extends GwtEvent<ConfigurationSelectedHandler> {

    public interface ConfigurationSelectedHandler extends EventHandler {
        void onTestbedConfigurationSelected(TestbedSelectedEvent event);
    }

    public static final Type<ConfigurationSelectedHandler> TYPE = new Type<ConfigurationSelectedHandler>();

    private final TestbedConfiguration configuration;

    public TestbedSelectedEvent(final TestbedConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected void dispatch(final ConfigurationSelectedHandler handler) {
        handler.onTestbedConfigurationSelected(this);
    }

    @Override
    public Type<ConfigurationSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    public TestbedConfiguration getConfiguration() {
        return configuration;
    }
}