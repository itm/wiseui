/*
 * Copyright 2011 Universität zu Lübeck, Institut für Telematik (ITM),
 *              Research Academic Computer Technology Institute (RACTI)
 *
 * ITM and RACTI license this file under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.wisebed.wiseui.client.testbedselection.presenter;

import com.google.common.base.Strings;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;

import eu.wisebed.wiseui.client.testbedlist.event.TestbedSelectedEvent;
import eu.wisebed.wiseui.client.testbedlist.event.TestbedSelectedEvent.ConfigurationSelectedHandler;
import eu.wisebed.wiseui.client.testbedselection.common.TestbedTreeViewModel;
import eu.wisebed.wiseui.client.testbedselection.event.ThrowableEvent;
import eu.wisebed.wiseui.client.testbedselection.event.ThrowableEvent.ThrowableHandler;
import eu.wisebed.wiseui.client.testbedselection.event.WisemlLoadedEvent;
import eu.wisebed.wiseui.client.testbedselection.event.WisemlLoadedEvent.WisemlLoadedHandler;
import eu.wisebed.wiseui.client.testbedselection.view.DetailView;
import eu.wisebed.wiseui.client.testbedselection.view.DetailView.Presenter;
import eu.wisebed.wiseui.client.util.EventBusManager;
import eu.wisebed.wiseui.shared.dto.Capability;
import eu.wisebed.wiseui.shared.dto.Node;
import eu.wisebed.wiseui.shared.dto.Setup;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;
import eu.wisebed.wiseui.shared.exception.WisemlException;


/**
 * Presenter implementation for the <code>DetailView</code>.
 * 
 * @author Malte Legenhausen
 */
public class DetailPresenter implements Presenter, ConfigurationSelectedHandler, WisemlLoadedHandler, ThrowableHandler, PlaceChangeEvent.Handler {

    private final DetailView view;

    private final EventBusManager eventBus;
    
    private final ListDataProvider<Capability> capabilityListDataProvider = new ListDataProvider<Capability>();

    private TestbedConfiguration configuration;

    private SingleSelectionModel<Node> nodeSelectionModel = new SingleSelectionModel<Node>();

    @Inject
    public DetailPresenter(final EventBus eventBus, final DetailView view) {
        this.view = view;
        this.eventBus = new EventBusManager(eventBus);
        capabilityListDataProvider.addDataDisplay(view.getCapababilitesList());
        view.showMessage("Select a Testbed Configuration.");
        bind();
    }

    public void bind() {
    	eventBus.addHandler(PlaceChangeEvent.TYPE, this);
        eventBus.addHandler(TestbedSelectedEvent.TYPE, this);
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
    	view.getNodeTypeHasText().setText(node.getNodeType());
		view.getNodePositionHasText().setText(node.getPosition().toString());
		
		final Boolean isGateway = node.isGateway();
		String gateway = "Unknown";
		if (isGateway != null) {
			gateway = isGateway ? "Yes" : "No";
		}
		view.getNodeGatewayHasText().setText(gateway);
		
		String description = node.getDescription();
		if (Strings.isNullOrEmpty(description)) {
			description = "No details available for this node.";
		}
		view.getNodeDescriptionHasText().setText(description);
		
		String programDetails = node.getProgramDetails();
		if (Strings.isNullOrEmpty(programDetails)) {
			programDetails = "No program details available for this node.";
		}
		view.getNodeProgramDetailsHasText().setText(programDetails);
		capabilityListDataProvider.setList(node.getCapability());
    }

    public void onTestbedSelected(final TestbedSelectedEvent event) {
    	configuration = event.getConfiguration();
    	view.getLoadingIndicator().showLoading("Loading Testbed");
        view.showMessage("");
        view.getDescriptionHasText().setText("");
    }

    public void onWisemlLoaded(final WisemlLoadedEvent event) {
        final Setup setup = event.getWiseml().getSetup();
        if (setup != null) {
        	view.getDescriptionHasText().setText(setup.getDescription());
        	view.setTreeViewModel(new TestbedTreeViewModel(configuration, setup.getNode(), nodeSelectionModel));
        }
        view.getLoadingIndicator().hideLoading();
    }

    public void onThrowable(final ThrowableEvent event) {
        if (event.getThrowable() instanceof WisemlException) {
            view.showMessage("Unable to load Testbed.");
        }
        view.getLoadingIndicator().hideLoading();
    }

	@Override
	public void onPlaceChange(final PlaceChangeEvent event) {
		eventBus.removeAll();
	}
}
