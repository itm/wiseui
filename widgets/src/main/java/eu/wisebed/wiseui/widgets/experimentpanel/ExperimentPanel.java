package eu.wisebed.wiseui.widgets.experimentpanel;

import java.util.List;
import java.util.Date;

import com.google.gwt.user.client.Timer;
import com.google.inject.Inject;

import eu.wisebed.wiseui.widgets.experimentpanel.ExperimentPanelView.Presenter;

public class ExperimentPanel implements Presenter {

	
	private int reservationID;
	private Date startDate;
	private Date stopDate;
	private List<String> urns;
	private Timer reservationStartTimer;
	private Timer reservationStopTimer;
	private ExperimentStatus status;
	private String imageFileName;

	
	public enum ExperimentStatus{
		PENDING			("Pending"),
		READY			("Ready"),
		RUNNING			("Running"),
		CANCELED		("Reservation was cancelled"),
		TERMINATED		("Terminated by user"),
		TIMEDOUT    	("Reservation time out");
		
		private String text;
		ExperimentStatus(String text){
			this.text = text;
		}
		
		public String getStatusText(){
			return text;
		}
	}
	
	public enum Button{
		START	 ("Start Experiment"),
		STOP	 ("Stop Experiment"),
		SHOWHIDE ("Show Output"),
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
	
    public interface Callback {
    	void onButtonClicked(final Button button);
    }
        
    private final ExperimentPanelView view;    

    @Inject
    public ExperimentPanel(final ExperimentPanelView view) {
        this.view = view;
        view.setPresenter(this);
    }
    
    public void initPanel(final int reservationID,
    		final Date startDate,final Date stopDate,final List<String> urns,
    		final String imageFileName){
    	this.setReservationID(reservationID);
    	this.setStartDate(startDate);
    	this.setStopDate(stopDate);
    	this.setUrns(urns);
    	this.setImageFileName(imageFileName);
    }
    
    public void setAsPendingExperiment(){
    	this.setStatus(ExperimentStatus.PENDING);
    	this.setButtons(Button.CANCEL);
    }
    
    public void setAsReadyExperiment(){
    	this.setStatus(ExperimentStatus.READY);
    	this.setButtons(Button.START,Button.STOP,Button.CANCEL);
    }
    
    public void setAsRunningExperiment(){
    	this.setStatus(ExperimentStatus.RUNNING);
    	this.setButtons(Button.SHOWHIDE,Button.STOP,Button.CANCEL);
    }
    
    public void setAsCancelledExperiment(){
    	this.setStatus(ExperimentStatus.CANCELED);
    	this.setButtons();
    }
    
    public void setAsTimedoutExperiment(){
    	this.setStatus(ExperimentStatus.TIMEDOUT);
    	this.setButtons();
    }
    
    public void setAsTerminatedExperiment(){
    	this.setStatus(ExperimentStatus.TERMINATED);
    	this.setButtons();
    }
    
    private void setButtons(final Button ... buttons) {
        final String[] strings = new String[buttons.length];
        int i = 0;
        for (final Button button : buttons) {
            strings[i++] = button.getValue();
        }
        view.setButtons(strings);
	}

	public void setReservationID(int reservationID) {
		this.reservationID = reservationID;
		view.setReservationID(new Integer(reservationID).toString());
	}


	public int getReservationID() {
		return reservationID;
	}


	@SuppressWarnings("deprecation")
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
		view.setStartDate(startDate.toLocaleString());
	}


	public Date getStartDate() {
		return startDate;
	}


	@SuppressWarnings("deprecation")
	public void setStopDate(Date stopDate) {
		this.stopDate = stopDate;
		view.setStopDate(stopDate.toLocaleString());
	}


	public Date getStopDate() {
		return stopDate;
	}


	public void setUrns(List<String> urns) {
		this.urns = urns;
		view.fillNodeTabPanel(urns);
	}


	public List<String> getUrns() {
		return urns;
	}

	public void setReservationStartTimer(Timer reservationStartTimer) {
		this.reservationStartTimer = reservationStartTimer;
	}


	public Timer getReservationStartTimer() {
		return reservationStartTimer;
	}


	public void setReservationStopTimer(Timer reservationStopTimer) {
		this.reservationStopTimer = reservationStopTimer;
	}


	public Timer getReservationStopTimer() {
		return reservationStopTimer;
	}


	public void setImageFileName(String imageFileName) {
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
	
	public ExperimentPanelView getView(){
		return view;
	}

	@Override
	public void buttonClicked(String button) {
		// TODO Auto-generated method stub		
	}
}