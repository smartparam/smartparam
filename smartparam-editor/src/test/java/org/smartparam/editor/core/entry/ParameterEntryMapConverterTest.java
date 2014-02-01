/*
 * Copyright 2014 Adam Dubiel, Przemek Hertel.
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
package org.smartparam.editor.core.entry;

import org.smartparam.editor.model.simple.SimpleLevel;
import org.smartparam.editor.model.simple.SimpleParameter;
import org.smartparam.editor.model.simple.SimpleParameterEntry;
import org.smartparam.engine.config.ParamEngineConfig;
import org.smartparam.engine.config.ParamEngineConfigBuilder;
import org.smartparam.engine.config.ParamEngineFactory;
import org.smartparam.engine.core.output.entry.MapEntry;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.engine.core.parameter.entry.ParameterEntry;
import org.smartparam.engine.matchers.BetweenMatcher;
import org.smartparam.engine.matchers.type.SimpleMatcherType;
import org.smartparam.engine.types.date.DateType;
import org.smartparam.engine.types.string.StringType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.smartparam.engine.test.ParamEngineAssertions.assertThat;

/**
 *
 * @author Adam Dubiel
 */
public class ParameterEntryMapConverterTest {

    private ParameterEntryMapConverter converter;

    @BeforeMethod
    public void setUp() {
        ParamEngineConfig config = ParamEngineConfigBuilder.paramEngineConfig()
                .withAnnotationScanDisabled()
                .withType("string", new StringType())
                .withType("date", new DateType())
                .withMatcher("between/ie", new BetweenMatcher())
                .withMatcherType("between/ie", new SimpleMatcherType())
                .build();
        converter = new ParameterEntryMapConverter(ParamEngineFactory.paramEngine(config));
    }

    @Test
    public void shouldProduceInterchangeableResults() {
        // given
        Parameter metadata = new SimpleParameter()
                .withLevel(new SimpleLevel().withName("level1"))
                .withLevel(new SimpleLevel().withName("level2"));
        ParameterEntry entry = new SimpleParameterEntry("value1", "value2");

        // when
        MapEntry map = converter.asMap(metadata, entry);
        ParameterEntry convertedEntry = converter.asEntry(metadata, map);

        // then
        assertThat(convertedEntry).hasLevels("value1", "value2");
    }

}
