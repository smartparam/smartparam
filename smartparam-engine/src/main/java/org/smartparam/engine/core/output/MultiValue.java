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
import java.util.Date;
import org.smartparam.engine.core.parameter.entry.ParameterEntryKey;
import org.smartparam.engine.core.type.ValueHolder;

/**
 * Represents single row of matrix returned from parameter querying. Immutable.
 * Each method returning value can throw:
 *
 * * {@link InvalidValueIndexException}
 * * {@link GettingWrongTypeException}
 * * {@link UnknownLevelNameException}
 *
 * @author Przemek Hertel
 * @since 0.9.0
 */
public interface MultiValue {

    /**
     * Returns row values as BigDecimals, equivalent to calling {@link #getBigDecimal(int) }
     * on every row value.
     */
    BigDecimal[] asBigDecimals();

    /**
     * Returns row values as strings, equivalent to calling {@link #getString(int) }
     * on every row value.
     */
    String[] asStrings();

    /**
     * Return value of column at given position cast to generic return value.
     */
    @SuppressWarnings(value = "unchecked")
    <T> T get(int position);

    /**
     * Return value of column at given position cast to object of class clazz.
     */
    <T> T get(int position, Class<T> clazz);

    /**
     * Return value of column with given name cast to generic return value.
     */
    @SuppressWarnings(value = "unchecked")
    <T> T get(String name);

    /**
     * Return value of column with given name cast to object of class clazz.
     */
    <T> T get(String name, Class<T> clazz);

    /**
     * Return array of values stored at position. Should be used if parameter
     * level contained list of values ({@link org.smartparam.engine.model.Level#isArray()}.
     * This string list is split into array of values using separator defined at
     * parameter level ({@link org.smartparam.engine.model.Parameter#getArraySeparator()}).
     * Type of each value holder in array is the same, defined by level type.
     */
    ValueHolder[] getArray(int position);

    /**
     * Return value of column under given name as array of raw value holders.
     */
    ValueHolder[] getArray(String name);

    /**
     * Return value of column at given position as BigDecimal.
     */
    BigDecimal getBigDecimal(int position);

    /**
     * Return value of column with given name as BigDecimal.
     */
    BigDecimal getBigDecimal(String name);

    /**
     * Return value of column at given position as BigDecimal array.
     */
    BigDecimal[] getBigDecimalArray(int position);

    /**
     * Return value of column under given name as array of raw big decimals.
     */
    BigDecimal[] getBigDecimalArray(String name);

    /**
     * Return value of column at given position as date.
     */
    Date getDate(int position);

    /**
     * Return value of column with given name as date.
     */
    Date getDate(String name);

    /**
     * Return value of column at given position as date array.
     */
    Date[] getDateArray(int position);

    /**
     * Return value of column under given name as array of dates.
     */
    Date[] getDateArray(String name);

    /**
     * Parses string value as enum entry, using {@link Enum#valueOf(java.lang.Class, java.lang.String) } method.
     */
    <T extends Enum<T>> T getEnum(int position, Class<T> enumClass);

    /**
     * Return value of column with given name as enum.
     */
    <T extends Enum<T>> T getEnum(String name, Class<T> enumClass);

    /**
     * Return raw holder of stored at given position.
     */
    ValueHolder getHolder(int position);

    /**
     * Return raw value holder of column with given name.
     */
    ValueHolder getHolder(String name);

    /**
     * Return value of column at given position as integer.
     */
    Integer getInteger(int position);

    /**
     * Return value of column with given name as integer.
     */
    Integer getInteger(String name);

    /**
     * Return value of column at given position as integer array.
     */
    Integer[] getIntegerArray(int position);

    /**
     * Return value of column under given name as array of integers.
     */
    Integer[] getIntegerArray(String name);

    /**
     * Return repository-scope unique identifier of this value. By using this value and knowing repository name (from
     * ParamValue) it is possible to pinpoint the exact entry in repository from which this value has been read, useful
     * for auditing but might hurt performance.
     *
     * @throws GettingKeyNotIdentifiableParameterException when parameter not flagged with identifiable entries
     * @see org.smartparam.engine.core.parameter.Parameter#isIdentifyEntries()
     */
    ParameterEntryKey getKey();

    /**
     * Return value of column at given position as long.
     */
    Long getLong(int position);

    /**
     * Return value of column with given name as long.
     */
    Long getLong(String name);

    /**
     * Return value of column at given position as integer array.
     */
    Long[] getLongArray(int position);

    /**
     * Return value of column under given name as array of longs.
     */
    Long[] getLongArray(String name);

    /**
     * Return value of column at given position as string.
     */
    String getString(int position);

    /**
     * Return value of column with given name as string.
     */
    String getString(String name);

    /**
     * Return value of column at given position as string array.
     */
    String[] getStringArray(int position);

    /**
     * Return value of column under given name as array of strings.
     */
    String[] getStringArray(String name);

    /**
     * Iteration mode, return value of next row element cast to generic type.
     */
    @SuppressWarnings(value = "unchecked")
    <T> T next();

    /**
     * Iteration mode, return value of next row element cast to object of class clazz.
     */
    <T> T next(Class<T> clazz);

    /**
     * Iteration mode, return value of next row element as holder array.
     */
    ValueHolder[] nextArray();

    /**
     * Iteration mode, get BigDecimal value of next row element.
     */
    BigDecimal nextBigDecimal();

    /**
     * Iteration mode, return value of next row element as BigDecimal array.
     */
    BigDecimal[] nextBigDecimalArray();

    /**
     * Iteration mode, get Date value of next row element.
     */
    Date nextDate();

    /**
     * Iteration mode, return value of next row element as Date array.
     */
    Date[] nextDateArray();

    /**
     * Iteration mode, get enum value of next row element.
     */
    <T extends Enum<T>> T nextEnum(Class<T> enumClass);

    /**
     * Iteration mode, return raw value holder of next row element.
     */
    ValueHolder nextHolder();

    /**
     * Iteration mode, get integer value of next row element.
     */
    Integer nextInteger();

    /**
     * Iteration mode, return value of next row element as integer array.
     */
    Integer[] nextIntegerArray();

    /**
     * Iteration mode, get long value of next row element.
     */
    Long nextLong();

    /**
     * Iteration mode, return value of next row element as long array.
     */
    Long[] nextLongArray();

    /**
     * Iteration mode, get string value of next row element.
     */
    String nextString();

    /**
     * Iteration mode, return value of next row element as string array.
     */
    String[] nextStringArray();

    /**
     * @return length of row
     */
    int size();

    @Override
    String toString();

    /**
     * Returns toString, but in single line.
     */
    String toStringInline();

    /**
     * Return array of unwrapped objects, this is a raw representation of
     * contents of AbstractHolders from MultiValue object.
     * Each array element is either value of {@link AbstractHolder#getValue() }
     * if level stores single value or array of {@link AbstractHolder#getValue() }
     * if level stores an array.
     */
    Object[] unwrap();

}
