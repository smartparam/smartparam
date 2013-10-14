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

import java.io.StringWriter;
import org.junit.Before;
import org.junit.Test;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.engine.model.editable.SimpleEditableParameter;
import static org.fest.assertions.api.Assertions.*;
import static org.smartparam.engine.test.builder.LevelTestBuilder.level;
import static org.smartparam.engine.test.builder.ParameterEntryTestBuilder.parameterEntry;
import static org.smartparam.engine.test.builder.ParameterTestBuilder.parameter;

/**
 *
 * @author Adam Dubiel
 */
public class JsonParameterConfigSerializerTest {

    private JsonParameterConfigSerializer serializer;

    @Before
    public void initialize() {
        serializer = new JsonParameterConfigSerializer(SimpleEditableParameter.class);
    }

    @Test
    public void shouldSerializeParameterConfigWithoutEntriesToJSON() {
        // given
        Level[] levels = new Level[]{
            level().withName("level1").build(),
            level().withName("level2").build()
        };
        ParameterEntry[] entries = new ParameterEntry[]{
            parameterEntry().withLevels("1").build()
        };
        Parameter parameter = parameter().withName("parameter").withInputLevels(3)
                .withLevels(levels).withEntries(entries)
                .build();
        StringWriter output = new StringWriter();

        // when
        serializer.serialize(parameter, output);
        output.flush();

        // then
        assertThat(output.toString()).isNotEmpty().contains("levels").contains("parameter")
                .doesNotContain("entries");
    }
}
