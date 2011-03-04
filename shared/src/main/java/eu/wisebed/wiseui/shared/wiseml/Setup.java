package eu.wisebed.wiseui.shared.wiseml;

import java.io.Serializable;
import java.util.List;

public class Setup implements Serializable {

    private static final long serialVersionUID = -298010549586832532L;
    private Coordinate origin;
    private String coordinateType;
    private String description;
    private List<Node> node;

    public Setup() {
    }

    public Coordinate getOrigin() {
        return origin;
    }

    public void setOrigin(final Coordinate origin) {
        this.origin = origin;
    }

    public String getCoordinateType() {
        return coordinateType;
    }

    public void setCoordinateType(final String coordinateType) {
        this.coordinateType = coordinateType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public List<Node> getNode() {
        return node;
    }

    public void setNode(final List<Node> nodes) {
        this.node = nodes;
    }
}
