package org.smartparam.serializer.config;

import org.junit.Before;
import org.junit.Test;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.editable.SimpleEditableLevel;
import org.smartparam.engine.model.editable.SimpleEditableParameter;
import static org.smartparam.engine.test.assertions.Assertions.assertThat;
import static org.smartparam.engine.test.builder.LevelTestBuilder.level;
import static org.smartparam.engine.test.builder.ParameterTestBuilder.parameter;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class JsonParameterConfigSerializerIntegrationTest {

    private JsonParameterConfigSerializer serializer;

    private JsonParameterConfigDeserializer deserializer;

    @Before
    public void initialize() {
        serializer = new JsonParameterConfigSerializer();
        deserializer = new JsonParameterConfigDeserializer(SimpleEditableParameter.class, SimpleEditableLevel.class);
    }

    @Test
    public void shouldBeAbleToDeserialieSerializedParameter() {
        // given
        Level[] levels = new Level[] {
            level().withName("level1").build(),
            level().withName("level2").build()
        };
        Parameter parameter = parameter().withName("parameter").cacheable(true)
                .nullable(false).withInputLevels(3)
                .withLevels(levels)
                .build();

        // when
        String serializedConfig = serializer.serialize(parameter);
        Parameter deserializedParameter = deserializer.deserialize(serializedConfig);

        // then
        assertThat(deserializedParameter).hasName("parameter").isNotNullable()
                .isCacheable().hasInputLevels(3).hasLevels(2);
    }
}
