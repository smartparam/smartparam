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
package org.smartparam.engine.core.output;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import org.smartparam.engine.core.type.AbstractHolder;
import org.smartparam.engine.util.Printer;

/**
 * Represents single row of matrix returned from parameter querying. Immutable.
 * Each method returning value can throw:
 *
 * * {@link InvalidRowIndexException}
 * * {@link InvalidValueIndexException}
 * * {@link GettingWrongTypeException}
 * * {@link UnknownLevelNameException}
 *
 * @author Przemek Hertel
 * @since 0.9.0
 */
public class MultiValue {

    private static final int INLINE_TO_STRING_LENGTH = 100;

    /**
     * Values held, each entry is either AbstractHolder or AbstractHolder[].
     */
    private final Object[] values;

    private Map<String, Integer> indexMap;

    /**
     * Keeps iteration state, used to power next* methods.
     */
    private int last = 0;

    public MultiValue(Object[] values) {
        this.values = values;
    }

    public MultiValue(Object[] values, Map<String, Integer> indexMap) {
        this.values = values;
        this.indexMap = indexMap;
    }

    /**
     * Returns value stored at position.
     */
    public AbstractHolder getValue(int position) {
        Object obj = getHolder(position);

        if (obj instanceof AbstractHolder) {
            return (AbstractHolder) obj;
        }

        throw new GettingWrongTypeException(position, "AbstractHolder", obj);
    }

    /**
     * @return string representation of value
     */
    public String getString(int position) {
        return getValue(position).getString();
    }

    /**
     * @return big decimal value, if supported by holder
     */
    public BigDecimal getBigDecimal(int position) {
        return getValue(position).getBigDecimal();
    }

    /**
     * @return date value, if supported by holder
     */
    public Date getDate(int position) {
        return getValue(position).getDate();
    }

    /**
     * @return integer value, if supported by holder
     */
    public Integer getInteger(int position) {
        return getValue(position).getInteger();
    }

    /**
     * @return long value, if supported by holder
     */
    public Long getLong(int position) {
        return getValue(position).getLong();
    }

    public AbstractHolder getValue(String name) {
        return getValue(index(name));
    }

    public String getString(String name) {
        return getString(index(name));
    }

    public BigDecimal getBigDecimal(String name) {
        return getBigDecimal(index(name));
    }

    public Date getDate(String name) {
        return getDate(index(name));
    }

    public Integer getInteger(String name) {
        return getInteger(index(name));
    }

    public Long getLong(String name) {
        return getLong(index(name));
    }

    private int index(String name) {
        if (indexMap != null) {
            Integer k = indexMap.get(name);
            if (k != null) {
                return k;
            }
        }

        throw new UnknownLevelNameException(name);
    }

    /**
     * Parses string value as enum entry, using {@link Enum#valueOf(java.lang.Class, java.lang.String) } method.
     */
    public <T extends Enum<T>> T getEnum(int position, Class<T> enumClass) {
        String code = getString(position);
        return code != null ? codeToEnum(code, enumClass) : null;
    }

    public <T extends Enum<T>> T getEnum(String name, Class<T> enumClass) {
        return getEnum(index(name), enumClass);
    }

    private <T extends Enum<T>> T codeToEnum(String code, Class<T> enumClass) {
        try {
            return Enum.valueOf(enumClass, code);
        } catch (IllegalArgumentException e) {
            throw new GettingWrongTypeException(e, enumClass, code);
        }
    }

    /**
     * Return array of values stored at position. Should be used if parameter
     * level contained list of values ({@link org.smartparam.engine.model.Level#isArray()}.
     * This string list is split into array of values using separator defined at
     * parameter level ({@link org.smartparam.engine.model.Parameter#getArraySeparator()}).
     * Type of each value holder in array is the same, defined by level type.
     */
    public AbstractHolder[] getArray(int position) {
        Object obj = getHolder(position);

        if (obj instanceof AbstractHolder[]) {
            return (AbstractHolder[]) obj;
        }

        throw new GettingWrongTypeException(position, "AbstractHolder[]", obj);
    }

    /**
     * Return array of unwrapped objects, this is a raw representation of
     * contents of AbstractHolders from MultiValue object.
     * Each array element is either value of {@link AbstractHolder#getValue() }
     * if level stores single value or array of {@link AbstractHolder#getValue() }
     * if level stores an array.
     */
    public Object[] unwrap() {
        Object[] result = new Object[values.length];

        for (int i = 0; i < values.length; i++) {
            Object val = values[i];

            // if object at i is holder
            if (val instanceof AbstractHolder) {
                AbstractHolder cell = (AbstractHolder) val;
                result[i] = cell.getValue();
            }

            // if object at i is holder array
            if (val instanceof AbstractHolder[]) {
                AbstractHolder[] cell = (AbstractHolder[]) val;
                Object[] array = new Object[cell.length];
                for (int j = 0; j < cell.length; j++) {
                    array[j] = cell[j].getValue();
                }
                result[i] = array;
            }
        }

        return result;
    }

    /**
     * @return string array, if supported by holder
     */
    public String[] getStringArray(int position) {
        AbstractHolder[] array = getArray(position);
        String[] result = new String[array.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = array[i].getString();
        }
        return result;
    }

    /**
     * @return date array, if supported by holder
     */
    public Date[] getDateArray(int position) {
        AbstractHolder[] array = getArray(position);
        Date[] result = new Date[array.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = array[i].getDate();
        }
        return result;
    }

    /**
     * @return integer array, if supported by holder
     */
    public Integer[] getIntegerArray(int position) {
        AbstractHolder[] array = getArray(position);
        Integer[] result = new Integer[array.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = array[i].getInteger();
        }
        return result;
    }

    /**
     * @return big decimal array, if supported by holder
     */
    public BigDecimal[] getBigDecimalArray(int position) {
        AbstractHolder[] array = getArray(position);
        BigDecimal[] result = new BigDecimal[array.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = array[i].getBigDecimal();
        }
        return result;
    }

    private Object getHolder(int position) {
        if (position >= 0 && position < values.length) {
            return values[position];
        }
        throw new InvalidValueIndexException(position, values);
    }

    /**
     * Returns row values as strings, equivalent to calling {@link #getString(int) }
     * on every row value.
     *
     * @return
     */
    public String[] asStrings() {
        String[] array = new String[values.length];
        for (int i = 0; i < array.length; i++) {
            array[i] = getString(i);
        }
        return array;
    }

    /**
     * Returns row values as BigDecimals, equivalent to calling {@link #getBigDecimal(int) }
     * on every row value.
     *
     * @return
     */
    public BigDecimal[] asBigDecimals() {
        BigDecimal[] array = new BigDecimal[values.length];
        for (int i = 0; i < array.length; i++) {
            array[i] = getBigDecimal(i);
        }
        return array;
    }

    @Override
    public String toString() {
        return Printer.print(values, "MultiValue");
    }

    /**
     * Returns toString, but in single line.
     *
     * @return string representation of object
     */
    public String toStringInline() {
        Object[] rawValues = unwrap();
        StringBuilder sb = new StringBuilder(INLINE_TO_STRING_LENGTH);
        sb.append('[');

        for (int i = 0; i < rawValues.length; ++i) {
            if (i > 0) {
                sb.append(", ");
            }

            Object v = rawValues[i];

            if (v instanceof Object[]) {
                sb.append(Arrays.toString((Object[]) v));
            } else {
                sb.append(v);
            }
        }

        sb.append(']');
        return sb.toString();
    }

    private int nextPosition() {
        int position = last;
        last++;
        return position;
    }

    /**
     * Iteration mode, return value of next row element.
     *
     * @return raw value
     * @see #getValue(int)
     */
    public AbstractHolder nextValue() {
        return getValue(nextPosition());
    }

    /**
     * Iteration mode, get string value of next row element.
     *
     * @return string value
     * @see #getString(int)
     */
    public String nextString() {
        return getString(nextPosition());
    }

    /**
     * Iteration mode, get BigDecimal value of next row element.
     *
     * @return BigDecimal value
     * @see #getBigDecimal(int)
     */
    public BigDecimal nextBigDecimal() {
        return getBigDecimal(nextPosition());
    }

    /**
     * Iteration mode, get Date value of next row element.
     *
     * @return Date value
     * @see #getDate(int)
     */
    public Date nextDate() {
        return getDate(nextPosition());
    }

    /**
     * Iteration mode, get integer value of next row element.
     *
     * @return integer value
     * @see #getInteger(int)
     */
    public Integer nextInteger() {
        return getInteger(nextPosition());
    }

    /**
     * Iteration mode, get long value of next row element.
     *
     * @return long value
     * @see #getLong(int)
     */
    public Long nextLong() {
        return getLong(nextPosition());
    }

    /**
     * Iteration mode, get enum value of next row element.
     *
     * @return enum of given class
     * @see #getEnum(int, java.lang.Class)
     */
    public <T extends Enum<T>> T nextEnum(Class<T> enumClass) {
        return getEnum(nextPosition(), enumClass);
    }

    /**
     * Iteration mode, return value of next row element as holder array.
     *
     * @return raw elements array
     * @see #getArray(int)
     */
    public AbstractHolder[] nextArray() {
        return getArray(nextPosition());
    }

    /**
     * Iteration mode, return value of next row element as string array.
     *
     * @return string array
     * @see #getStringArray(int)
     */
    public String[] nextStringArray() {
        return getStringArray(nextPosition());
    }

    /**
     * Iteration mode, return value of next row element as BigDecimal array.
     *
     * @return BigDecimal array
     * @see #getBigDecimalArray(int)
     */
    public BigDecimal[] nextBigDecimalArray() {
        return getBigDecimalArray(nextPosition());
    }

    /**
     * @return length of row
     */
    public int size() {
        return values != null ? values.length : 0;
    }
}
