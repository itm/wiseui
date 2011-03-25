package eu.wisebed.wiseui.client.reservation.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionModel;

import eu.wisebed.wiseui.shared.ReservationDetails;
import eu.wisebed.wiseui.shared.SensorDetails;
import eu.wisebed.wiseui.shared.TestbedConfiguration;
import eu.wisebed.wiseui.widgets.CalendarWidget;
import eu.wisebed.wiseui.widgets.ImageUploadWidget;
import eu.wisebed.wiseui.widgets.SensorListWidget;
import eu.wisebed.wiseui.widgets.TimeSelectorWidget;

public class ReservationViewImpl extends Composite implements ReservationView {

	@UiTemplate("ReservationViewImpl.ui.xml")
    interface ReservationViewImplUiBinder extends
            UiBinder<Widget, ReservationViewImpl> {
    }

	private CalendarWidget calendarWidget = new CalendarWidget();
	private TimeSelectorWidget timeWidget = new TimeSelectorWidget();
	private ImageUploadWidget imageUploadWidget = new ImageUploadWidget();
	private SensorListWidget sensorListWidget = new SensorListWidget();
	private Presenter presenter;

	@UiField VerticalPanel outerPanel;
	@UiField HTMLPanel loginRequiredPanel;
	@UiField HorizontalPanel dateTimeImagePanel;
	@UiField VerticalPanel imagePanel;
	@UiField HTMLPanel errorPanel;
	@UiField Button reserveButton;
	@UiField HTMLPanel testbedsPanel;
	@UiField CellList<TestbedConfiguration> testbedList;
	
    private static ReservationViewImplUiBinder uiBinder = GWT
            .create(ReservationViewImplUiBinder.class);

    public ReservationViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void setPresenter(final Presenter presenter) {
    	this.presenter = presenter;
    }
    
	/**
	 * Initialize reservation view by internally calling initialization 
	 * procedure for each widget the view consists of.
	 */
	public void initRsView() {
		dateTimeImagePanel.add(calendarWidget);
		dateTimeImagePanel.add(imagePanel);
		imagePanel.add(timeWidget);
		imagePanel.add(imageUploadWidget);
		outerPanel.add(sensorListWidget);
		sensorListWidget.init();
		loginRequiredPanel(false);
	}
	
	@UiFactory
	public CellList<TestbedConfiguration> testbedsLoggedIn(){
		final Cell<TestbedConfiguration> testbed = 
			new AbstractCell<TestbedConfiguration>() {
            public void render(final Context context,
                    final TestbedConfiguration testbed,
                    final SafeHtmlBuilder builder) {
                for(int i=0; i<testbed.getUrnPrefixList().size(); i++){
                	builder.appendHtmlConstant("<div class=\"celllist-entry\">");
                	builder.appendEscaped(testbed.getName());
                	builder.appendHtmlConstant("</div>");
                }
            }
		};
		return  new CellList<TestbedConfiguration>(testbed);		
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
	
    @Override
    public void renderTestbeds(final List<TestbedConfiguration> testbeds) {
        testbedList.setRowCount(testbeds.size());
        testbedList.setRowData(0, testbeds);
    }
    
    public HasData<TestbedConfiguration> getTestbedList(){
    	return this.testbedList;
    }
    
    @Override
    public void setTestbedSelectionModel(
    		final SelectionModel<TestbedConfiguration> selectionModel) {
        testbedList.setSelectionModel(selectionModel);
    }
    
	/**
	 * Toggle visibility of reservation error panel.
	 * @param visible True if visible.
	 */
	public void reservationErrorPanel(boolean visible){
		errorPanel.setVisible(visible);
	}
	
	/**
	 * Toggle visibility of reservation button.
	 * @param visible True if visible.
	 */
	public void reserveButton(boolean visible){
		reserveButton.setVisible(visible);
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
		return imageUploadWidget.getImageFileName();
	}

	public String getImageFileNameField() {
		return imageUploadWidget.getImageFileNameField();
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
		return (imageUploadWidget.checkImageSelected()) ? true : false;
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

	/**
	 * Renders a login required message at the top of the view.
	 */
	public void loginRequiredPanel(boolean visible){
		loginRequiredPanel.setVisible(visible);
	}
	
	/**
	 * Tells the listener to continue with a new reservation after checking
	 * that all reservation details have properly been set
	 * 
	 * @param e ClickEvent
	 */
	@UiHandler("reserveButton")
	public void onClickReserveButton(ClickEvent e) {
		if(checkReservationDetails()){
			TestbedConfiguration bed = 	
				presenter.getTestbedSelectionModel().getSelectedObject();
			presenter.makeReservation(bed.getRsEndpointUrl(), bed.getUrnPrefixList().get(0));
		}
	}
}
