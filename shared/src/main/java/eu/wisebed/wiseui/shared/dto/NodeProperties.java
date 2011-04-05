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
