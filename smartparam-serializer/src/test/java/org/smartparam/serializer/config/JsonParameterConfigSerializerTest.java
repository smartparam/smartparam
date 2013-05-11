package org.smartparam.serializer.config;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.test.mock.LevelMock;
import org.smartparam.engine.test.mock.ParameterEntryMock;
import org.smartparam.engine.test.mock.ParameterMockBuilder;
import org.smartparam.serializer.mock.EditableLevelMock;
import org.smartparam.serializer.mock.EditableParameterMock;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class JsonParameterConfigSerializerTest {

    private JsonParameterConfigSerializer serializer;

    @Before
    public void initialize() {
        serializer = new JsonParameterConfigSerializer(EditableLevelMock.class);
    }

    @Test
    public void testSerialize() {
        Parameter parameter = (new ParameterMockBuilder("parameter")).cacheable(true)
                .multivalue(true).nullable(false).withInputLevels(3)
                .withLevels(new LevelMock("creator1", "type", true, "matcher1"),
                new LevelMock("creator2", "type", true, "matcher2"),
                new LevelMock("creator3", "type", true, "matcher3"))
                .withEntries(new ParameterEntryMock("v1", "v2", "v3")).get();

        String serializedConfig = serializer.serialize(parameter);

        assertNotNull(serializedConfig);
        assertFalse(serializedConfig.contains("entries"));
        assertTrue(serializedConfig.contains("levels"));
    }

    @Test
    public void testSimpleDeserialize() {
        String json = "{ \"name\": \"parameter\", \"cacheable\": \"true\","
                + "\"multivalue\": \"true\", \"nullable\": \"true\", \"inputLevels\": 3 }";

        Parameter parameter = serializer.deserialize(json, EditableParameterMock.class);

        assertNotNull(parameter);
        assertEquals("parameter", parameter.getName());
        assertEquals(true, parameter.isCacheable());
        assertEquals(true, parameter.isMultivalue());
        assertEquals(true, parameter.isNullable());
        assertEquals(3, parameter.getInputLevels());
    }

    @Test
    public void testDeserializeWithoutQuotationMarks() {
        String json = "{ name: \"parameter\" }";

        Parameter parameter = serializer.deserialize(json, EditableParameterMock.class);

        assertNotNull(parameter);
        assertEquals("parameter", parameter.getName());
    }

    @Test
    public void testDeserializeWithLevels() {
        String json = "{ \"name\": \"parameter\", \"levels\": ["
                + "{ \"label\": \"level1\", \"levelCreator\": \"l1LevelCreator\", \"type\": \"l1Type\", \"matcherCode\": \"l1Matcher\" },"
                + "{ \"label\": \"level2\", \"levelCreator\": \"l2LevelCreator\", \"type\": \"l2Type\", \"matcherCode\": \"l2Matcher\" },"
                + "{ \"label\": \"level3\", \"levelCreator\": \"l3LevelCreator\", \"type\": \"l3Type\", \"matcherCode\": \"l3Matcher\" }"
                + "] }";

        Parameter parameter = serializer.deserialize(json, EditableParameterMock.class);

        assertNotNull(parameter);
        assertEquals("parameter", parameter.getName());
        assertEquals(3, parameter.getLevels().size());
        assertEquals("l1Type", parameter.getLevels().get(0).getType());
        assertEquals("l1Matcher", parameter.getLevels().get(0).getMatcherCode());
    }
}
