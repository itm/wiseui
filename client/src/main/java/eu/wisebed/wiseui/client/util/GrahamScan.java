package eu.wisebed.wiseui.client.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

import eu.wisebed.wiseui.shared.wiseml.Coordinate;


/**
 * 
 * 
 * @author Malte Legenhausen
 */
public class GrahamScan {

	private static final int MINIMUM_POINTS = 3;
	
	/**
	 * Comparator class to compare points by their angle from the positive
	 * x-axis with reference from a given point.
	 * 
	 * @author William Bittle
	 * @version 2.2.0
	 * @since 2.2.0
	 */
	private class PointComparator implements Comparator<Coordinate> {
		/** The positive x-axis */
		private final Coordinate x = new Coordinate(1.0, 0.0, 0.0, 0.0, 0.0);

		/** The reference point for testing polar angles */
		private Coordinate reference;

		/**
		 * Full constructor.
		 * 
		 * @param reference
		 *            the reference point for finding angles
		 */
		public PointComparator(final Coordinate reference) {
			this.reference = reference;
		}

		@Override
		public int compare(final Coordinate o1, final Coordinate o2) {
			// get the vectors from p to the points
			final Coordinate v1 = Coordinates.difference(reference, o1);
			final Coordinate v2 = Coordinates.difference(reference, o2);
			// compare the vector's angles with the x-axis
			return (int) Math.signum(Coordinates.angle(v2, x) - Coordinates.angle(v1, x));
		}
	}
	
	public static List<Coordinate> calculate(final List<Coordinate> coordinate) {
		return new GrahamScan().generate(new ArrayList<Coordinate>(coordinate));
	}

	private List<Coordinate> generate(final List<Coordinate> points) {
		// get the size
		final int size = points.size();

		// check the size
		if (size < MINIMUM_POINTS) {
			return points;
		}

		// find the point of minimum y (choose the point of minimum x if there
		// is a tie)
		Coordinate minY = points.get(0);
		for (final Coordinate p : points) {
			if (p.getY() < minY.getY()) {
				minY = p;
			} else if (p.getY() == minY.getY()) {
				if (p.getX() < minY.getX()) {
					minY = p;
				}
			}
		}

		// create the comparator for the array
		final PointComparator pc = new PointComparator(minY);
		// sort the array by angle
		Collections.sort(points, pc);

		// build the hull
		final Stack<Coordinate> stack = new Stack<Coordinate>();
		stack.push(points.get(0));
		stack.push(points.get(1));
		int i = 2;
		while (i < size) {
			// if the stack size is one then just
			// push the current point onto the stack
			// thereby making a line segment
			if (stack.size() == 1) {
				stack.push(points.get(i));
				i++;
				continue;
			}
			// otherwise get the top two items off the stack
			final Coordinate p1 = stack.get(stack.size() - 2);
			final Coordinate p2 = stack.peek();
			// get the current point
			final Coordinate p3 = points.get(i);
			// test if the current point is to the left of the line
			// created by the top two items in the stack (the last edge
			// on the current convex hull)
			final double location = Coordinates.location(p3, p1, p2);
			if (location > 0.0) {
				// if its to the left, then push the new point on
				// the stack since it maintains convexity
				stack.push(p3);
				i++;
			} else {
				// otherwise the pop the previous point off the stack
				// since this indicates that if we added the current
				// point to the stack we would make a concave section
				stack.pop();
			}
		}
		// return the array
		return stack;
	}
}
