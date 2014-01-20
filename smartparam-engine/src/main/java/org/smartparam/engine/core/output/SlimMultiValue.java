/*
 * Copyright 2014 Adam Dubiel.
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
import org.smartparam.engine.core.parameter.entry.ParameterEntryKey;
import org.smartparam.engine.core.type.ValueHolder;
import org.smartparam.engine.util.Printer;

/**
 *
 * @author Adam Dubiel
 */
public class SlimMultiValue implements MultiValue {

    private static final int INLINE_TO_STRING_LENGTH = 100;

    /**
     * Values held, each entry is either AbstractHolder or AbstractHolder[].
     */
    private final Object[] values;

    private Map<String, Integer> indexMap;

    private ParameterEntryKey key;

    /**
     * Keeps iteration state, used to power next* methods.
     */
    private int last = 0;

    public SlimMultiValue(Object[] values) {
        this.values = Arrays.copyOf(values, values.length);
    }

    public SlimMultiValue(Object[] values, Map<String, Integer> indexMap) {
        this(values);
        this.indexMap = indexMap;
    }

    public SlimMultiValue(ParameterEntryKey key, Object[] values, Map<String, Integer> indexMap) {
        this(values, indexMap);
        this.key = key;
    }

    @Override
    public ParameterEntryKey getKey() {
        if (key == null) {
            throw new GettingKeyNotIdentifiableParameterException();
        }
        return key;
    }

    @Override
    public ValueHolder getHolder(int position) {
        Object obj = getAbstractHolder(position);

        if (obj instanceof ValueHolder) {
            return (ValueHolder) obj;
        }

        throw new GettingWrongTypeException(position, "AbstractHolder", obj);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(int position) {
        return (T) getHolder(position).getValue();
    }

    @Override
    public <T> T get(int position, Class<T> clazz) {
        return clazz.cast(getHolder(position).getValue());
    }

    @Override
    public String getString(int position) {
        return getHolder(position).getString();
    }

    @Override
    public BigDecimal getBigDecimal(int position) {
        return getHolder(position).getBigDecimal();
    }

    @Override
    public Date getDate(int position) {
        return getHolder(position).getDate();
    }

    @Override
    public Integer getInteger(int position) {
        return getHolder(position).getInteger();
    }

    @Override
    public Long getLong(int position) {
        return getHolder(position).getLong();
    }

    @Override
    public ValueHolder getHolder(String name) {
        return getHolder(index(name));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String name) {
        return (T) getHolder(index(name)).getValue();
    }

    @Override
    public <T> T get(String name, Class<T> clazz) {
        return clazz.cast(getHolder(index(name)).getValue());
    }

    @Override
    public String getString(String name) {
        return getString(index(name));
    }

    @Override
    public BigDecimal getBigDecimal(String name) {
        return getBigDecimal(index(name));
    }

    @Override
    public Date getDate(String name) {
        return getDate(index(name));
    }

    @Override
    public Integer getInteger(String name) {
        return getInteger(index(name));
    }

    @Override
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

    @Override
    public <T extends Enum<T>> T getEnum(int position, Class<T> enumClass) {
        String code = getString(position);
        return code != null ? codeToEnum(code, enumClass) : null;
    }

    @Override
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

    @Override
    public ValueHolder[] getArray(int position) {
        Object obj = getAbstractHolder(position);

        if (obj instanceof ValueHolder[]) {
            return (ValueHolder[]) obj;
        }

        throw new GettingWrongTypeException(position, "AbstractHolder[]", obj);
    }

    @Override
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

    @Override
    public String[] getStringArray(int position) {
        ValueHolder[] array = getArray(position);
        String[] result = new String[array.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = array[i].getString();
        }
        return result;
    }

    @Override
    public Date[] getDateArray(int position) {
        ValueHolder[] array = getArray(position);
        Date[] result = new Date[array.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = array[i].getDate();
        }
        return result;
    }

    @Override
    public Integer[] getIntegerArray(int position) {
        ValueHolder[] array = getArray(position);
        Integer[] result = new Integer[array.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = array[i].getInteger();
        }
        return result;
    }

    @Override
    public Long[] getLongArray(int position) {
        ValueHolder[] array = getArray(position);
        Long[] result = new Long[array.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = array[i].getLong();
        }
        return result;
    }

    @Override
    public BigDecimal[] getBigDecimalArray(int position) {
        ValueHolder[] array = getArray(position);
        BigDecimal[] result = new BigDecimal[array.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = array[i].getBigDecimal();
        }
        return result;
    }

    @Override
    public ValueHolder[] getArray(String name) {
        return getArray(index(name));
    }

    @Override
    public String[] getStringArray(String name) {
        return getStringArray(index(name));
    }

    @Override
    public BigDecimal[] getBigDecimalArray(String name) {
        return getBigDecimalArray(index(name));
    }

    @Override
    public Date[] getDateArray(String name) {
        return getDateArray(index(name));
    }

    @Override
    public Integer[] getIntegerArray(String name) {
        return getIntegerArray(index(name));
    }

    @Override
    public Long[] getLongArray(String name) {
        return getLongArray(index(name));
    }

    private Object getAbstractHolder(int position) {
        if (position >= 0 && position < values.length) {
            return values[position];
        }
        throw new InvalidValueIndexException(position, values);
    }

    @Override
    public String[] asStrings() {
        String[] array = new String[values.length];
        for (int i = 0; i < array.length; i++) {
            array[i] = getString(i);
        }
        return array;
    }

    @Override
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
    @Override
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

    @Override
    public ValueHolder nextHolder() {
        return getHolder(nextPosition());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T next() {
        return (T) nextHolder().getValue();
    }

    @Override
    public <T> T next(Class<T> clazz) {
        return clazz.cast(nextHolder().getValue());
    }

    @Override
    public String nextString() {
        return getString(nextPosition());
    }

    @Override
    public BigDecimal nextBigDecimal() {
        return getBigDecimal(nextPosition());
    }

    @Override
    public Date nextDate() {
        return getDate(nextPosition());
    }

    @Override
    public Integer nextInteger() {
        return getInteger(nextPosition());
    }

    @Override
    public Long nextLong() {
        return getLong(nextPosition());
    }

    @Override
    public <T extends Enum<T>> T nextEnum(Class<T> enumClass) {
        return getEnum(nextPosition(), enumClass);
    }

    @Override
    public ValueHolder[] nextArray() {
        return getArray(nextPosition());
    }

    @Override
    public String[] nextStringArray() {
        return getStringArray(nextPosition());
    }

    @Override
    public BigDecimal[] nextBigDecimalArray() {
        return getBigDecimalArray(nextPosition());
    }

    @Override
    public Date[] nextDateArray() {
        return getDateArray(nextPosition());
    }

    @Override
    public Integer[] nextIntegerArray() {
        return getIntegerArray(nextPosition());
    }

    @Override
    public Long[] nextLongArray() {
        return getLongArray(nextPosition());
    }

    @Override
    public int size() {
        return values != null ? values.length : 0;
    }

}
