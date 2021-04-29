/*
 * Created on 27.07.2006
 */
package at.hagru.hgbase.lib;

/**
 * Test a object if this is the searched one.
 * Used by the find-methods of HGBaseTools.
 *
 * @author hagru
 */
public interface IFinderListener<T> {

    /**
     * @param o The object to test.
     * @return True if this is the object it was looked for, otherwise false.
     */
    public boolean test(T o);

}
