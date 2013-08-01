package org.smartparam.serializer.config;

import org.junit.Before;
import org.smartparam.engine.model.editable.SimpleEditableLevel;
import org.smartparam.engine.model.editable.SimpleEditableParameter;

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

//    @Test
//    public void serializeAndDeserialize() {
//        Parameter parameter = (new ParameterMockBuilder("parameter")).cacheable(true)
//                .multivalue(true).nullable(false).withInputLevels(3)
//                .withLevels(new LevelMock("creator1", "type", true, "matcher1"),
//                new LevelMock("creator2", "type", true, "matcher2"),
//                new LevelMock("creator3", "type", true, "matcher3"))
//                .withEntries(new ParameterEntryMock("v1", "v2", "v3")).get();
//
//        String serializedConfig = serializer.serialize(parameter);
//        Parameter deserializedParameter = deserializer.deserialize(serializedConfig);
//
//        assertEquals("parameter", deserializedParameter.getName());
//        assertEquals(true, deserializedParameter.isMultivalue());
//        assertEquals(true, deserializedParameter.isCacheable());
//        assertEquals(false, deserializedParameter.isNullable());
//        assertEquals(3, deserializedParameter.getInputLevels());
//        assertEquals(3, deserializedParameter.getLevels().size());
//        assertEquals("creator1", deserializedParameter.getLevels().get(0).getLevelCreator());
//        assertTrue(deserializedParameter.getEntries().isEmpty());
//    }
}
