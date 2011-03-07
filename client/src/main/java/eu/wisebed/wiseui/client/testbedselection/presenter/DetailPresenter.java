package eu.wisebed.wiseui.client.testbedselection.presenter;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;

import eu.wisebed.wiseui.client.testbedselection.TestbedSelectionPlace;
import eu.wisebed.wiseui.client.testbedselection.common.TestbedTreeViewModel;
import eu.wisebed.wiseui.client.testbedselection.event.ConfigurationSelectedEvent;
import eu.wisebed.wiseui.client.testbedselection.event.ConfigurationSelectedEvent.ConfigurationSelectedHandler;
import eu.wisebed.wiseui.client.testbedselection.event.ThrowableEvent;
import eu.wisebed.wiseui.client.testbedselection.event.ThrowableEvent.ThrowableHandler;
import eu.wisebed.wiseui.client.testbedselection.event.WisemlLoadedEvent;
import eu.wisebed.wiseui.client.testbedselection.event.WisemlLoadedEvent.WisemlLoadedHandler;
import eu.wisebed.wiseui.client.testbedselection.view.DetailView;
import eu.wisebed.wiseui.client.testbedselection.view.DetailView.Presenter;
import eu.wisebed.wiseui.shared.TestbedConfiguration;
import eu.wisebed.wiseui.shared.exception.WisemlException;
import eu.wisebed.wiseui.shared.wiseml.Capability;
import eu.wisebed.wiseui.shared.wiseml.Node;
import eu.wisebed.wiseui.shared.wiseml.Setup;

public class DetailPresenter implements Presenter, ConfigurationSelectedHandler, WisemlLoadedHandler, ThrowableHandler {

    private final DetailView view;

    private final EventBus eventBus;
    
    private final ListDataProvider<Capability> capabilityListDataProvider = new ListDataProvider<Capability>();

    private TestbedConfiguration configuration;
    
    private SingleSelectionModel<Node> nodeSelectionModel = new SingleSelectionModel<Node>(); 

    @Inject
    public DetailPresenter(final EventBus eventBus, final DetailView view) {
        this.view = view;
        this.eventBus = eventBus;
        capabilityListDataProvider.addDataDisplay(view.getCapababilitesList());
        bind();
        view.showMessage("Select a Testbed Configuration.");
    }

    private void bind() {
        eventBus.addHandler(ConfigurationSelectedEvent.TYPE, this);
        eventBus.addHandler(WisemlLoadedEvent.TYPE, this);
        eventBus.addHandler(ThrowableEvent.TYPE, this);
        nodeSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
        	
			@Override
			public void onSelectionChange(final SelectionChangeEvent event) {
				final Node node = nodeSelectionModel.getSelectedObject();
				onNodeSelection(node);
			}
		});
    }
    
    private void onNodeSelection(final Node node) {
    	view.getNodeIdHasText().setText(node.getId());
		view.getNodePositionHasText().setText(node.getPosition().toString());
		
		final Boolean isGateway = node.isGateway();
		String gateway = "Unknown";
		if (isGateway != null) {
			gateway = isGateway ? "Yes" : "No";
		}
		view.getNodeGatewayHasText().setText(gateway);
		
		String description = node.getDescription();
		if (description == null || description.isEmpty()) {
			description = "No details available for this node.";
		}
		view.getNodeDescriptionHasText().setText(description);
		
		String programDetails = node.getProgramDetails();
		if (programDetails == null || programDetails.isEmpty()) {
			programDetails = "No program details available for this node.";
		}
		view.getNodeProgramDetailsHasText().setText(programDetails);
		capabilityListDataProvider.setList(node.getCapability());
    }

    public void setPlace(final TestbedSelectionPlace place) {

    }

    public void onTestbedConfigurationSelected(final ConfigurationSelectedEvent event) {
        configuration = event.getConfiguration();
        view.showMessage("Loading Testbed...");
    }

    public void onWisemlLoaded(final WisemlLoadedEvent event) {
        final Setup setup = event.getWiseml().getSetup();
        if (null == setup) return;
        view.setTreeViewModel(new TestbedTreeViewModel(configuration, setup.getNode(), nodeSelectionModel));
    }

    public void onThrowable(final ThrowableEvent event) {
        if (event.getThrowable() instanceof WisemlException) {
            view.showMessage("Unable to load Testbed.");
        }
    }
}
