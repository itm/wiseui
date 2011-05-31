/**
 * Copyright (C) 2011 Universität zu Lübeck, Institut für Telematik (ITM), Research Academic Computer Technology Institute (RACTI)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.wisebed.wiseui.client.testbedlist;

import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;

import eu.wisebed.wiseui.api.TestbedConfigurationServiceAsync;
import eu.wisebed.wiseui.client.WiseUiGinjector;
import eu.wisebed.wiseui.client.main.WiseUiPlace;
import eu.wisebed.wiseui.client.testbedlist.event.EditTestbedEvent;
import eu.wisebed.wiseui.client.testbedlist.event.RefreshTestbedListEvent;
import eu.wisebed.wiseui.client.testbedlist.event.ShowLoginDialogEvent;
import eu.wisebed.wiseui.client.testbedlist.event.TestbedSelectedEvent;
import eu.wisebed.wiseui.client.testbedlist.presenter.LoginDialogPresenter;
import eu.wisebed.wiseui.client.testbedlist.presenter.TestbedEditPresenter;
import eu.wisebed.wiseui.client.testbedlist.view.LoginDialogView;
import eu.wisebed.wiseui.client.testbedlist.view.TestbedEditView;
import eu.wisebed.wiseui.client.testbedlist.view.TestbedListView;
import eu.wisebed.wiseui.client.testbedlist.view.TestbedListView.Presenter;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;
import eu.wisebed.wiseui.widgets.messagebox.MessageBox;
import eu.wisebed.wiseui.widgets.messagebox.MessageBox.Button;

/**
 * The presenter for the {@link eu.wisebed.wiseui.client.testbedlist.view.TestbedListView}.
 *
 * @author Malte Legenhausen
 */
public class TestbedListActivity  extends AbstractActivity implements Presenter, RefreshTestbedListEvent.Handler {

	private final WiseUiGinjector injector;
    private EventBus eventBus;
    private final TestbedListView view;
    private WiseUiPlace place;
    private final PlaceController placeController;
    private SingleSelectionModel<TestbedConfiguration> configurationSelectionModel;
    private List<TestbedConfiguration> configurations;
    private final TestbedConfigurationServiceAsync configurationService;

    @Inject
    public TestbedListActivity(final WiseUiGinjector injector,
                               final TestbedListView view,
                               final PlaceController placeController,
                               final TestbedConfigurationServiceAsync configurationService) {
    	this.injector = injector;
        this.view = view;
        this.placeController = placeController;
        this.configurationService = configurationService;
        
        view.setPresenter(this);
        // Init selection model
        configurationSelectionModel = new SingleSelectionModel<TestbedConfiguration>();
        view.setTestbedConfigurationSelectionModel(configurationSelectionModel);
        view.getLoginEnabled().setEnabled(false);
        view.getTestbedEditEnabled().setEnabled(false);
        view.getTestbedDeleteEnabled().setEnabled(false);
    }
    
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
        this.eventBus = eventBus;
        initLoginDialogPart();
        initTestbedEditPart();
		panel.setWidget(view);
		bind();
	}

    public void setPlace(final WiseUiPlace place) {
    	this.place = place;
        loadTestbedConfigurations();
    }

    private void bind() {
    	eventBus.addHandler(RefreshTestbedListEvent.TYPE, this);
    	
        configurationSelectionModel.addSelectionChangeHandler(new Handler() {

            @Override
            public void onSelectionChange(final SelectionChangeEvent event) {
                onConfigurationSelectionChange(event);
            }
        });
    }
    
    private void initLoginDialogPart() {
        GWT.log("Init Login Dialog Part");
        final LoginDialogPresenter loginDialogPresenter = injector.getLoginDialogPresenter();
        final LoginDialogView loginDialogView = injector.getLoginDialogView();
        loginDialogView.setPresenter(loginDialogPresenter);
    }
    
    private void initTestbedEditPart() {
    	GWT.log("Init Testbed Edit Part");
    	final TestbedEditPresenter testbedEditPresenter = injector.getTestbedEditPresenter();
    	final TestbedEditView testbedEditView = injector.getTestbedEditView();
    	testbedEditView.setPresenter(testbedEditPresenter);
    }

    private void onConfigurationSelectionChange(final SelectionChangeEvent event) {
        final TestbedConfiguration configuration = configurationSelectionModel.getSelectedObject();
        if (null == configuration) return;
        final Integer index = configurations.indexOf(configuration);
        final TestbedListPlace testbedListPlace = (TestbedListPlace) place.get(TestbedListPlace.class);
        if (!index.equals(testbedListPlace.getTestbedId())) {
            placeController.goTo(place.update(new TestbedListPlace(index)));
        }
    }

    private void loadTestbedConfigurations() {
        final AsyncCallback<List<TestbedConfiguration>> callback = new AsyncCallback<List<TestbedConfiguration>>() {

            @Override
            public void onSuccess(final List<TestbedConfiguration> result) {
                configurations = result;
                view.setConfigurations(result);
                loadConfigurationSelectionFromPlace();
            }

            @Override
            public void onFailure(final Throwable caught) {
                GWT.log(caught.getMessage());
            }
        };
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {

            @Override
            public void execute() {
                configurationService.getConfigurations(callback);
            }
        });
    }

    public TestbedConfiguration getSelectedConfiguration() {
    	final TestbedListPlace testbedListPlace = (TestbedListPlace) place.get(TestbedListPlace.class);
        final Integer selection = testbedListPlace.getTestbedId();
        return selection != null ? configurations.get(selection) : null;
    }

    private void loadConfigurationSelectionFromPlace() {
    	final TestbedListPlace testbedListPlace = (TestbedListPlace) place.get(TestbedListPlace.class);
        final Integer selection = testbedListPlace.getTestbedId();
        GWT.log("Selection: " + selection);
        if (selection != null) {
            final TestbedConfiguration configuration = getSelectedConfiguration();
            if (configuration != null) {
                configurationSelectionModel.setSelected(configuration, true);
                view.getLoginEnabled().setEnabled(true);
                view.getTestbedEditEnabled().setEnabled(true);
                view.getTestbedDeleteEnabled().setEnabled(true);
                eventBus.fireEvent(new TestbedSelectedEvent(configuration));
            }
        }
    }

    @Override
    public void showLoginDialog() {
        eventBus.fireEventFromSource(new ShowLoginDialogEvent(), this);
    }

	@Override
	public void showNewTestbedDialog() {
		eventBus.fireEventFromSource(new EditTestbedEvent(), this);
	}

	@Override
	public void showEditTestbedDialog() {
		final TestbedConfiguration configuration = getSelectedConfiguration();
		eventBus.fireEventFromSource(new EditTestbedEvent(configuration), this);
	}

	@Override
	public void deleteTestbed() {
		final TestbedConfiguration configuration = getSelectedConfiguration();
		
		final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				MessageBox.error("Delete Testbed", "Unable to delete testbed " + configuration.getName(), caught, null);
			}

			@Override
			public void onSuccess(Void result) {
				MessageBox.success("Testbed successful deleted", configuration.getName() + " was successfully deleted", null);
			}
		};
		
		MessageBox.warning(configuration.getName(), "Do you really want to delete " + configuration.getName() + "?", new MessageBox.Callback() {
			
			@Override
			public void onButtonClicked(Button button) {
				if (Button.OK.equals(button)) {
					configurationService.removeConfiguration(configuration.getId(), callback);
				}
			}
		});
	}

	@Override
	public void refresh() {
		eventBus.fireEventFromSource(new RefreshTestbedListEvent(), this);
	}

	@Override
	public void onRefreshTestbedList(RefreshTestbedListEvent event) {
		loadTestbedConfigurations();
	}
	
    public boolean isAuthenticated(final TestbedConfiguration testbedConfiguration) {
    	return injector.getAuthenticationManager().isAuthenticated(testbedConfiguration);
    }
}
