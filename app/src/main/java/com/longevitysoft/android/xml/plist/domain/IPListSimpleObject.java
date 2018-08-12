package com.longevitysoft.android.xml.plist.domain;

/**
 * Interface that simple PList objects implement. This includes all objects
 * besides from {@link Array}s and {@link Dict}s.
 */
public interface IPListSimpleObject<E extends Object> {

    /**
     * Get the value of the plist object.
     *
     * @return
     */
    public E getValue();

    /**
     * Set the value of the PList object.
     *
     * @param val
     */
    public void setValue(E val);

    /**
     * Set the value of the PList object from a string.
     *
     * @param val
     */
    public void setValue(java.lang.String val);
}
