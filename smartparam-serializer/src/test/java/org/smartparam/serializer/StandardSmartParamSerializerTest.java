package org.smartparam.serializer;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.test.mock.LevelMock;
import org.smartparam.engine.test.mock.ParameterEntryMock;
import org.smartparam.engine.test.mock.ParameterMockBuilder;
import org.smartparam.mgmt.test.mock.EditableLevelMock;
import org.smartparam.mgmt.test.mock.EditableParameterEntryMock;
import org.smartparam.mgmt.test.mock.EditableParameterMock;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class StandardSmartParamSerializerTest {

    private StandardSmartParamSerializer serializer;

    private StandardSmartParamDeserializer deserializer;

    private StandardSerializationConfig config;

    @Before
    public void initialize() {
        serializer = new StandardSmartParamSerializer(EditableParameterEntryMock.class);
        deserializer = new StandardSmartParamDeserializer(EditableParameterMock.class, EditableLevelMock.class, EditableParameterEntryMock.class);
        config = new StandardSerializationConfig('"', ';', '#', "\n");
    }

    @Test
    public void testDeserializationFromTestFile() throws Exception {
        Reader reader = new InputStreamReader(this.getClass().getResourceAsStream("/sampleParam.csv"));

        Parameter parameter = deserializer.deserialize(config, reader);

        assertEquals("testParameter", parameter.getName());
        assertEquals(true, parameter.isCacheable());
        assertEquals(true, parameter.isMultivalue());
        assertEquals(false, parameter.isNullable());
        assertEquals(4, parameter.getInputLevels());
        assertEquals(3, parameter.getLevels().size());
        assertEquals("type3", parameter.getLevels().get(2).getType());
        assertEquals(2, parameter.getEntries().size());
    }

    @Test
    public void testSerializeAndDeserialize() throws Exception {
        Parameter parameter = (new ParameterMockBuilder("parameter")).cacheable(true)
                .multivalue(true).nullable(false).withInputLevels(3)
                .withLevels(new LevelMock("creator1", "type", true, "matcher1"),
                new LevelMock("creator2", "type", true, "matcher2"),
                new LevelMock("creator3", "type", true, "matcher3"))
                .withEntries(new ParameterEntryMock("pe0_1", "pe0_2", "pe0_3"),
                new ParameterEntryMock("pe1_1", "pe1_2", "pe1_3")).get();

        StringWriter paramWriter = new StringWriter();
        serializer.serialize(config, parameter, paramWriter);

        StringReader paramReader = new StringReader(paramWriter.toString());
        Parameter processedParameter = deserializer.deserialize(config, paramReader);

        assertEquals(parameter.getName(), processedParameter.getName());
        assertEquals(parameter.isMultivalue(), processedParameter.isMultivalue());
        assertEquals(parameter.isNullable(), processedParameter.isNullable());
        assertEquals(parameter.isCacheable(), processedParameter.isCacheable());
        assertEquals(parameter.getInputLevels(), processedParameter.getInputLevels());
        assertEquals(parameter.getLevels().size(), processedParameter.getLevels().size());
        assertEquals(parameter.getEntries().size(), processedParameter.getEntries().size());
    }
}
