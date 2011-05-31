/**
 * Copyright (C) 2011 Universität zu Lübeck, Institut für Telematik (ITM), Research Academic Computer Technology Institute (RACTI)
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
package eu.wisebed.wiseui.client.testbedlist.view;

import java.util.Map;
import java.util.TreeMap;

import eu.maydu.gwt.validation.client.i18n.ValidationMessages;

public class TestbedEditValidationMessages extends ValidationMessages {

	private static final Map<String, String> DESCRIPTIONS = new TreeMap<String, String>();
	
	static {
		DESCRIPTIONS.put("nameDescription", "Please enter at least 3 characters.");
		DESCRIPTIONS.put("testbedUrlDescription", "Please enter at least 3 characters.");
		DESCRIPTIONS.put("snaaEndpointUrlDescription", "Please enter at least 3 characters.");
	}
	
	@Override
	public String getDescriptionMessage(final String msgKey) {
		return DESCRIPTIONS.get(msgKey);
	}
}
