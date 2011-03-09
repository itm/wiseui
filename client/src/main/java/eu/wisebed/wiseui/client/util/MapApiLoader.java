package eu.wisebed.wiseui.client.util;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.maps.client.Maps;


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
	
	private static final MapConstants CONSTANTS = GWT.create(MapConstants.class);
	
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
			Maps.loadMapsApi(CONSTANTS.key(), CONSTANTS.version(), CONSTANTS.useSensors(), this);
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
