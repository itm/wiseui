package eu.wisebed.wiseui.shared;

import java.io.Serializable;

public class ExperimentMessage implements Serializable {

	private static final long serialVersionUID = -1716821485528278298L;

	private int reservationID;
	private String message;
	
//	public enum ExperimentMessageType{
//		MESSAGE ("Message"),
//		STATUS ("RequestStatus"),
//		NOTIFICATION ("Notification");
//		
//		private String type;
//		
//		ExperimentMessageType(final String type){
//			this.type = type;
//		}
//		
//		public String getType() {
//			return type;
//		}
//	}
//	
//	 public class Message{
//		private String source;
//		private String level;
//		private String data;
//		private String timeStamp;
//		
//
//		public void setSource(final String source) {
//			this.source = source;
//		}
//
//		public String getSource() {
//			return source;
//		}
//
//		public void setLevel(final String level) {
//			this.level = level;
//		}
//
//		public String getLevel() {
//			return level;
//		}
//
//		public void setData(final String data) {
//			this.data = data;
//		}
//
//		public String getData() {
//			return data;
//		}
//
//		public void setTimeStamp(String timeStamp) {
//			this.timeStamp = timeStamp;
//		}
//
//		public String getTimeStamp() {
//			return timeStamp;
//		}
//	};
//	
//	public class RequestStatus{
//		private String requestStatusID;
//		private String nodeID;
//		private String msg;
//		private String value;
//		public void setNodeID(String nodeID) {
//			this.nodeID = nodeID;
//		}
//		public String getNodeID() {
//			return nodeID;
//		}
//		public void setMsg(String msg) {
//			this.msg = msg;
//		}
//		public String getMsg() {
//			return msg;
//		}
//		public void setValue(String value) {
//			this.value = value;
//		}
//		public String getValue() {
//			return value;
//		}
//		public void setRequestStatusID(String requestStatusID) {
//			this.requestStatusID = requestStatusID;
//		}
//		public String getRequestStatusID() {
//			return requestStatusID;
//		}
//
//		
//	};
//	
//	public class Notification{
//		private String text;
//				
//		public void setText(final String text){
//			this.text = text;
//		}
//		
//		public String getText(){
//			return text;
//		}
//	};
//	
//	private Message message;
//	private RequestStatus requestStatus;
//	private Notification notification;
//	private ExperimentMessageType experimentMessageType;
//	private int reservationID;
//	
//	
//	public void setMessage(Message message) {
//		this.message = message;
//	}
//
//	public Message getMessage() {
//		return message;
//	}
//
//	public void setRequestStatus(RequestStatus requestStatus) {
//		this.requestStatus = requestStatus;
//	}
//
//	public RequestStatus getRequestStatus() {
//		return requestStatus;
//	}
//
//	public void setNotification(Notification notification) {
//		this.notification = notification;
//	}
//
//	public Notification getNotification() {
//		return notification;
//	}
//
//	public void setExperimentMessageType(final ExperimentMessageType
//			experimentMessageType) {
//		this.experimentMessageType = experimentMessageType;
//		
//		switch(this.experimentMessageType){
//		case MESSAGE:
//			message = new Message(); // TODO ugly don't like it change it later
//			requestStatus = null;
//			notification = null;
//			break;
//		case STATUS:
//			message = null;
//			requestStatus = new RequestStatus();
//			notification = null;
//			break;
//		case NOTIFICATION:
//			message = null;
//			requestStatus = null;
//			notification = new Notification();
//			break;
//	}
//	}
//
//	public ExperimentMessageType getExperimentMessageType() {
//		return experimentMessageType;
//	}
//
	public void setReservationID(final int reservationID) {
		this.reservationID = reservationID;
	}

	public int getReservationID() {
		return reservationID;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
