package eu.wisebed.wiseui.shared;

import java.io.Serializable;

public class ExperimentMessage implements Serializable {

	private static final long serialVersionUID = -1716821485528278298L;

	private int reservationID;
	private String sourceUrn;
	private String textMessage;
	private String timeStamp;
	private String level;
	private String statusOfJob;
	
	public ExperimentMessage(){}
	
	public final String getSourceUrn() {
		return sourceUrn;
	}

	public final void setSourceUrn(final String sourceUrn) {
		this.sourceUrn = sourceUrn;
	}

	public final String getTextMessage() {
		return textMessage;
	}

	public final void setTextMessage(final String textMessage) {
		this.textMessage = textMessage;
	}

	public final String getTimeStamp() {
		return timeStamp;
	}

	public final void setTimeStamp(final String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public void setReservationID(int reservationID) {
		this.reservationID = reservationID;
	}

	public int getReservationID() {
		return reservationID;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getLevel() {
		return level;
	}

	public void setStatusOfJob(String statusOfJob) {
		this.statusOfJob = statusOfJob;
	}

	public String getStatusOfJob() {
		return statusOfJob;
	}
}
