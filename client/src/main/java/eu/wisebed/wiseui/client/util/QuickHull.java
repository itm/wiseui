package eu.wisebed.wiseui.client.util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;

import eu.wisebed.wiseui.shared.wiseml.Coordinate;

public class QuickHull {
	final Coordinate[] originalCoordinates;
	int fullSteps = 0;
	final Vector<Coordinate> hullCoordinates = new Vector<Coordinate>();

	public QuickHull(Coordinate[] originalCoordinates) {
		this.originalCoordinates = originalCoordinates;
		qhull(originalCoordinates, 0, 0);
		reorderCoordinates(hullCoordinates);
	}

	/**
	 * Returns original {@link Coordinate} array.
	 * 
	 * @return original {@link Coordinate} array
	 */
	public Coordinate[] getOriginalCoordinates() {
		return originalCoordinates;
	}

	/**
	 * Returns convex hull Coordinates as {@link Vector}.
	 * 
	 * @return convex hull Coordinates as {@link Vector}.
	 */
	public Vector<Coordinate> getHullCoordinatesAsVector() {
		return hullCoordinates;
	}

	/**
	 * Returns convex hull Coordinates as {@link Coordinate}[].
	 * 
	 * @return convex hull Coordinates as {@link Coordinate}[].
	 */
	public Coordinate[] getHullCoordinatesAsArray() {
		if (hullCoordinates == null)
			return null;
		Coordinate[] hulldots = new Coordinate[hullCoordinates.size()];
		for (int i = 0; i < hulldots.length; i++) {
			hulldots[i] = (Coordinate) hullCoordinates.elementAt(i);
		}
		return hulldots;
	}

	void reorderCoordinates(Vector<Coordinate> v) {
		AngleWrapper[] angleWrappers = new AngleWrapper[v.size()];
		double xc = 0;
		double yc = 0;
		for (int i = 0; i < v.size(); i++) {
			Coordinate pt = (Coordinate) v.elementAt(i);
			xc += pt.getX();
			yc += pt.getY();
		}

		xc /= v.size();
		yc /= v.size();

		for (int i = 0; i < angleWrappers.length; i++) {
			angleWrappers[i] = createAngleWrapper((Coordinate) v.elementAt(i),
					xc, yc);
		}
		Arrays.sort(angleWrappers, new AngleComparator());
		v.removeAllElements();
		for (int i = 0; i < angleWrappers.length; i++) {
			v.add(angleWrappers[i].pt);
		}
	}

	void qhull(Coordinate[] dots0, int up, int step) {
		fullSteps++;
		if (dots0 == null || dots0.length < 1 || step > 200)
			return;
		if (dots0.length < 2) {
			addHullCoordinate((Coordinate) dots0[0]);
			return;
		}
		try {
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
			Coordinate leftCoordinate = (Coordinate) dots0[leftIndex];
			Coordinate rightCoordinate = (Coordinate) dots0[rightIndex];
			addHullCoordinate(leftCoordinate);
			addHullCoordinate(rightCoordinate);
			if (dots0.length == 3) {
				int middleCoordinate = -1;
				for (int i = 0; i < dots0.length; i++) {
					if (i == leftIndex || i == rightIndex)
						continue;
					middleCoordinate = i;
					break;
				}
				addHullCoordinate((Coordinate) dots0[middleCoordinate]);
			} else if (dots0.length > 3) {
				Vector<Coordinate> vIn = new Vector<Coordinate>();
				Vector<Coordinate> vOut = new Vector<Coordinate>();
				if (up >= 0) {
					int upIndex = selectCoordinates(dots0, leftCoordinate,
							rightCoordinate, true, vIn);
					if (upIndex >= 0 && vIn.size() > 0) {
						Coordinate upCoordinate = (Coordinate) vIn
								.elementAt(upIndex);
						vOut.removeAllElements();
						selectCoordinates(vIn, leftCoordinate, upCoordinate,
								true, vOut);
						qhull(vOut.toArray(new Coordinate[0]), 1, step + 1);
						vOut.removeAllElements();
						selectCoordinates(vIn, upCoordinate, rightCoordinate, true, vOut);
						qhull(vOut.toArray(new Coordinate[0]), 1, step + 1);
					}
				}
				if (up <= 0) {
					vIn.removeAllElements();
					int downIndex = selectCoordinates(dots0, rightCoordinate,
							leftCoordinate, false, vIn);
					if (downIndex >= 0 && vIn.size() > 0) {
						Coordinate downCoordinate = (Coordinate) vIn
								.elementAt(downIndex);
						vOut.removeAllElements();
						selectCoordinates(vIn, rightCoordinate, downCoordinate,
								false, vOut);
						qhull(vOut.toArray(new Coordinate[0]), -1, step + 1);
						vOut.removeAllElements();
						selectCoordinates(vIn, downCoordinate, leftCoordinate,
								false, vOut);
						qhull(vOut.toArray(new Coordinate[0]), -1, step + 1);
					}
				}
			}
		} catch (Throwable t) {
		}
	}

	void addHullCoordinate(Coordinate pt) {
		if (!hullCoordinates.contains(pt))
			hullCoordinates.add(pt);
	}

	static int selectCoordinates(Object[] pIn, Coordinate pLeft,
			Coordinate pRight, boolean up, Vector<Coordinate> vOut) {
		int retValue = -1;
		if (pIn == null || vOut == null)
			return retValue;
		double k = (pRight.getY() - pLeft.getY())
				/ (pRight.getX() - pLeft.getX());
		double A = -k;
		double B = 1;
		double C = k * pLeft.getX() - pLeft.getY();
		double dup = 0;
		for (int i = 0; i < pIn.length; i++) {
			Coordinate pt = (Coordinate) pIn[i];
			if (pt.equals(pLeft) || pt.equals(pRight))
				continue;
			double px = pt.getX();
			double py = pt.getY();
			double y = pLeft.getY() + k * (px - pLeft.getX());
			if ((!up && y < py) || (up && y > py)) {
				vOut.add(pt);
				double d = (A * px + B * py + C);
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

	static int selectCoordinates(Vector<Coordinate> vIn, Coordinate pLeft,
			Coordinate pRight, boolean up, Vector<Coordinate> vOut) {
		int retValue = -1;
		if (vIn == null || vOut == null)
			return retValue;
		double k = (pRight.getY() - pLeft.getY())
				/ (pRight.getY() - pLeft.getY());
		double A = -k;
		double B = 1;
		double C = k * pLeft.getX() - pLeft.getY();
		double dup = 0;
		for (int i = 0; i < vIn.size(); i++) {
			Coordinate pt = (Coordinate) vIn.elementAt(i);
			if (pt.equals(pLeft) || pt.equals(pRight))
				continue;
			double px = pt.getX();
			double py = pt.getY();
			double y = pLeft.getY() + k * (px - pLeft.getX());
			if ((!up && y < py) || (up && y > py)) {
				vOut.add(pt);
				double d = (A * px + B * py + C);
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

	static AngleWrapper createAngleWrapper(Coordinate pt, double xc, double yc) {
		double angle = Math.atan2(pt.getY() - yc, pt.getX() - xc);
		if (angle < 0)
			angle += 2 * Math.PI;
		return new AngleWrapper(angle, new Coordinate(pt));
	}

	static class AngleComparator implements Comparator<AngleWrapper> {
		public int compare(AngleWrapper obj1, AngleWrapper obj2) {
			AngleWrapper ac1 = (AngleWrapper) obj1;
			AngleWrapper ac2 = (AngleWrapper) obj2;
			return (ac1.angle < ac2.angle) ? -1 : 1;
		}
	}

	static class AngleWrapper implements Comparable<AngleWrapper> {
		double angle;
		Coordinate pt;

		AngleWrapper(double angle, Coordinate pt) {
			this.angle = angle;
			this.pt = pt;
		}

		public int compareTo(AngleWrapper obj) {
			AngleWrapper ac = (AngleWrapper) obj;
			return (ac.angle < angle) ? -1 : 1;
		}
	}

}
