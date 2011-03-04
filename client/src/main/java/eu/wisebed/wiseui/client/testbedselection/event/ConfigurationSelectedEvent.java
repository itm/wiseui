package eu.wisebed.wiseui.client.testbedselection.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import eu.wisebed.wiseui.client.testbedselection.event.ConfigurationSelectedEvent.ConfigurationSelectedHandler;
import eu.wisebed.wiseui.shared.TestbedConfiguration;

public class ConfigurationSelectedEvent extends GwtEvent<ConfigurationSelectedHandler> {

    public interface ConfigurationSelectedHandler extends EventHandler {
        void onTestbedConfigurationSelected(ConfigurationSelectedEvent event);
    }

    public static final Type<ConfigurationSelectedHandler> TYPE = new Type<ConfigurationSelectedHandler>();

    private final TestbedConfiguration configuration;

    public ConfigurationSelectedEvent(final TestbedConfiguration configuration) {
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