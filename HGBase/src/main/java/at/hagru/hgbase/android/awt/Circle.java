package at.hagru.hgbase.android.awt;

import android.graphics.Point;

import androidx.annotation.NonNull;

/**
 * This class represents a circle in two-dimensional space, defined by its center point and radius.
 */
public class Circle {
    /**
     * The center point of the circle.
     */
    private Point center;
    /**
     * The radius of the circle.
     */
    private int radius;

    /**
     * Constructs a new circle with the given center point and radius.
     *
     * @param center The center point of the circle.
     * @param radius The radius of the circle.
     */
    public Circle(Point center, int radius) {
        setCenter(center);
        setRadius(radius);
    }

    /**
     * Returns the center point of the circle.
     *
     * @return The center point of the circle.
     */
    public Point getCenter() {
        return center;
    }

    /**
     * Sets the center point of the circle to the given point.
     *
     * @param center The new center point of the circle.
     */
    public void setCenter(Point center) {
        this.center = center;
    }

    /**
     * Returns the radius of the circle.
     *
     * @return The radius of the circle.
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Sets the radius of the circle to the given value.
     *
     * @param radius The new radius of the circle.
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }

    /**
     * Sets the radius of the circle based on the given diameter.
     *
     * @param diameter The new diameter of the circle.
     */
    public void setDiameter(int diameter) {
        this.radius = diameter / 2;
    }

    /**
     * Returns the diameter of the circle.
     *
     * @return The diameter of the circle.
     */
    public int getDiameter() {
        return radius * 2;
    }

    /**
     * Returns the area of the circle.
     *
     * @return The area of the circle.
     */
    public double getArea() {
        return Math.PI * radius * radius;
    }

    /**
     * Returns the circumference of the circle.
     *
     * @return The circumference of the circle.
     */
    public double getCircumference() {
        return 2 * Math.PI * radius;
    }

    /**
     * Returns {@code true} if the given point is inside the circle, and {@code false} otherwise.
     *
     * @param point The point to test.
     * @return {@code true} if the given point is inside the circle, and {@code false} otherwise.
     */
    public boolean contains(@NonNull Point point) {
        double distance = Math.sqrt(Math.pow((point.x - center.x), 2) + Math.pow((point.y - center.y), 2));
        return distance <= radius;
    }

    /**
     * Converts the circle to a regular polygon with the given number of sides.
     *
     * @param numSides the number of sides of the polygon
     * @return a new Polygon object representing the regular polygon
     */
    public Polygon toPolygon(int numSides) {
        double angleIncrement = 2 * Math.PI / numSides;
        int[] xPoints = new int[numSides];
        int[] yPoints = new int[numSides];
        for (int i = 0; i < numSides; i++) {
            double angle = i * angleIncrement;
            int x = (int) Math.round(center.x + radius * Math.cos(angle));
            int y = (int) Math.round(center.y + radius * Math.sin(angle));
            xPoints[i] = x;
            yPoints[i] = y;
        }
        return new Polygon(xPoints, yPoints, numSides);
    }
}
