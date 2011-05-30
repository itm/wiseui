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
package eu.wisebed.wiseui.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import eu.wisebed.wiseui.widgets.ReservationDetails.Presenter;

public class ReservationDetailsWidget extends Composite{
	
	@UiTemplate("ReservationDetailsWidget.ui.xml")
	interface CalendarImplUiBinder extends UiBinder<Widget,ReservationDetailsWidget>{}
	
	@UiField
	HTMLPanel container;
	@UiField
	DecoratedPopupPanel popUp;
	@UiField
	VerticalPanel panel;
	@UiField
	Label reservedBy;
	@UiField
	Label start;
	@UiField
	Label end;
	@UiField
	TextArea textArea;
	@UiField
	Button delete;
	
	private static CalendarImplUiBinder uiBinder = 
		GWT.create(CalendarImplUiBinder.class);
	
	private Presenter presenter;
	
	public ReservationDetailsWidget(){
		initWidget(uiBinder.createAndBindUi(this));
		textArea.setEnabled(false);
        popUp.setAnimationEnabled(true);
        popUp.setAutoHideEnabled(true);
        popUp.setModal(false);
		panel.setPixelSize(275, 400);
	}
	
	public void setListener(Presenter presenter) {
		this.presenter = presenter;
	}

	public Presenter getListener() {
		return presenter;
	}
	
	public DecoratedPopupPanel getPopUp(){
		return popUp;
	}
	
	public void setReservedBy(final String string){
		reservedBy.setText(string);
	}
	
	public void setStart(final String string){
		start.setText(string);
	}
	
	public void setEnd(final String string){
		end.setText(string);
	}

	public void setDescription(final String string){
		textArea.setText(string);
	}
	
	public Button getDeleteButton(){
		return delete;
	}
}
