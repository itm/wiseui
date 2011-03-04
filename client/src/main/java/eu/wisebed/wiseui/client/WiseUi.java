package eu.wisebed.wiseui.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;
import eu.wisebed.wiseui.client.main.view.WiseUiView;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 *
 * @author Malte Legenhausen
 */
public class WiseUi implements EntryPoint {

    private final WiseUiGinjector injector = GWT.create(WiseUiGinjector.class);

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        final WiseUiView appWidget = injector.getAppWidget();

        injector.getNavigationActivityManager().setDisplay(
                appWidget.getNavigationPanel());

        // Start ActivityManager for the main widget with our ActivityMapper
        injector.getContentActivityManager().setDisplay(
                appWidget.getContentPanel());

        RootPanel.get().add(appWidget.asWidget());

        // Goes to place represented on URL or default place
        injector.getPlaceHistoryHandler().handleCurrentHistory();

        // Init session.
        injector.getAuthenticationManager().init();

        hideLoadingIndicator();
    }

    /**
     * Hides the loading indicator that is shown before the app starts.
     */
    private void hideLoadingIndicator() {
        final ScheduledCommand command = new ScheduledCommand() {
            public void execute() {
                DOM.getElementById("loading").getStyle().setVisibility(Visibility.HIDDEN);
            }
        };
        Scheduler.get().scheduleDeferred(command);
    }
}
