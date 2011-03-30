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
import eu.wisebed.wiseui.shared.ExperimentMessage.ExperimentMessageType;
import eu.wisebed.wiseui.shared.SensorDetails;
import eu.wisebed.wiseui.shared.exception.ExperimentationException;


public class ExperimentPresenter implements Presenter {

	// ENUMS ( want to GINject them)
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
	
	// button Callback interface want to GINject it to!
    public interface Callback { // TODO GINject this callback
    	void onButtonClicked(final Button button);
    }
        
    private final ExperimentView view;
    private Callback callback;
	private int reservationID;
	private Date startDate;
	private Date stopDate;
	private Set<SensorDetails> sensors;
	private List<String> urns;
	private Timer reservationStartTimer = null;	// TODO GINject those two timers
	private Timer reservationStopTimer = null;		// TODO GINject all those fuc..
	private Timer messageCollectionTimer = null;
	private ExperimentStatus status;
	private String imageFileName;
	private String urnPrefix;
	private final ExperimentationServiceAsync service; // TODO GINject service instead of GWT.create

    @Inject
    public ExperimentPresenter(final ExperimentView view) {
    	
    	// init view
        this.view = view;
		this.view.setPresenter(this);
    	
        // init service
        service = GWT.create(ExperimentationService.class);
    }
    
    public void initialize(final int reservationID,
    		final Date startDate,final Date stopDate,final Set<SensorDetails> sensors,
    		final String imageFileName,final String urnPrefix,
    		final Callback callback) {
    	
    	// init attributes
    	this.reservationID = reservationID;
    	this.startDate = startDate;
    	this.stopDate = stopDate;
    	this.sensors = sensors;
		this.urns = new ArrayList<String>();
		for(SensorDetails s : sensors) {
			urns.add(s.getUrn());
		}
    	this.imageFileName = imageFileName;
    	this.urnPrefix = urnPrefix;
    	this.callback = callback;
    	
        // determine experiment state at construction
        determineExperimentState(this.startDate,this.stopDate);

        // setup view
		this.view.setReservationID(new Integer(this.reservationID).toString());
		this.view.setStartDate(this.startDate.toLocaleString());
		this.view.setStopDate(this.stopDate.toLocaleString());
		this.view.setImageFilename(this.imageFileName);
		this.view.fillNodeTabPanel(urns);
		this.view.setStatus(this.status.getStatusText());
		this.view.setUrnPrefix(this.urnPrefix);
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
        	view.setReservationTime("-");
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
    	this.reservationStartTimer = timer;
    	
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
    	reservationStopTimer = timer;

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
    	messageCollectionTimer = timer;

    	// start message timer
    	getMessageCollectionTimer().scheduleRepeating(500);
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
    	view.setReservationTime("-");
    	
    	// stop all timers
    	stopAllTimers();
    	
    	// cancel experiment and reservation on server via RPC
    	cancelExperiment();
    }
    
    public void setAsTerminatedExperiment() {
    	// arrange status and view
    	setStatus(ExperimentStatus.TERMINATED);
    	setButtons(Button.SHOWHIDE);
    	
    	// stop all timers
    	stopAllTimers();
    	
    	// stop experiment on server via RPC
    	stopExperimentController();
    }
    
    public void setAsTimedOutExperiment() {
    	// arrange status and view
    	if(status == ExperimentStatus.RUNNING) // in case the experiment was running before canceling
    		setButtons(Button.SHOWHIDE);
    	else
    		setButtons();
    	setStatus(ExperimentStatus.TIMEDOUT);
    	
    	// stop all timers
    	stopAllTimers();
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
	
	private void stopAllTimers(){
		if(reservationStartTimer != null) reservationStartTimer.cancel();
		if(reservationStopTimer != null) reservationStopTimer.cancel();
		if(messageCollectionTimer != null) messageCollectionTimer.cancel();
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
				collectExperimentMessages();
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
				public void onSuccess(final ExperimentMessage result) {
					if(result == null) // case there is no message to deliver 
						return;
					
					GWT.log("ExperimentMessage arrived! reservation ID : " 
							+ result.getReservationID());
					ExperimentMessageType type = 
						result.getExperimentMessageType();
					GWT.log("ExperimentMessageType : " + type.getType());
					switch(type){
					case MESSAGE:
						GWT.log("Source :" + result.getSourceNodeID());
						GWT.log("Level : " + result.getLevel());
						GWT.log("Data : " + result.getData());
						GWT.log("TimeStamp :" + result.getTimeStamp());
						view.printExperimentMessageInNodeTabPanel(
								result.getSourceNodeID(),
								result.getLevel(),
								result.getData(),
								result.getTimeStamp());
						break;
					case NOTIFICATION:
						GWT.log("Notification :" + result.getNotificationText());
						break;
					case STATUS:
						GWT.log("Request status ID :" + result.getRequestStatusID());
						GWT.log("Request status node :" + result.getNodeID());
						GWT.log("Request status msg :" + result.getRequestStatusMsg());
						GWT.log("Request status value:" + result.getValue());
						break;
					}
				}
			
		};
		
		// make the rpc
		service.getNextUndeliveredMessage(reservationID,callback);
	}
	
	private final void stopExperimentController() {
		// setup callback
		final AsyncCallback callback
			= new AsyncCallback(){

				@Override
				public void onFailure(Throwable caught) {
					if(caught instanceof ExperimentationException) {
						GWT.log(caught.getMessage());
					}								
				}

				@Override
				public void onSuccess(Object result) {
					GWT.log("Experiment stopped on the server");
				}
			
		};
		
		// make the rpc
		service.terminateExperiment(reservationID,callback);
		//service.cancelReservation();
	}
	
	private final void cancelExperiment() {
		stopExperimentController();
		//service.cancelReservation() THIS IS AN RPC ;
	}

	public int getReservationID() {
		return reservationID;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getStopDate() {
		return stopDate;
	}

	public Set<SensorDetails> getSensors() {
		return sensors;
	}
	
	public List<String> getUrns(){
		return urns;
	}

	public Timer getReservationStartTimer() {
		return reservationStartTimer;
	}

	public Timer getReservationStopTimer() {
		return reservationStopTimer;
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
	
	public Timer getMessageCollectionTimer() {
		return messageCollectionTimer;
	}
	
	public String getUrnPrefix() {
		return urnPrefix;
	}
}