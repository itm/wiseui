package eu.wisebed.wiseui.client.testbedlist;

import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;

import eu.wisebed.wiseui.api.TestbedConfigurationServiceAsync;
import eu.wisebed.wiseui.client.WiseUiPlace;
import eu.wisebed.wiseui.client.testbedlist.event.TestbedSelectedEvent;
import eu.wisebed.wiseui.client.testbedlist.view.TestbedListView;
import eu.wisebed.wiseui.client.testbedlist.view.TestbedListView.Presenter;
import eu.wisebed.wiseui.client.testbedselection.TestbedSelectionPlace;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;

/**
 * The presenter for the {@link eu.wisebed.wiseui.client.testbedlist.view.TestbedListView}.
 *
 * @author Malte Legenhausen
 */
public class TestbedListActivity  extends AbstractActivity implements Presenter {

    private final EventBus eventBus;
    private final TestbedListView view;
    private WiseUiPlace place;
    private final PlaceController placeController;
    private SingleSelectionModel<TestbedConfiguration> configurationSelectionModel;
    private List<TestbedConfiguration> configurations;
    private final TestbedConfigurationServiceAsync configurationService;

    @Inject
    public TestbedListActivity(final EventBus eventBus,
                               final TestbedListView view,
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
    
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(view);
	}

    public void setPlace(final WiseUiPlace place) {
    	this.place = place;
        loadTestbedConfigurations();
        bind();
    }

    private void bind() {
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
            placeController.goTo(place.copy(index));
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
}
