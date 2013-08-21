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
import org.smartparam.engine.core.engine.PreparedParameter;

/**
 *
 * @author Adam Dubiel
 */
public class PreparedParameterAssert extends AbstractAssert<PreparedParameterAssert, PreparedParameter> {

    private PreparedParameterAssert(PreparedParameter actual) {
        super(actual, PreparedParameterAssert.class);
    }

    public static PreparedParameterAssert assertThat(PreparedParameter actual) {
        return new PreparedParameterAssert(actual);
    }

    public PreparedParameterAssert hasIndex() {
        Assertions.assertThat(actual.getIndex()).isNotNull();
        return this;
    }

    public PreparedParameterAssert hasName(String name) {
        Assertions.assertThat(actual.getName()).isSameAs(name);
        return this;
    }

    public PreparedParameterAssert hasInputLevels(int inputLevels) {
        Assertions.assertThat(actual.getInputLevelsCount()).isEqualTo(inputLevels);
        return this;
    }

    public PreparedParameterAssert hasArraySeparator(char separator) {
        Assertions.assertThat(actual.getArraySeparator()).isSameAs(separator);
        return this;
    }

    public PreparedLevelAssert level(int index) {
        return PreparedLevelAssert.assertThat(actual.getLevels()[index]);
    }
}