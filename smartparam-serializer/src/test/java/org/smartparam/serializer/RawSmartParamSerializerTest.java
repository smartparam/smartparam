package org.smartparam.serializer;

import java.io.StringWriter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.SimpleParameter;
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
        serializer = new RawSmartParamSerializer(new StandardSerializationConfig(), configSerializer, entrySerializer);
    }

    @Test
    public void testSerialize() throws SmartParamSerializationException {
        SimpleParameter parameter = new SimpleParameter();

        when(configSerializer.serialize(any(Parameter.class))).thenReturn("multi\nline");

        StringWriter stringWriter = new StringWriter();
        serializer.serialize(parameter, stringWriter);

        String expected = "#multi\n"
                + "#line\n"
                + "##\n";
        assertEquals(expected, stringWriter.toString());
    }
}
