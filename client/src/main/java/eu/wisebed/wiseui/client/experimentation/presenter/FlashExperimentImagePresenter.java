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

import java.util.List;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import eu.wisebed.wiseui.api.ExperimentationServiceAsync;
import eu.wisebed.wiseui.client.experimentation.event.FlashBinaryImageEvent;
import eu.wisebed.wiseui.client.experimentation.event.SuccessfulImageUploadEvent;
import eu.wisebed.wiseui.client.experimentation.event.FailedImageUploadEvent;
import eu.wisebed.wiseui.client.experimentation.view.FlashExperimentImageView;
import eu.wisebed.wiseui.client.util.EventBusManager;
import eu.wisebed.wiseui.shared.dto.BinaryImage;
import eu.wisebed.wiseui.widgets.messagebox.MessageBox;

public class FlashExperimentImagePresenter implements 
FlashExperimentImageView.Presenter,
SuccessfulImageUploadEvent.Handler,FailedImageUploadEvent.Handler{
	
	private BinaryImage selected;
	private FlashExperimentImageView view;
	private ImageUploadWidgetPresenter imageUpload;
	private ExperimentationServiceAsync service;
	private EventBusManager eventBus;

	
	@Inject
	public FlashExperimentImagePresenter(
			final ExperimentationServiceAsync service,
			final FlashExperimentImageView view,
			final EventBus eventBus,
			final ImageUploadWidgetPresenter imageUpload) {
		this.service = service;
		this.view = view;
		this.view.setPresenter(this);
		this.imageUpload = imageUpload;
		this.view.getImageUploadWidget().setPresenter(imageUpload);
		this.eventBus = new EventBusManager(eventBus);
		bind();
	}
	
	public void bind() {
		eventBus.addHandler(SuccessfulImageUploadEvent.TYPE, this);
		eventBus.addHandler(FailedImageUploadEvent.TYPE, this);
	}
	
	@Override
	public void onSuccesfullImageUploadEvent(SuccessfulImageUploadEvent event) {
		if(event.getSource() == imageUpload) {
			getAvailableImages();
		}
	}
	
	@Override
	public void onFailedUploadImageEvent(FailedImageUploadEvent event) {
		if(event.getSource() == imageUpload) {
			MessageBox.error("Flash Experiment image", "Uploading image failed", null, null);
		}
	}
	
	public FlashExperimentImageView getView() {
		return view;
	}
	
	@Override
	public void submit() {
		final FlashExperimentImagePresenter source = this;
		eventBus.fireEventFromSource(new FlashBinaryImageEvent(selected), source);
	}
	
	@Override
	public void setSelectedImage(final BinaryImage selected) {
		this.selected = selected;
	}
	
	@Override
	public void getAvailableImages() {

		// setup callback
		AsyncCallback<List<BinaryImage>> callback = new AsyncCallback<List<BinaryImage>>() {

			@Override
			public void onFailure(Throwable caught) {
				MessageBox.error("Experimentation Service", caught.getMessage(), caught, null);
			}

			@Override
			public void onSuccess(List<BinaryImage> results) {
				view.setAvailableImages(results);
			}

		};

		// make the rpc
		service.returnUploadedExperimentImages(callback);
	}
}
