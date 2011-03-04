package eu.wisebed.wiseui.client.testbedselection.presenter;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import eu.wisebed.wiseui.client.testbedselection.TestbedSelectionPlace;
import eu.wisebed.wiseui.client.testbedselection.event.ConfigurationSelectedEvent;
import eu.wisebed.wiseui.client.testbedselection.event.ConfigurationSelectedEvent.ConfigurationSelectedHandler;
import eu.wisebed.wiseui.client.testbedselection.event.ThrowableEvent;
import eu.wisebed.wiseui.client.testbedselection.event.ThrowableEvent.ThrowableHandler;
import eu.wisebed.wiseui.client.testbedselection.event.WisemlLoadedEvent;
import eu.wisebed.wiseui.client.testbedselection.event.WisemlLoadedEvent.WisemlLoadedHandler;
import eu.wisebed.wiseui.client.testbedselection.view.DetailView;
import eu.wisebed.wiseui.client.testbedselection.view.DetailView.Presenter;
import eu.wisebed.wiseui.shared.TestbedConfiguration;
import eu.wisebed.wiseui.shared.exception.WisemlException;
import eu.wisebed.wiseui.shared.wiseml.Setup;

public class DetailPresenter implements Presenter, ConfigurationSelectedHandler, WisemlLoadedHandler, ThrowableHandler {

    private final DetailView view;

    private final EventBus eventBus;

    private TestbedConfiguration configuration;

    @Inject
    public DetailPresenter(final EventBus eventBus, final DetailView view) {
        this.view = view;
        this.eventBus = eventBus;
        bind();
    }

    private void bind() {
        eventBus.addHandler(ConfigurationSelectedEvent.TYPE, this);
        eventBus.addHandler(WisemlLoadedEvent.TYPE, this);
        eventBus.addHandler(ThrowableEvent.TYPE, this);
    }

    public void setPlace(final TestbedSelectionPlace place) {

    }

    public void onTestbedConfigurationSelected(final ConfigurationSelectedEvent event) {
        configuration = event.getConfiguration();
        view.getDescription().setText("Loading details...");
        view.setDescriptionCoordinate(null);
    }

    public void onWisemlLoaded(final WisemlLoadedEvent event) {
        final Setup setup = event.getWiseml().getSetup();
        if (null == setup) return;
        String description = setup.getDescription();
        if (description == null || description.isEmpty()) {
            description = "No description found for this testbed.";
        }
        view.getDescription().setText(description);
        view.setDescriptionCoordinate(setup.getOrigin());
    }

    public void onThrowable(ThrowableEvent event) {
        if (event.getThrowable() instanceof WisemlException) {
            final String message = "Unable to load WiseML information from " + configuration.getName() + ".";
            view.getDescription().setText(message);
            view.setDescriptionCoordinate(null);
        }
    }
}
