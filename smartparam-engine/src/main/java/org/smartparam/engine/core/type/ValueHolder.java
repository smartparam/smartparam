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
 * Concrete representation of value of {@link Type}.
 *
 * Holder implementation is obliged to override {@link #getValue() } method,
 * but also might override any number of convenience methods if they are
 * suitable for given type.
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public interface ValueHolder extends Comparable<ValueHolder> {

    /**
     * Return object held in holder.
     * Implementations of ValueHolder should change return value
     * with appropriate for type held, for example:
     * <pre>
     *  BigDecimal getValue() { //if BigDecimal holder }
     * </pre>
     *
     * @return object held
     */
    Object getValue();

    /**
     * Is held value null.
     */
    boolean isNull();

    /**
     * Is held value not null.
     */
    boolean isNotNull();

    /**
     * Does held value implement {@link Comparable} interface.
     */
    boolean isComparable();

    /**
     * Get value as string.
     */
    String getString();

    /**
     * Get value as primitive int.
     */
    int intValue();

    /**
     * Get value as primitive long.
     */
    long longValue();

    /**
     * Get value as primitive double.
     */
    double doubleValue();

    /**
     * Get value as primitive boolean.
     */
    boolean booleanValue();

    /**
     * Get value as nullable integer.
     */
    Integer getInteger();

    /**
     * Get value as nullable long.
     */
    Long getLong();

    /**
     * Get value as nullable double.
     */
    Double getDouble();

    /**
     * Get value as nullable boolean.
     */
    Boolean getBoolean();

    /**
     * Get value as BigDecimal.
     */
    BigDecimal getBigDecimal();

    /**
     * Get value as Date.
     */
    Date getDate();
}
