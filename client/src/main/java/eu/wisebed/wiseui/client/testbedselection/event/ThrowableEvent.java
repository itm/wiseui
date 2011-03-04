package eu.wisebed.wiseui.client.testbedselection.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class ThrowableEvent extends GwtEvent<ThrowableEvent.ThrowableHandler> {

    public interface ThrowableHandler extends EventHandler {

        void onThrowable(ThrowableEvent event);
    }

    public static final Type<ThrowableHandler> TYPE = new Type<ThrowableHandler>();
    private final Throwable throwable;

    public ThrowableEvent(final Throwable throwable) {
        this.throwable = throwable;
    }

    protected void dispatch(final ThrowableHandler handler) {
        handler.onThrowable(this);
    }

    @Override
    public Type<ThrowableHandler> getAssociatedType() {
        return TYPE;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
