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
import org.smartparam.engine.core.parameter.Parameter;

/**
 *
 * @author Adam Dubiel
 */
public class ParameterAssert extends AbstractAssert<ParameterAssert, Parameter> {

    private ParameterAssert(Parameter actual) {
        super(actual, ParameterAssert.class);
    }

    public static ParameterAssert assertThat(Parameter actual) {
        return new ParameterAssert(actual);
    }

    public LevelAssert level(int levelIndex) {
        return LevelAssert.assertThat(actual.getLevels().get(levelIndex));
    }

    public ParameterAssert hasName(String name) {
        Assertions.assertThat(actual.getName()).isEqualTo(name);
        return this;
    }

    public ParameterAssert isNotNullable() {
        Assertions.assertThat(actual.isNullable()).isFalse();
        return this;
    }

    public ParameterAssert isNullable() {
        Assertions.assertThat(actual.isNullable()).isTrue();
        return this;
    }

    public ParameterAssert isCacheable() {
        Assertions.assertThat(actual.isCacheable()).isTrue();
        return this;
    }

    public ParameterAssert isNotCacheable() {
        Assertions.assertThat(actual.isCacheable()).isFalse();
        return this;
    }

    public ParameterAssert hasArraySeparator(char arraySeparator) {
        Assertions.assertThat(actual.getArraySeparator()).isEqualTo(arraySeparator);
        return this;
    }

    public ParameterAssert hasInputLevels(int inputLevels) {
        Assertions.assertThat(actual.getInputLevels()).isEqualTo(inputLevels);
        return this;
    }

    public ParameterAssert hasLevels(int levelCount) {
        Assertions.assertThat(actual.getLevels()).hasSize(levelCount);
        return this;
    }

    public ParameterAssert hasEntries(int entryCount) {
        Assertions.assertThat(actual.getEntries()).hasSize(entryCount);
        return this;
    }

    public ParameterAssert hasNoEntries() {
        Assertions.assertThat(actual.getEntries()).isEmpty();
        return this;
    }
}