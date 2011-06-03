/**
 * Copyright (C) 2011 Universität zu Lübeck, Institut für Telematik (ITM), Research Academic Computer
 *                             Technology Institute (RACTI)
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
package eu.wisebed.wiseui.client.util;

import com.google.gwt.maps.client.Maps;

import java.util.ArrayList;
import java.util.List;


/**
 * Use this class when loading the map api. It prevents the multiple loading of the api at the same time
 * which can leads to errors.
 * The map api properties lik api key, version and sensor usage is loaded from the resource bundle MapConstants.
 *
 * @author Malte Legenhausen
 */
public class MapApiLoader implements Runnable {

    private enum State {
        NOT_LOADED, LOADING, LOADED
    }

    private static MapApiLoader INSTANCE;

    private final List<Runnable> runnables = new ArrayList<Runnable>();

    private State state = State.NOT_LOADED;

    private MapApiLoader() {

    }

    public static MapApiLoader get() {
        if (INSTANCE == null) {
            INSTANCE = new MapApiLoader();
        }
        return INSTANCE;
    }

    public void loadMapApi(final Runnable runnable) {
        if (State.NOT_LOADED.equals(state)) {
            state = State.LOADING;
            runnables.add(runnable);
            Maps.loadMapsApi(MapConstants.INSTANCE.key(),
                    MapConstants.INSTANCE.version(),
                    MapConstants.INSTANCE.useSensors(),
                    this);
        } else if (State.LOADING.equals(state)) {
            runnables.add(runnable);
        } else {
            runnable.run();
        }
    }

    @Override
    public void run() {
        state = State.LOADED;
        for (final Runnable runnable : runnables) {
            runnable.run();
        }
    }
}
