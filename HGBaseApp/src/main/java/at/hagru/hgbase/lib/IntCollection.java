package at.hagru.hgbase.lib;

/**
 * Helper class for managing a collection of integer values. With this class the using of an integer array can
 * be made more transparent.
 *
 * @author hagru
 */
public class IntCollection implements Cloneable {

    int[] values;

    public IntCollection(int[] values) {
        super();
        this.values = values.clone();
    }

    /**
     * @param index The index of the integer value.
     * @return The value at the given index or null.
     */
    public int get(int index) {
        if (index >= 0 && index < values.length) {
            return values[index];
        }
        return HGBaseTools.INVALID_INT;
    }

    /**
     * @param index The index of the integer value.
     * @param value The new int value for the given index.
     */
    public void set(int index, int value) {
        if (index >= 0 && index < values.length) {
            values[index] = value;
        }
    }

    /**
     * @return The number of ints saved in this collection.
     */
    public int size() {
        return values.length;
    }

    /**
     * @return The values as array.
     */
    public int[] values() {
        return values.clone();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return new IntCollection(values);
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();
        final int size = this.size();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                text.append(" / ");
            }
            text.append(String.valueOf(i));
            text.append("=");
            text.append(get(i));
        }
        return text.toString();
    }

    /**
     * Checks if the collection contains the specified {@code value}.
     * 
     * @param value The value to check.
     * @return {@code true} if the collection contains the specified {@code value}.
     */
    public boolean contains(int value) {
      for (int existing : values) {
        if (existing == value) {
          return true;
        }
      }
      return false;
    }

    /**
     * Checks if the specified collection contains the specified {@code value}.
     * 
     * @param values The collection.
     * @param value The value to check.
     * @return {@code true} if the specified collection contains the specified {@code value}.
     */
    public static boolean contains(int[] values, int value) {
      return (new IntCollection(values)).contains(value);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o2) {
        if (o2 instanceof IntCollection) {
            IntCollection ic2 = (IntCollection) o2;
            final int size = this.size();
            if (size != ic2.size()) {
                return false;
            }
            for (int i = 0; i < size; i++) {
                if (this.get(i) != ic2.get(i)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }

}
