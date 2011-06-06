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
package eu.wisebed.wiseui.shared.dto;

import java.io.Serializable;

public class ExperimentMessage implements Serializable {

	private static final long serialVersionUID = -1716821485528278298L;

	private String secretReservationKeyValue;
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
	
	public void setupAsRequestStatus(final String requestStatusID,
			final String nodeID,final String requestStatusMsg,final String value) {
		experimentMessageType = ExperimentMessageType.STATUS;
		this.requestStatusID = requestStatusID;
		this.nodeID = nodeID;
		this.requestStatusMsg = requestStatusMsg;
		this.value = value;
	}

	public void setSecretReservationKeyValue(final String value) {
		this.secretReservationKeyValue = value;
	}

	public String getSecretReservationKeyValue() {
		return secretReservationKeyValue;
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
	
	public String toString() {
		
		String value = new String("");
		
		switch(this.experimentMessageType) {
		case MESSAGE: 
			value = "["+sourceNodeID+"][" +timeStamp+"][" + level +"][" + data +"]";
		case NOTIFICATION:
			value = "[Notification][" + notificationText+"]";
		case STATUS:
			value = "[RequestStatus][" + requestStatusID + "]" +
				   "[status msg][" + requestStatusMsg + "]" + 
				   "[node][" + nodeID + "]" + 
				   "[value][" + value + "]";			
		}
		return value;
	}
}
