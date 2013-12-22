/*
 * Copyright 2013 Adam Dubiel, Przemek Hertel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.smartparam.engine.core.type;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Adam Dubiel
 */
public abstract class AbstractValueHolder implements ValueHolder {

    private static final int EXPECTED_TOSTRING_LENGTH = 32;

    @Override
    public boolean isNull() {
        return getValue() == null;
    }

    @Override
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
            Object v2 = ((ValueHolder) obj).getValue();

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

    @Override
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
    public int compareTo(ValueHolder o) {
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

    private UnsupportedOperationException prepareUnexpectedUsageException(String valueType) {
        return new UnsupportedOperationException(String.format("Trying to get [%s] value from %s, which does not support this type. "
                + "Check if type of parameter level is correct.", valueType, this.getClass()));
    }

    /**
     * Returns String representation of held object (by using toString).
     *
     * @return toString value or null if none value held
     */
    @Override
    public String getString() {
        Object value = getValue();
        return value != null ? value.toString() : null;
    }

    /**
     * Returns int value if holder is capable of doing it, otherwise
     * UnsupportedOperationException is thrown.
     *
     * @return int value
     */
    @Override
    public int intValue() {
        throw prepareUnexpectedUsageException("int");
    }

    /**
     * Returns long value if holder is capable of doing it, otherwise
     * UnsupportedOperationException is thrown.
     *
     * @return long value
     */
    @Override
    public long longValue() {
        throw prepareUnexpectedUsageException("long");
    }

    /**
     * Returns double value if holder is capable of doing it, otherwise
     * UnsupportedOperationException is thrown.
     *
     * @return double value
     */
    @Override
    public double doubleValue() {
        throw prepareUnexpectedUsageException("double");
    }

    /**
     * Returns boolean value if holder is capable of doing it, otherwise
     * UnsupportedOperationException is thrown.
     *
     * @return boolean value
     */
    @Override
    public boolean booleanValue() {
        throw prepareUnexpectedUsageException("boolean");
    }

    /**
     * Returns Integer value if holder is capable of doing it, otherwise
     * UnsupportedOperationException is thrown.
     *
     * @return Integer value
     */
    @Override
    public Integer getInteger() {
        throw prepareUnexpectedUsageException("Integer");
    }

    /**
     * Returns Long value if holder is capable of doing it, otherwise
     * UnsupportedOperationException is thrown.
     *
     * @return Long value
     */
    @Override
    public Long getLong() {
        throw prepareUnexpectedUsageException("Long");
    }

    /**
     * Returns Double value if holder is capable of doing it, otherwise
     * UnsupportedOperationException is thrown.
     *
     * @return Double value
     */
    @Override
    public Double getDouble() {
        throw prepareUnexpectedUsageException("Double");
    }

    /**
     * Returns Boolean value if holder is capable of doing it, otherwise
     * UnsupportedOperationException is thrown.
     *
     * @return Boolean value
     */
    @Override
    public Boolean getBoolean() {
        throw prepareUnexpectedUsageException("Boolean");
    }

    /**
     * Returns BigDecimal value if holder is capable of doing it, otherwise
     * UnsupportedOperationException is thrown.
     *
     * @return BigDecimal value
     */
    @Override
    public BigDecimal getBigDecimal() {
        throw prepareUnexpectedUsageException("BigDecimal");
    }

    /**
     * Returns Date value if holder is capable of doing it, otherwise
     * UnsupportedOperationException is thrown.
     *
     * @return Date value
     */
    @Override
    public Date getDate() {
        throw prepareUnexpectedUsageException("Date");
    }
}
