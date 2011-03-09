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
}
