package eu.wisebed.wiseui.client.experimentation.presenter;


import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
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
import eu.wisebed.wiseui.client.testbedselection.event.ThrowableEvent;
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

	private final WiseUiGinjector injector;
	private ExperimentView view;
	private ExperimentationServiceAsync service;
	private EventBusManager eventBus;
	private String key;
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
    	setStartTimer();
    }
    
    public void bind() {
		eventBus.addHandler(ReservationTimeStartedEvent.TYPE, this);
		eventBus.addHandler(ReservationTimeEndedEvent.TYPE, this);
		eventBus.addHandler(PlaceChangeEvent.TYPE, this);
    }

	
    @Override
	public void onReservationTimeStarted(ReservationTimeStartedEvent event) {
    	GWT.log("onReservationTimeStarted() : presenter with SRK : " + key);
    	GWT.log("onReservationTimeStarted() : Previous status : " + status.getStatusText() );
    	status = ExperimentStatus.READY;
    	view.setStatus(status.getStatusText());
    	GWT.log("onReservationTimeStarted() : Current status : " + status.getStatusText() );
    	setStopTimer();
	}
    
	@Override
	public void onReservationTimeEnded(ReservationTimeEndedEvent event) {
    	GWT.log("onReservationTimeEnded() : presenter with SRK : " + key);
    	GWT.log("onReservationTimeEnded() : Previous status : " + status.getStatusText() );
    	status = ExperimentStatus.TIMEDOUT;
    	view.setStatus(status.getStatusText());
    	GWT.log("onReservationTimeEnded() : Current status" + status.getStatusText() );
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
    	view.setStartDate(fromDate.toLocaleString());
    	view.setStopDate(toDate.toLocaleString());
    	view.setExperimentTiming(experimentTiming);
    	view.setStatus(status.getStatusText());
    	view.setNodeUrns(nodeUrns);
    }
    
    private void setStartTimer() {
    	
    	// reservation start timer counts till the reservation starts
    	reservationStartTimer = new Timer() {
			@Override
			public void run() {
				long diffInMillis = 
					fromDate.getTime() - (new Date()).getTime();
				if(diffInMillis <= 0) {
					this.cancel();
				}else{
					experimentTiming = "Starting in " + StringTimer.elapsedTimeToString(diffInMillis);
					view.setExperimentTiming(experimentTiming);
					eventBus.fireEventFromSource(new ReservationTimeStartedEvent(),this);
				}
			}
		};
        	
    	// start reservation start timer
		reservationStartTimer.scheduleRepeating(1000);
    }
    
    private void setStopTimer() {
    	
    	// reservation stop timer counts till the reservation ends
    	reservationStopTimer = new Timer() {
    		@Override
    		public void run() {
    			long diffInMillis = 
    				toDate.getTime() - (new Date()).getTime();
    			if(diffInMillis <= 0) {
    				this.cancel();
    			}else{
    				experimentTiming = "Finishing in " +
    						StringTimer.elapsedTimeToString(diffInMillis);
    				view.setExperimentTiming(experimentTiming);
					eventBus.fireEventFromSource(new ReservationTimeEndedEvent(),this);
    			}
    		}
    	};
    	
    	// start reservation stop timer
    	reservationStopTimer.scheduleRepeating(1000);
    }
}