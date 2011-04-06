package eu.wisebed.wiseui.client;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import eu.wisebed.wiseui.client.activity.WiseUiPlaceHistoryMapper;
import eu.wisebed.wiseui.client.main.WiseUiPlace;
import eu.wisebed.wiseui.client.testbedselection.TestbedSelectionPlace;


/**
 * Configuration module for the WiseUiGinjector.
 * 
 * @author Malte Legenhausen
 */
public class WiseUiModule extends AbstractGinModule {

    @Override
    protected void configure() {}

    @Singleton
    @Provides
    EventBus provideEventBus() {
        return new SimpleEventBus();
    }

    @Singleton
    @Provides
    PlaceHistoryHandler providePlaceHistoryHandler(
            final WiseUiPlaceHistoryMapper mapper,
            final PlaceController placeController, final EventBus eventBus) {
        final PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(
                mapper);
        historyHandler.register(placeController, eventBus, new WiseUiPlace());
        return historyHandler;
    }

    @Singleton
    @Provides
    PlaceController providePlaceController(final EventBus eventBus) {
        return new PlaceController(eventBus);
    }
}
