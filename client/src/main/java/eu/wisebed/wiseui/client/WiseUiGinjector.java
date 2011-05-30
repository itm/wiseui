/*
 * Copyright 2011 Universität zu Lübeck, Institut für Telematik (ITM),
 *              Research Academic Computer Technology Institute (RACTI)
 *
 * ITM and RACTI license this file under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.wisebed.wiseui.client;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;

import eu.wisebed.wiseui.client.activity.ContentActivityManager;
import eu.wisebed.wiseui.client.activity.NavigationActivityManager;
import eu.wisebed.wiseui.client.activity.TestbedListActivityManager;
import eu.wisebed.wiseui.client.experimentation.gin.ExperimentationGinjector;
import eu.wisebed.wiseui.client.main.view.WiseUiView;
import eu.wisebed.wiseui.client.navigation.gin.NavigationGinjector;
import eu.wisebed.wiseui.client.reservation.gin.ReservationGinjector;
import eu.wisebed.wiseui.client.testbedlist.TestbedListGinjector;
import eu.wisebed.wiseui.client.testbedselection.gin.TestbedSelectionGinjector;
import eu.wisebed.wiseui.client.util.AuthenticationManager;
import eu.wisebed.wiseui.client.util.ExperimentationManager;
import eu.wisebed.wiseui.client.util.ReservationManager;


/**
 * Global WiseUi Ginjector for the WiseUi.
 * 
 * @author Malte Legenhausen
 */
@GinModules({
        WiseUiModule.class
})
public interface WiseUiGinjector extends Ginjector, NavigationGinjector, TestbedListGinjector, TestbedSelectionGinjector, ReservationGinjector, ExperimentationGinjector {

    AuthenticationManager getAuthenticationManager();
    
    ReservationManager getReservationManager();
    
    ExperimentationManager getExperimentManager();

    EventBus getEventBus();

    WiseUiView getAppWidget();

    NavigationActivityManager getNavigationActivityManager();
    
    TestbedListActivityManager getTestbedListActivityManager();

    ContentActivityManager getContentActivityManager();

    PlaceHistoryHandler getPlaceHistoryHandler();

    PlaceController getPlaceController();
}
