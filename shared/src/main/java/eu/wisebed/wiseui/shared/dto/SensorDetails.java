/**
 * Copyright (C) 2011 Universität zu Lübeck, Institut für Telematik (ITM), Research Academic Computer
 *                             Technology Institute (RACTI)
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

public class SensorDetails implements Serializable{


	private static final long serialVersionUID = -5976952750575967397L;
	
	private int sensorid;
	private String urn;
	private String[] position;
	private String type;
	private String description;

	public SensorDetails(){}
	
	public SensorDetails(String urn, String[] position, String type, 
			String description){
		this.urn = urn;
		this.position = position;
		this.type = type;
		this.description = description;
	}
	
	public void setSensorid(int sensorid){
		this.sensorid = sensorid;
	}
	
	public void setUrn(String urn){
		this.urn = urn;
	}
	
	public void setPosition(String[] position){
		this.position = position;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public int getSensorid(){
		return this.sensorid;
	}
	
	public String getUrn(){
		return this.urn;
	}
	
	public String[] getPosition(){
		return this.position;
	}
	
	public String getType(){
		return this.type;
	}
	
	public String getDescription(){
		return this.description;
	}
}
