/**
 * Copyright (C) 2011 Universität zu Lübeck, Institut für Telematik (ITM),
 *                             Research Academic Computer Technology Institute (RACTI)
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
package eu.wisebed.wiseui.client.experimentation.presenter;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;

import eu.wisebed.wiseui.client.experimentation.event.FailedImageUploadEvent;
import eu.wisebed.wiseui.client.experimentation.event.SuccessfulImageUploadEvent;
//import eu.wisebed.wiseui.client.experimentation.event.FailedImageUploadEvent;
import eu.wisebed.wiseui.client.experimentation.view.ImageUploadWidget;
import eu.wisebed.wiseui.client.util.EventBusManager;

public class ImageUploadWidgetPresenter implements ImageUploadWidget.Presenter{

	private ImageUploadWidget widget;
	private EventBusManager eventBus;
	
	@Inject
	public ImageUploadWidgetPresenter(
			final ImageUploadWidget widget, final EventBus eventBus) {
		this.widget = widget;
		this.widget.setPresenter(this);
		this.eventBus = new EventBusManager(eventBus);
		bind();
	}
	
	public void bind() {
		
	}
	
	@Override
	public void fireSuccessfullImageUploadEvent() {
		final ImageUploadWidgetPresenter source = this;
		eventBus.fireEventFromSource(new SuccessfulImageUploadEvent(), source);
	}
	

	@Override
	public void fireFailedImageUploadEvent() {
		final ImageUploadWidgetPresenter source = this;
		eventBus.fireEventFromSource(new FailedImageUploadEvent(), source);		
	}
}
