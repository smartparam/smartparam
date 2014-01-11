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
import org.smartparam.engine.core.type.ValueHolder;
import org.smartparam.engine.util.Formatter;
import org.smartparam.engine.util.Printer;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class ParamValueImpl implements ParamValue {

    private static final MultiValue[] EMPTY = {};

    private final MultiValue[] rows;

    public ParamValueImpl(MultiValue[] rows) {
        this.rows = Arrays.copyOf(rows, rows.length);
    }

    public static ParamValueImpl empty() {
        return new ParamValueImpl(EMPTY);
    }

    @Override
    public boolean isEmpty() {
        return rows == EMPTY || rows.length == 0;
    }

    @Override
    public MultiValue row(int rowNo) {
        if (rowNo >= 0 && rowNo < size()) {
            return rows[rowNo];
        }
        throw new InvalidRowIndexException(rowNo, rows);
    }

    @Override
    public MultiValue row() {
        return row(0);
    }

    @Override
    public List<MultiValue> rows() {
        return Arrays.asList(rows);
    }

    @Override
    public Iterator<MultiValue> iterator() {
        return rows().iterator();
    }

    @Override
    public ValueHolder getHolder(int rowNo, int colNo) {
        return row(rowNo).getHolder(colNo);
    }

    @Override
    public <T> T get(int rowNo, int colNo) {
        return row(rowNo).get(colNo);
    }

    @Override
    public <T> T get(int rowNo, int colNo, Class<T> clazz) {
        return row(rowNo).get(colNo, clazz);
    }

    @Override
    public ValueHolder getHolder(int rowNo, String name) {
        return row(rowNo).getHolder(name);
    }

    @Override
    public <T> T get(int rowNo, String name) {
        return row(rowNo).get(name);
    }

    @Override
    public <T> T get(int rowNo, String name, Class<T> clazz) {
        return row(rowNo).get(name, clazz);
    }

    @Override
    public ValueHolder getHolder(int colNo) {
        return row().getHolder(colNo);
    }

    @Override
    public <T> T get(int colNo) {
        return row().get(colNo);
    }

    @Override
    public <T> T get(int colNo, Class<T> clazz) {
        return row().get(colNo, clazz);
    }

    @Override
    public ValueHolder getHolder(String name) {
        return row().getHolder(name);
    }

    @Override
    public <T> T get(String name) {
        return row().get(name);
    }

    public <T> T get(String name, Class<T> clazz) {
        return row().get(name, clazz);
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
    public ValueHolder getHolder() {
        return row().getHolder(0);
    }

    @Override
    public <T> T get() {
        return row().get(0);
    }

    @Override
    public <T> T get(Class<T> clazz) {
        return row().get(0, clazz);
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

    @Override
    public String toString() {
        return Printer.print(Arrays.asList(rows), "ParamValue", 0, new MultiValueInlineFormatter());
    }

    static final class MultiValueInlineFormatter implements Formatter {

        @Override
        public String format(Object obj) {
            MultiValue mv = (MultiValue) obj;
            return mv.toStringInline();
        }
    }
}
