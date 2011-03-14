package eu.wisebed.wiseui.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class TimeSelectorWidget extends Composite implements TimeSelector{

	@UiTemplate("TimeSelectorWidget.ui.xml")
	interface TimePickerImplUiBinder extends UiBinder<Widget,TimeSelectorWidget>{}

	private static TimePickerImplUiBinder uiBinder = 
		GWT.create(TimePickerImplUiBinder.class);
	
	private Presenter listener;
	
	@UiField FlowPanel timePanel;
	@UiField TextBox startTimePicker;
	@UiField TextBox stopTimePicker;
	@UiField Element timeSpan;

	/**
	 * Initialize timePicker widget by setting default start and stop time to 
	 * current time.
	 */
	public TimeSelectorWidget(){
		initWidget(uiBinder.createAndBindUi(this));
		// TODO: Set default values as watermarks
		startTimePicker.setText("23:00");
		stopTimePicker.setText("23:02");
		startTimePicker.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event){
				if (!validTimeFormat(startTimePicker.getText()))
					timeSpan.setInnerText("Not a valid time format (hh:mm)");
				else
					timeSpan.setInnerText("Valid start time format");
			}
		});
		stopTimePicker.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event){
				if (!validTimeFormat(stopTimePicker.getText()))
					timeSpan.setInnerText("Not a valid time format (hh:mm)");
				else
					timeSpan.setInnerText("Valid stop time format");
			}
		});
		timePanel.add(startTimePicker);
		timePanel.add(stopTimePicker);
	}

	public void setListener(Presenter listener) {
		this.listener = listener;
	}

	public Presenter getListener() {
		return listener;
	}

	/**
	 * Gets start time from the start time picker.
	 * @return An array of integers with two entries, the first one 
	 * representing the hours and the second one the minutes.
	 */
	public Integer[] getStartTime(){
		if (validTimeFormat(stopTimePicker.getText())){
			String[] startTimeStr = new String[2];
			Integer[] startTimeInt = new Integer[2];
			startTimeStr = startTimePicker.getText().split(":");
			startTimeInt[0] = Integer.parseInt(startTimeStr[0]);
			startTimeInt[1] = Integer.parseInt(startTimeStr[1]);
			return startTimeInt;
		}
		return null;
	}
	
	/**
	 * Gets stop time from the stop time picker.
	 * @return An array of integers with two entries, the first one 
	 * representing the hours and the second one the minutes.
	 */
	public Integer[] getStopTime(){
		if (validTimeFormat(stopTimePicker.getText())){
			String[] stopTimeStr = new String[2];
			Integer[] stopTimeInt = new Integer[2];
			stopTimeStr = stopTimePicker.getText().split(":");
			stopTimeInt[0] = Integer.parseInt(stopTimeStr[0]);
			stopTimeInt[1] = Integer.parseInt(stopTimeStr[1]);
			return stopTimeInt;
		}
		return null;
	}
	
	/**
	 * Checks if the given string has a valid time format (hh:mm)
	 * @param time
	 * @return
	 */
	private boolean validTimeFormat(String time){
		RegExp timeRegex = RegExp.compile(
				"^([1-9]|[0-1][0-9]|2[0-3]){1}(:[0-5][0-9]){1}$");
		return timeRegex.test(time) ? true: false; 		
	}
	
}