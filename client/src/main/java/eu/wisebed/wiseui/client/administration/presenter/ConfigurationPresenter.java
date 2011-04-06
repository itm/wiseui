package eu.wisebed.wiseui.client.administration.presenter;

import java.util.List;

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
import eu.wisebed.wiseui.client.administration.AdministrationPlace;
import eu.wisebed.wiseui.client.administration.event.CreateConfigurationEvent;
import eu.wisebed.wiseui.client.administration.event.CreateConfigurationEvent.CreateConfigurationHandler;
import eu.wisebed.wiseui.client.testbedlist.event.TestbedSelectedEvent;
import eu.wisebed.wiseui.client.testbedlist.view.TestbedListView;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;

/**
 * Presenter for the testbed configuration view of the administration part.
 * 
 * @author Malte Legenhausen
 */
public class ConfigurationPresenter implements TestbedListView.Presenter, CreateConfigurationHandler {

	private final TestbedListView view;
	private final TestbedConfigurationServiceAsync configurationService;
	private final SingleSelectionModel<TestbedConfiguration> configurationSelectionModel;
    private final PlaceController placeController;
    private List<TestbedConfiguration> configurations;
    private AdministrationPlace place;
    private final EventBus eventBus;
	
	@Inject
	public ConfigurationPresenter(final EventBus eventBus, final TestbedListView view, final PlaceController placeController, final TestbedConfigurationServiceAsync configurationService) {
		this.eventBus = eventBus;
		this.view = view;
		this.placeController = placeController;
		this.configurationService = configurationService;
		
        // Init selection model
        configurationSelectionModel = new SingleSelectionModel<TestbedConfiguration>();
        view.setTestbedConfigurationSelectionModel(configurationSelectionModel);
	}
	
	public void setPlace(final AdministrationPlace place) {
		this.place = place;
        loadTestbedConfigurations();
        bind();
	}

    private void bind() {
    	eventBus.addHandler(CreateConfigurationEvent.TYPE, this);
        configurationSelectionModel.addSelectionChangeHandler(new Handler() {

            @Override
            public void onSelectionChange(final SelectionChangeEvent event) {
                onConfigurationSelectionChange(event);
            }
        });
    }

    private void onConfigurationSelectionChange(final SelectionChangeEvent event) {
        final TestbedConfiguration configuration = configurationSelectionModel.getSelectedObject();
        if (null == configuration) return;
        final Integer index = configurations.indexOf(configuration);
        if (!index.equals(place.getTestbedId())) {
            placeController.goTo(new AdministrationPlace(index));
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
        final Integer selection = place.getTestbedId();
        return selection != null ? configurations.get(selection) : null;
    }

    private void loadConfigurationSelectionFromPlace() {
        final Integer selection = place.getTestbedId();
        GWT.log("Selection: " + selection);
        if (selection != null) {
            final TestbedConfiguration configuration = getSelectedConfiguration();
            if (configuration != null) {
                configurationSelectionModel.setSelected(configuration, true);
                eventBus.fireEvent(new TestbedSelectedEvent(configuration));
            }
        }
    }

	@Override
	public void onCreateConfiguration(final CreateConfigurationEvent event) {
		placeController.goTo(new AdministrationPlace());
	}
}
