package eu.wisebed.wiseui.client.util;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;


/**
 * EventBus implementation that stores all registration handlers inside for easy autoremove.
 * 
 * @author Malte Legenhausen
 */
public class EventBusManager extends EventBus {

	private final List<HandlerRegistration> registrations = new ArrayList<HandlerRegistration>();
	
	private final EventBus eventBus;
	
	public EventBusManager(final EventBus eventBus) {
		this.eventBus = eventBus;
	}
	
	@Override
	public <H extends EventHandler> HandlerRegistration addHandler(Type<H> type, H handler) {
    	final HandlerRegistration registration = eventBus.addHandler(type, handler);
    	registrations.add(registration);
    	return registration;
    }
	
	@Override
	public void fireEvent(GwtEvent<?> event) {
		eventBus.fireEvent(event);
	}
	
	@Override
	public void fireEventFromSource(GwtEvent<?> event, Object source) {
		eventBus.fireEventFromSource(event, source);
	}

	@Override
	public <H extends EventHandler> HandlerRegistration addHandlerToSource(Type<H> type, Object source, H handler) {
		final HandlerRegistration registration = eventBus.addHandlerToSource(type, source, handler);
		registrations.add(registration);
		return registration;
	}
	
	public void removeAll() {
		for (final HandlerRegistration registration : registrations) {
    		registration.removeHandler();
    	}
	}
}
