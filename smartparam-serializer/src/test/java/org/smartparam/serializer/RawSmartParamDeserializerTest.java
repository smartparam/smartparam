package org.smartparam.serializer;

import java.io.StringReader;
import static org.junit.Assert.assertSame;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.test.mock.ParameterMock;
import org.smartparam.serializer.config.ParameterConfigDeserializer;
import org.smartparam.serializer.entries.ParameterEntryDeserializer;
import org.smartparam.serializer.exception.SmartParamSerializationException;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class RawSmartParamDeserializerTest {

    private ParameterConfigDeserializer configDeserializer;

    private ParameterEntryDeserializer entryDeserializer;

    private RawSmartParamDeserializer deserializer;

    @Before
    public void initialize() {
        configDeserializer = mock(ParameterConfigDeserializer.class);
        entryDeserializer = mock(ParameterEntryDeserializer.class);
        deserializer = new RawSmartParamDeserializer(new StandardSerializationConfig(), configDeserializer, entryDeserializer);
    }

    @Test
    public void testDeserialize() throws SmartParamSerializationException {
        String config = "#{\n"
                + "#name: \"parameter\"\n"
                + "#}\n";
        String commentlessConfig = "{name: \"parameter\"}";

        Parameter expectedParameter = new ParameterMock();
        when(configDeserializer.deserialize(commentlessConfig)).thenReturn(expectedParameter);

        StringReader stringReader = new StringReader(config);
        Parameter parameter = deserializer.deserialize(stringReader);

        assertSame(expectedParameter, parameter);
    }
}
