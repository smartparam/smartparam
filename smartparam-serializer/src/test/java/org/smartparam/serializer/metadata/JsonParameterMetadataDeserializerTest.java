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
package org.smartparam.serializer.metadata;

import java.io.BufferedReader;
import org.junit.Before;
import org.junit.Test;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.serializer.exception.ParamSerializationException;
import org.smartparam.serializer.test.builder.StringStreamUtil;
import static org.smartparam.serializer.test.assertions.SerializerAssertions.*;

/**
 *
 * @author Adam Dubiel
 */
public class JsonParameterMetadataDeserializerTest {

    private JsonParameterMetadataDeserializer deserializer;

    @Before
    public void initialize() {
        deserializer = new JsonParameterMetadataDeserializer();
    }

    @Test
    public void shouldDeserializeParameterMetadataSectionFromJSON() throws ParamSerializationException {
        // given
        String json = "{ \"name\": \"parameter\", \"cacheable\": \"true\","
                + "\"nullable\": \"true\", \"inputLevels\": 1, \"levels\": ["
                + "{\"name\": \"level1\", \"levelCreator\": \"level1Creator\", \"type\": \"level1Type\", \"matcher\": \"level1Matcher\"},"
                + "{\"name\": \"level2\"}"
                + "]}";

        // when
        Parameter parameter = deserializer.deserialize(StringStreamUtil.reader(json));

        // then
        assertThat(parameter).hasName("parameter").isCacheable().isNullable().hasInputLevels(1).hasLevels(2)
                .level(0).hasName("level1").hasLevelCreator("level1Creator")
                .hasType("level1Type").hasMatcher("level1Matcher");
    }

    @Test
    public void shouldDeserializeNonStrictJSON() throws ParamSerializationException {
        // given
        String json = "{ name: \"parameter\" }";

        // when
        Parameter parameter = deserializer.deserialize(StringStreamUtil.reader(json));

        // then
        assertThat(parameter).isNotNull().hasName("parameter");
    }

    @Test
    public void shouldDeserializeOnlyFirstJSONObject() throws Exception {
        // given
        String json = "{ name: \"parameter\" }\n"
                + "hello;hello";
        BufferedReader reader = StringStreamUtil.reader(json);

        // when
        Parameter parameter = deserializer.deserialize(reader);

        // then
        assertThat(parameter).isNotNull().hasName("parameter");
        assertThat(reader).hasTextLeft("\nhello;hello");
    }
}
