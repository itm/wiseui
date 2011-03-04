package eu.wisebed.wiseui.client.testbedselection.presenter;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import eu.wisebed.wiseui.client.testbedselection.TestbedSelectionPlace;
import eu.wisebed.wiseui.client.testbedselection.event.ConfigurationSelectedEvent;
import eu.wisebed.wiseui.client.testbedselection.event.ConfigurationSelectedEvent.ConfigurationSelectedHandler;
import eu.wisebed.wiseui.client.testbedselection.event.WisemlLoadedEvent;
import eu.wisebed.wiseui.client.testbedselection.event.WisemlLoadedEvent.WisemlLoadedHandler;
import eu.wisebed.wiseui.client.testbedselection.view.NetworkView;
import eu.wisebed.wiseui.shared.wiseml.Node;
import eu.wisebed.wiseui.shared.wiseml.Setup;
import eu.wisebed.wiseui.shared.wiseml.Wiseml;

import java.util.ArrayList;

public class NetworkPresenter implements NetworkView.Presenter, WisemlLoadedHandler, ConfigurationSelectedHandler {

    private final NetworkView view;

    private final EventBus eventBus;

    @Inject
    public NetworkPresenter(final EventBus eventBus, final NetworkView view) {
        this.view = view;
        this.eventBus = eventBus;
        bind();
    }

    private void bind() {
        eventBus.addHandler(WisemlLoadedEvent.TYPE, this);
        eventBus.addHandler(ConfigurationSelectedEvent.TYPE, this);
    }

    public void setPlace(final TestbedSelectionPlace place) {

    }

    public void onWisemlLoaded(final WisemlLoadedEvent event) {
        final Wiseml wiseml = event.getWiseml();
        final Setup setup = wiseml.getSetup();
        view.setNodes(setup.getNode());
    }

    public void onTestbedConfigurationSelected(ConfigurationSelectedEvent event) {
        view.setNodes(new ArrayList<Node>(0));
    }
}
