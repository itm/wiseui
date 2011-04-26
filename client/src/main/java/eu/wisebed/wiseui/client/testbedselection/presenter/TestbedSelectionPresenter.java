package eu.wisebed.wiseui.client.testbedselection.presenter;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;

import eu.wisebed.wiseui.client.main.WiseUiPlace;
import eu.wisebed.wiseui.client.testbedlist.event.TestbedSelectedEvent;
import eu.wisebed.wiseui.client.testbedlist.event.TestbedSelectedEvent.ConfigurationSelectedHandler;
import eu.wisebed.wiseui.client.testbedselection.TestbedSelectionPlace;
import eu.wisebed.wiseui.client.testbedselection.event.ThrowableEvent;
import eu.wisebed.wiseui.client.testbedselection.event.ThrowableEvent.ThrowableHandler;
import eu.wisebed.wiseui.client.testbedselection.event.WisemlLoadedEvent;
import eu.wisebed.wiseui.client.testbedselection.event.WisemlLoadedEvent.WisemlLoadedHandler;
import eu.wisebed.wiseui.client.testbedselection.view.TestbedSelectionView;
import eu.wisebed.wiseui.client.testbedselection.view.TestbedSelectionView.Presenter;
import eu.wisebed.wiseui.client.util.EventBusManager;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;
import eu.wisebed.wiseui.shared.exception.WisemlException;
import eu.wisebed.wiseui.widgets.messagebox.MessageBox;

/**
 * The presenter for the {@link TestbedSelectionView}.
 *
 * @author Malte Legenhausen
 */
public class TestbedSelectionPresenter implements Presenter, ConfigurationSelectedHandler,
        WisemlLoadedHandler, ThrowableHandler, PlaceChangeEvent.Handler {

    private final EventBusManager eventBus;
    private final TestbedSelectionView view;
    private PlaceController placeController;
    private WiseUiPlace place;
    private TestbedConfiguration configuration;

    @Inject
    public TestbedSelectionPresenter(final EventBus eventBus,
                                     final PlaceController placeController,
                                     final TestbedSelectionView view) {
        this.eventBus = new EventBusManager(eventBus);
        this.placeController = placeController;
        this.view = view;
        view.getReloadEnabled().setEnabled(false);
        bind();
    }
    
	public void bind() {
		eventBus.addHandler(PlaceChangeEvent.TYPE, this);
		eventBus.addHandler(WisemlLoadedEvent.TYPE, this);
        eventBus.addHandler(TestbedSelectedEvent.TYPE, this);
        eventBus.addHandler(ThrowableEvent.TYPE, this);
	}

    @Override
    public void reload() {
        view.getReloadEnabled().setEnabled(false);
        eventBus.fireEvent(new TestbedSelectedEvent(configuration));
    }

    public void setPlace(final WiseUiPlace place) {
    	this.place = place;
    	final TestbedSelectionPlace testbedSelectionPlace =
                (TestbedSelectionPlace) place.get(TestbedSelectionPlace.class);
    	view.setContentSelection(testbedSelectionPlace.getView());
    }

    @Override
    public void onWisemlLoaded(final WisemlLoadedEvent event) {
        view.getReloadEnabled().setEnabled(true);
    }

    @Override
    public void onTestbedSelected(final TestbedSelectedEvent event) {
        configuration = event.getConfiguration();
    }

    @Override
    public void onThrowable(final ThrowableEvent event) {
        if (event.getThrowable() instanceof WisemlException) {
            final String title = "Unavailable Testbed " + configuration.getName();
            final String message = "The Testbed "
                    + configuration.getName()
                    + " is not available.\n"
                    + event.getThrowable().getMessage();
            MessageBox.error(title, message, event.getThrowable(), null);
        }
    }

    @Override
    public void setContentSelection(final String view) {
    	placeController.goTo(place.update(new TestbedSelectionPlace(view)));
    }

	@Override
	public void onPlaceChange(PlaceChangeEvent event) {
		eventBus.removeAll();
	}
}
