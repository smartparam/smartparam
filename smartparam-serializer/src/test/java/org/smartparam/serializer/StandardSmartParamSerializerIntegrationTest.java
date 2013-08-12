package org.smartparam.serializer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import org.junit.Before;
import org.junit.Test;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.engine.model.editable.SimpleEditableLevel;
import org.smartparam.engine.model.editable.SimpleEditableParameter;
import org.smartparam.engine.model.editable.SimpleEditableParameterEntry;
import static org.smartparam.engine.test.assertions.Assertions.*;
import static org.smartparam.engine.test.builder.LevelTestBuilder.level;
import static org.smartparam.engine.test.builder.ParameterEntryTestBuilder.parameterEntry;
import static org.smartparam.engine.test.builder.ParameterTestBuilder.parameter;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class StandardSmartParamSerializerIntegrationTest {

    private StandardParamSerializer serializer;

    private StandardParamDeserializer deserializer;

    @Before
    public void initialize() {
        SerializationConfig config = new StandardSerializationConfig('"', ';', '#', "\n", "UTF-8");
        serializer = new StandardParamSerializer(config, SimpleEditableParameterEntry.class);
        deserializer = new StandardParamDeserializer(config, SimpleEditableParameter.class, SimpleEditableLevel.class, SimpleEditableParameterEntry.class);
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
        Level[] levels = new Level[] {
            level().withName("level1").build(),
            level().withName("level2").build()
        };
        ParameterEntry[] entries = new ParameterEntry[] {
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
