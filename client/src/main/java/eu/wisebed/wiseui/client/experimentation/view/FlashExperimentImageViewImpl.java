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
package eu.wisebed.wiseui.client.experimentation.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

import eu.wisebed.wiseui.widgets.HasWidgetsDialogBox;

public class FlashExperimentImageViewImpl extends HasWidgetsDialogBox implements FlashExperimentImageView{
	
	private static FlashExperimentImageViewImplBinder uiBinder = GWT.create(FlashExperimentImageViewImplBinder.class);
	
	@UiTemplate("FlashExperimentImageViewImpl.ui.xml")
	interface FlashExperimentImageViewImplBinder extends UiBinder<Widget, FlashExperimentImageViewImpl> {
	}

	private Presenter presenter;
	
    @UiField
    Button flashButton;
    
    @UiField
    Button cancelButton;
    
//   @UiField
//   CaptionPanel uploadedImagesContainer;
//    
//   @UiField
//   CaptionPanel imageUploadContainer;
//    
//   @UiField
//   ImageUploadWidget imageUploadWidget;
        
	public FlashExperimentImageViewImpl(){
        uiBinder.createAndBindUi(this);
        
        setModal(true);
        setGlassEnabled(true);
        setAnimationEnabled(true);
	}
	
    @UiFactory
    protected FlashExperimentImageViewImpl createDialog() {
        return this;
    }
		
	@UiHandler("flashButton")
	public void handleFlashButtonClick(final ClickEvent event) {
		presenter.submit();
	}
	
	@UiHandler("cancelButton")
	public void handleCancelButtonClick(final ClickEvent event) {
		presenter.cancel();
	}
	
    @Override
    public void show(final String title) {
        setText(title);
        center();
        show();
    }

	@Override
	public void setPresenter(final Presenter presenter) {
		this.presenter = presenter;		
	}
}
