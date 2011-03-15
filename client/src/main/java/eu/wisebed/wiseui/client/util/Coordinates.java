package eu.wisebed.wiseui.client.util;

import eu.wisebed.wiseui.shared.wiseml.Coordinate;


/**
 * Utility class for Coordinates.
 * 
 * @author Malte Legenhausen
 */
public class Coordinates {
	
	public static final double WGS84_A = 6378137.0;
	
	public static final double WGS84_ALPHA = 1.0 / 298.257222101;
	
    public static Coordinate rotate(final Coordinate coordinate, final Double phi) {
    	final Double rad = Math.toRadians(phi);
    	final Double cos = Math.cos(rad);
    	final Double sin = Math.sin(rad);
    	final Double x = coordinate.getX() * cos - coordinate.getY() * sin;
    	final Double y = coordinate.getY() * cos + coordinate.getX() * sin;
    	return new Coordinate(x, y, coordinate.getZ(), coordinate.getPhi(), coordinate.getTheta());
    }
    
    public static Coordinate absolute(final Coordinate origin, final Coordinate coordinate) {
    	final Double y = coordinate.getY() + origin.getY();
    	final Double x = coordinate.getX() + origin.getX();
    	return new Coordinate(x, y, origin.getZ(), origin.getPhi(), origin.getTheta());
    }
    
	public static Coordinate difference(final Coordinate source, final Coordinate vector) {
		return new Coordinate(vector.getX() - source.getX(), vector.getY() - source.getY(), 0.0, 0.0, 0.0);
	}
	
	public static double angle(final Coordinate source, final Coordinate vector) {
        return Math.atan2(vector.getY(), vector.getX()) - Math.atan2(source.getY(), source.getX());
	}

	public static double location(final Coordinate point, final Coordinate linePoint1, final Coordinate linePoint2) {
		return (linePoint2.getX() - linePoint1.getX())
				* (point.getY() - linePoint1.getY())
				- (point.getX() - linePoint1.getX())
				* (linePoint2.getY() - linePoint1.getY());
	}
	
	public static Coordinate xyz2blh(final Coordinate coordinate) {
        final double x = coordinate.getX();
        final double y = coordinate.getY();
        final double z = coordinate.getZ();
        
        final double roh = 180.0 / Math.PI;
        
        final double b = WGS84_A * (1 - WGS84_ALPHA);
        final double c = (WGS84_A * WGS84_A) / b;
     
        final double e0 = (WGS84_A * WGS84_A) - (b * b);
        final double e1 = Math.sqrt(e0 / (WGS84_A * WGS84_A));
        final double e2 = Math.sqrt(e0 / (b * b));
     
        final double p = Math.sqrt((x * x) + (y * y));    
     
        final double Theta = Math.atan((z * WGS84_A) / (p * b));
     
        final double L = Math.atan(y / x) * roh;
        final double B = Math.atan((z + (e2 * e2 * b * Math.pow(Math.sin(Theta), 3))) / (p - (e1 * e1 * WGS84_A * Math.pow(Math.cos(Theta), 3))));
     
        final double eta2 = e2 * e2 * Math.pow(Math.cos(B), 2);
        final double V = Math.sqrt(1 + eta2);
        final double N = c / V;
     
        final double h = (p / Math.cos(B)) - N;
        return new Coordinate(B * roh, L, h, coordinate.getPhi(), coordinate.getTheta());
	}
	
	public static Coordinate blh2xyz(final Coordinate coordinate) {        
        final double roh = Math.PI / 180.0;
        
        final double b = WGS84_A * (1 - WGS84_ALPHA);
        final double c = WGS84_A * WGS84_A / b;
     
        
        final double e2 = Math.sqrt(((WGS84_A * WGS84_A) - (b * b))/ (b * b));    
     
        final double B = coordinate.getX() * roh;
        final double L = coordinate.getY() * roh;
     
        final double eta2 = e2 * e2 * Math.pow(Math.cos(B), 2);
        final double V = Math.sqrt(1 + eta2);
        final double N = c / V;
     
        final double h = coordinate.getZ();
        final double x = (N + h) * Math.cos(B) * Math.cos(L);
        final double y = (N + h) * Math.cos(B) * Math.sin(L);
        final double z = (Math.pow(b / WGS84_A, 2) * N + h) * Math.sin(B);
        return new Coordinate(x, y, z, coordinate.getPhi(), coordinate.getTheta());
	}
}
