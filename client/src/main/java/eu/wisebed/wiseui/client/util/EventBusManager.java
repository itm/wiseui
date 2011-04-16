package eu.wisebed.wiseui.client.util;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;

public class EventBusManager {

	private final List<HandlerRegistration> registrations = new ArrayList<HandlerRegistration>();
	
	private final EventBus eventBus;
	
	public EventBusManager(final EventBus eventBus) {
		this.eventBus = eventBus;
	}
	
	public <H extends EventHandler> void addHandler(Type<H> type, H handler) {
    	final HandlerRegistration registration = eventBus.addHandler(type, handler);
    	registrations.add(registration);
    }
	
	public void fireEvent(GwtEvent<?> event) {
		eventBus.fireEvent(event);
	}
	
	public void fireEventFromSource(GwtEvent<?> event, Object source) {
		eventBus.fireEventFromSource(event, source);
	}
	
	public void removeAll() {
		for (final HandlerRegistration registration : registrations) {
    		registration.removeHandler();
    	}
	}
}
