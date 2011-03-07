package eu.wisebed.wiseui.client.testbedselection.presenter;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;

import eu.wisebed.wiseui.client.testbedselection.TestbedSelectionPlace;
import eu.wisebed.wiseui.client.testbedselection.event.ConfigurationSelectedEvent;
import eu.wisebed.wiseui.client.testbedselection.event.ConfigurationSelectedEvent.ConfigurationSelectedHandler;
import eu.wisebed.wiseui.client.testbedselection.event.WisemlLoadedEvent;
import eu.wisebed.wiseui.client.testbedselection.event.WisemlLoadedEvent.WisemlLoadedHandler;
import eu.wisebed.wiseui.client.testbedselection.view.MapView;
import eu.wisebed.wiseui.client.util.QuickHull;
import eu.wisebed.wiseui.shared.TestbedConfiguration;
import eu.wisebed.wiseui.shared.wiseml.Coordinate;
import eu.wisebed.wiseui.shared.wiseml.Node;
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

    @Override
    public void setPlace(final TestbedSelectionPlace place) {
    }

    @Override
    public void onWisemlLoaded(final WisemlLoadedEvent event) {
        final Setup setup = event.getWiseml().getSetup();
        view.setTestbedCoordinate(setup.getOrigin(), configuration.getName(), setup.getDescription());
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			
			@Override
			public void execute() {
				final List<Node> nodes = setup.getNode();
				if (nodes.size() < 3) {
					return;
				}
				final List<Coordinate> coordinates = new ArrayList<Coordinate>(nodes.size());
				for (final Node node : setup.getNode()) {
					coordinates.add(node.getPosition());
				}
				QuickHull quickHull = new QuickHull(coordinates.toArray(new Coordinate[0]));
				view.setTestbedShape(quickHull.getHullCoordinatesAsVector());
			}
		});
    }

    @Override
    public void onTestbedConfigurationSelected(final ConfigurationSelectedEvent event) {
        this.configuration = event.getConfiguration();
        view.setTestbedCoordinate(null, null, null);
    }
}
