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

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Joiner;
import com.google.common.base.Predicates;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.google.gwt.place.shared.Place;


/**
 * Use this class to store string key values in a Place.
 * 
 * @author Malte Legenhausen
 */
public class KeyValuePlace extends Place {
	
	private static final String DEFAULT_JOINER = "&";
	
	private static final String DEFAULT_SEPARATOR = "=";
	
	private final String joiner;
	
	private final String separator;

	private final Map<String, String> variables = new HashMap<String, String>();

	public KeyValuePlace(final String joiner, final String separator) {
		this.joiner = joiner;
		this.separator = separator;
	}
	
	public KeyValuePlace() {
		this(DEFAULT_JOINER, DEFAULT_SEPARATOR);
	}
	
	public KeyValuePlace(final String token) {
		this(DEFAULT_JOINER, DEFAULT_SEPARATOR);
	}

	public String get(final String key) {
		return variables.get(key);
	}

	public void set(final String key, final String value) {
		variables.put(key, value);
	}
	
    public void parse(final String token) {
    	if (token != null) {
    		final Iterable<String> vars = Splitter.on(joiner).split(token);
        	for (final String variable : vars) {
        		final String[] tokens = variable.split(separator);
        		if (tokens.length == 2) {
        			set(tokens[0], tokens[1]);
        		}
        	}
    	}
    }
    
    public static <T extends KeyValuePlace> T parse(T instance, String token) {
    	instance.parse(token);
    	return instance;
    }
    
    @Override
    public String toString() {
    	final Map<String, String> vars = Maps.filterValues(this.variables, Predicates.notNull());
    	return Joiner.on(joiner).withKeyValueSeparator(separator).join(vars);
    }
}
