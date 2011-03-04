package eu.wisebed.wiseui.client.testbedselection.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import eu.wisebed.wiseui.client.testbedselection.event.WisemlLoadedEvent.WisemlLoadedHandler;
import eu.wisebed.wiseui.shared.wiseml.Wiseml;

public class WisemlLoadedEvent extends GwtEvent<WisemlLoadedHandler> {

    public interface WisemlLoadedHandler extends EventHandler {
        void onWisemlLoaded(WisemlLoadedEvent event);
    }

    public static final Type<WisemlLoadedHandler> TYPE = new Type<WisemlLoadedHandler>();

    private final Wiseml wiseml;

    public WisemlLoadedEvent(final Wiseml wiseml) {
        this.wiseml = wiseml;
    }

    @Override
    public Type<WisemlLoadedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final WisemlLoadedHandler handler) {
        handler.onWisemlLoaded(this);
    }

    public Wiseml getWiseml() {
        return wiseml;
    }
}
