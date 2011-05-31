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
package eu.wisebed.wiseui.client.testbedlist.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;

public class EditTestbedEvent extends GwtEvent<EditTestbedEvent.Handler> {

	public interface Handler extends EventHandler {
		
		void onEditTestbed(EditTestbedEvent event);
	}
	
	public static final Type<Handler> TYPE = new Type<Handler>();

	private final TestbedConfiguration configuration;
	
	public EditTestbedEvent() {
		configuration = null;
	}
	
	public EditTestbedEvent(final TestbedConfiguration configuration) {
		this.configuration = configuration;
	}
	
	public TestbedConfiguration getConfiguration() {
		return configuration;
	}
	
	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(final Handler handler) {
		handler.onEditTestbed(this);
	}
}
