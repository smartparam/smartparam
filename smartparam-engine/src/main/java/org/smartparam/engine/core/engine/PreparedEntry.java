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
package org.smartparam.engine.core.engine;

import java.util.Arrays;
import org.smartparam.engine.model.function.Function;

/**
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class PreparedEntry {

    //todo ph: par 2 clean, toString prepared entry
    private String[] levels;

    @Deprecated
    private String value;

    @Deprecated
    private Function function;

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public String[] getLevels() {
        return levels;
    }

    public void setLevels(String[] levels) {
        this.levels = trimRight(levels);
        //todo ph: par 1 preparedEntry: level=null -> ""
    }

    String[] trimRight(String[] array) {
        if (array == null) {
            return EMPTY_ARRAY;
        }

        int len = array.length;
        while (len > 0 && array[len - 1] == null) {
            --len;
        }

        // len to dlugosc tablicy z pominieciem koncowych null (jesli wystepuja)
        return len < array.length ? Arrays.copyOf(array, len) : array;
    }

    public String getLevel(int k) {
        return (k >= 1 && k <= levels.length) ? levels[k - 1] : null;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private static final String[] EMPTY_ARRAY = {};

}
