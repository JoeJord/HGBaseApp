package at.hagru.hgbase.android.awt;

import android.graphics.Point;
import android.graphics.Rect;

/**
 * Implements the necessary functionality of a reactangle. Source code taken from Sun/Oracle Java.
 * 
 * @author hagru
 */
public class Rectangle {

    /**
     * The X coordinate of the upper-left corner of the <code>Rectangle</code>.
     */
    public int x;

    /**
     * The Y coordinate of the upper-left corner of the <code>Rectangle</code>.
     */
    public int y;

    /**
     * The width of the <code>Rectangle</code>.
     */
    public int width;

    /**
     * The height of the <code>Rectangle</code>.
     */
    public int height;

    /**
     * Constructs a new <code>Rectangle</code> whose upper-left corner is at (0,&nbsp;0) in the coordinate
     * space, and whose width and height are both zero.
     */
    public Rectangle() {
        this(0, 0, 0, 0);
    }

    /**
     * Constructs a new <code>Rectangle</code>, initialized to match the values of the specified
     * <code>Rectangle</code>.
     * 
     * @param r the <code>Rectangle</code> from which to copy initial values to a newly constructed
     *            <code>Rectangle</code>
     */
    public Rectangle(Rectangle r) {
        this(r.x, r.y, r.width, r.height);
    }

    /**
     * Constructs a new <code>Rectangle</code> whose upper-left corner is specified as {@code (x,y)} and whose
     * width and height are specified by the arguments of the same name.
     * 
     * @param x the specified X coordinate
     * @param y the specified Y coordinate
     * @param width the width of the <code>Rectangle</code>
     * @param height the height of the <code>Rectangle</code>
     */
    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Constructs a new <code>Rectangle</code> whose upper-left corner is at (0,&nbsp;0) in the coordinate
     * space, and whose width and height are specified by the arguments of the same name.
     * 
     * @param width the width of the <code>Rectangle</code>
     * @param height the height of the <code>Rectangle</code>
     */
    public Rectangle(int width, int height) {
        this(0, 0, width, height);
    }

    /**
     * Constructs a new <code>Rectangle</code> whose upper-left corner is specified by the {@link Point}
     * argument, and whose width and height are specified by the {@link Dimension} argument.
     * 
     * @param p a <code>Point</code> that is the upper-left corner of the <code>Rectangle</code>
     * @param d a <code>Dimension</code>, representing the width and height of the <code>Rectangle</code>
     */
    public Rectangle(Point p, Dimension d) {
        this(p.x, p.y, d.width, d.height);
    }

    /**
     * Constructs a new <code>Rectangle</code> whose upper-left corner is the specified <code>Point</code>,
     * and whose width and height are both zero.
     * 
     * @param p a <code>Point</code> that is the top left corner of the <code>Rectangle</code>
     */
    public Rectangle(Point p) {
        this(p.x, p.y, 0, 0);
    }

    /**
     * Constructs a new <code>Rectangle</code> whose top left corner is (0,&nbsp;0) and whose width and height
     * are specified by the <code>Dimension</code> argument.
     * 
     * @param d a <code>Dimension</code>, specifying width and height
     */
    public Rectangle(Dimension d) {
        this(0, 0, d.width, d.height);
    }

    /**
     * Constructs a new {@code Rectangle}, initialized to match the values of the specified {@code Rect}.
     * 
     * @param rect The {@code Rect} from which to copy initial values to a newly constructed {@code Rectangle}.
     * @see Rect
     */
    public Rectangle(Rect rect) {
	this(rect.left, rect.top, rect.width(), rect.height());
    }

    /**
     * Create a new rectangle with the specified coordinates. Note: no range checking is performed, so the caller must ensure that left <= right and top <= bottom.
     * 
     * @param upperLeft The upper left corner.
     * @param lowerRight The lower right corner.
     */
    public Rectangle(Point upperLeft, Point lowerRight) {
	this(upperLeft.x, upperLeft.y, lowerRight.x - upperLeft.x, lowerRight.y - upperLeft.y);
    }

    /**
     * Returns the X coordinate of the bounding <code>Rectangle</code> in <code>double</code> precision.
     * 
     * @return the X coordinate of the bounding <code>Rectangle</code>.
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the Y coordinate of the bounding <code>Rectangle</code> in <code>double</code> precision.
     * 
     * @return the Y coordinate of the bounding <code>Rectangle</code>.
     */
    public double getY() {
        return y;
    }

    /**
     * Returns the width of the bounding <code>Rectangle</code> in <code>double</code> precision.
     * 
     * @return the width of the bounding <code>Rectangle</code>.
     */
    public double getWidth() {
        return width;
    }

    /**
     * Returns the height of the bounding <code>Rectangle</code> in <code>double</code> precision.
     * 
     * @return the height of the bounding <code>Rectangle</code>.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Returns the X coordinate of the lower-right corner of this {@code Rectangle}.
     * 
     * @return The X coordinate of the lower-right corner of this {@code Rectangle}.
     */
    public int getRight() {
	return x + width;
    }

    /**
     * Returns the Y coordinate of the lower-right corner of this {@code Rectangle}.
     * 
     * @return The Y coordinate of the lower-right corner of this {@code Rectangle}.
     */
    public int getBottom() {
	return y + height;
    }

    /**
     * Gets the bounding <code>Rectangle</code> of this <code>Rectangle</code>.
     * <p>
     * This method is included for completeness, to parallel the <code>getBounds</code> method of
     * {@link Component}.
     * 
     * @return a new <code>Rectangle</code>, equal to the bounding <code>Rectangle</code> for this
     *         <code>Rectangle</code>.
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    /**
     * Returns the location of this <code>Rectangle</code>.
     * <p>
     * This method is included for completeness, to parallel the <code>getLocation</code> method of
     * <code>Component</code>.
     * 
     * @return the <code>Point</code> that is the upper-left corner of this <code>Rectangle</code>.
     */
    public Point getLocation() {
        return new Point(x, y);
    }

    /**
     * Returns the location of the lower-right corner of this {@code Rectangle}.
     * 
     * @return The location of the lower-right corner of this {@code Rectangle}.
     */
    public Point getLowerRight() {
	return new Point(getRight(), getBottom());
    }

    /**
     * Gets the size of this <code>Rectangle</code>, represented by the returned <code>Dimension</code>.
     * <p>
     * This method is included for completeness, to parallel the <code>getSize</code> method of
     * <code>Component</code>.
     * 
     * @return a <code>Dimension</code>, representing the size of this <code>Rectangle</code>.
     */
    public Dimension getSize() {
        return new Dimension(width, height);
    }

    /**
     * Checks whether or not this <code>Rectangle</code> contains the specified <code>Point</code>.
     * 
     * @param p the <code>Point</code> to test
     * @return <code>true</code> if the specified <code>Point</code> is inside this <code>Rectangle</code>;
     *         <code>false</code> otherwise.
     */
    public boolean contains(Point p) {
        return contains(p.x, p.y);
    }

    /**
     * Checks whether or not this <code>Rectangle</code> contains the point at the specified location
     * {@code (x,y)}.
     *
     * @param X the specified X coordinate
     * @param Y the specified Y coordinate
     * @return <code>true</code> if the point {@code (x,y)} is inside this <code>Rectangle</code>;
     *         <code>false</code> otherwise.
     */
    public boolean contains(int X, int Y) {
        int w = this.width;
        int h = this.height;
        if ((w | h) < 0) {
            // At least one of the dimensions is negative...
            return false;
        }
        // Note: if either dimension is zero, tests below must return false...
        int x = this.x;
        int y = this.y;
        if (X < x || Y < y) {
            return false;
        }
        w += x;
        h += y;
        // overflow || intersect
        return ((w < x || w > X) && (h < y || h > Y));
    }

    public boolean isEmpty() {
        return (width <= 0) || (height <= 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Rectangle) {
            Rectangle r = (Rectangle) obj;
            return ((x == r.x) && (y == r.y) && (width == r.width) && (height == r.height));
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return getClass().getName() + "[x=" + x + ",y=" + y + ",width=" + width + ",height=" + height + "]";
    }

    /**
     * Converts this rectangle to a {@link Rect}.
     * 
     * @return The converted {@link Rect}.
     */
    public Rect toRect() {
	return new Rect(this.x, this.y, this.x + this.width, this.y + this.height);
    }

    /**
     * Returns {@code true} if this rectangle intersects the specified rectangle.
     * 
     * @param other The other rectangle being tested for intersection.
     * @return {@code true} if this rectangle intersects the specified rectangle.
     */
    public boolean intersects(Rectangle other) {
	return Rect.intersects(this.toRect(), other.toRect());
    }

    /**
     * Checks if the specified rectangle intersects with this rectangle.<br>
     * If so, the intersection rectangle will be returned.<br>
     * If there is no intersection, then {@code null} will be returned.
     * 
     * @param other The other rectangle being tested for intersection.
     * @return The intersection rectangle or {@code null} if there is no intersection.
     */
    public Rectangle getIntersection(Rectangle other) {
	Rect intersection = new Rect();
	if (intersection.setIntersect(this.toRect(), other.toRect())) {
	    return new Rectangle(intersection);
	} else {
	    return null;
	}
    }

    /**
     * Moves the location of this rectangle by the specified values.
     * 
     * @param diffX The difference in the X coordinate.
     * @param diffY The difference in the Y coordinate.
     */
    public void move(int diffX, int diffY) {
	x += diffX;
	y += diffY;
    }
}
