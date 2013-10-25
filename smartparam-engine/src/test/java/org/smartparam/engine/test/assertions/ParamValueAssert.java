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
package org.smartparam.engine.test.assertions;

import org.fest.assertions.api.AbstractAssert;
import org.smartparam.engine.core.engine.MultiValue;
import org.smartparam.engine.core.engine.ParamValue;
import org.smartparam.engine.core.type.AbstractHolder;

/**
 *
 * @author Adam Dubiel
 */
public class ParamValueAssert extends AbstractAssert<ParamValueAssert, ParamValue> {

    private ParamValueAssert(ParamValue actual) {
        super(actual, ParamValueAssert.class);
    }

    public static ParamValueAssert assertThat(ParamValue actual) {
        return new ParamValueAssert(actual);
    }

    public ParamValueAssert hasValue(Object value) {
        Assertions.assertThat(actual.get().getValue()).isEqualTo(value);
        return this;
    }

    public ParamValueAssert hasRowWithValues(Object... values) {
        boolean anyMatches = false;
        for(MultiValue row : actual.rows()) {
            if(rowValuesMatch(row, values)) {
                anyMatches = true;
                break;
            }
        }

        Assertions.assertThat(anyMatches).isTrue();
        return this;
    }

    private boolean rowValuesMatch(MultiValue row, Object... values) {
        boolean matches = true;
        int index = 0;
        for(Object rowValue : row.unwrap()) {
            if(!rowValue.equals(values[index])) {
                matches = false;
                break;
            }
            index++;
        }
        return matches;
    }

    public ParamValueAssert hasSingleRow(Object... values) {
        Assertions.assertThat(actual.row().unwrap()).containsSequence(values);
        return this;
    }

    public ParamValueAssert hasArray(int levelIndex, Object... values) {
        int index = 0;
        for (AbstractHolder holder : actual.row().getArray(levelIndex)) {
            Assertions.assertThat(holder.getValue()).isEqualTo(values[index]);
            index++;
        }
        return this;
    }

    public ParamValueAssert hasRows(int count) {
        Assertions.assertThat(actual.rows()).hasSize(count);
        return this;
    }
}
