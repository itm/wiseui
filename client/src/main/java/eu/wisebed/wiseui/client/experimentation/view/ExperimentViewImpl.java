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
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class ExperimentViewImpl extends Composite implements ExperimentView {
	private static ExperimentPanelViewImplBinder uiBinder = GWT.create(ExperimentPanelViewImplBinder.class);

	@UiTemplate("ExperimentViewImpl.ui.xml")
	interface ExperimentPanelViewImplBinder extends UiBinder<Widget, ExperimentViewImpl> {
	}

	private Presenter presenter;
	
	@UiField 
	Label secretReservationKey;
	@UiField 
	Label startDate;
	@UiField 
	Label stopDate;
	@UiField
	Label experimentTiming;
	@UiField
	Label uploadedImageFilename;
	@UiField
	Label status;
	@UiField
	Label username;
	@UiField
	Button flashImageButton;
	@UiField
	Button startExperimentButton;
	@UiField
	Button stopExperimentButton;
	@UiField
	Button getWiseMLButton;
	@UiField(provided = true)
	CellTable<String> nodeTable;
	@UiField(provided = true)
	SimplePager pager;

	public ExperimentViewImpl() {
		setupNodeTable();
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(final Presenter presenter) {
		this.presenter = presenter;
	}

	public Presenter getPresenter() {
		return presenter;
	}

	@Override
	public String getSecretReservationKey() {
		return secretReservationKey.getText();
	}

	@Override
	public void setSecretReservationKey(final String text) {
		secretReservationKey.setText(text);
	}

	@Override
	public String getStartDate() {
		return startDate.getText();
	}

	@Override
	public void setStartDate(final String text) {
		startDate.setText(text);
	}

	@Override
	public String getStopDate() {
		return stopDate.getText();
	}

	@Override
	public void setStopDate(final String text) {
		stopDate.setText(text);
	}

	@Override
	public void setExperimentTiming(String timing) {
		experimentTiming.setText(timing);
	}

	@Override
	public String getExperimentTiming() {
		return experimentTiming.getText();
	}

	@Override
	public void setStatus(String value) {
		status.setText(value);
	}

	@Override
	public String getStatus() {
		return status.getText();
	}

	@Override
	public void setNodeUrns(List<String> nodeUrns) {
		nodeTable.setRowCount(nodeUrns.size());
		nodeTable.setRowData(nodeUrns);
		pager.setDisplay(nodeTable);
	}

	@Override
	public void setUsername(String username) {
		this.username.setText(username);
	}

	@Override
	public String getUsername() {
		return username.getText();
	}
	
	public String getUploadedImageFilename() {
		return uploadedImageFilename.getText();
	}

	public void setUploadedImageFilename(final String filename) {
		this.uploadedImageFilename.setText(filename);
	}
	
	@Override
	public void activateStartExperimentButton() {
		startExperimentButton.setEnabled(true);
	}

	@Override
	public void activateFlashExperimentButton() {
		flashImageButton.setEnabled(true);
	}

	@Override
	public void activateStopExperimentButton() {
		stopExperimentButton.setEnabled(true);
	}
	
	@Override
	public void activateDownloadWiseMLButton() {
		getWiseMLButton.setEnabled(true);
	}
	
	@Override
	public void deactivateStartExperimentButton() {
		startExperimentButton.setEnabled(false);
	}

	@Override
	public void deactivateFlashExperimentButton() {
		flashImageButton.setEnabled(false);
	}

	@Override
	public void deactivateStopExperimentButton() {
		stopExperimentButton.setEnabled(false);
	}
	
	@Override
	public void deactivateDownloadWiseMLButton() {
		getWiseMLButton.setEnabled(false);
	}
	
	@UiHandler("flashImageButton")
	public void handleFlashImageButtonClick(final ClickEvent e) {
		presenter.flashExperimentImage();
	}
	
	@UiHandler("startExperimentButton")
	public void handleStartExperimentButtonClick(final ClickEvent e) {
		presenter.startExperiment();
	}
	
	@UiHandler("stopExperimentButton")
	public void handleStopExperimentButtonClick(final ClickEvent e) {
		presenter.stopExperiment();
	}
	
	@UiHandler("getWiseMLButton")
	public void handleGetWiseMLButtonClick(final ClickEvent e){
		//presenter.getWiseMLDocument
	}
		
	private void setupNodeTable(){
		
		// if null create it
		if(nodeTable == null) {
			nodeTable = new CellTable<String>();
		}

		// if no columns are set, set the main one
		if(nodeTable.getColumnCount() == 0) {
			nodeTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
			nodeTable.setKeyboardPagingPolicy(KeyboardPagingPolicy.CHANGE_PAGE);

			TextColumn<String> nodeUrnCol = new TextColumn<String>() {
				@Override
				public String getValue(String object) {
					return object;
				}
			};
			nodeTable.addColumn(nodeUrnCol,"Experiment Nodes");
		}

		// set selection model
		final SingleSelectionModel<String> selectionModel = new SingleSelectionModel<String>();
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				String selected = selectionModel.getSelectedObject();
				if (selected != null) {
					presenter.showNodeOutput(selected);
				}
			}
		});
		nodeTable.setSelectionModel(selectionModel);
		
		// set the pager
		if(pager == null) {
			SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
			pager = new SimplePager(TextLocation.CENTER,pagerResources,false,0,true);
		}
	}
}
