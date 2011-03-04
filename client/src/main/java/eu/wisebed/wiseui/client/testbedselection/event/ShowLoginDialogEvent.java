package eu.wisebed.wiseui.client.testbedselection.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import eu.wisebed.wiseui.client.testbedselection.event.ShowLoginDialogEvent.ShowLoginDialogHandler;

public class ShowLoginDialogEvent extends GwtEvent<ShowLoginDialogHandler> {

    public interface ShowLoginDialogHandler extends EventHandler {
        void onShowLoginDialog(ShowLoginDialogEvent event);
    }

    public static final Type<ShowLoginDialogHandler> TYPE = new Type<ShowLoginDialogHandler>();

    @Override
    public GwtEvent.Type<ShowLoginDialogHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final ShowLoginDialogHandler handler) {
        handler.onShowLoginDialog(this);
    }
}
