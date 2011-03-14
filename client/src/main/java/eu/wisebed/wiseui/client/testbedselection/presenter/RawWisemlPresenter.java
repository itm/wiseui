package eu.wisebed.wiseui.client.testbedselection.presenter;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import eu.wisebed.wiseui.api.SessionManagementServiceAsync;
import eu.wisebed.wiseui.client.testbedselection.event.ConfigurationSelectedEvent;
import eu.wisebed.wiseui.client.testbedselection.event.ConfigurationSelectedEvent.ConfigurationSelectedHandler;
import eu.wisebed.wiseui.client.testbedselection.view.RawWisemlView;
import eu.wisebed.wiseui.client.testbedselection.view.RawWisemlView.Presenter;
import eu.wisebed.wiseui.shared.TestbedConfiguration;

public class RawWisemlPresenter implements Presenter, ConfigurationSelectedHandler {

    private final EventBus eventBus;

    private final RawWisemlView view;

    private final SessionManagementServiceAsync sessionManagementService;

    private TestbedConfiguration configuration;

    @Inject
    public RawWisemlPresenter(final EventBus eventBus,
                              final RawWisemlView view,
                              final SessionManagementServiceAsync sessionManagementService) {
        this.eventBus = eventBus;
        this.view = view;
        this.sessionManagementService = sessionManagementService;
        bind();
        view.getHighlighter().setText("Select a Testbed Configuration.");
    }

    private void bind() {
        eventBus.addHandler(ConfigurationSelectedEvent.TYPE, this);
    }

    @Override
    public void onTestbedConfigurationSelected(final ConfigurationSelectedEvent event) {
        configuration = event.getConfiguration();
        view.getHighlighter().setText("Loading WiseML...");
        sessionManagementService.getWisemlAsXml(configuration.getSessionmanagementEndointUrl(), new AsyncCallback<String>() {

            @Override
            public void onSuccess(final String result) {
                //view.getHighlighter().setHTML("<pre class=\"brush: xml\">" + SafeHtmlUtils.htmlEscape(result) + "</pre>");
                view.getHighlighter().setText(SafeHtmlUtils.htmlEscape(result));
            }

            @Override
            public void onFailure(final Throwable caught) {
                view.getHighlighter().setText("Unable to load WiseML.");
            }
        });
    }
}