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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

import eu.wisebed.wiseui.widgets.HasWidgetsDialogBox;

public class ExperimentWiseMLOutputViewImpl extends HasWidgetsDialogBox implements  ExperimentWiseMLOutputView{
	
	private static ExperimentWiseMLOutputViewImplBinder uiBinder = GWT.create(ExperimentWiseMLOutputViewImpl.class);
	
	@UiTemplate("ExperimentWiseMLOutputViewImpl.ui.xml")
	interface ExperimentWiseMLOutputViewImplBinder extends UiBinder<Widget, ExperimentWiseMLOutputViewImpl> {
	}

	@UiField
	Button closeButton;
	
	@UiField
	TextArea outputTextArea;

	public ExperimentWiseMLOutputViewImpl(){
		uiBinder.createAndBindUi(this);

		setModal(true);
		setGlassEnabled(true);
		setAnimationEnabled(true);
		
		outputTextArea.setText("");	
		setScrollPositionAtEnd();
	}
	
	@Override
	public void show(final String title) {
		setText(title);
		center();
		show();		
	}
	
	@UiHandler("closeButton")
	public void handleCancelButtonClick(final ClickEvent event) {
		hide();
	}

	@Override
	public void addWiseMLOutput(final String wiseMlString) {
		String previousText = outputTextArea.getText();
		outputTextArea.setText(previousText + '\n'+ wiseMlString);
		setScrollPositionAtEnd();
	}
	
	@Override
	public void setWiseMLOutput(final String wiseMlString) {
		outputTextArea.setText(wiseMlString);
		setScrollPositionAtEnd();
	}

	@Override
	public void clearOutput() {
		outputTextArea.setText("");
	}
	
	private void setScrollPositionAtEnd() {
		outputTextArea.getElement().getFirstChildElement().setScrollTop(
				outputTextArea.getElement().getFirstChildElement().getScrollHeight()
		);
	}
}
