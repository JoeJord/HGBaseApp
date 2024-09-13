package at.hagru.hgbase.lib;

/**
 * A pair with two objects. Objects may be null.
 *
 * @param <F> type of the first object
 * @param <S> type of the second object
 * @author hagru
 * @deprecated Use {@link android.util.Pair} instead.
 */
@Deprecated
public class Pair<F, S> {
    /**
     * The first object.
     */
    private F first;
    /**
     * The second object.
     */
    private S second;

    /**
     * Creates a new pair of {@code null} objects.
     */
    public Pair() {
        this(null, null);
    }

    /**
     * Create a new pair of objects.
     *
     * @param first  the first object, may be {@code null}
     * @param second the second object, may be {@code null}
     */
    public Pair(F first, S second) {
        setFirst(first);
        setSecond(second);
    }

    /**
     * Sets the first object.
     *
     * @param first The object to set, may be {@code null}.
     */
    public void setFirst(F first) {
        this.first = first;
    }

    /**
     * Returns the first object.
     *
     * @return the first object, may be {@code null}
     */
    public F getFirst() {
        return first;
    }

    /**
     * Sets the second object.
     *
     * @param second The object to set, may be {@code null}.
     */
    public void setSecond(S second) {
        this.second = second;
    }

    /**
     * Returns the second object.
     *
     * @return the second object, may be {@code null}
     */
    public S getSecond() {
        return second;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o2) {
        if (o2 instanceof Pair<?, ?>) {
            Pair<?, ?> p2 = (Pair<?, ?>) o2;
            return HGBaseTools.equalObject(getFirst(), p2.getFirst())
                    && HGBaseTools.equalObject(getSecond(), p2.getSecond());
        } else {
            return false;
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return HGBaseTools.hashCode(getFirst()) * 31 + HGBaseTools.hashCode(getSecond());
    }
}
