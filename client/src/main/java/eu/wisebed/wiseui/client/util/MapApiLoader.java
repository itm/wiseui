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
