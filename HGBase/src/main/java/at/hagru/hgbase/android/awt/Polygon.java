package at.hagru.hgbase.android.awt;

/**
 * Implements the necessary functionality of a polygon. Source code taken from Sun/Oracle Java.
 * 
 * @author hagru
 */

import android.graphics.Point;

import java.util.Arrays;

import at.hagru.hgbase.lib.HGBaseTools;

public class Polygon {

    /**
     * The total number of points. The value of <code>npoints</code> represents the number of valid points in
     * this <code>Polygon</code> and might be less than the number of elements in {@link #xpoints xpoints} or
     * {@link #ypoints ypoints}. This value can be NULL.
     */
    public int npoints;

    /**
     * The array of X coordinates. The number of elements in this array might be more than the number of X
     * coordinates in this <code>Polygon</code>. The extra elements allow new points to be added to this
     * <code>Polygon</code> without re-creating this array. The value of {@link #npoints npoints} is equal to
     * the number of valid points in this <code>Polygon</code>.
     */
    public int[] xpoints;

    /**
     * The array of Y coordinates. The number of elements in this array might be more than the number of Y
     * coordinates in this <code>Polygon</code>. The extra elements allow new points to be added to this
     * <code>Polygon</code> without re-creating this array. The value of <code>npoints</code> is equal to the
     * number of valid points in this <code>Polygon</code>.
     */
    public int[] ypoints;

    /**
     * The bounds of this {@code Polygon}. This value can be null.
     */
    protected Rectangle bounds;

    /*
     * Default length for xpoints and ypoints.
     */
    private static final int MIN_LENGTH = 4;

    /**
     * Creates an empty polygon.
     */
    public Polygon() {
        xpoints = new int[MIN_LENGTH];
        ypoints = new int[MIN_LENGTH];
    }

    /**
     * Constructs and initializes a <code>Polygon</code> from the specified parameters.
     * 
     * @param xpoints an array of X coordinates
     * @param ypoints an array of Y coordinates
     * @param npoints the total number of points in the <code>Polygon</code>
     * @throws NegativeArraySizeException if the value of <code>npoints</code> is negative.
     * @throws IndexOutOfBoundsException if <code>npoints</code> is greater than the length of
     *             <code>xpoints</code> or the length of <code>ypoints</code>.
     * @throws NullPointerException if <code>xpoints</code> or <code>ypoints</code> is <code>null</code>.
     */
    public Polygon(int[] xpoints, int[] ypoints, int npoints) {
        if (npoints > xpoints.length || npoints > ypoints.length) {
            throw new IndexOutOfBoundsException("npoints > xpoints.length || " + "npoints > ypoints.length");
        }
        if (npoints < 0) {
            throw new NegativeArraySizeException("npoints < 0");
        }
        this.npoints = npoints;
        this.xpoints = Arrays.copyOf(xpoints, npoints);
        this.ypoints = Arrays.copyOf(ypoints, npoints);
    }

    /**
     * Calculates the bounding box of the points passed to the constructor. Sets <code>bounds</code> to the
     * result.
     * 
     * @param xpoints array of <i>x</i> coordinates
     * @param ypoints array of <i>y</i> coordinates
     * @param npoints the total number of points
     */
    void calculateBounds(int[] xpoints, int[] ypoints, int npoints) {
        int boundsMinX = Integer.MAX_VALUE;
        int boundsMinY = Integer.MAX_VALUE;
        int boundsMaxX = Integer.MIN_VALUE;
        int boundsMaxY = Integer.MIN_VALUE;

        for (int i = 0; i < npoints; i++) {
            int x = xpoints[i];
            boundsMinX = Math.min(boundsMinX, x);
            boundsMaxX = Math.max(boundsMaxX, x);
            int y = ypoints[i];
            boundsMinY = Math.min(boundsMinY, y);
            boundsMaxY = Math.max(boundsMaxY, y);
        }
        bounds = new Rectangle(boundsMinX, boundsMinY, boundsMaxX - boundsMinX, boundsMaxY - boundsMinY);
    }

    /**
     * Resizes the bounding box to accomodate the specified coordinates.
     * 
     * @param x,&nbsp;y the specified coordinates
     */
    void updateBounds(int x, int y) {
        if (x < bounds.x) {
            bounds.width = bounds.width + (bounds.x - x);
            bounds.x = x;
        } else {
            bounds.width = Math.max(bounds.width, x - bounds.x);
            // bounds.x = bounds.x;
        }

        if (y < bounds.y) {
            bounds.height = bounds.height + (bounds.y - y);
            bounds.y = y;
        } else {
            bounds.height = Math.max(bounds.height, y - bounds.y);
            // bounds.y = bounds.y;
        }
    }

    /**
     * Appends the specified coordinates to this <code>Polygon</code>.
     * <p>
     * If an operation that calculates the bounding box of this <code>Polygon</code> has already been
     * performed, such as <code>getBounds</code> or <code>contains</code>, then this method updates the
     * bounding box.
     * 
     * @param x the specified X coordinate
     * @param y the specified Y coordinate
     */
    public void addPoint(int x, int y) {
        if (npoints >= xpoints.length || npoints >= ypoints.length) {
            int newLength = npoints * 2;
            // Make sure that newLength will be greater than MIN_LENGTH and
            // aligned to the power of 2
            if (newLength < MIN_LENGTH) {
                newLength = MIN_LENGTH;
            } else if ((newLength & (newLength - 1)) != 0) {
                newLength = Integer.highestOneBit(newLength);
            }

            xpoints = Arrays.copyOf(xpoints, newLength);
            ypoints = Arrays.copyOf(ypoints, newLength);
        }
        xpoints[npoints] = x;
        ypoints[npoints] = y;
        npoints++;
        if (bounds != null) {
            updateBounds(x, y);
        }
    }

    /**
     * Gets the bounding box of this <code>Polygon</code>. The bounding box is the smallest {@link Rectangle}
     * whose sides are parallel to the x and y axes of the coordinate space, and can completely contain the
     * <code>Polygon</code>.
     * 
     * @return a <code>Rectangle</code> that defines the bounds of this <code>Polygon</code>.
     */
    public Rectangle getBounds() {
        if (npoints == 0) {
            return new Rectangle();
        }
        if (bounds == null) {
            calculateBounds(xpoints, ypoints, npoints);
        }
        return bounds.getBounds();
    }

    /**
     * Determines whether the specified {@link Point} is inside this <code>Polygon</code>.
     * 
     * @param p the specified <code>Point</code> to be tested
     * @return <code>true</code> if the <code>Polygon</code> contains the <code>Point</code>;
     *         <code>false</code> otherwise.
     */
    public boolean contains(Point p) {
        return contains(p.x, p.y);
    }

    /**
     * Determines whether the specified coordinates are inside this <code>Polygon</code>.
     * <p>
     * 
     * @param x the specified X coordinate to be tested
     * @param y the specified Y coordinate to be tested
     * @return {@code true} if this {@code Polygon} contains the specified coordinates {@code (x,y)};
     *         {@code false} otherwise.
     */
    public boolean contains(int x, int y) {
        if (npoints <= 2 || !getBounds().contains(x, y)) {
            return false;
        }
        int hits = 0;

        int lastx = xpoints[npoints - 1];
        int lasty = ypoints[npoints - 1];
        int curx, cury;

        // Walk the edges of the polygon
        for (int i = 0; i < npoints; lastx = curx, lasty = cury, i++) {
            curx = xpoints[i];
            cury = ypoints[i];

            if (cury == lasty) {
                continue;
            }

            int leftx;
            if (curx < lastx) {
                if (x >= lastx) {
                    continue;
                }
                leftx = curx;
            } else {
                if (x >= curx) {
                    continue;
                }
                leftx = lastx;
            }

            double test1, test2;
            if (cury < lasty) {
                if (y < cury || y >= lasty) {
                    continue;
                }
                if (x < leftx) {
                    hits++;
                    continue;
                }
                test1 = x - curx;
                test2 = y - cury;
            } else {
                if (y < lasty || y >= cury) {
                    continue;
                }
                if (x < leftx) {
                    hits++;
                    continue;
                }
                test1 = x - lastx;
                test2 = y - lasty;
            }

            if (test1 < (test2 / (lasty - cury) * (lastx - curx))) {
                hits++;
            }
        }

        return ((hits & 1) != 0);
    }

    /**
     * Returns {@code true} if the specified rectangle lies completely within this polygon.
     *
     * @param rectangle The rectangle to check.
     * @return {@code true} if the specified rectangle lies completely within this polygon.
     */
    public boolean contains(Rectangle rectangle) {
        if (rectangle == null) {
            return false;
        }
        return contains(rectangle.getUpperLeft()) && contains(rectangle.getUpperRight()) && contains(rectangle.getLowerLeft()) && contains(rectangle.getLowerRight());
    }

    @Override
    public String toString() {
        return getClass().getName() + " x = " + Arrays.asList(HGBaseTools.toIntegerArray(xpoints))
                                    + ", y = " + Arrays.asList(HGBaseTools.toIntegerArray(ypoints));
    }
}
