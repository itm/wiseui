package eu.wisebed.wiseui.shared.wiseml;

import java.io.Serializable;

public class Coordinate implements Serializable {

    private static final long serialVersionUID = 4172459795364749105L;
    private Double x;
    private Double y;
    private Double z;
    private Double phi;
    private Double theta;

    public Coordinate() {
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
        String s = "x=" + x +
                ", y=" + y +
                ", z=" + z;
        if (phi != null) s += ", phi=" + phi;
        if (theta != null) s += ", theta=" + theta;

        return s;
    }
}
