package eu.wisebed.wiseui.shared.dto;

import java.io.Serializable;

/**
 * Holds all information about a coordinate for a {@link Node}. Can also be used for GPS positioning.
 *
 * @author Malte Legenhausen
 */
public class Coordinate implements Serializable {

    private static final long serialVersionUID = 4172459795364749105L;
    private Double x;
    private Double y;
    private Double z;
    private Double phi;
    private Double theta;

    public Coordinate() {
    }

    public Coordinate(final Double x, final Double y, final Double z, final Double phi, final Double theta) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.phi = phi;
        this.theta = theta;
    }

    public Coordinate(final Coordinate coordinate) {
        x = coordinate.getX();
        y = coordinate.getY();
        z = coordinate.getZ();
        phi = coordinate.getPhi();
        theta = coordinate.getTheta();
    }

    public Double getX() {
        return x;
    }

    public void setX(final Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(final Double y) {
        this.y = y;
    }

    public Double getZ() {
        return z;
    }

    public void setZ(final Double z) {
        this.z = z;
    }

    public Double getPhi() {
        return phi;
    }

    public void setPhi(final Double phi) {
        this.phi = phi;
    }

    public Double getTheta() {
        return theta;
    }

    public void setTheta(final Double theta) {
        this.theta = theta;
    }

    @Override
    public String toString() {
        String s = "x=" + x
                 + ", y=" + y
                 + ", z=" + z;
        if (phi != null) s += ", phi=" + phi;
        if (theta != null) s += ", theta=" + theta;

        return s;
    }
}
