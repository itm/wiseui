/**
 * Copyright (C) 2011 Universität zu Lübeck, Institut für Telematik (ITM),
 *                             Research Academic Computer Technology Institute (RACTI)
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

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import eu.wisebed.wiseui.client.activity.WiseUiPlaceHistoryMapper;
import eu.wisebed.wiseui.client.main.WiseUiPlace;


/**
 * Configuration module for the WiseUiGinjector.
 *
 * @author Malte Legenhausen
 */
public class WiseUiModule extends AbstractGinModule {

    @Override
    protected void configure() {
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
        historyHandler.register(placeController, eventBus, new WiseUiPlace());
        return historyHandler;
    }

    @Singleton
    @Provides
    PlaceController providePlaceController(final EventBus eventBus) {
        return new PlaceController(eventBus);
    }
}
