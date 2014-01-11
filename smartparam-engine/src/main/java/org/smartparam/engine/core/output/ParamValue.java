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
import java.util.List;
import org.smartparam.engine.core.type.ValueHolder;

/**
 * Container of parameter sub-matrix returned after querying the parameter.
 *
 * In case of providing values that are out of bounds,
 * {@link org.smartparam.engine.core.exception.SmartParamException} with
 * {@link org.smartparam.engine.core.exception.SmartParamErrorCode#INDEX_OUT_OF_BOUNDS}
 * reason is thrown.
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public interface ParamValue extends Iterable<MultiValue> {

    /**
     * Are there any rows?
     */
    boolean isEmpty();

    /**
     * Return resulting matrix row.
     */
    MultiValue row(int rowNo);

    /**
     * Returns first row of matrix.
     */
    MultiValue row();

    /**
     * Return all rows of matrix.
     */
    List<MultiValue> rows();

    /**
     * Get cell of matrix.
     */
    ValueHolder getHolder(int rowNo, int colNo);

    /**
     * Get object of type T from cell of matrix.
     */
    <T> T get(int rowNo, int colNo);

    /**
     * Get object of given class from cell of matrix.
     */
    <T> T get(int rowNo, int colNo, Class<T> clazz);

    /**
     * Get cell of matrix using level name as column indicator.
     */
    ValueHolder getHolder(int rowNo, String name);

    /**
     * Get object of type T from cell of matrix, where column is described by name.
     */
    <T> T get(int rowNo, String name);

    /**
     * Get object of given class from cell of matrix, where column is described by name.
     */
    <T> T get(int rowNo, String name, Class<T> clazz);

    /**
     * Get value from first row and given column.
     */
    ValueHolder getHolder(int colNo);

    /**
     * Get object of type T from first row and column with given index.
     */
    <T> T get(int colNo);

    /**
     * Get object of given class from first row and column with given index.
     */
    <T> T get(int colNo, Class<T> clazz);

    /**
     * Get value from first row using level name as column indicator.
     */
    ValueHolder getHolder(String name);

    /**
     * Get object of type T from first row and column with given name.
     */
    <T> T get(String name);

    /**
     * Get object of given class from first row and column with given name.
     */
    <T> T get(String name, Class<T> clazz);

    /**
     * Get string from first row and column with given name.
     */
    String getString(String name);

    /**
     * Get BigDecimal from first row and column with given name.
     */
    BigDecimal getBigDecimal(String name);

    /**
     * Get Date from first row and column with given name.
     */
    Date getDate(String name);

    /**
     * Get Integer from first row and column with given name.
     */
    Integer getInteger(String name);

    /**
     * Get Long from first row and column with given name.
     */
    Long getLong(String name);

    /**
     * Get enum of given class from first row and column with given name.
     */
    <T extends Enum<T>> T getEnum(String name, Class<T> enumClass);

    /**
     * Return first value from first row, useful if parameter returns only
     * single value.
     *
     * @return value held in first cell of first row
     */
    ValueHolder getHolder();

    /**
     * Return value from first column of first row as generic object.
     */
    <T> T get();

    /**
     * Return value from first column of first row as object of given class.
     */
    <T> T get(Class<T> clazz);

    /**
     * Return value from first column of first row as string.
     */
    String getString();

    /**
     * Return value from first column of first row as BigDecimal.
     */
    BigDecimal getBigDecimal();

    /**
     * Return value from first column of first row as Date.
     */
    Date getDate();

    /**
     * Return value from first column of first row as Integer.
     */
    Integer getInteger();

    /**
     * Return value from first column of first row as Long.
     */
    Long getLong();

    /**
     * Return value from first column of first row as enum.
     */
    <T extends Enum<T>> T getEnum(Class<T> enumClass);

    /**
     *
     * @return number of rows
     */
    int size();
}
