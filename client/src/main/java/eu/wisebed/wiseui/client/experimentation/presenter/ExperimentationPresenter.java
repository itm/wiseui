package eu.wisebed.wiseui.client.experimentation.presenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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
import eu.wisebed.wiseui.client.util.EventBusManager;
import eu.wisebed.wiseui.shared.dto.ReservationDetails;
import eu.wisebed.wiseui.shared.dto.SecretReservationKey;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;
import eu.wisebed.wiseui.shared.exception.ExperimentationException;
import eu.wisebed.wiseui.shared.dto.SecretAuthenticationKey;
import eu.wisebed.wiseui.widgets.messagebox.MessageBox;


public class ExperimentationPresenter implements ExperimentationView.Presenter,
	TestbedSelectedEvent.ConfigurationSelectedHandler,
	ThrowableEvent.ThrowableHandler, PlaceChangeEvent.Handler {

	private final WiseUiGinjector injector;
	private ExperimentationView view;
    private WiseUiPlace place;
	private ReservationServiceAsync service;
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
		this.placeController = placeController;
		this.eventBus = new EventBusManager(eventBus);
		bind();
	}

	public void setView(final ExperimentationView view) {
		this.view = view;
	}
	
	public void setPlace(WiseUiPlace place) {
		this.place = place;
	}
	
	public void bind(){
		eventBus.addHandler(PlaceChangeEvent.TYPE, this);
        eventBus.addHandler(TestbedSelectedEvent.TYPE, this);
        eventBus.addHandler(ThrowableEvent.TYPE, this);
	}
	
	@Override
    public void onTestbedSelected(final TestbedSelectedEvent event) {
		getUserReservations();
    }

    @Override
    public void onPlaceChange(final PlaceChangeEvent event) {
    	eventBus.removeAll();
    }

	@SuppressWarnings("deprecation")
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
		
		// retrieve secret reservation keys stored in cookies
        // assuming one urn prefix
		List<SecretReservationKey> filteredKeys = 
			injector.getReservationManager().getFilteredSecretReservationKeys(
					testbedConfiguration.getUrnPrefixList().get(0));
		
		// make the rpc
//		service.getUserReservations(key, callback);
	}
}
