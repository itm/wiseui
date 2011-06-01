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

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import eu.wisebed.wiseui.shared.common.Checks;
import eu.wisebed.wiseui.shared.dto.BinaryImage;
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
	
	@UiField
	Label noImageUploaded;

	@UiField(provided=true)
	ImageUploadWidget imageUploadWidget;

	@UiField(provided = true)
	CellTable<BinaryImage> imageTable;
	@UiField(provided = true)
	SimplePager pager;

	public FlashExperimentImageViewImpl(){
		setupImageTable();

		imageUploadWidget = new ImageUploadWidgetImpl();
		
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
		flashButton.setEnabled(false);
		center();
		show();
	}
	
	@Override
	public ImageUploadWidget getImageUploadWidget() {
		return imageUploadWidget;
	}

	@Override
	public void setPresenter(final Presenter presenter) {
		this.presenter = presenter;		
	}

	@Override
	public void setAvailableImages(final List<BinaryImage> images){
		try{
			Checks.ifNullOrEmpty(images, "");
		}catch(RuntimeException e) {
			imageTable.setVisible(false);
			pager.setVisible(false);
			noImageUploaded.setVisible(true);
			return;
		}
		imageTable.setRowCount(images.size(),true);
		imageTable.setRowData(0,images);
		imageTable.setVisible(true);

		pager.setDisplay(imageTable);
		pager.setVisible(true);

		noImageUploaded.setVisible(false);
	}
	
	private void setupImageTable(){

		// if null create it
		if(imageTable == null) {
			imageTable = new CellTable<BinaryImage>();
		}

		// if no columns are set, set the main one
		if(imageTable.getColumnCount() == 0) {
			imageTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
			imageTable.setKeyboardPagingPolicy(KeyboardPagingPolicy.CHANGE_PAGE);

			TextColumn<BinaryImage> binaryImageNameCol = new TextColumn<BinaryImage>() {
				@Override
				public String getValue(BinaryImage object) {
					return object.getFileName();
				}
			};
			
			TextColumn<BinaryImage> binaryImageIdCol = new TextColumn<BinaryImage>() {
				@Override
				public String getValue(BinaryImage object) {
					return object.getId().toString();
				}
			};
			
			imageTable.addColumn(binaryImageIdCol,"Experiment Image ID");
			imageTable.addColumn(binaryImageNameCol,"Experiment Images");
		}

		// set selection model
		final SingleSelectionModel<BinaryImage> selectionModel = new SingleSelectionModel<BinaryImage>();
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				BinaryImage selected = selectionModel.getSelectedObject();
				if (selected != null) {
					presenter.setSelectedImage(selected);
					flashButton.setEnabled(true);
				}
			}
		});
		imageTable.setSelectionModel(selectionModel);

		// set the pager
		if(pager == null) {
			SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
			pager = new SimplePager(TextLocation.CENTER,pagerResources,false,0,true);
		}
	}
}
