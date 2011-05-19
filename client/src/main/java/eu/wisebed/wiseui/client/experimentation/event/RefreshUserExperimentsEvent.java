package eu.wisebed.wiseui.client.experimentation.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class RefreshUserExperimentsEvent extends GwtEvent<RefreshUserExperimentsEvent.Handler>{

	public interface Handler extends EventHandler {
		void onRefreshUserExperiments(RefreshUserExperimentsEvent event);
	}
	
	public static final Type<RefreshUserExperimentsEvent.Handler> TYPE = new Type<RefreshUserExperimentsEvent.Handler>();


	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onRefreshUserExperiments(this);
	}
}
