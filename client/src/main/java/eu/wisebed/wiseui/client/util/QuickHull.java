package eu.wisebed.wiseui.client.util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import eu.wisebed.wiseui.shared.wiseml.Coordinate;


/**
 * An implementation of the quick hull algorithm that is used to calculate the convex hull of a amount of points.
 * This code was taken from http://source.concord.org/swing/ and optimized for GWT.
 * 
 * @author Malte Legenhausen
 */
public class QuickHull {
	
	private static final int MAX_STEPS = 200;
	
	private static final int MIN_POINTS = 3;
	
	private final List<Coordinate> hullCoordinates = new Vector<Coordinate>();

	private QuickHull(final List<Coordinate> originalCoordinates) {
		quickHull(originalCoordinates.toArray(new Coordinate[0]), 0, 0);
		reorderCoordinates(hullCoordinates);
	}
	
	public static List<Coordinate> calculate(final List<Coordinate> coordinates) {
		return new QuickHull(coordinates).getHullCoordinates();
	}

	/**
	 * Returns convex hull Coordinates.
	 * 
	 * @return convex hull Coordinates.
	 */
	public List<Coordinate> getHullCoordinates() {
		return hullCoordinates;
	}

	private void reorderCoordinates(final List<Coordinate> v) {
		final AngleWrapper[] angleWrappers = new AngleWrapper[v.size()];
		double xc = 0;
		double yc = 0;
		for (int i = 0; i < v.size(); i++) {
			final Coordinate pt = v.get(i);
			xc += pt.getX();
			yc += pt.getY();
		}

		xc /= v.size();
		yc /= v.size();

		for (int i = 0; i < angleWrappers.length; i++) {
			angleWrappers[i] = createAngleWrapper(v.get(i), xc, yc);
		}
		Arrays.sort(angleWrappers, new AngleComparator());
		v.clear();
		for (int i = 0; i < angleWrappers.length; i++) {
			v.add(angleWrappers[i].pt);
		}
	}

	private void quickHull(final Coordinate[] dots0, final int up, final int step) {
		if (dots0 == null || dots0.length < 1 || step > MAX_STEPS) {
			return;
		}
		if (dots0.length < 2) {
			addHullCoordinate(dots0[0]);
			return;
		}
		int leftIndex = 0;
		int rightIndex = 0;
		for (int i = 1; i < dots0.length; i++) {
			if (dots0[i].getX() < dots0[leftIndex].getX()) {
				leftIndex = i;
			}
			if (dots0[i].getX() > dots0[rightIndex].getX()) {
				rightIndex = i;
			}
		}
		final Coordinate leftCoordinate = dots0[leftIndex];
		final Coordinate rightCoordinate = dots0[rightIndex];
		addHullCoordinate(leftCoordinate);
		addHullCoordinate(rightCoordinate);
		if (dots0.length == MIN_POINTS) {
			int middleCoordinate = -1;
			for (int i = 0; i < dots0.length; i++) {
				if (i == leftIndex || i == rightIndex) {
					continue;
				}
				middleCoordinate = i;
				break;
			}
			addHullCoordinate(dots0[middleCoordinate]);
		} else if (dots0.length > MIN_POINTS) {
			final List<Coordinate> vIn = new Vector<Coordinate>();
			final List<Coordinate> vOut = new Vector<Coordinate>();
			if (up >= 0) {
				final int upIndex = selectCoordinates(dots0, leftCoordinate, rightCoordinate, true, vIn);
				if (upIndex >= 0 && vIn.size() > 0) {
					final Coordinate upCoordinate = vIn.get(upIndex);
					vOut.clear();
					selectCoordinates(vIn, leftCoordinate, upCoordinate, true, vOut);
					quickHull(vOut.toArray(new Coordinate[0]), 1, step + 1);
					vOut.clear();
					selectCoordinates(vIn, upCoordinate, rightCoordinate, true, vOut);
					quickHull(vOut.toArray(new Coordinate[0]), 1, step + 1);
				}
			}
			if (up <= 0) {
				vIn.clear();
				final int downIndex = selectCoordinates(dots0, rightCoordinate, leftCoordinate, false, vIn);
				if (downIndex >= 0 && vIn.size() > 0) {
					final Coordinate downCoordinate = vIn.get(downIndex);
					vOut.clear();
					selectCoordinates(vIn, rightCoordinate, downCoordinate, false, vOut);
					quickHull(vOut.toArray(new Coordinate[0]), -1, step + 1);
					vOut.clear();
					selectCoordinates(vIn, downCoordinate, leftCoordinate, false, vOut);
					quickHull(vOut.toArray(new Coordinate[0]), -1, step + 1);
				}
			}
		}
	}

	private void addHullCoordinate(final Coordinate pt) {
		if (!hullCoordinates.contains(pt)) {
			hullCoordinates.add(pt);
		}
	}

	private static int selectCoordinates(final Coordinate[] pIn, final Coordinate pLeft, final Coordinate pRight, final boolean up, final List<Coordinate> vOut) {
		int retValue = -1;
		if (pIn == null || vOut == null)
			return retValue;
		final double k = (pRight.getY() - pLeft.getY()) / (pRight.getX() - pLeft.getX());
		final double A = -k;
		final double B = 1;
		final double C = k * pLeft.getX() - pLeft.getY();
		double dup = 0;
		for (int i = 0; i < pIn.length; i++) {
			final Coordinate pt = pIn[i];
			if (pt.equals(pLeft) || pt.equals(pRight)) {
				continue;
			}
			final double px = pt.getX();
			final double py = pt.getY();
			final double y = pLeft.getY() + k * (px - pLeft.getX());
			if ((!up && y < py) || (up && y > py)) {
				vOut.add(pt);
				double d = A * px + B * py + C;
				if (d < 0)
					d = -d;
				if (d > dup) {
					dup = d;
					retValue = vOut.size() - 1;
				}
			}
		}
		vOut.add(pLeft);
		vOut.add(pRight);
		return retValue;
	}

	private static int selectCoordinates(final List<Coordinate> vIn, final Coordinate pLeft, final Coordinate pRight, final boolean up, final List<Coordinate> vOut) {
		int retValue = -1;
		if (vIn == null || vOut == null) {
			return retValue;
		}
		final double k = (pRight.getY() - pLeft.getY()) / (pRight.getY() - pLeft.getY());
		final double A = -k;
		final double B = 1;
		final double C = k * pLeft.getX() - pLeft.getY();
		double dup = 0;
		for (int i = 0; i < vIn.size(); i++) {
			final Coordinate pt = vIn.get(i);
			if (pt.equals(pLeft) || pt.equals(pRight)) {
				continue;
			}
			final double px = pt.getX();
			final double py = pt.getY();
			final double y = pLeft.getY() + k * (px - pLeft.getX());
			if ((!up && y < py) || (up && y > py)) {
				vOut.add(pt);
				double d = A * px + B * py + C;
				if (d < 0)
					d = -d;
				if (d > dup) {
					dup = d;
					retValue = vOut.size() - 1;
				}
			}
		}
		vOut.add(pLeft);
		vOut.add(pRight);
		return retValue;
	}

	private static AngleWrapper createAngleWrapper(final Coordinate pt, final double xc, final double yc) {
		double angle = Math.atan2(pt.getY() - yc, pt.getX() - xc);
		if (angle < 0) {
			angle += 2 * Math.PI;
		}
		return new AngleWrapper(angle, new Coordinate(pt));
	}

	private static class AngleComparator implements Comparator<AngleWrapper> {
		public int compare(final AngleWrapper obj1, final AngleWrapper obj2) {
			return (obj1.angle < obj2.angle) ? -1 : 1;
		}
	}

	private static class AngleWrapper implements Comparable<AngleWrapper> {
		private final double angle;
		private final Coordinate pt;

		public AngleWrapper(final double angle, final Coordinate pt) {
			this.angle = angle;
			this.pt = pt;
		}

		public int compareTo(final AngleWrapper obj) {
			return (obj.angle < angle) ? -1 : 1;
		}
	}
}
