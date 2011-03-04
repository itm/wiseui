package eu.wisebed.wiseui.client.testbedselection.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import eu.wisebed.wiseui.api.TestbedConfigurationServiceAsync;
import eu.wisebed.wiseui.client.testbedselection.TestbedSelectionPlace;
import eu.wisebed.wiseui.client.testbedselection.event.ConfigurationSelectedEvent;
import eu.wisebed.wiseui.client.testbedselection.view.ConfigurationView;
import eu.wisebed.wiseui.client.testbedselection.view.ConfigurationView.Presenter;
import eu.wisebed.wiseui.shared.TestbedConfiguration;

import java.util.List;

public class ConfigurationPresenter implements Presenter {

    private final EventBus eventBus;
    private final ConfigurationView view;
    private TestbedSelectionPlace place;
    private final PlaceController placeController;
    private SingleSelectionModel<TestbedConfiguration> configurationSelectionModel;
    private List<TestbedConfiguration> configurations;
    private final TestbedConfigurationServiceAsync configurationService;

    @Inject
    public ConfigurationPresenter(final EventBus eventBus,
                                  final ConfigurationView view,
                                  final PlaceController placeController,
                                  final TestbedConfigurationServiceAsync configurationService) {
        this.eventBus = eventBus;
        this.view = view;
        this.placeController = placeController;
        this.configurationService = configurationService;

        // Init selection model
        configurationSelectionModel = new SingleSelectionModel<TestbedConfiguration>();
        view.setTestbedConfigurationSelectionModel(configurationSelectionModel);
    }

    public void setPlace(final TestbedSelectionPlace place) {
        this.place = place;
        loadTestbedConfigurations();
        bind();
    }

    private void bind() {
        configurationSelectionModel.addSelectionChangeHandler(new Handler() {

            public void onSelectionChange(final SelectionChangeEvent event) {
                onConfigurationSelectionChange(event);
            }
        });
    }

    private void onConfigurationSelectionChange(final SelectionChangeEvent event) {
        final TestbedConfiguration configuration = configurationSelectionModel.getSelectedObject();
        if (null == configuration) return;
        final Integer index = configurations.indexOf(configuration);
        if (!index.equals(place.getSelection())) {
            placeController.goTo(new TestbedSelectionPlace(index));
        }
    }

    private void loadTestbedConfigurations() {
        final AsyncCallback<List<TestbedConfiguration>> callback = new AsyncCallback<List<TestbedConfiguration>>() {

            public void onSuccess(final List<TestbedConfiguration> result) {
                configurations = result;
                view.setConfigurations(result);
                loadConfigurationSelectionFromPlace();
            }

            public void onFailure(final Throwable caught) {
                GWT.log(caught.getMessage());
            }
        };
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {

            public void execute() {
                configurationService.getConfigurations(callback);
            }
        });
    }

    public TestbedConfiguration getSelectedConfiguration() {
        final Integer selection = place.getSelection();
        return selection != null ? configurations.get(selection) : null;
    }

    private void loadConfigurationSelectionFromPlace() {
        final Integer selection = place.getSelection();
        GWT.log("Selection: " + selection);
        if (selection != null) {
            final TestbedConfiguration configuration = getSelectedConfiguration();
            if (configuration != null) {
                configurationSelectionModel.setSelected(configuration, true);
                eventBus.fireEvent(new ConfigurationSelectedEvent(configuration));
            }
        }
    }


}
