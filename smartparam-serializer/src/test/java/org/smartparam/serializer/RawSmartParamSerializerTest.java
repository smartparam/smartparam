package org.smartparam.serializer;

import java.io.StringWriter;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import org.smartparam.engine.model.Parameter;
import org.smartparam.serializer.config.ParameterConfigSerializer;
import org.smartparam.serializer.entries.ParameterEntrySerializer;
import org.smartparam.serializer.exception.SmartParamSerializationException;
import static org.smartparam.engine.test.assertions.Assertions.*;
import static org.smartparam.engine.test.builder.ParameterTestBuilder.parameter;

/**
 *
 * @author Adam Dubiel
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
    public void shouldAppendCommentCharToEachLineOfParameterConfigSectionAndEndItWithDoubleCommentChar() throws SmartParamSerializationException {
        // given
        Parameter parameter = parameter().withEntries().build();
        when(configSerializer.serialize(parameter)).thenReturn("multi\nline");
        StringWriter stringWriter = new StringWriter();

        // when
        serializer.serialize(parameter, stringWriter);

        // then
        assertThat(stringWriter.toString()).isEqualTo("#multi\n"
                + "#line\n"
                + "##\n");
    }
}
