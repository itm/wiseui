package eu.wisebed.wiseui.client.experimentation.presenter;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import eu.wisebed.wiseui.api.ReservationServiceAsync;
import eu.wisebed.wiseui.client.WiseUiGinjector;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentationView;
import eu.wisebed.wiseui.client.main.WiseUiPlace;
import eu.wisebed.wiseui.client.testbedlist.event.TestbedSelectedEvent;
import eu.wisebed.wiseui.client.testbedselection.event.ThrowableEvent;
import eu.wisebed.wiseui.client.util.EventBusManager;
import eu.wisebed.wiseui.shared.common.Checks;
import eu.wisebed.wiseui.shared.dto.ReservationDetails;
import eu.wisebed.wiseui.shared.dto.SecretReservationKey;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;
import eu.wisebed.wiseui.shared.exception.ReservationException;
import eu.wisebed.wiseui.widgets.messagebox.MessageBox;


public class ExperimentationPresenter implements ExperimentationView.Presenter,
	TestbedSelectedEvent.ConfigurationSelectedHandler,
	ThrowableEvent.ThrowableHandler, PlaceChangeEvent.Handler {

	private final WiseUiGinjector injector;
	private ExperimentationView view;
	private ReservationServiceAsync service;
	private WiseUiPlace place;
	private EventBusManager eventBus;
    private TestbedConfiguration testbedConfiguration;

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
		bind();
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
	}
	
	@Override
    public void onTestbedSelected(final TestbedSelectedEvent event) {
        this.testbedConfiguration = event.getConfiguration();
		getUserReservations();
    }

    @Override
    public void onPlaceChange(final PlaceChangeEvent event) {
        view.getLoadingIndicator().hideLoading();
    	eventBus.removeAll();
    }
    
	@Override
	public void onThrowable(ThrowableEvent event) {
        view.getLoadingIndicator().hideLoading();
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
	public void getUserReservations() {
		
        // retrieve configured testbed
        String testbedName = "<Unknown testbed>";
        if (testbedConfiguration != null) {
            testbedName = testbedConfiguration.getName();
        }
       
        GWT.log("Loading reservations for Testbed '" + testbedName + "'");

        if (testbedConfiguration == null) {
            final String errorMessage = "rs endpoint URL not found for Testbed '" + testbedName + "'";
            GWT.log(errorMessage);
            MessageBox.warning("Configuration could not be loaded!",
            		errorMessage,
                    null);
        }
		
		// retrieve the respected secret reservation keys stored in cookies
        Checks.ifNullOrEmpty(testbedConfiguration.getUrnPrefixList(),
        		"Null or empty urn prefix list");
		String firstUrnPrefix = testbedConfiguration.getUrnPrefixList().get(0);
        List<SecretReservationKey> filteredKeys = 
			injector.getReservationManager().getFilteredSecretReservationKeys(
					firstUrnPrefix);
        GWT.log("Found (" + filteredKeys.size() +") " +
        		"reservation keys for testbed :" + testbedName);
		
		// setup RPC callback
		AsyncCallback<List<ReservationDetails>> callback = new 
			AsyncCallback<List<ReservationDetails>>(){

				@Override
				public void onFailure(Throwable caught) {
					if(caught instanceof ReservationException) {
						GWT.log(caught.getMessage());
						MessageBox.error("Reservation Service", caught.getMessage(), caught, null);
					}
				}

				@Override
				public void onSuccess(List<ReservationDetails> results) {
						view.renderUserReservations();
				}
			
		};
		
		// make the RPC
		String rsEndpointUrl = testbedConfiguration.getRsEndpointUrl(); 
		service.getUserReservations(filteredKeys, rsEndpointUrl, callback);
	}
}
