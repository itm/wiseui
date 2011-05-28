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


import java.util.Date;
import java.util.List;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Timer;

import com.google.inject.Inject;

import eu.wisebed.wiseui.api.ExperimentationServiceAsync;
import eu.wisebed.wiseui.client.WiseUiGinjector;
import eu.wisebed.wiseui.client.experimentation.event.ReservationTimeStartedEvent;
import eu.wisebed.wiseui.client.experimentation.event.ReservationTimeEndedEvent;
import eu.wisebed.wiseui.client.experimentation.util.StringTimer;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentView;
import eu.wisebed.wiseui.client.util.EventBusManager;
import eu.wisebed.wiseui.shared.common.Checks;
import eu.wisebed.wiseui.shared.dto.ConfidentialReservationData;
import eu.wisebed.wiseui.widgets.messagebox.MessageBox;

public class ExperimentPresenter implements ExperimentView.Presenter,
ReservationTimeStartedEvent.Handler,ReservationTimeEndedEvent.Handler,
PlaceChangeEvent.Handler{
	
	public enum ExperimentStatus {
		PENDING			("Pending"),
		READY			("Ready"),
		RUNNING			("Running"),
		CANCELED		("Reservation was cancelled"),
		TERMINATED		("Terminated by user"),
		TIMEDOUT    	("Reservation time out");

		private String text;
		ExperimentStatus(String text) {
			this.text = text;
		}

		public String getStatusText() {
			return text;
		}
	}

	@SuppressWarnings("unused")
	private final WiseUiGinjector injector;
	private ExperimentView view;
	@SuppressWarnings("unused")
	private ExperimentationServiceAsync service;
	private EventBusManager eventBus;
	private String key;
	private String username;
	private Date fromDate;
	private Date toDate;
	private List<String> nodeUrns;
	private String experimentTiming;
	private Timer reservationStartTimer;
	private Timer reservationStopTimer;
	private ExperimentStatus status;
	
    @Inject
    public ExperimentPresenter(final WiseUiGinjector injector,
    		final ExperimentationServiceAsync service,
    		final ExperimentView view,			
    		final PlaceController placeController,
			final EventBus eventBus) {
    	
    	this.injector = injector;
    	this.view = view;
    	this.view.setPresenter(this);
        this.service = service;
        this.eventBus = new EventBusManager(eventBus);
        bind();
    } 


    public ExperimentView getView() {
    	return view;
    }
        
    public void setupExperimentPresenter(final ConfidentialReservationData data) {
    	setExperimentData(data);
    	initView();
    	initStartTimer();
    }
    
    public void bind() {
		eventBus.addHandler(ReservationTimeStartedEvent.TYPE, this);
		eventBus.addHandler(ReservationTimeEndedEvent.TYPE, this);
		eventBus.addHandler(PlaceChangeEvent.TYPE, this);
    }

	
    @Override
	public void onReservationTimeStarted(ReservationTimeStartedEvent event) {
    	if(event.getSource() == this ){
    		status = ExperimentStatus.READY;
    		view.setStatus(status.getStatusText());
    		initStopTimer();
    	}
	}
    
	@Override
	public void onReservationTimeEnded(ReservationTimeEndedEvent event) {
    	if(event.getSource() == this ){
    		status = ExperimentStatus.TIMEDOUT;
    		view.setStatus(status.getStatusText());
    		view.setExperimentTiming("-");
    	}
    }
	
	@Override
	public void onPlaceChange(PlaceChangeEvent event) {
		eventBus.removeAll();
	}
    
	private void setExperimentData(final ConfidentialReservationData data) {
    	
		fromDate = data.getFrom();
    	toDate = data.getTo();
    	nodeUrns = data.getNodeURNs();
    	key = data.getData().get(0).getSecretReservationKey();
    	username = data.getData().get(0).getUsername();
    	experimentTiming = "-";
        status = ExperimentStatus.PENDING;
    }
	
    @SuppressWarnings("deprecation")
    private void initView() {
    	try{
    		Checks.ifNull(key, "Experiment key is null");
    		Checks.ifNull(fromDate, "Experiment start date is null");
    		Checks.ifNull(toDate, "Experiment start date is null");
    	}catch(RuntimeException cause){
    		MessageBox.error("Error",cause.getMessage(),cause,null);
    	}
    	
    	// initialize view
    	view.setSecretReservationKey(key);
    	view.setUsername(username);
    	view.setStartDate(fromDate.toGMTString());
    	view.setStopDate(toDate.toGMTString());
    	view.setExperimentTiming(experimentTiming);
    	view.setStatus(status.getStatusText());
    	view.setNodeUrns(nodeUrns);
    }
    
    private void initStartTimer() {
    	
    	// source is this presenter
    	final ExperimentPresenter source = this;
    	
    	// reservation start timer counts till the reservation starts
    	reservationStartTimer = new Timer() {
			@Override
			public void run() {
				long diffInMillis = 
					fromDate.getTime() - (new Date()).getTime();
				if(diffInMillis <= 0) {
					this.cancel();
					eventBus.fireEventFromSource(new ReservationTimeStartedEvent(),source);
				}else{
					experimentTiming = "Starting in " + StringTimer.elapsedTimeToString(diffInMillis);
					view.setExperimentTiming(experimentTiming);
				}
			}
		};
        	
    	// start reservation start timer
		reservationStartTimer.scheduleRepeating(1000);
    }
    
    private void initStopTimer() {
    	
    	// source is this presenter
    	final ExperimentPresenter source = this;
    	
    	// reservation stop timer counts till the reservation ends
    	reservationStopTimer = new Timer() {
    		@Override
    		public void run() {
    			long diffInMillis = 
    				toDate.getTime() - (new Date()).getTime();
    			if(diffInMillis <= 0) {
    				this.cancel();
					eventBus.fireEventFromSource(new ReservationTimeEndedEvent(),source);
    			}else{
    				experimentTiming = "Finishing in " +
    						StringTimer.elapsedTimeToString(diffInMillis);
    				view.setExperimentTiming(experimentTiming);
    			}
    		}
    	};
    	
    	// start reservation stop timer
    	reservationStopTimer.scheduleRepeating(1000);
    }
}