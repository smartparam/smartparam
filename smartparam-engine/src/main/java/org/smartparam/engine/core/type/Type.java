package org.smartparam.engine.core.type;

/**
 * Contract for parameter engine type system entity. Type gives behavior to
 * its value - most of all decodes and encodes from/to String form. Each type
 * should have {@link AbstractHolder} defined, which is a concrete representation
 * (instance) of type.
 *
 * To use custom type in engine, register it at {@link org.smartparam.engine.core.repository.TypeRepository},
 * or use {@link org.smartparam.engine.annotations.ParamType} if annotation scan
 * is enabled.
 *
 * @param T held value
 *
 * @author Przemek Hertel <przemek.hertel@gamil.com>
 * @since 1.0.0
 */
public interface Type<T extends AbstractHolder> {

    /**
     * Create String representation of value. Transformation has to be reversible using
     * {@link Type#decode(java.lang.String)} method:
     * <pre>
     * decode( encode(obj) ) == obj
     * </pre>
     *
     * @param holder value to stringify
     * @return string representation
     */
    String encode(T holder);

    /**
     * Create value out of provided string. This is opposite to
     * {@link Type#encode(org.smartparam.engine.core.type.AbstractHolder) }, so:
     * <pre>
     * encode( decode(str) ) == str
     * </pre>
     *
     * @param text string to interprete
     * @return value
     */
    T decode(String text);

    /**
     * Can convert any object to value of Type. Should throw IllegalArgumentException
     * if unable to convert from object.
     *
     * @param obj object to try and convert
     * @return value
     */
    T convert(Object obj);

    /**
     * Should create new, empty array of given size.
     *
     * @param size size of array
     * @return empty array of values
     */
    T[] newArray(int size);
}
