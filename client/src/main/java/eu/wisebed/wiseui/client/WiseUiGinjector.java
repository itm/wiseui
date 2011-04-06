package eu.wisebed.wiseui.client;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;

import eu.wisebed.wiseui.client.activity.ContentActivityManager;
import eu.wisebed.wiseui.client.activity.NavigationActivityManager;
import eu.wisebed.wiseui.client.activity.TestbedListActivityManager;
import eu.wisebed.wiseui.client.administration.gin.AdministrationGinjector;
import eu.wisebed.wiseui.client.experimentation.gin.ExperimentationGinjector;
import eu.wisebed.wiseui.client.main.view.WiseUiView;
import eu.wisebed.wiseui.client.navigation.gin.NavigationGinjector;
import eu.wisebed.wiseui.client.reservation.gin.ReservationGinjector;
import eu.wisebed.wiseui.client.testbedlist.TestbedListGinjector;
import eu.wisebed.wiseui.client.testbedselection.gin.TestbedSelectionGinjector;
import eu.wisebed.wiseui.client.util.AuthenticationManager;


/**
 * Global WiseUi Ginjector for the WiseUi.
 * 
 * @author Malte Legenhausen
 */
@GinModules({
        WiseUiModule.class
})
public interface WiseUiGinjector extends Ginjector, NavigationGinjector, TestbedListGinjector, TestbedSelectionGinjector, ReservationGinjector, ExperimentationGinjector, AdministrationGinjector {

    AuthenticationManager getAuthenticationManager();

    EventBus getEventBus();

    WiseUiView getAppWidget();

    NavigationActivityManager getNavigationActivityManager();
    
    TestbedListActivityManager getTestbedListActivityManager();

    ContentActivityManager getContentActivityManager();

    PlaceHistoryHandler getPlaceHistoryHandler();

    PlaceController getPlaceController();
}
