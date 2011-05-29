/*
 * Copyright 2011 Universität zu Lübeck, Institut für Telematik (ITM),
 *              Research Academic Computer Technology Institute (RACTI)
 *
 * ITM and RACTI license this file under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.wisebed.wiseui.client.experimentation.presenter;

import java.util.ArrayList;
import java.util.Date;
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
import eu.wisebed.wiseui.client.experimentation.event.RefreshUserExperimentsEvent;
import eu.wisebed.wiseui.client.util.EventBusManager;
import eu.wisebed.wiseui.shared.common.Checks;
import eu.wisebed.wiseui.shared.dto.ConfidentialReservationData;
import eu.wisebed.wiseui.shared.dto.SecretAuthenticationKey;
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

		getUserReservations();
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

		getUserReservations();
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
	public void getUserReservations() {

		// get testbedconfiguration
		List<String> urnPrefixList = testbedConfiguration.getUrnPrefixList();
		String rsEndpointUrl = testbedConfiguration.getRsEndpointUrl();
		try{
			Checks.ifNull(urnPrefixList, "No testbed selected");
			Checks.ifNull(rsEndpointUrl, "No testbed selected");
		}catch(RuntimeException cause){
			MessageBox.error("Error", cause.getMessage(), cause, null);
			return;
		}


		// the secret authentication key & two dates are required 
		String urnPrefix = urnPrefixList.get(0);
		final SecretAuthenticationKey key =
			injector.getAuthenticationManager().getMap().get(urnPrefix);
		final Date fromDate = view.getFromDate();
		final Date toDate = view.getToDate();
		try{
			Checks.ifNull(key, "You must be authenticated to the selected" +
					" testbed in order to retrieve your reservations");
			Checks.ifNull(fromDate, "You must specify the starting date");
			Checks.ifNull(toDate, "You must specify the ending date");
		}catch(RuntimeException cause){
			MessageBox.error("Error", cause.getMessage(), cause, null);
			return;
		}
		
		// sanity test for the dates
		if(fromDate.after(toDate)){
			MessageBox.error("Error", "Starting date cannot be after ending date", null,null);
			return;
		}
			

		// loading
		view.getLoadingIndicator().showLoading("Loading reservations");

		// clear experimentation panel
		view.clearExperimentationPanel();

		// setup RPC callback
		AsyncCallback<List<ConfidentialReservationData>> callback = new 
		AsyncCallback<List<ConfidentialReservationData>>(){

			@Override
			public void onFailure(Throwable caught) {
				if(caught instanceof ReservationException) {
					GWT.log(caught.getMessage());
					MessageBox.error("Reservation Service", caught.getMessage(), caught, null);
					view.getLoadingIndicator().hideLoading();
				}
			}

			@Override
			public void onSuccess(List<ConfidentialReservationData> dataList) {

				// check if results are null
				try{
					Checks.ifNull(dataList, "Null reservations returned");
				}catch(RuntimeException cause){
					MessageBox.error("Error", cause.getMessage(), cause, null);
					view.getLoadingIndicator().hideLoading();
					return;
				}

				// check if results are empty
				try{
					Checks.ifNullOrEmpty(dataList, "There are no reservations for you");
				}catch(RuntimeException cause){
					MessageBox.info("Reservation Service", cause.getMessage(),null);
					view.getLoadingIndicator().hideLoading();
					return;
				}

				// initialize presenter and add it to the list also add the respected view in the container 
				for(ConfidentialReservationData data : dataList) {
					GWT.log(data.toString());
					ExperimentPresenter experiment =
						injector.getExperimentPresenter();
					experiment.setupExperimentPresenter(data,
							testbedConfiguration.getSessionmanagementEndpointUrl());
					view.addExperimentPanel(experiment.getView());
				}

				// stop loading
				view.getLoadingIndicator().hideLoading();
			}
		};

		// make the RPC
		List<SecretAuthenticationKey> secretAuthenticationKeys 
			= new ArrayList<SecretAuthenticationKey>();
		secretAuthenticationKeys.add(key);
		service.getPrivateReservations(rsEndpointUrl,secretAuthenticationKeys,fromDate,toDate,callback);
	}
}