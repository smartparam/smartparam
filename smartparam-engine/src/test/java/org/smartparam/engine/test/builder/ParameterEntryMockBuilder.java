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

import org.smartparam.engine.model.function.Function;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.engine.util.EngineUtil;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * {@link ParameterEntry} mock object builder.
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public class ParameterEntryMockBuilder {

    private ParameterEntry parameterEntry;

    private ParameterEntryMockBuilder() {
        this.parameterEntry = mock(ParameterEntry.class);
    }

    public static ParameterEntryMockBuilder parameterEntry() {
        return new ParameterEntryMockBuilder();
    }

    public static ParameterEntry parameterEntry(String... levels) {
        return parameterEntry().withLevels(levels).get();
    }

    public static ParameterEntry parameterEntryCsv(String csvLevels) {
        return parameterEntry().withLevels(EngineUtil.split(csvLevels, ';')).get();
    }

    public ParameterEntry get() {
        return parameterEntry;
    }

    public ParameterEntryMockBuilder withLevels(String... levels) {
        when(parameterEntry.getLevels()).thenReturn(levels);
        return this;
    }

    public ParameterEntryMockBuilder withFunction(String function) {
        when(parameterEntry.getFunction()).thenReturn(function);
        return this;
    }
}
