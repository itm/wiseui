package eu.wisebed.wiseui.client.experimentation.presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import eu.wisebed.wiseui.api.ExperimentationService;
import eu.wisebed.wiseui.api.ExperimentationServiceAsync;
import eu.wisebed.wiseui.client.experimentation.util.StringTimer;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentView;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentView.Presenter;
import eu.wisebed.wiseui.shared.ExperimentMessage;
import eu.wisebed.wiseui.shared.SensorDetails;
import eu.wisebed.wiseui.shared.exception.ExperimentationException;


public class ExperimentPresenter implements Presenter {

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
	
	public enum Button {
		START	 ("Start Experiment"),
		STOP	 ("Stop Experiment"),
		SHOWHIDE ("Show/Hide Output"),
		CANCEL	 ("Cancel Reservation");
        private final String value;

        private Button(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Button fromValue(final String value) {
            for (Button button : Button.values()) {
                if (button.getValue().equals(value)) {
                    return button;
                }
            }
            throw new IllegalArgumentException("Unknown Button value: " + value);
        }
	}
	
    public interface Callback { // TODO GINject this callback
    	void onButtonClicked(final Button button);
    }
        
    private ExperimentView view;
    private Callback callback;
	private int reservationID;
	private Date startDate;
	private Date stopDate;
	private Set<SensorDetails> sensors;
	private Timer reservationStartTimer;	// TODO GINject those two timers
	private Timer reservationStopTimer;
	private Timer messageTimer;
	private ExperimentStatus status;
	private String imageFileName;
	private String urnPrefix;
	private ExperimentationServiceAsync service; // TODO GINject service instead of GWT.create

    @Inject
    public ExperimentPresenter(final ExperimentView view) {
        this.view = view;
        view.setPresenter(this);
        
        // init service
        service = GWT.create(ExperimentationService.class);
    }
    
    public void initialize(final int reservationID,
    		final Date startDate,final Date stopDate,final Set<SensorDetails> sensors,
    		final String imageFileName,final String urnPrefix,
    		final Callback callback) {
    	determineExperimentState(startDate,stopDate);
    	setReservationID(reservationID);
    	setStartDate(startDate);
    	setStopDate(stopDate);
    	setSensors(sensors);
    	setImageFileName(imageFileName);
    	setUrnPrefix(urnPrefix);
    	this.callback = callback;
    }
    
    @SuppressWarnings("deprecation")
	private void determineExperimentState(final Date startDate, final Date stopDate) {
    	// check for date validity (start date cannot be after stop date)
    	if(startDate.after(stopDate)) {
    		GWT.log("Invalid Dates involved in experiment (ID : "
    				+ getReservationID() +").");
    		throw new IllegalArgumentException("Invalid start date value ." +
    				"Start date cannot be " +
    				"after stop date of an experiment : " +
    				" startDate:(" + startDate.toLocaleString() +")" +
    				" stopDate:(" + stopDate.toLocaleString() +")");
    	}
    	
    	// check if status is terminated, cancelled or timedout then cancel timers
    	if(status == ExperimentStatus.TERMINATED || 
    	   status == ExperimentStatus.CANCELED   ||
    	   status == ExperimentStatus.TIMEDOUT) {
    		return;
    	}
    	
    	// get the present date
    	Date now = new Date();
    	
    	if(now.before(startDate)) {
    		// pending experiment
    		setAsPendingExperiment();
    		countDownUntilStartDate();
		}
    	else if(now.after(startDate) && now.before(stopDate)) {
    		// ready experiment
    		setAsReadyExperiment();
    		countDownUntilStopDate();
    	}
    	else if(now.after(stopDate)) {
    		// timed out experiment
    		setAsTimedOutExperiment();
    		removeCountDown();
    	}
    }
    
    private void countDownUntilStartDate() {
    	// setup timer
    	Timer timer = new Timer() {
			@Override
			public void run() {
				long diffInMillis = 
					getStartDate().getTime() - (new Date()).getTime();
				if(diffInMillis <= 0) {
					determineExperimentState(getStartDate(),getStopDate());
					this.cancel();
				}else{
					view.setReservationTime(
							"Starting in " +
							StringTimer.elapsedTimeToString(diffInMillis));
				}
			}
		};
    	
		// set as reservation start timer
    	setReservationStartTimer(timer);
    	
    	// start reservation start timer
    	getReservationStartTimer().scheduleRepeating(1000);
    }
    
    private void countDownUntilStopDate() {
    	// setup timer
    	Timer timer = new Timer() {
			@Override
			public void run() {
				long diffInMillis = 
					getStopDate().getTime() - (new Date()).getTime();
				if(diffInMillis <= 0) {
					determineExperimentState(getStartDate(),getStopDate());
					this.cancel();
				}else{
					view.setReservationTime(
							"Finishing in " +
							StringTimer.elapsedTimeToString(diffInMillis));
				}
			}
		};
		// set as reservation stop timer
    	setReservationStopTimer(timer);

    	// start reservation stop timer
    	getReservationStopTimer().scheduleRepeating(1000);
    }
    
    private void collectExperimentMessages(){
    	// setup timer
    	Timer timer = new Timer() {

			@Override
			public void run() {
				getExperimentMessage();	
			}
    	};
    
    	// set as message timer
    	setMessageTimer(timer);
    	// start message timer
    	getMessageTimer().scheduleRepeating(500);
    	
    }
    
    private void removeCountDown() {
    	view.setReservationTime("-");
    }
    
    public void setAsPendingExperiment(){
    	setStatus(ExperimentStatus.PENDING);
    	setButtons(Button.CANCEL);
    }
    
    public void setAsReadyExperiment(){
    	// arrange status and view
    	setStatus(ExperimentStatus.READY);
    	setButtons(Button.START,Button.STOP,Button.CANCEL);
    }

    public void setAsRunningExperiment(){
    	// arrange status and view
    	setStatus(ExperimentStatus.RUNNING);
    	setButtons(Button.SHOWHIDE,Button.STOP,Button.CANCEL);
    	
    	// setup experiment controller on server via RPC
    	setupExperimentController();
    }
    
    public void setAsCancelledExperiment() {
    	// arrange status and view
    	if(status == ExperimentStatus.RUNNING) // in case the experiment was running before canceling
    		setButtons(Button.SHOWHIDE);
    	else
    		setButtons();
    	setStatus(ExperimentStatus.CANCELED);
    	
    	// cancel experiment and reservation on server via RPC
    	cancelExperiment();
    }
    
    public void setAsTerminatedExperiment() {
    	// arrange status and view

    	setStatus(ExperimentStatus.TERMINATED);
    	setButtons(Button.SHOWHIDE);
    	
    	// stop experiment on server via RPC
    	stopExperimentController();
    }
    
    public void setAsTimedOutExperiment() {
    	if(status == ExperimentStatus.RUNNING) // in case the experiment was running before canceling
    		setButtons(Button.SHOWHIDE);
    	else
    		setButtons();
    	setStatus(ExperimentStatus.TIMEDOUT);
    }
    
    private void setButtons(final Button ... buttons) {
        final String[] strings = new String[buttons.length];
        int i = 0;
        for (final Button button : buttons) {
            strings[i++] = button.getValue();
        }
        view.setButtons(strings);
	}
    
	@Override
	public void buttonClicked(String button) {
        if (callback != null) {
            callback.onButtonClicked(Button.fromValue(button));
        }
    }
	
	private final void setupExperimentController() {
		// setup callback
		final AsyncCallback callback = new AsyncCallback() {

			@Override
			public void onFailure(Throwable caught) {
				if(caught instanceof ExperimentationException) {
					GWT.log(caught.getMessage());
				}
			}

			@Override
			public void onSuccess(final Object result) {
				GWT.log("Experiment Controller is setup");
				startExperimentController();
			}
			
		};
		
		// make the rpc
		service.bindAndStartExperimentController(reservationID,callback);
	}
	
	private final void startExperimentController() {
		// setup callback
		final AsyncCallback callback = new AsyncCallback() {

			@Override
			public void onFailure(Throwable caught) {
				if(caught instanceof ExperimentationException) {
					GWT.log(caught.getMessage());
				}				
			}

			@Override
			public void onSuccess(final Object result) {
				GWT.log("Experiment controller is now running");
				//collectExperimentMessages();
			}
		};
		
		// make the rpc
		service.flashExperimentImage(reservationID,callback);
	}
	
	private final void getExperimentMessage() {
		// setup callback
		final AsyncCallback<ExperimentMessage> callback 
			= new AsyncCallback<ExperimentMessage>(){

				@Override
				public void onFailure(Throwable caught) {
					if(caught instanceof ExperimentationException) {
						GWT.log(caught.getMessage());
					}				
				}

				@Override
				public void onSuccess(ExperimentMessage result) {
					GWT.log(result.getTextMessage());
				}
			
		};
		
		// make the rpc
		service.getNextUndeliveredMessage(reservationID,callback);
	}
	
	private final void stopExperimentController() {
		//
	}
	
	private final void cancelExperiment() {
		//
	}

	public void setReservationID(int reservationID) {
		this.reservationID = reservationID;
		view.setReservationID(new Integer(reservationID).toString());
	}


	public int getReservationID() {
		return reservationID;
	}


	@SuppressWarnings("deprecation")
	public void setStartDate(final Date startDate) {
		this.startDate = startDate;
		view.setStartDate(startDate.toLocaleString());
	}


	public Date getStartDate() {
		return startDate;
	}


	@SuppressWarnings("deprecation")
	public void setStopDate(final Date stopDate) {
		this.stopDate = stopDate;
		view.setStopDate(stopDate.toLocaleString());
	}


	public Date getStopDate() {
		return stopDate;
	}


	public void setSensors(final Set<SensorDetails> sensors) {
		// set sensors
		this.sensors = sensors;
		
		// make a list of their urns
		List<String> urns = new ArrayList<String>();
		for(SensorDetails sensor : sensors) {
			urns.add(sensor.getUrn());
		}
		
		// set the list above in the node tab panel
		view.fillNodeTabPanel(urns);
	}


	public Set<SensorDetails> getSensors() {
		return sensors;
	}

	public void setReservationStartTimer(final Timer reservationStartTimer) {
		this.reservationStartTimer = reservationStartTimer;
	}


	public Timer getReservationStartTimer() {
		return reservationStartTimer;
	}


	public void setReservationStopTimer(final Timer reservationStopTimer) {
		this.reservationStopTimer = reservationStopTimer;
	}


	public Timer getReservationStopTimer() {
		return reservationStopTimer;
	}


	public void setImageFileName(final String imageFileName) {
		this.imageFileName = imageFileName;
		view.setImageFilename(imageFileName);
	}


	public String getImageFileName() {
		return imageFileName;
	}


	public void setStatus(final ExperimentStatus status) {
		this.status = status;
		view.setStatus(status.getStatusText());
	}


	public ExperimentStatus getStatus() {
		return status;
	}
	
	public ExperimentView getView() {
		return view;
	}
	
	public Timer getMessageTimer() {
		return messageTimer;
	}

	public void setMessageTimer(final Timer messageTimer) {
		this.messageTimer = messageTimer;
	}

	public void setUrnPrefix(String urnPrefix) {
		this.urnPrefix = urnPrefix;
		view.setUrnPrefix(urnPrefix);
	}

	public String getUrnPrefix() {
		return urnPrefix;
	}
}