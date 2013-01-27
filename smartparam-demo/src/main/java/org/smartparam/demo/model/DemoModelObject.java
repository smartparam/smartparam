package org.smartparam.demo.model;

/**
 * Sample model object used to retrieve values from sample parameter.
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public class DemoModelObject {

    /**
     * Unique code of object.
     */
    private String code;

    /**
     * Constructs new object and sets its properties.
     *
     * @param code unique code
     */
    public DemoModelObject(String code) {
        this.code = code;
    }

    /**
     * Returns code, read-only field.
     *
     * @return unique code
     */
    public String getCode() {
        return code;
    }
}
