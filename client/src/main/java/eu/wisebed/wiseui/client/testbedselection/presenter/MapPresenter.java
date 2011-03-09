package eu.wisebed.wiseui.client.testbedselection.presenter;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;

import eu.wisebed.wiseui.client.testbedselection.TestbedSelectionPlace;
import eu.wisebed.wiseui.client.testbedselection.event.ConfigurationSelectedEvent;
import eu.wisebed.wiseui.client.testbedselection.event.ConfigurationSelectedEvent.ConfigurationSelectedHandler;
import eu.wisebed.wiseui.client.testbedselection.event.WisemlLoadedEvent;
import eu.wisebed.wiseui.client.testbedselection.event.WisemlLoadedEvent.WisemlLoadedHandler;
import eu.wisebed.wiseui.client.testbedselection.view.MapView;
import eu.wisebed.wiseui.client.util.Coordinates;
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
        final Coordinate origin = setup.getOrigin();
        view.setTestbedCoordinate(origin, configuration.getName(), setup.getDescription());
        final List<Coordinate> coordinates = Lists.transform(setup.getNode(), new Function<Node, Coordinate>() {
			@Override
			public Coordinate apply(final Node input) {
				return Coordinates.absolute(origin, Coordinates.rotate(input.getPosition(), origin.getPhi()));
			}
		});
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			
			@Override
			public void execute() {
				view.setTestbedShape(QuickHull.calcuate(coordinates));
			}
		});
    }

    @Override
    public void onTestbedConfigurationSelected(final ConfigurationSelectedEvent event) {
        this.configuration = event.getConfiguration();
        view.setTestbedCoordinate(null, null, null);
        view.setTestbedShape(null);
    }
}
