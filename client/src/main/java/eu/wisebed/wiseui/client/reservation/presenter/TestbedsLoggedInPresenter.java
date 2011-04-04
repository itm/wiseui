package eu.wisebed.wiseui.client.reservation.presenter;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.inject.Inject;

import eu.wisebed.wiseui.api.ReservationServiceAsync;
import eu.wisebed.wiseui.api.TestbedConfigurationServiceAsync;
import eu.wisebed.wiseui.client.WiseUiGinjector;
import eu.wisebed.wiseui.client.reservation.ReservationPlace;
import eu.wisebed.wiseui.client.reservation.common.Messages;
import eu.wisebed.wiseui.client.reservation.event.TestbedSelectedChangedEvent;
import eu.wisebed.wiseui.client.reservation.view.TestbedsLoggedInView;
import eu.wisebed.wiseui.client.reservation.view.TestbedsLoggedInView.Presenter;

import eu.wisebed.wiseui.shared.SensorDetails;
import eu.wisebed.wiseui.shared.TestbedConfiguration;
import eu.wisebed.wiseui.widgets.messagebox.MessageBox;

public class TestbedsLoggedInPresenter implements Presenter {

	private final TestbedConfigurationServiceAsync configurationService;
	private final ReservationServiceAsync reservationService;
	private final PlaceController placeController;
	private ReservationPlace place;
	private WiseUiGinjector injector;
	private final TestbedsLoggedInView view;
	private SingleSelectionModel<TestbedConfiguration> testbedSelectionModel;
	private EventBus eventBus;
	private List<TestbedConfiguration> testbedList;
	private final ListDataProvider<TestbedConfiguration> dataProvider = 
		new ListDataProvider<TestbedConfiguration>();

	@Inject
	public TestbedsLoggedInPresenter(final WiseUiGinjector injector,
			final PlaceController placeController,
			final TestbedConfigurationServiceAsync configurationService,
			final ReservationServiceAsync reservationService,
			final EventBus eventBus,
			final TestbedsLoggedInView view){
		this.injector = injector;
		this.placeController = placeController;
		this.configurationService = configurationService;
		this.reservationService = reservationService;
		this.eventBus = eventBus;
		this.view = view;
        // Init testbed selection list
		dataProvider.addDataDisplay(view.getTestbedList());
        testbedSelectionModel = new SingleSelectionModel<TestbedConfiguration>();
        view.setTestbedSelectionModel(testbedSelectionModel);
        bindEvents();
	}
	
    @Override
    public void setPlace(final ReservationPlace place) {
    	this.place = place;
    	if( testbedSelectionModel.getSelectedObject()==null)
    		injector.getReservationView().reserveButton(false);
    	if (place.getSelection()==null || testbedList==null)
    		return;
		for (TestbedConfiguration bed: testbedList){
			if(place.getSelection()==bed.getTestbedID()){
				testbedSelectionModel.setSelected(bed, true);
			}
		}
    }
    
	public void bindEvents() {
		testbedSelectionModel.addSelectionChangeHandler(new Handler() {
			public void onSelectionChange(final SelectionChangeEvent event) {
				onTestbedSelectionChange(event);
			}
		});
	}
	
	/**
	 * Send the urn prefix to the server to identify the testbed the user
	 * has previously logged in.
	 */
	public void getTestbedsLoggedIn(final List<String> urnPrefix){
		configurationService.getTestbedLoggedIn(urnPrefix, 
				new AsyncCallback<List<TestbedConfiguration>>(){
			public void onFailure(Throwable caught){
				GWT.log("Failed rpc");
			}
			public void onSuccess(List<TestbedConfiguration> testbeds){
				if(testbeds == null){
					MessageBox.error(Messages.TESTBEDS_IDENTIFIED_TITLE,
							Messages.TESTBEDS_IDENTIFIED, null, null);
				}else{
					for(TestbedConfiguration bed: testbeds){
						dataProvider.getList().add(bed);
						if(place.getSelection()==null) continue;
						if(place.getSelection()==bed.getTestbedID()){
							testbedSelectionModel.setSelected(bed, true);
							injector.getReservationPresenter().enableReservation();
						}
					}
					testbedList = testbeds;
				}
			}
		});
	}

	/**
	 * Makes an Asynchronous callback to the server 'asking' for the 
	 * nodes located in the network. Result consists of an array with all
	 * sensors' useful information
	 */
	public void getNetwork(final String sessionManagementEndpointUrl) {
		reservationService.getNodeList(sessionManagementEndpointUrl,
				new AsyncCallback<ArrayList<SensorDetails>>(){
			public void onFailure(Throwable caught){
				GWT.log("Failed rpc");
			}
			public void onSuccess(ArrayList<SensorDetails> nodeList){
				if(nodeList==null){
					MessageBox.error(Messages.NO_NODES_RETURNED_TITLE,
							Messages.NO_NODES_RETURNED, null, null);
				}else{
					injector.getNewReservationView().renderNodes(nodeList);
				}
			}
		});
	}
	
	public void renderTestbeds(List<TestbedConfiguration> testbeds) {
		view.renderTestbeds(testbeds);
	}	

	public SingleSelectionModel<TestbedConfiguration> getTestbedSelectionModel(){
		return testbedSelectionModel;
	}

	private void onTestbedSelectionChange(final SelectionChangeEvent event){
		TestbedConfiguration bed = testbedSelectionModel.getSelectedObject();
		getNetwork(bed.getSessionmanagementEndpointUrl());
		eventBus.fireEvent(new TestbedSelectedChangedEvent(bed));
		if ( place.getSelection()==null || place.getSelection()!= bed.getTestbedID()){
			placeController.goTo(
				new ReservationPlace(bed.getTestbedID(), place.getView()));
		}
	}
}
