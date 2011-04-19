package eu.wisebed.wiseui.client.testbedselection.presenter;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.inject.Inject;

import eu.wisebed.wiseui.client.testbedlist.event.TestbedSelectedEvent;
import eu.wisebed.wiseui.client.testbedlist.event.TestbedSelectedEvent.ConfigurationSelectedHandler;
import eu.wisebed.wiseui.client.testbedselection.event.ThrowableEvent;
import eu.wisebed.wiseui.client.testbedselection.event.ThrowableEvent.ThrowableHandler;
import eu.wisebed.wiseui.client.testbedselection.event.WisemlLoadedEvent;
import eu.wisebed.wiseui.client.testbedselection.event.WisemlLoadedEvent.WisemlLoadedHandler;
import eu.wisebed.wiseui.client.testbedselection.view.MapView;
import eu.wisebed.wiseui.client.util.Coordinates;
import eu.wisebed.wiseui.client.util.EventBusManager;
import eu.wisebed.wiseui.client.util.GrahamScan;
import eu.wisebed.wiseui.shared.dto.Coordinate;
import eu.wisebed.wiseui.shared.dto.Node;
import eu.wisebed.wiseui.shared.dto.Setup;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;

/**
 * The presenter for the {@link MapView}.
 * 
 * @author Malte Legenhausen
 */
public class MapPresenter implements MapView.Presenter, WisemlLoadedHandler, ConfigurationSelectedHandler, ThrowableHandler, PlaceChangeEvent.Handler {

    private final EventBusManager eventBus;
    private final MapView view;
    private TestbedConfiguration configuration;

    @Inject
    public MapPresenter(final EventBus eventBus, final MapView view) {
        this.eventBus = new EventBusManager(eventBus);
        this.view = view;
        bind();
    }

    public void bind() {
    	eventBus.addHandler(PlaceChangeEvent.TYPE, this);
        eventBus.addHandler(TestbedSelectedEvent.TYPE, this);
        eventBus.addHandler(WisemlLoadedEvent.TYPE, this);
        eventBus.addHandler(ThrowableEvent.TYPE, this);
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
    public void onTestbedSelected(final TestbedSelectedEvent event) {
    	view.getLoadingIndicator().showLoading("Loading Testbed");
        this.configuration = event.getConfiguration();
        view.setTestbedCoordinate(null, null, null);
        view.setTestbedShape(null);
    }

	@Override
	public void onThrowable(ThrowableEvent event) {
		view.getLoadingIndicator().hideLoading();
	}

	@Override
	public void onPlaceChange(final PlaceChangeEvent event) {
		view.getLoadingIndicator().hideLoading();
		eventBus.removeAll();
	}
}
