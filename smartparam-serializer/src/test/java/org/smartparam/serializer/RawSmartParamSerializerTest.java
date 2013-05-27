package org.smartparam.serializer;

import java.io.StringWriter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.smartparam.engine.model.Parameter;
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
        Parameter parameter = new ParameterMockBuilder("parameter").get();

        when(configSerializer.serialize(any(Parameter.class))).thenReturn("multi\nline");

        StringWriter stringWriter = new StringWriter();
        serializer.serialize(new StandardSerializationConfig(), parameter, stringWriter);

        String expected = "#multi\n"
                + "#line\n"
                + "##\n";
        assertEquals(expected, stringWriter.toString());
    }

}
