package eu.wisebed.wiseui.client;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import eu.wisebed.wiseui.client.activity.ContentActivityManager;
import eu.wisebed.wiseui.client.activity.ContentActivityMapper;
import eu.wisebed.wiseui.client.activity.NavigationActivityManager;
import eu.wisebed.wiseui.client.activity.NavigationActivityMapper;
import eu.wisebed.wiseui.client.activity.WiseUiPlaceHistoryMapper;
import eu.wisebed.wiseui.client.main.view.WiseUiView;
import eu.wisebed.wiseui.client.main.view.WiseUiViewImpl;
import eu.wisebed.wiseui.client.testbedselection.TestbedSelectionPlace;
import eu.wisebed.wiseui.client.util.AuthenticationManager;

public class WiseUiModule extends AbstractGinModule {

    @Override
    protected void configure() {
        bind(ContentActivityMapper.class);
        bind(ContentActivityManager.class).in(Singleton.class);
        bind(NavigationActivityMapper.class);
        bind(NavigationActivityManager.class).in(Singleton.class);

        // View binding
        bind(WiseUiView.class).to(WiseUiViewImpl.class).in(Singleton.class);

        bind(AuthenticationManager.class);
    }

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
        historyHandler.register(placeController, eventBus, new TestbedSelectionPlace());
        return historyHandler;
    }

    @Singleton
    @Provides
    PlaceController providePlaceController(final EventBus eventBus) {
        return new PlaceController(eventBus);
    }
}
