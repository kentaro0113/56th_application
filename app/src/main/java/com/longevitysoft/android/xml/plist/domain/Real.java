package com.longevitysoft.android.xml.plist.domain;

/**
 * Represents a simple plist real element.
 */
public class Real extends PListObject implements IPListSimpleObject<Float> {

    // TODO: Double?
    protected Float real;

    /**
     *
     */
    private static final long serialVersionUID = -4204214862534504729L;

    public Real() {
        setType(PListObjectType.REAL);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.longevitysoft.android.xml.plist.domain.IPListSimpleObject#getValue()
     */
    @Override
    public Float getValue() {
        return real;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.longevitysoft.android.xml.plist.domain.IPListSimpleObject#setValue
     * (java.lang.Object)
     */
    @Override
    public void setValue(Float val) {
        this.real = val;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.longevitysoft.android.xml.plist.domain.IPListSimpleObject#setValue
     * (java.lang.String)
     */
    @Override
    public void setValue(java.lang.String val) {
        this.real = new Float(Float.parseFloat(val.trim()));
    }
}
