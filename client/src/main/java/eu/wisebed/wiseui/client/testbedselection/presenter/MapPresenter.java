package eu.wisebed.wiseui.client.testbedselection.presenter;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;

import eu.wisebed.wiseui.client.testbedselection.TestbedSelectionPlace;
import eu.wisebed.wiseui.client.testbedselection.event.ConfigurationSelectedEvent;
import eu.wisebed.wiseui.client.testbedselection.event.ConfigurationSelectedEvent.ConfigurationSelectedHandler;
import eu.wisebed.wiseui.client.testbedselection.event.WisemlLoadedEvent;
import eu.wisebed.wiseui.client.testbedselection.event.WisemlLoadedEvent.WisemlLoadedHandler;
import eu.wisebed.wiseui.client.testbedselection.view.MapView;
import eu.wisebed.wiseui.client.util.Coordinates;
import eu.wisebed.wiseui.client.util.GrahamScan;
import eu.wisebed.wiseui.shared.TestbedConfiguration;
import eu.wisebed.wiseui.shared.wiseml.Coordinate;
import eu.wisebed.wiseui.shared.wiseml.Node;
import eu.wisebed.wiseui.shared.wiseml.Setup;

/**
 * The presenter for the {@link MapView}.
 * 
 * @author Malte Legenhausen
 */
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
        final Coordinate cartesian = Coordinates.blh2xyz(origin);
        final List<Coordinate> coordinates = Lists.transform(setup.getNode(), new Function<Node, Coordinate>() {
			@Override
			public Coordinate apply(final Node node) {
				final Coordinate rotated = Coordinates.rotate(node.getPosition(), origin.getPhi());
				final Coordinate absolute = Coordinates.absolute(cartesian, rotated);
				return Coordinates.xyz2blh(absolute);
			}
		});
        view.setTestbedShape(GrahamScan.calculate(coordinates));
        view.getLoadingIndicator().hideLoading();
    }

    @Override
    public void onTestbedConfigurationSelected(final ConfigurationSelectedEvent event) {
    	view.getLoadingIndicator().showLoading("Loading Testbed");
        this.configuration = event.getConfiguration();
        view.setTestbedCoordinate(null, null, null);
        view.setTestbedShape(null);
    }
}
