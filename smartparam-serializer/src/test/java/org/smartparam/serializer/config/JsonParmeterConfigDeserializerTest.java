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
package org.smartparam.serializer.config;

import org.junit.Before;
import org.junit.Test;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.editable.SimpleEditableLevel;
import org.smartparam.engine.model.editable.SimpleEditableParameter;
import static org.smartparam.engine.test.assertions.Assertions.*;

/**
 *
 * @author Adam Dubiel
 */
public class JsonParmeterConfigDeserializerTest {

    private JsonParameterConfigDeserializer deserializer;

    @Before
    public void initialize() {
        deserializer = new JsonParameterConfigDeserializer(SimpleEditableParameter.class, SimpleEditableLevel.class);
    }

    @Test
    public void shouldDeserializeParaeterConfigSectionFromJSON() {
        // given
        String json = "{ \"name\": \"parameter\", \"cacheable\": \"true\","
                + "\"nullable\": \"true\", \"inputLevels\": 1, \"levels\": ["
                + "{\"name\": \"level1\", \"levelCreator\": \"level1Creator\", \"type\": \"level1Type\", \"matcher\": \"level1Matcher\"},"
                + "{\"name\": \"level2\"}"
                + "]}";

        // when
        Parameter parameter = deserializer.deserialize(json);

        // then
        assertThat(parameter).hasName("parameter").isCacheable().isNullable().hasInputLevels(1).hasLevels(2)
                .level(0).hasName("level1").hasLevelCreator("level1Creator")
                .hasType("level1Type").hasMatcher("level1Matcher");
    }

    @Test
    public void shouldDeserializeNonStrictJSON() {
        // given
        String json = "{ name: \"parameter\" }";

        // when
        Parameter parameter = deserializer.deserialize(json);

        // then
        assertThat(parameter).isNotNull().hasName("parameter");
    }
}
