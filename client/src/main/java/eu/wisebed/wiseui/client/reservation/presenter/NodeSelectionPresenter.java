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
package eu.wisebed.wiseui.client.reservation.presenter;

import java.util.Set;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.inject.Inject;

import eu.wisebed.wiseui.api.SessionManagementServiceAsync;
import eu.wisebed.wiseui.client.reservation.common.NodeTreeViewModel;
import eu.wisebed.wiseui.client.reservation.event.UpdateNodesSelectedEvent;
import eu.wisebed.wiseui.client.reservation.view.NodeSelectionView;
import eu.wisebed.wiseui.client.testbedlist.event.TestbedSelectedEvent;
import eu.wisebed.wiseui.client.testbedselection.event.ThrowableEvent;
import eu.wisebed.wiseui.client.testbedselection.event.WisemlLoadedEvent;
import eu.wisebed.wiseui.client.util.DefaultsHelper;
import eu.wisebed.wiseui.client.util.EventBusManager;
import eu.wisebed.wiseui.shared.dto.Node;
import eu.wisebed.wiseui.shared.dto.Setup;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;
import eu.wisebed.wiseui.shared.dto.Wiseml;
import eu.wisebed.wiseui.widgets.messagebox.MessageBox;

/**
 * @author Soenke Nommensen
 */
public class NodeSelectionPresenter implements NodeSelectionView.Presenter,
        TestbedSelectedEvent.ConfigurationSelectedHandler,
        ThrowableEvent.ThrowableHandler, PlaceChangeEvent.Handler,
        WisemlLoadedEvent.WisemlLoadedHandler {

    private final EventBusManager eventBus;
    private final NodeSelectionView view;
    private final SessionManagementServiceAsync service;
    private TestbedConfiguration testbedConfiguration;
    private MultiSelectionModel<Node> nodeSelectionModel = new MultiSelectionModel<Node>();
    
    @Inject
    public NodeSelectionPresenter(final EventBus eventBus,
                                  final NodeSelectionView view,
                                  final SessionManagementServiceAsync service) {
        this.eventBus = new EventBusManager(eventBus);
        this.view = view;
        this.service = service;
        bind();
    }

    public void bind() {
        eventBus.addHandler(PlaceChangeEvent.TYPE, this);
        eventBus.addHandler(TestbedSelectedEvent.TYPE, this);
        eventBus.addHandler(ThrowableEvent.TYPE, this);
        eventBus.addHandler(WisemlLoadedEvent.TYPE, this);

        nodeSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(final SelectionChangeEvent event) {
                final Set<Node> nodes = nodeSelectionModel.getSelectedSet();
                onNodeSelection(nodes);
            }
        });
    }

    private void onNodeSelection(final Set<Node> nodes) {
        eventBus.fireEvent(new UpdateNodesSelectedEvent(nodes));
    }

    @Override
    public void onTestbedSelected(final TestbedSelectedEvent event) {
        view.getLoadingIndicator().showLoading("Testbed Nodes");

        this.testbedConfiguration = event.getConfiguration();

        final AsyncCallback<Wiseml> callback = new AsyncCallback<Wiseml>() {

            public void onSuccess(final Wiseml result) {
                if (result != null) {
                    result.setSetup(DefaultsHelper.apply(result.getSetup()));
                }
                eventBus.fireEvent(new WisemlLoadedEvent(result));
                view.getLoadingIndicator().hideLoading();
            }

            public void onFailure(final Throwable caught) {
                view.getLoadingIndicator().hideLoading();
                eventBus.fireEvent(new ThrowableEvent(caught));
            }
        };
        final String url = testbedConfiguration.getSessionmanagementEndpointUrl().trim();
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

            public void execute() {
                service.getWiseml(url, callback);
            }
        });
    }

    @Override
    public void onWisemlLoaded(final WisemlLoadedEvent event) {
        final Setup setup = event.getWiseml().getSetup();
        if (setup != null) {
            view.setTreeViewModel(new NodeTreeViewModel(testbedConfiguration, setup.getNode(), nodeSelectionModel));
        }
        //view.getLoadingIndicator().hideLoading();
    }

    @Override
    public void onPlaceChange(final PlaceChangeEvent event) {
        view.getLoadingIndicator().hideLoading();
        eventBus.removeAll();
    }

    @Override
    public void onThrowable(final ThrowableEvent event) {
        view.getLoadingIndicator().hideLoading();
        if (event.getThrowable() == null) return;
        if (testbedConfiguration != null && testbedConfiguration.getName() != null) {
            MessageBox.error("Error fetching testbed data for testbed '"
                    + testbedConfiguration.getName()
                    + "'!",
                    event.getThrowable().getMessage(),
                    event.getThrowable(), null);
        } else {
            MessageBox.error("Error fetching testbed data!",
                    event.getThrowable().getMessage(),
                    event.getThrowable(), null);
        }
    }
}
