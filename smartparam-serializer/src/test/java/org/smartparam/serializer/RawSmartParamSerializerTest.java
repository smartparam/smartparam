package org.smartparam.serializer;

import java.io.StringReader;
import java.io.StringWriter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.test.mock.LevelMock;
import org.smartparam.engine.test.mock.ParameterEntryMock;
import org.smartparam.engine.test.mock.ParameterMock;
import org.smartparam.engine.test.mock.ParameterMockBuilder;
import org.smartparam.serializer.config.ParameterConfigSerializer;
import org.smartparam.serializer.entries.ParameterEntrySerializer;
import org.smartparam.serializer.exception.SmartParamSerializationException;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class RawSmartParamSerializerTest {

    private ParameterConfigSerializer configSerializer;

    private ParameterEntrySerializer entrySerializer;

    private RawSmartParamSerializer serializer;

    @Before
    public void initialize() {
        configSerializer = mock(ParameterConfigSerializer.class);
        entrySerializer = mock(ParameterEntrySerializer.class);
        serializer = new RawSmartParamSerializer(configSerializer, entrySerializer);
    }

    @Test
    public void testSerialize() throws SmartParamSerializationException {
        Parameter parameter = (new ParameterMockBuilder("parameter")).cacheable(true)
                .multivalue(true).nullable(false).withInputLevels(3)
                .withLevels(new LevelMock("creator1", "type", true, "matcher1"),
                new LevelMock("creator2", "type", true, "matcher2"),
                new LevelMock("creator3", "type", true, "matcher3"))
                .withEntries(new ParameterEntryMock("pe0_1", "pe0_2", "pe0_3"),
                new ParameterEntryMock("pe1_1", "pe1_2", "pe1_3")).get();

        when(configSerializer.serialize(any(Parameter.class))).thenReturn("multi\nline");

        StringWriter stringWriter = new StringWriter();
        serializer.serialize(null, parameter, stringWriter);

        String expected = "#multi\n"
                + "#line\n"
                + "#EOF-config\n";
        assertEquals(expected, stringWriter.toString());
    }

    @Test
    public void testDeserialize() throws SmartParamSerializationException {
        String config = "#{\n"
                + "#name: \"parameter\"\n"
                + "#}\n"
                + "#EOF-config";
        String commentlessConfig = "{name: \"parameter\"}";

        Parameter expectedParameter = new ParameterMock();
        when(configSerializer.deserialize(commentlessConfig)).thenReturn(expectedParameter);

        StringReader stringReader = new StringReader(config);
        Parameter parameter = serializer.deserialize(null, stringReader);

        assertSame(expectedParameter, parameter);
    }
}
