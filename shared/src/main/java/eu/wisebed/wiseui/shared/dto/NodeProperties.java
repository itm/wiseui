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
package eu.wisebed.wiseui.shared.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NodeProperties implements Serializable {	

	private static final long serialVersionUID = -519654443952359482L;
	
	private Coordinate position;
	private Boolean gateway;
	private String programDetails;
	private String nodeType;
	private String description;
	private List<Capability> capability;
	
	public NodeProperties() {
	}

	public Coordinate getPosition() {
		return position;
	}

	public void setPosition(final Coordinate position) {
		this.position = position;
	}

	public Boolean getGateway() {
		return gateway;
	}

	public void setGateway(final Boolean gateway) {
		this.gateway = gateway;
	}

	public String getProgramDetails() {
		return programDetails;
	}

	public void setProgramDetails(final String programDetails) {
		this.programDetails = programDetails;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(final String nodeType) {
		this.nodeType = nodeType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public List<Capability> getCapability() {
		if (this.capability == null) {
			this.capability = new ArrayList<Capability>();
		}
		return capability;
	}

	public void setCapability(final List<Capability> capability) {
		this.capability = capability;
	}
}
