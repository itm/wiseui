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
package eu.wisebed.wiseui.client.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Constants;


/**
 * Constants that are used for loading the map api from google.
 * 
 * @author Malte Legenhausen
 */
public interface MapConstants extends Constants {
	
    public static final MapConstants INSTANCE = GWT.create(MapConstants.class);

	@DefaultStringValue("ABQIAAAAJF12r4xVlog3DZkEwDC09BRisPSeHzj7Yhj17FYCkK1ytSRbxBQV16SxQgD_zuTEDGaTRK9sHFtMDQ")
	String key();
	
	@DefaultStringValue("2")
	String version();
	
	@DefaultBooleanValue(false)
	boolean useSensors();
}
