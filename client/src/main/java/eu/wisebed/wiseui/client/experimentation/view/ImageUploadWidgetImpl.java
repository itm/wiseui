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
package eu.wisebed.wiseui.client.experimentation.view;

import gwtupload.client.IUploader;
import gwtupload.client.MultiUploader;
import gwtupload.client.IUploadStatus.Status;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ImageUploadWidgetImpl extends Composite implements ImageUploadWidget{

	@UiTemplate("ImageUploadWidgetImpl.ui.xml")
	interface ImageUploadImplUiBinder extends UiBinder<Widget,
		ImageUploadWidgetImpl>{}

	@UiField MultiUploader imagePicker = new MultiUploader();
	
	private static ImageUploadImplUiBinder uiBinder = 
		GWT.create(ImageUploadImplUiBinder.class);
	
	private Presenter presenter;

	public ImageUploadWidgetImpl(){
		initWidget(uiBinder.createAndBindUi(this));
        imagePicker.addOnFinishUploadHandler(onFinishUploaderHandler);
	}

	/**
	 * Catch response from server after finishing image upload. The server can 
	 * send information to the client about the image sent from client. Parse 
	 * this information using XML or JSON libraries. Also keeps image
	 * information for the filename and the filename's field in variables so as
	 * later to be transfered within ReservationDetails object.
	 */
	private IUploader.OnFinishUploaderHandler onFinishUploaderHandler = 
		new IUploader.OnFinishUploaderHandler() {
		public void onFinish(IUploader uploader) {
			if(uploader.getStatus() == Status.SUCCESS) {
				presenter.fireSuccessfullImageUploadEvent();
			}
		}
	};

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	public Presenter getPresenter() {
		return presenter;
	}
}
