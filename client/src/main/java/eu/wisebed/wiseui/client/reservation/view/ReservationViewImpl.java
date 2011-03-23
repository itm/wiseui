package eu.wisebed.wiseui.client.reservation.view;

import java.util.ArrayList;
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
		imagePanel.add(this.timeWidget);
		imagePanel.add(this.imageUploadWidget);
		outerPanel.add(this.sensorListWidget);
		loginRequiredPanel(false);
	}
	
	@UiFactory
	public CellList<TestbedConfiguration> testbedsLoggedIn(){
		final Cell<TestbedConfiguration> testbed = new AbstractCell<TestbedConfiguration>() {
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

	public void renderNodes(ArrayList<SensorDetails> nodes) {
		this.sensorListWidget.renderNodes(nodes);
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
	 * Toggle visibility of reservation error panel
	 * @param visible True if visible
	 */
	public void reservationErrorPanel(boolean visible){
		errorPanel.setVisible(visible);
	}
	
	/**
	 * Toggle visibility of reservation button
	 * @param visible True if visible
	 */
	public void reserveButton(boolean visible){
		reserveButton.setVisible(visible);
	}

	/**
	 * Tells the listener to continue with a new reservation after checking
	 * that all reservation details have properly been set
	 * 
	 * @param e ClickEvent
	 */
	@UiHandler("reserveButton")
	void onClickReserveButton(ClickEvent e) {
		if(presenter.checkRsDetails())
			presenter.makeReservation();
	}
	
	/**
	 * Renders a login required message at the top of the view.
	 */
	public void loginRequiredPanel(boolean visible){
		loginRequiredPanel.setVisible(visible);
	}

}
