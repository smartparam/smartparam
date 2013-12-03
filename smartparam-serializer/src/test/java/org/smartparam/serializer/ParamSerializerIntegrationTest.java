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
package org.smartparam.serializer;

import org.smartparam.serializer.config.SerializationConfig;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import org.junit.Before;
import org.junit.Test;
import org.smartparam.engine.core.parameter.Level;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.engine.core.parameter.ParameterEntry;
import static org.smartparam.engine.test.assertions.Assertions.*;
import static org.smartparam.engine.model.LevelTestBuilder.level;
import static org.smartparam.engine.model.ParameterEntryTestBuilder.parameterEntry;
import static org.smartparam.engine.model.ParameterTestBuilder.parameter;
import static org.smartparam.serializer.config.SerializationConfigBuilder.serializationConfig;

/**
 *
 * @author Adam Dubiel
 */
public class ParamSerializerIntegrationTest {

    private ParamSerializer serializer;

    private ParamDeserializer deserializer;

    @Before
    public void initialize() {
        SerializationConfig config = serializationConfig()
                .withCharset("UTF-8").withCsvDelimiter(';').withCsvQuote('"').withEndOfLine("\n").build();
        serializer = ParamSerializerFactory.paramSerializer(config);
        deserializer = ParamSerializerFactory.paramDeserializer(config);
    }

    @Test
    public void shouldDeserializeParameterFromTestFile() throws Exception {
        // given
        BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/sampleParam.csv")));

        // when
        Parameter parameter = deserializer.deserialize(reader);

        // then
        assertThat(parameter).hasName("testParameter").isCacheable().isNotNullable()
                .hasInputLevels(4).hasLevels(3).hasEntries(2);
    }

    @Test
    public void shouldBeAbleToDeserializeOnceSerializedParameter() throws Exception {
        // given
        Level[] levels = new Level[]{
            level().withName("level1").build(),
            level().withName("level2").build()
        };
        ParameterEntry[] entries = new ParameterEntry[]{
            parameterEntry().withLevels("level1").build(),
            parameterEntry().withLevels("level1").build()
        };
        Parameter parameter = parameter().withName("parameter").withInputLevels(3)
                .withLevels(levels).withEntries(entries).build();
        StringWriter paramWriter = new StringWriter();
        serializer.serialize(parameter, paramWriter);

        BufferedReader reader = new BufferedReader(new StringReader(paramWriter.toString()));

        // when
        Parameter processedParameter = deserializer.deserialize(reader);

        // then
        assertThat(processedParameter).hasName("parameter").isNotNullable()
                .isCacheable().hasInputLevels(3).hasLevels(2).hasEntries(2);
    }
}
