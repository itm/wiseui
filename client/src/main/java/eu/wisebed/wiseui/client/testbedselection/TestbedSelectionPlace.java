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
package eu.wisebed.wiseui.client.testbedselection;

import eu.wisebed.wiseui.client.testbedselection.common.TestbedSelectionParams;
import eu.wisebed.wiseui.client.util.KeyValuePlace;
import eu.wisebed.wiseui.client.util.Objects2;

/**
 * This place represents the current state of the testbed-selection-view as reflected by the URL.
 * The state consists of the two parameters: testbed selection, view.
 * Testbed selection is the number of the currently selected testbed.
 * View is the currently displayed view of the selected testbed.
 * The view can be "map" (default), "detail" or "raw".
 *
 * @author Malte Legenhausen, Sönke Nommensen
 */
public class TestbedSelectionPlace extends KeyValuePlace {

    public TestbedSelectionPlace() {
    	this(TestbedSelectionParams.MAP_VIEW.getValue());
    }

    public TestbedSelectionPlace(final String view) {
        set(TestbedSelectionParams.TESTBED_VIEW_STRING.getValue(), view);
    }
    
    public String getView() {
		return Objects2.nullOrToString(get(TestbedSelectionParams.TESTBED_VIEW_STRING.getValue()));
	}
}
