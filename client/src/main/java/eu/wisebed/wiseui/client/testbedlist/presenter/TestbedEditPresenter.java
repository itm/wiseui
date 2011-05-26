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
package eu.wisebed.wiseui.client.testbedlist.presenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.base.Objects;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;

import eu.wisebed.wiseui.api.TestbedConfigurationServiceAsync;
import eu.wisebed.wiseui.client.testbedlist.event.EditTestbedEvent;
import eu.wisebed.wiseui.client.testbedlist.event.RefreshTestbedListEvent;
import eu.wisebed.wiseui.client.testbedlist.view.TestbedEditView;
import eu.wisebed.wiseui.client.testbedlist.view.TestbedEditView.Presenter;
import eu.wisebed.wiseui.client.util.EventBusManager;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;
import eu.wisebed.wiseui.widgets.messagebox.MessageBox;
import eu.wisebed.wiseui.widgets.messagebox.MessageBox.Button;

public class TestbedEditPresenter implements Presenter, EditTestbedEvent.Handler {

    private static final String DEFAULT_NEW_TITLE = "New Testbed Configuration";

    private final EventBusManager eventBus;

    private final TestbedEditView view;

    private final ListDataProvider<String> urnPrefixProvider = new ListDataProvider<String>();

    private final SingleSelectionModel<String> selectionModel = new SingleSelectionModel<String>();

    private final TestbedConfigurationServiceAsync service;

    private TestbedConfiguration configuration;

    private String title = DEFAULT_NEW_TITLE;

    @Inject
    public TestbedEditPresenter(final EventBus eventBus,
                                final TestbedEditView view,
                                final TestbedConfigurationServiceAsync service) {
        this.eventBus = new EventBusManager(eventBus);
        this.view = view;
        this.service = service;

        urnPrefixProvider.addDataDisplay(view.getUrnPrefixHasData());
        view.setFederatedItems(Arrays.asList("Yes", "No"));
        view.setUrnPrefixSelectionModel(selectionModel);
        view.getUrnPrefixRemoveHasEnabled().setEnabled(false);
        bind();
    }

    private void bind() {
        eventBus.addHandler(EditTestbedEvent.TYPE, this);

        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(final SelectionChangeEvent event) {
                view.getUrnPrefixRemoveHasEnabled().setEnabled(null != selectionModel.getSelectedObject());
            }
        });
    }

    private void loadConfigurationToView() {
        view.getNameHasText().setText(configuration.getName());
        view.getTestbedUrlHasText().setText(configuration.getTestbedUrl());
        view.getSnaaEndpointUrlHasText().setText(configuration.getSnaaEndpointUrl());
        view.getRsEndpointUrlHasText().setText(configuration.getRsEndpointUrl());
        view.getSessionManagementEndpointUrlHasText().setText(configuration.getSessionmanagementEndpointUrl());
        view.setFederatedSelectedIndex(configuration.isFederated() ? 0 : 1);
        urnPrefixProvider.setList(new ArrayList<String>(configuration.getUrnPrefixList()));
    }

    @Override
    public void submit() {
        if (!view.validate()) return;

        configuration.setName(view.getNameHasText().getText());
        configuration.setTestbedUrl(view.getTestbedUrlHasText().getText());
        configuration.setSnaaEndpointUrl(view.getSnaaEndpointUrlHasText().getText());
        configuration.setRsEndpointUrl(view.getRsEndpointUrlHasText().getText());
        configuration.setSessionmanagementEndpointUrl(view.getSessionManagementEndpointUrlHasText().getText());
        configuration.setFederated(view.getFederatedSelectedIndex() != 0);
        // Copy list to avoid SerializationException!
        List<String> urnPrefixes = new ArrayList<String>(urnPrefixProvider.getList().size());
        for (String s : urnPrefixProvider.getList()) {
            urnPrefixes.add(s);
        }
        configuration.setUrnPrefixList(urnPrefixes);

        final MessageBox.Callback messageBoxCallback = new MessageBox.Callback() {

            @Override
            public void onButtonClicked(final Button button) {
                view.hide();
                eventBus.fireEventFromSource(new RefreshTestbedListEvent(), TestbedEditPresenter.this);
            }
        };

        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                MessageBox.error("Save Testbed", "Unable to save testbed.", caught, null);
            }

            @Override
            public void onSuccess(Void result) {
                MessageBox.success(title, configuration.getName() + " was successfully saved.", messageBoxCallback);
            }
        };
        service.storeConfiguration(configuration, callback);
    }

    @Override
    public void cancel() {
        MessageBox.warning(title, "Do you want to discard your changes?", new MessageBox.Callback() {

            @Override
            public void onButtonClicked(final Button button) {
                if (Button.OK.equals(button)) {
                    view.hide();
                }
            }
        });
    }

    @Override
    public void add() {
        final String urnPrefix = view.getUrnPrefixHasText().getText();
        urnPrefixProvider.getList().add(urnPrefix);
        urnPrefixProvider.refresh();
    }

    @Override
    public void remove() {
        final String urnPrefix = selectionModel.getSelectedObject();
        urnPrefixProvider.getList().remove(urnPrefix);
        urnPrefixProvider.refresh();
        view.getUrnPrefixRemoveHasEnabled().setEnabled(false);
    }

    @Override
    public void onEditTestbed(final EditTestbedEvent event) {
        configuration = Objects.firstNonNull(event.getConfiguration(), new TestbedConfiguration());
        title = Objects.firstNonNull(configuration.getName(), DEFAULT_NEW_TITLE);
        loadConfigurationToView();
        view.show(title);
    }
}
