package org.smartparam.serializer.config;

import org.junit.Before;
import org.junit.Test;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import static org.fest.assertions.api.Assertions.*;
import static org.smartparam.engine.test.builder.LevelTestBuilder.level;
import static org.smartparam.engine.test.builder.ParameterEntryTestBuilder.parameterEntry;
import static org.smartparam.engine.test.builder.ParameterTestBuilder.parameter;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class JsonParameterConfigSerializerTest {

    private JsonParameterConfigSerializer serializer;

    @Before
    public void initialize() {
        serializer = new JsonParameterConfigSerializer();
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

        // when
        String serializedConfig = serializer.serialize(parameter);

        // then
        assertThat(serializedConfig).isNotEmpty().contains("levels").contains("parameter")
                .doesNotContain("entries");
    }
}
