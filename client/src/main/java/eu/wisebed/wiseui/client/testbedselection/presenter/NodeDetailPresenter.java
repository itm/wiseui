package eu.wisebed.wiseui.client.testbedselection.presenter;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;

import eu.wisebed.wiseui.client.testbedselection.TestbedSelectionPlace;
import eu.wisebed.wiseui.client.testbedselection.view.NodeDetailView;

public class NodeDetailPresenter implements NodeDetailView.Presenter {
	
	private final EventBus eventBus;
	
	private final NodeDetailView view;
	
	@Inject
	public NodeDetailPresenter(final EventBus eventBus, NodeDetailView view) {
		this.eventBus = eventBus;
		this.view = view;
	}

	public void setPlace(TestbedSelectionPlace place) {
		// TODO Auto-generated method stub
		
	}
}
