package at.hagru.hgbase.android.awt;

import androidx.annotation.NonNull;

/**
 * An {@code Insets} object is a representation of the borders
 * of a container. It specifies the space that a container must leave
 * at each of its edges. The space can be a border, a blank space, or
 * a title.
 */
public class Insets implements Cloneable {
    /**
     * The inset from the top.
     * This value is added to the Top of the rectangle to yield a new location for the Top.
     */
    public int top;
    /**
     * The inset from the left.
     * This value is added to the Left of the rectangle to yield a new location for the Left edge.
     */
    public int left;
    /**
     * The inset from the bottom.
     * This value is subtracted from the Bottom of the rectangle to yield a new location for the Bottom.
     */
    public int bottom;
    /**
     * The inset from the right.
     * This value is subtracted from the Right of the rectangle to yield a new location for the Right edge.
     */
    public int right;

    /**
     * Creates and initializes a new {@code Insets} object with the specified top, left, bottom, and right insets.
     *
     * @param top    the inset from the top.
     * @param left   the inset from the left.
     * @param bottom the inset from the bottom.
     * @param right  the inset from the right.
     */
    public Insets(int top, int left, int bottom, int right) {
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }

    /**
     * Set top, left, bottom, and right to the specified values.
     *
     * @param top    the inset from the top.
     * @param left   the inset from the left.
     * @param bottom the inset from the bottom.
     * @param right  the inset from the right.
     */
    public void set(int top, int left, int bottom, int right) {
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }

    /**
     * Checks whether two insets objects are equal. Two instances of {@code Insets} are equal if the four integer values of the fields {@code top}, {@code left}, {@code bottom} and {@code right} are all equal.
     *
     * @return {@code true} if the two insets are equal; otherwise {@code false}.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Insets) {
            Insets insets = (Insets) obj;
            return ((top == insets.top) && (left == insets.left) && (bottom == insets.bottom) && (right == insets.right));
        }
        return false;
    }

    /**
     * Returns the hash code for this {@code Insets}.
     *
     * @return a hash code for this {@code Insets}.
     */
    @Override
    public int hashCode() {
        int sum1 = left + bottom;
        int sum2 = right + top;
        int val1 = sum1 * (sum1 + 1) / 2 + left;
        int val2 = sum2 * (sum2 + 1) / 2 + top;
        int sum3 = val1 + val2;
        return sum3 * (sum3 + 1) / 2 + val2;
    }

    /**
     * Returns a string representation of this {@code Insets} object.
     * This method is intended to be used only for debugging purposes, and the content and format of the returned string may vary between implementations. The returned string may be empty but may not be {@code null}.
     *
     * @return a string representation of this {@code Insets} object.
     */
    @NonNull
    @Override
    public String toString() {
        return getClass().getName() + "[top=" + top + ",left=" + left + ",bottom=" + bottom + ",right=" + right + "]";
    }

    /**
     * Create a copy of this object.
     *
     * @return a copy of this {@code Insets} object.
     */
    @NonNull
    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new RuntimeException(e);
        }
    }
}
