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
package eu.wisebed.wiseui.client.main;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

import eu.wisebed.wiseui.client.experimentation.ExperimentationPlace;
import eu.wisebed.wiseui.client.navigation.NavigationPlace;
import eu.wisebed.wiseui.client.reservation.ReservationPlace;
import eu.wisebed.wiseui.client.testbedlist.TestbedListPlace;
import eu.wisebed.wiseui.client.testbedselection.TestbedSelectionPlace;
import eu.wisebed.wiseui.client.util.KeyValuePlace;

public class WiseUiPlace extends Place {
	
	private interface Factory<T extends KeyValuePlace> {
		
		public T create(String token);
	}
	
	private static final Map<String, Factory<?>> mapping = new TreeMap<String, Factory<?>>();
	
	static {
		mapping.put(shortClassName(TestbedSelectionPlace.class), new Factory<TestbedSelectionPlace>() {
			@Override
			public TestbedSelectionPlace create(final String token) {
				return KeyValuePlace.parse(new TestbedSelectionPlace(), token);
			}
		});
		mapping.put(shortClassName(ExperimentationPlace.class), new Factory<ExperimentationPlace>() {
			@Override
			public ExperimentationPlace create(final String token) {
				return KeyValuePlace.parse(new ExperimentationPlace(), token);
			}
		});
		mapping.put(shortClassName(ReservationPlace.class), new Factory<ReservationPlace>() {
			@Override
			public ReservationPlace create(final String token) {
				return KeyValuePlace.parse(new ReservationPlace(), token);
			}
		});
		mapping.put(shortClassName(NavigationPlace.class), new Factory<NavigationPlace>() {
			@Override
			public NavigationPlace create(String token) {
				return KeyValuePlace.parse(new NavigationPlace(), token);
			}
		});
		mapping.put(shortClassName(TestbedListPlace.class), new Factory<TestbedListPlace>() {
			@Override
			public TestbedListPlace create(String token) {
				return KeyValuePlace.parse(new TestbedListPlace(), token);
			}
		});
	}
	
	public static final String DEFAULT_JOINER = ";";
	
	public static final String DEFAULT_SEPARATOR = ":";
	
	private static final int TUPLE_LENGTH = 2;
	
	private final String joiner;
	
	private final String separator;

    private Map<String, KeyValuePlace> places = new TreeMap<String, KeyValuePlace>();
    
    public WiseUiPlace() {
		this(DEFAULT_JOINER, DEFAULT_SEPARATOR);
	}
    
    public WiseUiPlace(final String joiner, final String separator) {
    	this.joiner = joiner;
    	this.separator = separator;
    }

    private WiseUiPlace(final Map<String, KeyValuePlace> places) {
    	this(DEFAULT_JOINER, DEFAULT_SEPARATOR);
        this.places = places;
    }
    
    private WiseUiPlace(String token) {
    	this(DEFAULT_JOINER, DEFAULT_SEPARATOR);
    	parse(token);
    }
    
    private static String shortClassName(Class<?> clazz) {
    	return Iterables.getLast(Splitter.on(".").split(clazz.getName()));
    }

    public Collection<KeyValuePlace> getPlaces() {
        return places.values();
    }

    public KeyValuePlace get(final Class<? extends Place> clazz) {
    	final String name = shortClassName(clazz);
    	KeyValuePlace place = places.get(name);
    	if (place == null) {
    		place = mapping.get(name).create(null);
    	}
    	return place;
    }

    public WiseUiPlace update(final KeyValuePlace place) {
        places.put(shortClassName(place.getClass()), place);
        return new WiseUiPlace(places);
    }
    
    public String toString() {
    	return Joiner.on(joiner).withKeyValueSeparator(separator).join(places);
    }
    
    public void parse(final String token) {
    	final Iterable<String> tokens = Splitter.on(joiner).split(token);
    	for (final String place : tokens) {
    		parsePlace(place);
    	}
    }
    
    private void parsePlace(String token) {
    	final String[] tuple = token.split(separator, TUPLE_LENGTH);
    	if (tuple.length == TUPLE_LENGTH) {
    		final String name = tuple[0];
    		if (mapping.containsKey(name)) {
    			final KeyValuePlace place = mapping.get(tuple[0]).create(tuple[1]);
        		places.put(name, place);
    		}
    	}
    }
    
    public static class Tokenizer implements PlaceTokenizer<WiseUiPlace> {
    	@Override
    	public WiseUiPlace getPlace(String token) {
    		return new WiseUiPlace(token);
    	}

    	@Override
    	public String getToken(WiseUiPlace place) {
    		return place.toString();
    	}
    }
}
