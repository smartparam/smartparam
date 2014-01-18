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

import org.smartparam.engine.core.parameter.level.Level;
import org.smartparam.engine.core.parameter.entry.ParameterEntry;
import java.util.Arrays;
import java.util.HashSet;

/**
 *
 * @author Adam Dubiel
 */
public class ParameterTestBuilder {

    private final TestParameter parameter = new TestParameter();

    public static ParameterTestBuilder parameter() {
        return new ParameterTestBuilder();
    }

    public Parameter build() {
        return parameter;
    }

    public ParameterTestBuilder withName(String name) {
        parameter.name = name;
        return this;
    }

    public ParameterTestBuilder noncacheable() {
        parameter.cacheable = false;
        return this;
    }

    public ParameterTestBuilder nullable() {
        parameter.nullable = true;
        return this;
    }

    public ParameterTestBuilder identifyEntries() {
        parameter.identifyEntries = true;
        return this;
    }

    public ParameterTestBuilder withInputLevels(int inputLevels) {
        parameter.inputLevels = inputLevels;
        return this;
    }

    public ParameterTestBuilder withArraySeparator(char separator) {
        parameter.arraySeparator = separator;
        return this;
    }

    public ParameterTestBuilder withLevels(Level... levels) {
        parameter.levels = Arrays.asList(levels);
        return this;
    }

    public ParameterTestBuilder withEntries(ParameterEntry... entries) {
        parameter.entries = new HashSet<ParameterEntry>(Arrays.asList(entries));
        return this;
    }
}
