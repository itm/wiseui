package eu.wisebed.wiseui.client.administration.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import eu.wisebed.wiseui.client.administration.event.CreateConfigurationEvent.CreateConfigurationHandler;

public class CreateConfigurationEvent extends GwtEvent<CreateConfigurationHandler> {

	public interface CreateConfigurationHandler extends EventHandler {
        void onCreateConfiguration(CreateConfigurationEvent event);
    }

    public static final Type<CreateConfigurationHandler> TYPE = new Type<CreateConfigurationHandler>();

    public CreateConfigurationEvent() {
    }

    @Override
    protected void dispatch(final CreateConfigurationHandler handler) {
        handler.onCreateConfiguration(this);
    }

    @Override
    public Type<CreateConfigurationHandler> getAssociatedType() {
        return TYPE;
    }
}
