package org.smartparam.engine.core.type;

import java.math.BigDecimal;
import java.util.Date;
import org.smartparam.engine.core.exception.SmartParamUsageException;
import org.smartparam.engine.core.exception.SmartParamErrorCode;

/**
 * Concrete representation of value of {@link Type}.
 *
 * Holder implementation is obliged to override {@link #getValue() } method,
 * but also might override any number of convenience methods if they are
 * suitable for given type.
 *
 * @author Przemek Hertel <przemek.hertel@gmail.com>
 * @since 1.0.0
 */
public abstract class AbstractHolder implements Comparable<AbstractHolder> {

    private static final int EXPECTED_TOSTRING_LENGTH = 32;

    /**
     * Return object held in holder.
     * Implementations of AbstractHolder should change return value
     * with appropriate for type held, for example:
     * <pre>
     * public BigDecimal getValue() { //if BigDecimal holder }
     * </pre>
     *
     * @return object held
     */
    public abstract Object getValue();

    /**
     *
     * @return if value is null
     */
    public boolean isNull() {
        return getValue() == null;
    }

    /**
     *
     * @return if value is not null
     */
    public boolean isNotNull() {
        return getValue() != null;
    }

    /**
     * Implementation of equals that returns true only if:
     * <ul>
     * <li>other object is of the same class</li>
     * <li>values held in this and other holder are equal (via equals() method)</li>
     * </ul>
     *
     * @param obj object to compare
     * @return true if both conditions are met
     */
    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == this.getClass()) {

            Object v1 = getValue();
            Object v2 = ((AbstractHolder) obj).getValue();

            if (v1 == null) {
                return v2 == null;
            }
            if (v2 != null) {
                return v1.equals(v2);
            }
        }
        return false;
    }

    /**
     * Calculate hash code using formula:
     * <pre>
     * hashcode(holder) := hashcode(holder.getClass()) XOR hashcode(holder.getValue())
     * </pre>
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        Object v = getValue();
        int vhash = v != null ? v.hashCode() : 1;
        return this.getClass().hashCode() ^ vhash;
    }

    /**
     * Returns true only if value held in this holder implement {@link Comparable}.
     * By default returns true.
     */
    public boolean isComparable() {
        return true;
    }

    /**
     * Compares two holders using algorithm:
     * <ul>
     * <li>is value held comparable? (checked using {@link AbstractHolder#isComparable() } method</li>
     * <li>if not return 0 (objects are equal, cos we don't know how to compare them)</li>
     * <li>if comparable, do a null-safe comparison using {@link Comparable#compareTo(java.lang.Object)} method</li>
     * </ul>
     */
    @Override
    @SuppressWarnings("unchecked")
    public int compareTo(AbstractHolder o) {
        if (isComparable()) {
            Comparable<Object> v1 = (Comparable<Object>) this.getValue();
            Comparable<Object> v2 = (Comparable<Object>) o.getValue();

            if (v1 != null) {
                return v2 != null ? v1.compareTo(v2) : 1;
            } else {
                return v2 != null ? -1 : 0;
            }
        }

        return 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(EXPECTED_TOSTRING_LENGTH);
        sb.append(this.getClass().getSimpleName());
        sb.append('[').append(getValue()).append(']');
        return sb.toString();
    }

    private SmartParamUsageException prepareUnexpectedUsageException(String valueType) {
        return new SmartParamUsageException(SmartParamErrorCode.GETTING_WRONG_TYPE,
                String.format("Trying to get [%s] value from %s, which does not support this type. "
                + "Check if type of parameter level is correct.", valueType, this.getClass()));
    }

    /**
     * Returns String representation of held object (by using toString).
     *
     * @return toString value or null if none value held
     */
    public String getString() {
        Object value = getValue();
        return value != null ? value.toString() : null;
    }

    /**
     * Returns int value if holder is capable of doing it, otherwise
     * exception with {@link SmartParamErrorCode#GETTING_WRONG_TYPE} is thrown.
     *
     * @return int value
     */
    public int intValue() {
        throw prepareUnexpectedUsageException("int");
    }

    /**
     * Returns long value if holder is capable of doing it, otherwise
     * exception with {@link SmartParamErrorCode#GETTING_WRONG_TYPE} is thrown.
     *
     * @return long value
     */
    public long longValue() {
        throw prepareUnexpectedUsageException("long");
    }

    /**
     * Returns double value if holder is capable of doing it, otherwise
     * exception with {@link SmartParamErrorCode#GETTING_WRONG_TYPE} is thrown.
     *
     * @return double value
     */
    public double doubleValue() {
        throw prepareUnexpectedUsageException("double");
    }

    /**
     * Returns boolean value if holder is capable of doing it, otherwise
     * exception with {@link SmartParamErrorCode#GETTING_WRONG_TYPE} is thrown.
     *
     * @return boolean value
     */
    public boolean booleanValue() {
        throw prepareUnexpectedUsageException("boolean");
    }

    /**
     * Returns Integer value if holder is capable of doing it, otherwise
     * exception with {@link SmartParamErrorCode#GETTING_WRONG_TYPE} is thrown.
     *
     * @return Integer value
     */
    public Integer getInteger() {
        throw prepareUnexpectedUsageException("Integer");
    }

    /**
     * Returns Long value if holder is capable of doing it, otherwise
     * exception with {@link SmartParamErrorCode#GETTING_WRONG_TYPE} is thrown.
     *
     * @return Long value
     */
    public Long getLong() {
        throw prepareUnexpectedUsageException("Long");
    }

    /**
     * Returns Double value if holder is capable of doing it, otherwise
     * exception with {@link SmartParamErrorCode#GETTING_WRONG_TYPE} is thrown.
     *
     * @return Double value
     */
    public Double getDouble() {
        throw prepareUnexpectedUsageException("Double");
    }

    /**
     * Returns Boolean value if holder is capable of doing it, otherwise
     * exception with {@link SmartParamErrorCode#GETTING_WRONG_TYPE} is thrown.
     *
     * @return Boolean value
     */
    public Boolean getBoolean() {
        throw prepareUnexpectedUsageException("Boolean");
    }

    /**
     * Returns BigDecimal value if holder is capable of doing it, otherwise
     * exception with {@link SmartParamErrorCode#GETTING_WRONG_TYPE} is thrown.
     *
     * @return BigDecimal value
     */
    public BigDecimal getBigDecimal() {
        throw prepareUnexpectedUsageException("BigDecimal");
    }

    /**
     * Returns Date value if holder is capable of doing it, otherwise
     * exception with {@link SmartParamErrorCode#GETTING_WRONG_TYPE} is thrown.
     *
     * @return Date value
     */
    public Date getDate() {
        throw prepareUnexpectedUsageException("Date");
    }
}
