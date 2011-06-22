/**
 * Copyright (C) 2011 Universität zu Lübeck, Institut für Telematik (ITM),
 *                             Research Academic Computer Technology Institute (RACTI)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.wisebed.wiseui.client.experimentation.presenter;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import eu.wisebed.wiseui.api.ReservationServiceAsync;
import eu.wisebed.wiseui.client.WiseUiGinjector;
import eu.wisebed.wiseui.client.experimentation.event.RefreshUserExperimentsEvent;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentationView;
import eu.wisebed.wiseui.client.main.WiseUiPlace;
import eu.wisebed.wiseui.client.testbedlist.event.TestbedSelectedEvent;
import eu.wisebed.wiseui.client.testbedselection.event.ThrowableEvent;
import eu.wisebed.wiseui.client.util.EventBusManager;
import eu.wisebed.wiseui.shared.common.Checks;
import eu.wisebed.wiseui.shared.dto.ConfidentialReservationData;
import eu.wisebed.wiseui.shared.dto.SecretAuthenticationKey;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;
import eu.wisebed.wiseui.shared.exception.ReservationException;
import eu.wisebed.wiseui.widgets.messagebox.MessageBox;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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
                                    final EventBus eventBus) {

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

    public void bind() {
        eventBus.addHandler(PlaceChangeEvent.TYPE, this);
        eventBus.addHandler(TestbedSelectedEvent.TYPE, this);
        eventBus.addHandler(ThrowableEvent.TYPE, this);
        eventBus.addHandler(RefreshUserExperimentsEvent.TYPE, this);
    }

    @Override
    public void onTestbedSelected(final TestbedSelectedEvent event) {
        testbedConfiguration = event.getConfiguration();

        // check the configuration for errors
        try {
            Checks.ifNullOrEmpty(testbedConfiguration.getUrnPrefixList(),
                    "Null or empty URN prefix list for selected testbed");
        } catch (RuntimeException cause) {
            MessageBox.error("Error", cause.getMessage(), cause, null);
            return;
        }

        getUserReservations();
    }

    @Override
    public void onRefreshUserExperiments(RefreshUserExperimentsEvent event) {

        if (testbedConfiguration == null) {
            MessageBox.warning("Warning", "No testbed selected!", null);
            return;
        }
        if (testbedConfiguration.getUrnPrefixList() == null || testbedConfiguration.getUrnPrefixList().isEmpty()) {
            MessageBox.warning("Warning", "Selected testbed has no URN prefixes!", null);
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
        final List<String> urnPrefixList = testbedConfiguration.getUrnPrefixList();
        final String rsEndpointUrl = testbedConfiguration.getRsEndpointUrl();
        if (urnPrefixList == null || rsEndpointUrl == null) {
            MessageBox.warning("Warning", "No testbed selected", null);
            return;
        }

        // the secret authentication key & two dates are required
        String urnPrefix = urnPrefixList.get(0);
        final SecretAuthenticationKey key =
                injector.getAuthenticationManager().getMap().get(urnPrefix);
        final Date fromDate = view.getFromDate();
        final Date toDate = view.getToDate();
        if (key == null) {
            MessageBox.warning("Warning", "You must be authenticated to the selected" +
                    " testbed in order to retrieve your reservations", null);
            return;
        }
        if (fromDate == null) {
            MessageBox.warning("Warning", "You must specify the starting date", null);
            return;
        }
        if (toDate == null) {
            MessageBox.warning("Warning", "You must specify the ending date", null);
            return;
        }

        // sanity test for the dates
        if (fromDate.after(toDate)) {
            MessageBox.warning("Warning", "Starting date cannot be after ending date!", null);
            return;
        }

        // loading
        view.getLoadingIndicator().showLoading("Loading reservations");

        // clear experimentation panel
        view.clearExperimentationPanel();

        // setup RPC callback
        AsyncCallback<List<ConfidentialReservationData>> callback = new
                AsyncCallback<List<ConfidentialReservationData>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        if (caught instanceof ReservationException) {
                            MessageBox.error("Reservation Service", caught.getMessage(), caught, null);
                            view.getLoadingIndicator().hideLoading();
                        }
                    }

                    @Override
                    public void onSuccess(List<ConfidentialReservationData> reservationDataList) {

                        // check if results are null
                        if (reservationDataList == null || reservationDataList.isEmpty()) {
                            MessageBox.warning("No reservation found", "There are no reservations for you.", null);
                            view.getLoadingIndicator().hideLoading();
                            return;
                        }

                        List<ConfidentialReservationData> filteredReservations
                                = new ArrayList<ConfidentialReservationData>();

                        for (ConfidentialReservationData c : reservationDataList) {
                            final String authenticatedUserName
                                    = injector.getAuthenticationManager()
                                              .getAuthenticatedUserName(testbedConfiguration);
                            if ((c.getData() == null) || (c.getData().isEmpty())) {
                                continue;
                            }
                            final String userName = c.getData().get(0).getUsername();
                            final String urnPrefix = c.getData().get(0).getUrnPrefix();
                            if (authenticatedUserName.equals(userName)) {
                                filteredReservations.add(c);
                            }
                        }

                        // initialize presenter and add it to the list also add the respected view in the container
                        for (ConfidentialReservationData data : filteredReservations) {

                            final String key = data.getData().get(0).getSecretReservationKey();

                            // check if experiment is currently an active experiment
                            ExperimentPresenter experiment =
                                    injector.getExperimentationManager().getExperimentFromActiveList(key);

                            if (experiment == null) {
                                // experiment is not in the active list create an instance and print it
                                experiment = injector.getExperimentPresenter();
                                experiment.setupExperimentPresenter(data,
                                        testbedConfiguration.getSessionmanagementEndpointUrl());

                            }
                            view.addExperimentPanel(experiment.getView());
                        }

                        // stop loading
                        view.getLoadingIndicator().hideLoading();
                    }
                };

        // make the RPC
        List<SecretAuthenticationKey> secretAuthenticationKeys = new ArrayList<SecretAuthenticationKey>();
        secretAuthenticationKeys.add(key);
        service.getConfidentialReservations(rsEndpointUrl, secretAuthenticationKeys, fromDate, toDate, callback);
    }
}