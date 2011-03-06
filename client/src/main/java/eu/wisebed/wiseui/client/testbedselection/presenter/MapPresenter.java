package eu.wisebed.wiseui.client.testbedselection.presenter;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;

import eu.wisebed.wiseui.client.testbedselection.TestbedSelectionPlace;
import eu.wisebed.wiseui.client.testbedselection.event.ConfigurationSelectedEvent;
import eu.wisebed.wiseui.client.testbedselection.event.ConfigurationSelectedEvent.ConfigurationSelectedHandler;
import eu.wisebed.wiseui.client.testbedselection.event.WisemlLoadedEvent;
import eu.wisebed.wiseui.client.testbedselection.event.WisemlLoadedEvent.WisemlLoadedHandler;
import eu.wisebed.wiseui.client.testbedselection.view.MapView;
import eu.wisebed.wiseui.shared.TestbedConfiguration;
import eu.wisebed.wiseui.shared.wiseml.Setup;

public class MapPresenter implements MapView.Presenter, WisemlLoadedHandler, ConfigurationSelectedHandler {

	private final EventBus eventBus;
	
	private final MapView view;
	
	private TestbedConfiguration configuration;
	
	@Inject
	public MapPresenter(final EventBus eventBus, final MapView view) {
		this.eventBus = eventBus;
		this.view = view;
		bind();
	}
	
	private void bind() {
		eventBus.addHandler(ConfigurationSelectedEvent.TYPE, this);
		eventBus.addHandler(WisemlLoadedEvent.TYPE, this);
	}
	
	public void setPlace(final TestbedSelectionPlace place) {
		
	}

	public void onWisemlLoaded(final WisemlLoadedEvent event) {
		final Setup setup = event.getWiseml().getSetup();
		view.setTestbedCoordinate(setup.getOrigin(), configuration.getName(), setup.getDescription());
	}

	public void onTestbedConfigurationSelected(final ConfigurationSelectedEvent event) {
		this.configuration = event.getConfiguration();
		view.setTestbedCoordinate(null, null, null);
	}

}
