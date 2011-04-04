package eu.wisebed.wiseui.client.reservation.event;

import com.google.gwt.event.shared.EventHandler;

public interface LoginRequiredEventHandler extends EventHandler{
	void onLoginRequired(LoginRequiredEvent event);
}
