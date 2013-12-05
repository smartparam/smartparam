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

import org.fest.assertions.api.AbstractAssert;
import org.smartparam.engine.core.parameter.ParameterEntry;
import org.smartparam.engine.test.ParamEngineAssertions;

/**
 *
 * @author Adam Dubiel
 */
public class ParameterEntryAssert extends AbstractAssert<ParameterEntryAssert, ParameterEntry> {

    private ParameterEntryAssert(ParameterEntry actual) {
        super(actual, ParameterEntryAssert.class);
    }

    public static ParameterEntryAssert assertThat(ParameterEntry actual) {
        return new ParameterEntryAssert(actual);
    }

    public ParameterEntryAssert hasLevels(int levelCount) {
        ParamEngineAssertions.assertThat(actual.getLevels()).hasSize(levelCount);
        return this;
    }

    public ParameterEntryAssert hasLevels(String... values) {
        ParamEngineAssertions.assertThat(actual.getLevels()).containsExactly(values);
        return this;
    }

    public ParameterEntryAssert hasDifferentLevelsThan(String... values) {
        ParamEngineAssertions.assertThat(actual.getLevels()).doesNotContain(values);
        return this;
    }

    public ParameterEntryAssert levelAtEquals(int levelIndex, String value) {
        ParamEngineAssertions.assertThat(actual.getLevels()[levelIndex]).isEqualTo(value);
        return this;
    }
}