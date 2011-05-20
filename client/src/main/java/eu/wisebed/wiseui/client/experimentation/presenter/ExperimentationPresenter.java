package eu.wisebed.wiseui.client.experimentation.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import eu.wisebed.wiseui.api.ReservationServiceAsync;
import eu.wisebed.wiseui.client.WiseUiGinjector;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentView;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentationView;
import eu.wisebed.wiseui.client.main.WiseUiPlace;
import eu.wisebed.wiseui.client.testbedlist.event.TestbedSelectedEvent;
import eu.wisebed.wiseui.client.testbedselection.event.ThrowableEvent;
import eu.wisebed.wiseui.client.experimentation.event.RefreshUserExperimentsEvent;
import eu.wisebed.wiseui.client.util.EventBusManager;
import eu.wisebed.wiseui.shared.common.Checks;
import eu.wisebed.wiseui.shared.dto.ReservationDetails;
import eu.wisebed.wiseui.shared.dto.SecretReservationKey;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;
import eu.wisebed.wiseui.shared.exception.ReservationException;
import eu.wisebed.wiseui.widgets.messagebox.MessageBox;


public class ExperimentationPresenter implements ExperimentationView.Presenter,
TestbedSelectedEvent.ConfigurationSelectedHandler,
ThrowableEvent.ThrowableHandler, PlaceChangeEvent.Handler,
RefreshUserExperimentsEvent.Handler {

	private final WiseUiGinjector injector;
	private ExperimentationView view;
	private ReservationServiceAsync service;
	private WiseUiPlace place;
	private EventBusManager eventBus;
	private TestbedConfiguration testbedConfiguration;
	private HashMap<String,List<ExperimentPresenter>> presenterMap;
	private HashMap<String,List<ExperimentView>> viewMap;

	@Inject
	public ExperimentationPresenter(final WiseUiGinjector injector,
			final ReservationServiceAsync service,
			final ExperimentationView view,
			final PlaceController placeController,
			final EventBus eventBus){

		this.injector = injector;
		this.service = service;
		this.view = view;
		this.eventBus = new EventBusManager(eventBus);
		this.presenterMap = new HashMap<String,List<ExperimentPresenter>>();
		this.viewMap = new HashMap<String,List<ExperimentView>>();
		bind();
		GWT.log("Initializing Experimentation Presenter");
	}

	public void setPlace(WiseUiPlace place) {
		this.place = place;
	}

	public WiseUiPlace getPlace() {
		return place;
	}

	public void setView(final ExperimentationView view) {
		this.view = view;
	}

	public void bind(){
		eventBus.addHandler(PlaceChangeEvent.TYPE, this);
		eventBus.addHandler(TestbedSelectedEvent.TYPE, this);
		eventBus.addHandler(ThrowableEvent.TYPE, this);
    	eventBus.addHandler(RefreshUserExperimentsEvent.TYPE, this);
	}

	@Override
	public void onTestbedSelected(final TestbedSelectedEvent event) {
		testbedConfiguration = event.getConfiguration();

		// check the configuration for errors
		try{
			Checks.ifNullOrEmpty(testbedConfiguration.getUrnPrefixList(),
			"Null or empty URN prefix list for selected testbed");
		}catch(RuntimeException cause){
			MessageBox.error("Error", cause.getMessage(), cause, null);
			return;
		}

		getUserReservations(testbedConfiguration.getUrnPrefixList(),
				testbedConfiguration.getRsEndpointUrl());
	}
	
	@Override
	public void onRefreshUserExperiments(RefreshUserExperimentsEvent event) {
		
		try{
			Checks.ifNull(testbedConfiguration, "No testbed selected");
		}catch(RuntimeException cause){
			MessageBox.error("Error", cause.getMessage(), cause, null);
			return;
		}
		
		try{
			Checks.ifNullOrEmpty(testbedConfiguration.getUrnPrefixList(),
			"Null or empty URN prefix list for selected testbed");
		}catch(RuntimeException cause){
			MessageBox.error("Error", cause.getMessage(), cause, null);
			return;
		}

		getUserReservations(testbedConfiguration.getUrnPrefixList(),
				testbedConfiguration.getRsEndpointUrl());
	} 

	@Override
	public void onPlaceChange(final PlaceChangeEvent event) {
		eventBus.removeAll();
	}

	@Override
	public void onThrowable(ThrowableEvent event) {
		if (event.getThrowable() == null) return;
		if (testbedConfiguration != null && testbedConfiguration.getName() != null) {
			MessageBox.error("Error fetching reservation data for testbed '"
					+ testbedConfiguration.getName()
					+ "'!",
					event.getThrowable().getMessage(),
					event.getThrowable(), null);
		} else {
			MessageBox.error("Error fetching reservation data!",
					event.getThrowable().getMessage(),
					event.getThrowable(), null);
		}
	}
	
	@Override
	public void refreshUserExperiments() {
		eventBus.fireEventFromSource(new RefreshUserExperimentsEvent(), this);
	}

	@Override
	public void getUserReservations(final List<String> urnPrefixList,
			final String rsEndpointUrl) {

		view.getLoadingIndicator().showLoading("Loading reservations");

		// retrieve the respected secret reservation keys stored in cookies		
		final String firstUrnPrefix = urnPrefixList.get(0);
		List<SecretReservationKey> keys = 
			injector.getReservationManager().getFilteredSecretReservationKeys(
					firstUrnPrefix);

		// setup RPC callback
		AsyncCallback<List<ReservationDetails>> callback = new 
		AsyncCallback<List<ReservationDetails>>(){

			@Override
			public void onFailure(Throwable caught) {
				if(caught instanceof ReservationException) {
					GWT.log(caught.getMessage());
					MessageBox.error("Reservation Service", caught.getMessage(), caught, null);
					view.getLoadingIndicator().hideLoading();
				}
			}

			@Override
			public void onSuccess(List<ReservationDetails> results) {
				
				// if testbed is not a key in the presenterMap add it
				if(presenterMap.containsKey(firstUrnPrefix) == false) {
					presenterMap.put(firstUrnPrefix, new ArrayList<ExperimentPresenter>());
				}
				
				// if testbed is not a key in the viewMap add it
				if(viewMap.containsKey(firstUrnPrefix) == false) {
					viewMap.put(firstUrnPrefix, new ArrayList<ExperimentView>());
				}
				
				// get both maps 
				List<ExperimentPresenter> presenterList =
					presenterMap.get(firstUrnPrefix);
				List<ExperimentView> viewList = viewMap.get(firstUrnPrefix);
				
				
				for(ReservationDetails result : results) {
					ExperimentPresenter presenter =
						injector.getExperimentPresenter();
					ExperimentView view = presenter.getView();
					view.setPresenter(presenter);
				}
				view.getLoadingIndicator().hideLoading();
			}
		};

		// make the RPC
		service.getUserReservations(keys, rsEndpointUrl, callback);
	}
}