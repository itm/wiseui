/**
 * Copyright (C) 2011 Universität zu Lübeck, Institut für Telematik (ITM), Research Academic Computer Technology Institute (RACTI)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.wisebed.wiseui.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
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

        injector.getNavigationActivityManager().setDisplay(appWidget.getNavigationPanel());
        injector.getTestbedListActivityManager().setDisplay(appWidget.getTestbedListPanel());

        // Start ActivityManager for the main widget with our ActivityMapper
        injector.getContentActivityManager().setDisplay(appWidget.getContentPanel());

        RootPanel.get().add(appWidget.asWidget());

        // Goes to place represented on URL or default place
        injector.getPlaceHistoryHandler().handleCurrentHistory();

        // Init authentication & reservation manager.
        injector.getAuthenticationManager().init();
        injector.getReservationManager().init();
        injector.getExperimentationManager().init();

        hideLoadingIndicator();
    }

    /**
     * Hides the loading indicator that is shown before the app starts.
     */
    private void hideLoadingIndicator() {
        final ScheduledCommand command = new ScheduledCommand() {
            public void execute() {
                DOM.getElementById("loading").removeFromParent();
            }
        };
        Scheduler.get().scheduleDeferred(command);
    }
}
