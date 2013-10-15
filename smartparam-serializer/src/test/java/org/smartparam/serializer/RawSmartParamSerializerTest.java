/*
 * Copyright 2013 Adam Dubiel, Przemek Hertel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.smartparam.serializer;

import org.smartparam.serializer.config.StandardSerializationConfig;
import java.io.StringWriter;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import org.smartparam.engine.model.Parameter;
import org.smartparam.serializer.metadata.ParameterMetadataSerializer;
import org.smartparam.serializer.entries.ParameterEntrySerializer;
import org.smartparam.serializer.exception.SmartParamSerializationException;
import static org.smartparam.engine.test.assertions.Assertions.*;
import static org.smartparam.engine.test.builder.ParameterTestBuilder.parameter;

/**
 *
 * @author Adam Dubiel
 */
public class RawSmartParamSerializerTest {

    private ParameterMetadataSerializer configSerializer;

    private ParameterEntrySerializer entrySerializer;

    private RawSmartParamSerializer serializer;

    @Before
    public void initialize() {
        configSerializer = mock(ParameterMetadataSerializer.class);
        entrySerializer = mock(ParameterEntrySerializer.class);
        serializer = new RawSmartParamSerializer(new StandardSerializationConfig(), configSerializer, entrySerializer);
    }

//    @Test
//    public void shouldAppendCommentCharToEachLineOfParameterConfigSectionAndEndItWithDoubleCommentChar() throws SmartParamSerializationException {
//        // given
//        Parameter parameter = parameter().withEntries().build();
//        when(configSerializer.serialize(parameter)).thenReturn("multi\nline");
//        StringWriter stringWriter = new StringWriter();
//
//        // when
//        serializer.serialize(parameter, stringWriter);
//
//        // then
//        assertThat(stringWriter.toString()).isEqualTo("#multi\n"
//                + "#line\n"
//                + "##\n");
//    }
}
