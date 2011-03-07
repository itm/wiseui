package eu.wisebed.wiseui.client.testbedselection.presenter;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import eu.wisebed.wiseui.client.testbedselection.TestbedSelectionPlace;
import eu.wisebed.wiseui.client.testbedselection.event.ConfigurationSelectedEvent;
import eu.wisebed.wiseui.client.testbedselection.event.ConfigurationSelectedEvent.ConfigurationSelectedHandler;
import eu.wisebed.wiseui.client.testbedselection.event.ShowLoginDialogEvent;
import eu.wisebed.wiseui.client.testbedselection.event.ThrowableEvent;
import eu.wisebed.wiseui.client.testbedselection.event.ThrowableEvent.ThrowableHandler;
import eu.wisebed.wiseui.client.testbedselection.event.WisemlLoadedEvent;
import eu.wisebed.wiseui.client.testbedselection.event.WisemlLoadedEvent.WisemlLoadedHandler;
import eu.wisebed.wiseui.client.testbedselection.view.TestbedSelectionView;
import eu.wisebed.wiseui.client.testbedselection.view.TestbedSelectionView.Presenter;
import eu.wisebed.wiseui.widgets.messagebox.MessageBox;
import eu.wisebed.wiseui.shared.TestbedConfiguration;
import eu.wisebed.wiseui.shared.exception.WisemlException;

public class TestbedSelectionPresenter implements Presenter, ConfigurationSelectedHandler, WisemlLoadedHandler, ThrowableHandler {

    private final EventBus eventBus;
    private final TestbedSelectionView view;
    private TestbedConfiguration configuration;

    @Inject
    public TestbedSelectionPresenter(final EventBus eventBus, final TestbedSelectionView view) {
        this.eventBus = eventBus;
        this.view = view;
        view.setContentSelection(0);
        view.getLoginEnabled().setEnabled(false);
        view.getReloadEnabled().setEnabled(false);
        bind();
    }

    private void bind() {
        eventBus.addHandler(WisemlLoadedEvent.TYPE, this);
        eventBus.addHandler(ConfigurationSelectedEvent.TYPE, this);
        eventBus.addHandler(ThrowableEvent.TYPE, this);
    }

    @Override
    public void reload() {
        view.getReloadEnabled().setEnabled(false);
        eventBus.fireEvent(new ConfigurationSelectedEvent(configuration));
    }

    @Override
    public void showLoginDialog() {
        eventBus.fireEventFromSource(new ShowLoginDialogEvent(), this);
    }

    @Override
    public void setPlace(final TestbedSelectionPlace place) {
    }

    @Override
    public void onWisemlLoaded(final WisemlLoadedEvent event) {
        view.getReloadEnabled().setEnabled(true);
    }

    @Override
    public void onTestbedConfigurationSelected(final ConfigurationSelectedEvent event) {
        configuration = event.getConfiguration();
        view.getLoginEnabled().setEnabled(true);
    }

    @Override
    public void onThrowable(final ThrowableEvent event) {
        if (event.getThrowable() instanceof WisemlException) {
            final String title = "Unavailable Testbed " + configuration.getName();
            final String message =
                    String.format("The Testbed %s is not available.\n%s",
                    configuration.getName(),
                    event.getThrowable().getMessage());
            MessageBox.error(title, message, event.getThrowable(), null);
        }
    }

    @Override
    public void setContentSelection(final Integer index) {
        view.setContentSelection(index);
    }
}
