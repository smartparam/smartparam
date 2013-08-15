package org.smartparam.serializer;

import java.io.BufferedReader;
import java.io.StringReader;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import org.smartparam.serializer.config.ParameterConfigDeserializer;
import org.smartparam.serializer.entries.ParameterEntryDeserializer;
import org.smartparam.serializer.exception.SmartParamSerializationException;

/**
 *
 * @author Adam Dubiel
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
    public void shouldStripCommentCharsFromParameterConfigSectionBeforeDeserialization() throws SmartParamSerializationException {
        // given
        String config = "#{\n"
                + "#name: \"parameter\"\n"
                + "#}\n";
        StringReader stringReader = new StringReader(config);

        // when
        deserializer.deserializeConfig(new BufferedReader(stringReader));

        // then
        verify(configDeserializer).deserialize("{name: \"parameter\"}");
    }
}
