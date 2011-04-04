package eu.wisebed.wiseui.client.reservation.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

import eu.wisebed.wiseui.shared.ReservationDetails;
import eu.wisebed.wiseui.shared.SensorDetails;
import eu.wisebed.wiseui.widgets.CalendarWidget;
import eu.wisebed.wiseui.widgets.ImageUploadWidget;
import eu.wisebed.wiseui.widgets.SensorListWidget;
import eu.wisebed.wiseui.widgets.TimeSelectorWidget;

@Singleton
public class NewReservationViewImpl extends Composite implements NewReservationView {

	@UiTemplate("NewReservationViewImpl.ui.xml")
    interface NewReservationViewImplUiBinder extends
            UiBinder<Widget, NewReservationViewImpl> {
    }

	private static NewReservationViewImplUiBinder uiBinder = GWT
    	.create(NewReservationViewImplUiBinder.class);

	public NewReservationViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	private Presenter presenter;
	
	public void setPresenter(final Presenter presenter){
		this.presenter = presenter;
	}
	
	private CalendarWidget calendarWidget = new CalendarWidget();
	private TimeSelectorWidget timeWidget = new TimeSelectorWidget();
	private SensorListWidget sensorListWidget = new SensorListWidget();
	private ImageUploadWidget imageWidget = new ImageUploadWidget();

	@UiField HTMLPanel container;
	@UiField HorizontalPanel topParametersPanel;
	@UiField HorizontalPanel bottomParametersPanel;
	@UiField HTMLPanel calendarPanel;
	@UiField HTMLPanel timePanel;
	@UiField HTMLPanel imagePanel;
	    
	/**
	 * Initialize reservation view by internally calling initialization 
	 * procedure for each widget the view consists of.
	 */
	public void enableNewReservationView(){
		calendarPanel.add(calendarWidget);
		timePanel.add(timeWidget);
		imagePanel.add(imageWidget);
		topParametersPanel.add(calendarPanel);
		topParametersPanel.add(timePanel);
		topParametersPanel.add(imagePanel);
		bottomParametersPanel.add(sensorListWidget);
		container.add(topParametersPanel);
		container.add(bottomParametersPanel);
		sensorListWidget.init();
	}

	@SuppressWarnings("deprecation")
	public ReservationDetails getReservationDetails(){
		Date date = getDateValue();
		Integer[] startTimeInt = getStartTime();
		Integer[] stopTimeInt = getStopTime();
		List<SensorDetails> sensors = getNodesSelected();
		List<String> sensorUrns = new ArrayList<String>();
		for(SensorDetails s: sensors ){
			sensorUrns.add(s.getUrn());
		}
		Date startTime = new Date(date.getYear() - 1900, date.getMonth(),
				date.getDate(), startTimeInt[0], startTimeInt[1]);
		Date stopTime = new Date(date.getYear()-1900, date.getMonth(),
				date.getDate(), stopTimeInt[0], stopTimeInt[1]);
		
		final ReservationDetails details = new ReservationDetails(startTime,
				stopTime, stopTime.getTime() - startTime.getTime(), sensorUrns, 
				getImageFileName(), getImageFileNameField()
		);
		return details;
		
	}

	public void renderNodes(ArrayList<SensorDetails> nodes) {
		sensorListWidget.renderNodes(nodes);
	}

	public Date getDateValue(){
		return calendarWidget.getDateValue();
	}

	/**
	 * Get from-time selected in time picker
	 */
	public Integer[] getStartTime() {
		return timeWidget.getStartTime();
	}
	
	/**
	 * Get to-time selected in time picker
	 */
	public Integer[] getStopTime() {
		return timeWidget.getStopTime();
	}
	
	public String getImageFileName() {
		return imageWidget.getImageFileName();
	}

	public String getImageFileNameField() {
		return imageWidget.getImageFileNameField();
	}

	/**
	 * Get nodes selected in cell list
	 */
	public ArrayList<SensorDetails> getNodesSelected() {
		return sensorListWidget.getNodesSelected();
	}

	/**
	 * Check if the user has uploaded any image.
	 * @return boolean, True if selected, false if not. 
	 * 
	 */
	public boolean checkImageSelected() {
		return (imageWidget.checkImageSelected()) ? true : false;
	}
	
	/**
	 * Check if user has set all required reservation details
	 */
	public boolean checkReservationDetails() {
		// Temporarily checking only for the image
		if (!checkImageSelected()){
			return false;
		}
		return true;
	}
}
