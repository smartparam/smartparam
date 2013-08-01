package org.smartparam.serializer.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.editable.SimpleEditableLevel;
import org.smartparam.engine.model.editable.SimpleEditableParameter;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class JsonParmeterConfigDeserializerTest {

    private JsonParameterConfigDeserializer deserializer;

    @Before
    public void initialize() {
        deserializer = new JsonParameterConfigDeserializer(SimpleEditableParameter.class, SimpleEditableLevel.class);
    }

    @Test
    public void testSimpleDeserialize() {
        String json = "{ \"name\": \"parameter\", \"cacheable\": \"true\","
                + "\"multivalue\": \"true\", \"nullable\": \"true\", \"inputLevels\": 3 }";

        Parameter parameter = deserializer.deserialize(json);

        assertNotNull(parameter);
        assertNotNull(parameter.getEntries());
        assertEquals("parameter", parameter.getName());
        assertEquals(true, parameter.isCacheable());
        assertEquals(true, parameter.isNullable());
        assertEquals(3, parameter.getInputLevels());
    }

    @Test
    public void testDeserializeWithoutQuotationMarks() {
        String json = "{ name: \"parameter\" }";

        Parameter parameter = deserializer.deserialize(json);

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

        Parameter parameter = deserializer.deserialize(json);

        assertNotNull(parameter);
        assertEquals("parameter", parameter.getName());
        assertEquals(3, parameter.getLevels().size());
        assertEquals("l1Type", parameter.getLevels().get(0).getType());
        assertEquals("l1Matcher", parameter.getLevels().get(0).getMatcherCode());
    }

}
