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
package org.smartparam.engine.test.builder;

import java.util.Arrays;
import java.util.HashSet;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.engine.model.editable.SimpleEditableParameter;

/**
 *
 * @author Adam Dubiel
 */
public class ParameterTestBuilder {

    private SimpleEditableParameter parameter = new SimpleEditableParameter();

    private ParameterTestBuilder() {
        parameter.setCacheable(true);
    }

    public static ParameterTestBuilder parameter() {
        return new ParameterTestBuilder();
    }

    public Parameter build() {
        return parameter;
    }

    public ParameterTestBuilder withName(String name) {
        parameter.setName(name);
        return this;
    }

    public ParameterTestBuilder noncacheable() {
        parameter.setCacheable(false);
        return this;
    }

    public ParameterTestBuilder nullable() {
        parameter.setNullable(true);
        return this;
    }

    public ParameterTestBuilder withInputLevels(int inputLevels) {
        parameter.setInputLevels(inputLevels);
        return this;
    }

    public ParameterTestBuilder withArraySeparator(char separator) {
        parameter.setArraySeparator(separator);
        return this;
    }

    public ParameterTestBuilder withLevels(Level... levels) {
        parameter.setLevels(Arrays.asList(levels));
        return this;
    }

    public ParameterTestBuilder withEntries(ParameterEntry... entries) {
        parameter.setEntries(new HashSet<ParameterEntry>(Arrays.asList(entries)));
        return this;
    }
}
