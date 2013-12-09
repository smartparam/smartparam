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
import org.smartparam.engine.core.type.AbstractHolder;

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
public interface ParamValue {

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
    MultiValue[] rows();

    /**
     * Get cell of matrix.
     */
    AbstractHolder get(int rowNo, int colNo);

    /**
     * Get cell of matrix using level name as column indicator.
     */
    AbstractHolder get(int rowNo, String name);

    /**
     * Get value from first row and given column.
     */
    AbstractHolder get(int colNo);

    /**
     * Get value from first row using level name as column indicator.
     */
    AbstractHolder getHolder(String name);

    /**
     * Get object of type T from first row and column with given name.
     */
    <T> T get(String name);

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
    AbstractHolder getHolder();

    /**
     * Return value from first column of first row as generic object.
     */
    <T> T get();

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
     * Return value from first column of first row as Ineger.
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
