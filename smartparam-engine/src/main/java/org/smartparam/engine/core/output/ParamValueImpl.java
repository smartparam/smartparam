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
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.core.exception.SmartParamUsageException;
import org.smartparam.engine.core.type.AbstractHolder;
import org.smartparam.engine.util.Formatter;
import org.smartparam.engine.util.Printer;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class ParamValueImpl implements ParamValue {

    private MultiValue[] rows;

    private Map<String, Integer> indexMap;

    public ParamValueImpl(MultiValue[] rows, Map<String, Integer> indexMap) {
        this.rows = rows;
        this.indexMap = indexMap;
    }

    @Override
    public MultiValue row(int rowNo) {
        if (rowNo >= 0 && rowNo < size()) {
            return rows[rowNo];
        }

        throw new SmartParamUsageException(
                SmartParamErrorCode.INDEX_OUT_OF_BOUNDS,
                String.format("Trying to get non-existing row: %d. Available rows: %d..%d.", rowNo, 0, size() - 1));
    }

    @Override
    public MultiValue row() {
        return row(0);
    }

    @Override
    public MultiValue[] rows() {
        return rows;
    }

    @Override
    public AbstractHolder get(int rowNo, int colNo) {
        return row(rowNo).getValue(colNo);
    }

    @Override
    public AbstractHolder get(int rowNo, String name) {
        return get(rowNo, index(name));
    }

    @Override
    public AbstractHolder get(int colNo) {
        return row().getValue(colNo);
    }

    @Override
    public AbstractHolder get(String name) {
        return row().getValue(index(name));
    }

    @Override
    public String getString(String name) {
        return row().getString(name);
    }

    @Override
    public BigDecimal getBigDecimal(String name) {
        return row().getBigDecimal(name);
    }

    @Override
    public Date getDate(String name) {
        return row().getDate(name);
    }

    @Override
    public Integer getInteger(String name) {
        return row().getInteger(name);
    }

    @Override
    public Long getLong(String name) {
        return row().getLong(name);
    }

    @Override
    public <T extends Enum<T>> T getEnum(String name, Class<T> enumClass) {
        return row().getEnum(name, enumClass);
    }

    @Override
    public AbstractHolder get() {
        return row().getValue(0);
    }

    @Override
    public String getString() {
        return row().getString(0);
    }

    @Override
    public BigDecimal getBigDecimal() {
        return row().getBigDecimal(0);
    }

    @Override
    public Date getDate() {
        return row().getDate(0);
    }

    @Override
    public Integer getInteger() {
        return row().getInteger(0);
    }

    @Override
    public Long getLong() {
        return row().getLong(0);
    }

    @Override
    public <T extends Enum<T>> T getEnum(Class<T> enumClass) {
        return row().getEnum(0, enumClass);
    }

    @Override
    public int size() {
        return rows != null ? rows.length : 0;
    }

    private int index(String name) {
        if (indexMap != null) {
            Integer k = indexMap.get(name);
            if (k != null) {
                return k;
            }
        }

        throw new SmartParamException("Unknown level name: " + name);
    }

    @Override
    public String toString() {
        String header = "ParamValue " + indexMap;
        return Printer.print(Arrays.asList(rows), header, 0, new MultiValueInlineFormatter());
    }

    static final class MultiValueInlineFormatter implements Formatter {

        @Override
        public String format(Object obj) {
            MultiValue mv = (MultiValue) obj;
            return mv.toStringInline();
        }
    }
}
