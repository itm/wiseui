package eu.wisebed.wiseui.widgets;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;

import eu.wisebed.wiseui.widgets.resources.CommonResources;


public class CalendarWidget extends Composite implements Calendar{

	@UiTemplate("CalendarWidget.ui.xml")
	interface CalendarImplUiBinder extends UiBinder<Widget,CalendarWidget>{}
	
	@UiField DatePicker datePicker = new DatePicker();
	
	private static CalendarImplUiBinder uiBinder = 
		GWT.create(CalendarImplUiBinder.class);
	
	private Presenter listener;
	
	public CalendarWidget(){
		CommonResources.INSTANCE.style().ensureInjected(); 
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setListener(Presenter listener) {
		this.listener = listener;
	}

	public Presenter getListener() {
		return listener;
	}
	
	/**
	 * Set default date to current date. 
	 */
	public void setDateValue(){
		this.datePicker.setValue(new Date());
	}
	

	/**
	 * Gets date selected in date picker.
	 * @return A Date object containing the date selected.
	 */
	@SuppressWarnings("deprecation")
	public Date getDateValue() {
		int year = this.datePicker.getValue().getYear();
		int month = this.datePicker.getValue().getMonth();
		int day = this.datePicker.getValue().getDate();

		return new Date(year + 1900, month, day);
	}
	
}
