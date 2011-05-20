package eu.wisebed.wiseui.client.reservation.event;

import java.util.Set;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import eu.wisebed.wiseui.shared.dto.Node;

public class UpdateNodesSelectedEvent extends GwtEvent<UpdateNodesSelectedEvent.Handler> {

	public interface Handler extends EventHandler {

		void onUpdateNodesSelected(UpdateNodesSelectedEvent event);
	}

	public static final Type<Handler> TYPE = new Type<Handler>();

	private final Set<Node> nodes;

	public UpdateNodesSelectedEvent(final Set<Node> nodes) {
		this.nodes = nodes;
	}

	public Set<Node> getNodes(){
		return this.nodes;
	}

	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(final Handler handler) {
		handler.onUpdateNodesSelected(this);
	}
}
