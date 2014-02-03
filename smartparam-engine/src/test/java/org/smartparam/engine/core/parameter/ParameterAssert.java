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
package org.smartparam.engine.core.parameter;

import org.smartparam.engine.core.parameter.level.LevelAssert;
import org.smartparam.engine.core.parameter.entry.ParameterEntryAssert;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.smartparam.engine.test.Iterables;
import org.smartparam.engine.test.ParamEngineAssertions;

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

    public LevelAssert firstLevel() {
        return level(0);
    }

    public ParameterEntryAssert onlyEntry() {
        hasEntries(1);
        return ParameterEntryAssert.assertThat(Iterables.onlyElement(actual.getEntries()));
    }

    public ParameterAssert namedLevelsInOrder(String... levelNames) {
        int index = 0;
        for (String levelName : levelNames) {
            Assertions.assertThat(actual.getLevels().get(index).getName()).isEqualTo(levelName);
            index++;
        }
        return this;
    }

    public ParameterAssert hasName(String name) {
        ParamEngineAssertions.assertThat(actual.getName()).isEqualTo(name);
        return this;
    }

    public ParameterAssert isNotNullable() {
        ParamEngineAssertions.assertThat(actual.isNullable()).isFalse();
        return this;
    }

    public ParameterAssert isNullable() {
        ParamEngineAssertions.assertThat(actual.isNullable()).isTrue();
        return this;
    }

    public ParameterAssert isCacheable() {
        ParamEngineAssertions.assertThat(actual.isCacheable()).isTrue();
        return this;
    }

    public ParameterAssert isNotCacheable() {
        ParamEngineAssertions.assertThat(actual.isCacheable()).isFalse();
        return this;
    }

    public ParameterAssert identifyEntries() {
        ParamEngineAssertions.assertThat(actual.isIdentifyEntries()).isTrue();
        return this;
    }

    public ParameterAssert dontIdentifyEntries() {
        ParamEngineAssertions.assertThat(actual.isIdentifyEntries()).isFalse();
        return this;
    }

    public ParameterAssert hasArraySeparator(char arraySeparator) {
        ParamEngineAssertions.assertThat(actual.getArraySeparator()).isEqualTo(arraySeparator);
        return this;
    }

    public ParameterAssert hasInputLevels(int inputLevels) {
        ParamEngineAssertions.assertThat(actual.getInputLevels()).isEqualTo(inputLevels);
        return this;
    }

    public ParameterAssert hasNoLevels() {
        return hasLevels(0);
    }

    public ParameterAssert hasLevels(int levelCount) {
        ParamEngineAssertions.assertThat(actual.getLevels()).hasSize(levelCount);
        return this;
    }

    public ParameterAssert hasEntries(int entryCount) {
        ParamEngineAssertions.assertThat(actual.getEntries()).hasSize(entryCount);
        return this;
    }

    public ParameterAssert hasNoEntries() {
        ParamEngineAssertions.assertThat(actual.getEntries()).isEmpty();
        return this;
    }
}
