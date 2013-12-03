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

import java.io.StringWriter;
import org.junit.Before;
import org.junit.Test;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.serializer.exception.ParamSerializationException;
import org.smartparam.serializer.test.builder.StringStreamUtil;
import static org.smartparam.engine.test.assertions.Assertions.assertThat;
import static org.smartparam.engine.model.LevelTestBuilder.level;
import static org.smartparam.engine.model.ParameterTestBuilder.parameter;

/**
 *
 * @author Adam Dubiel
 */
public class JsonParameterMetadataSerializerIntegrationTest {

    private JsonParameterMetadataSerializer serializer;

    private JsonParameterMetadataDeserializer deserializer;

    @Before
    public void initialize() {
        serializer = new JsonParameterMetadataSerializer();
        deserializer = new JsonParameterMetadataDeserializer();
    }

    @Test
    public void shouldBeAbleToDeserialieSerializedParameterMetadata() throws ParamSerializationException {
        // given
        Level[] levels = new Level[]{
            level().withName("level1").build(),
            level().withName("level2").build()
        };
        Parameter parameter = parameter().withName("parameter").withInputLevels(3)
                .withLevels(levels)
                .build();
        StringWriter output = new StringWriter();

        // when
        serializer.serialize(parameter, output);
        output.flush();
        Parameter deserializedParameter = deserializer.deserialize(StringStreamUtil.reader(output.toString()));

        // then
        assertThat(deserializedParameter).hasName("parameter").isNotNullable()
                .isCacheable().hasInputLevels(3).hasLevels(2);
    }
}
