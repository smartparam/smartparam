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
import org.smartparam.engine.core.type.ValueHolder;
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
        this.values = Arrays.copyOf(values, values.length);
    }

    public MultiValue(Object[] values, Map<String, Integer> indexMap) {
        this(values);
        this.indexMap = indexMap;
    }

    /**
     * Return raw holder of stored at given position.
     */
    public ValueHolder getHolder(int position) {
        Object obj = getAbstractHolder(position);

        if (obj instanceof ValueHolder) {
            return (ValueHolder) obj;
        }

        throw new GettingWrongTypeException(position, "AbstractHolder", obj);
    }

    /**
     * Return value of column at given position cast to generic return value.
     */
    @SuppressWarnings("unchecked")
    public <T> T get(int position) {
        return (T) getHolder(position).getValue();
    }

    /**
     * Return value of column at given position cast to object of class clazz.
     */
    public <T> T get(int position, Class<T> clazz) {
        return clazz.cast(getHolder(position).getValue());
    }

    /**
     * Return value of column at given position as string.
     */
    public String getString(int position) {
        return getHolder(position).getString();
    }

    /**
     * Return value of column at given position as BigDecimal.
     */
    public BigDecimal getBigDecimal(int position) {
        return getHolder(position).getBigDecimal();
    }

    /**
     * Return value of column at given position as date.
     */
    public Date getDate(int position) {
        return getHolder(position).getDate();
    }

    /**
     * Return value of column at given position as integer.
     */
    public Integer getInteger(int position) {
        return getHolder(position).getInteger();
    }

    /**
     * Return value of column at given position as long.
     */
    public Long getLong(int position) {
        return getHolder(position).getLong();
    }

    /**
     * Return raw value holder of column with given name.
     */
    public ValueHolder getHolder(String name) {
        return getHolder(index(name));
    }

    /**
     * Return value of column with given name cast to generic return value.
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        return (T) getHolder(index(name)).getValue();
    }

    /**
     * Return value of column with given name cast to object of class clazz.
     */
    public <T> T get(String name, Class<T> clazz) {
        return clazz.cast(getHolder(index(name)).getValue());
    }

    /**
     * Return value of column with given name as string.
     */
    public String getString(String name) {
        return getString(index(name));
    }

    /**
     * Return value of column with given name as BigDecimal.
     */
    public BigDecimal getBigDecimal(String name) {
        return getBigDecimal(index(name));
    }

    /**
     * Return value of column with given name as date.
     */
    public Date getDate(String name) {
        return getDate(index(name));
    }

    /**
     * Return value of column with given name as integer.
     */
    public Integer getInteger(String name) {
        return getInteger(index(name));
    }

    /**
     * Return value of column with given name as long.
     */
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

    /**
     * Return value of column with given name as enum.
     */
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
    public ValueHolder[] getArray(int position) {
        Object obj = getAbstractHolder(position);

        if (obj instanceof ValueHolder[]) {
            return (ValueHolder[]) obj;
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
            if (val instanceof ValueHolder) {
                ValueHolder cell = (ValueHolder) val;
                result[i] = cell.getValue();
            }

            // if object at i is holder array
            if (val instanceof ValueHolder[]) {
                ValueHolder[] cell = (ValueHolder[]) val;
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
     * Return value of column at given position as string array.
     */
    public String[] getStringArray(int position) {
        ValueHolder[] array = getArray(position);
        String[] result = new String[array.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = array[i].getString();
        }
        return result;
    }

    /**
     * Return value of column at given position as date array.
     */
    public Date[] getDateArray(int position) {
        ValueHolder[] array = getArray(position);
        Date[] result = new Date[array.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = array[i].getDate();
        }
        return result;
    }

    /**
     * Return value of column at given position as integer array.
     */
    public Integer[] getIntegerArray(int position) {
        ValueHolder[] array = getArray(position);
        Integer[] result = new Integer[array.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = array[i].getInteger();
        }
        return result;
    }

    /**
     * Return value of column at given position as integer array.
     */
    public Long[] getLongArray(int position) {
        ValueHolder[] array = getArray(position);
        Long[] result = new Long[array.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = array[i].getLong();
        }
        return result;
    }

    /**
     * Return value of column at given position as BigDecimal array.
     */
    public BigDecimal[] getBigDecimalArray(int position) {
        ValueHolder[] array = getArray(position);
        BigDecimal[] result = new BigDecimal[array.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = array[i].getBigDecimal();
        }
        return result;
    }

    /**
     * Return value of column under given name as array of raw value holders.
     */
    public ValueHolder[] getArray(String name) {
        return getArray(index(name));
    }

    /**
     * Return value of column under given name as array of strings.
     */
    public String[] getStringArray(String name) {
        return getStringArray(index(name));
    }

    /**
     * Return value of column under given name as array of raw big decimals.
     */
    public BigDecimal[] getBigDecimalArray(String name) {
        return getBigDecimalArray(index(name));
    }

    /**
     * Return value of column under given name as array of dates.
     */
    public Date[] getDateArray(String name) {
        return getDateArray(index(name));
    }

    /**
     * Return value of column under given name as array of integers.
     */
    public Integer[] getIntegerArray(String name) {
        return getIntegerArray(index(name));
    }

    /**
     * Return value of column under given name as array of longs.
     */
    public Long[] getLongArray(String name) {
        return getLongArray(index(name));
    }

    private Object getAbstractHolder(int position) {
        if (position >= 0 && position < values.length) {
            return values[position];
        }
        throw new InvalidValueIndexException(position, values);
    }

    /**
     * Returns row values as strings, equivalent to calling {@link #getString(int) }
     * on every row value.
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
     * Iteration mode, return raw value holder of next row element.
     */
    public ValueHolder nextHolder() {
        return getHolder(nextPosition());
    }

    /**
     * Iteration mode, return value of next row element cast to generic type.
     */
    @SuppressWarnings("unchecked")
    public <T> T next() {
        return (T) nextHolder().getValue();
    }

    /**
     * Iteration mode, return value of next row element cast to object of class clazz.
     */
    public <T> T next(Class<T> clazz) {
        return clazz.cast(nextHolder().getValue());
    }

    /**
     * Iteration mode, get string value of next row element.
     */
    public String nextString() {
        return getString(nextPosition());
    }

    /**
     * Iteration mode, get BigDecimal value of next row element.
     */
    public BigDecimal nextBigDecimal() {
        return getBigDecimal(nextPosition());
    }

    /**
     * Iteration mode, get Date value of next row element.
     */
    public Date nextDate() {
        return getDate(nextPosition());
    }

    /**
     * Iteration mode, get integer value of next row element.
     */
    public Integer nextInteger() {
        return getInteger(nextPosition());
    }

    /**
     * Iteration mode, get long value of next row element.
     */
    public Long nextLong() {
        return getLong(nextPosition());
    }

    /**
     * Iteration mode, get enum value of next row element.
     */
    public <T extends Enum<T>> T nextEnum(Class<T> enumClass) {
        return getEnum(nextPosition(), enumClass);
    }

    /**
     * Iteration mode, return value of next row element as holder array.
     */
    public ValueHolder[] nextArray() {
        return getArray(nextPosition());
    }

    /**
     * Iteration mode, return value of next row element as string array.
     */
    public String[] nextStringArray() {
        return getStringArray(nextPosition());
    }

    /**
     * Iteration mode, return value of next row element as BigDecimal array.
     */
    public BigDecimal[] nextBigDecimalArray() {
        return getBigDecimalArray(nextPosition());
    }

    /**
     * Iteration mode, return value of next row element as Date array.
     */
    public Date[] nextDateArray() {
        return getDateArray(nextPosition());
    }

    /**
     * Iteration mode, return value of next row element as integer array.
     */
    public Integer[] nextIntegerArray() {
        return getIntegerArray(nextPosition());
    }

    /**
     * Iteration mode, return value of next row element as long array.
     */
    public Long[] nextLongArray() {
        return getLongArray(nextPosition());
    }

    /**
     * @return length of row
     */
    public int size() {
        return values != null ? values.length : 0;
    }
}
