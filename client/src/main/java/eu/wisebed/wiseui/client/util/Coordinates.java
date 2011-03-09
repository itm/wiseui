package eu.wisebed.wiseui.client.util;

import eu.wisebed.wiseui.shared.wiseml.Coordinate;


/**
 * Utility class for Coordinates.
 * 
 * @author Malte Legenhausen
 */
public class Coordinates {

    public static Coordinate rotate(final Coordinate coordinate, final Double phi) {
    	final Double rad = Math.toRadians(phi);
    	final Double cos = Math.cos(rad);
    	final Double sin = Math.sin(rad);
    	final Double x = coordinate.getX() * cos - coordinate.getY() * sin;
    	final Double y = coordinate.getY() * cos + coordinate.getX() * sin;
    	return new Coordinate(x, y, coordinate.getZ(), coordinate.getPhi(), coordinate.getTheta());
    }
    
    public static Coordinate absolute(final Coordinate origin, final Coordinate coordinate) {
    	final Double y = coordinate.getY() * 0.00001 + origin.getY();
    	final Double x = coordinate.getX() * 0.00001 + origin.getX();
    	return new Coordinate(x, y, coordinate.getZ(), coordinate.getPhi(), coordinate.getTheta());
    }
    
	public static Coordinate difference(Coordinate source, Coordinate vector) {
		return new Coordinate(vector.getX() - source.getX(), vector.getY() - source.getY(), 0.0, 0.0, 0.0);
	}
	
	public static double angle(Coordinate source, Coordinate vector) {
        return Math.atan2(vector.getY(), vector.getX()) - Math.atan2(source.getY(), source.getX());
	}

	public static double location(Coordinate point, Coordinate linePoint1, Coordinate linePoint2) {
		return (linePoint2.getX() - linePoint1.getX())
				* (point.getY() - linePoint1.getY())
				- (point.getX() - linePoint1.getX())
				* (linePoint2.getY() - linePoint1.getY());
	}
}
