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
package eu.wisebed.wiseui.client.navigation;

import eu.wisebed.wiseui.client.util.KeyValuePlace;

public class NavigationPlace extends KeyValuePlace {

	private static final String INDEX_STRING = "index";
	
	private static final Integer DEFAULT_INDEX = 0;
	
	public NavigationPlace() {
		set(INDEX_STRING, DEFAULT_INDEX.toString());
	}
	
	public NavigationPlace(final Integer index) {
		set(INDEX_STRING, index.toString());
	}
	
	public Integer getIndex() {
		return Integer.valueOf(get(INDEX_STRING));
	}
}
