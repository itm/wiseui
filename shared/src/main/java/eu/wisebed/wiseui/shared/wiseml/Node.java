package eu.wisebed.wiseui.shared.wiseml;

import java.io.Serializable;
import java.util.List;

public class Node implements Serializable {

    private static final long serialVersionUID = -7074948156263912998L;
    private String id;
    private Coordinate position;
    private Boolean gateway;
    private String programDetails;
    private String nodeType;
    private String description;
    private List<Capability> capability;

    public Node() {
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public Coordinate getPosition() {
        return position;
    }

    public void setPosition(final Coordinate position) {
        this.position = position;
    }

    public Boolean isGateway() {
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
        return capability;
    }

    public void setCapability(final List<Capability> capability) {
        this.capability = capability;
    }
}
