package eu.wisebed.wiseui.shared;

import java.io.Serializable;

public class ExperimentMessage implements Serializable {

	private static final long serialVersionUID = -1716821485528278298L;

	private int reservationID;
	private ExperimentMessageType experimentMessageType;
	
	// required by MESSAGE type
	private String sourceNodeID;
	private String level;
	private String data;
	private String timeStamp;
	
	// required by NOTIFICATION type
	private String notificationText;
	
	// required by STATUS type
	private String requestStatusID;
	private String nodeID;
	private String requestStatusMsg;
	private String value;	
	
	public enum ExperimentMessageType{
		MESSAGE ("Message"),
		STATUS ("RequestStatus"),
		NOTIFICATION ("Notification");
		
		private String type;
		
		ExperimentMessageType(final String type){
			this.type = type;
		}
		
		public String getType() {
			return type;
		}
	}
	
	public void setupAsMessage(final String sourceNodeID,final String level,
			final String data,final String timeStamp) {
		experimentMessageType = ExperimentMessageType.MESSAGE;
		this.sourceNodeID = sourceNodeID;
		this.level = level;
		this.data = data;
		this.timeStamp = timeStamp;		
	}
	
	public void setupAsNotification(final String notificationText) {
		experimentMessageType = ExperimentMessageType.NOTIFICATION;
		this.notificationText = notificationText;
	}
	
	public void setupAsNotification(final String requestStatusID,
			final String nodeID,final String requestStatusMsg,final String value) {
		experimentMessageType = ExperimentMessageType.STATUS;
		this.requestStatusID = requestStatusID;
		this.nodeID = nodeID;
		this.requestStatusMsg = requestStatusMsg;
		this.value = value;
	}

	public void setReservationID(final int reservationID) {
		this.reservationID = reservationID;
	}

	public int getReservationID() {
		return reservationID;
	}

	public ExperimentMessageType getExperimentMessageType() {
		return experimentMessageType;
	}

	public void setExperimentMessageType(final ExperimentMessageType experimentMessageType) {
		this.experimentMessageType = experimentMessageType;
	}

	public String getSourceNodeID() {
		return sourceNodeID;
	}

	public void setSourceNodeID(final String sourceNodeID) {
		this.sourceNodeID = sourceNodeID;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(final String level) {
		this.level = level;
	}

	public String getData() {
		return data;
	}

	public void setData(final String data) {
		this.data = data;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(final String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getNotificationText() {
		return notificationText;
	}

	public void setNotificationText(String notificationText) {
		this.notificationText = notificationText;
	}

	public String getRequestStatusID() {
		return requestStatusID;
	}

	public void setRequestStatusID(final String requestStatusID) {
		this.requestStatusID = requestStatusID;
	}

	public String getNodeID() {
		return nodeID;
	}

	public void setNodeID(final String nodeID) {
		this.nodeID = nodeID;
	}

	public String getRequestStatusMsg() {
		return requestStatusMsg;
	}

	public void setRequestStatusMsg(final String requestStatusMsg) {
		this.requestStatusMsg = requestStatusMsg;
	}

	public String getValue() {
		return value;
	}

	public void setValue(final String value) {
		this.value = value;
	}
	
}
