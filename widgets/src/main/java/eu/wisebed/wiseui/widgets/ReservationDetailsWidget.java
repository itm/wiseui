package eu.wisebed.wiseui.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
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
}
