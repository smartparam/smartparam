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
import java.io.BufferedReader;
import java.io.StringReader;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import org.smartparam.serializer.metadata.ParameterMetadataDeserializer;
import org.smartparam.serializer.entries.ParameterEntryDeserializer;
import org.smartparam.serializer.exception.SmartParamSerializationException;

/**
 *
 * @author Adam Dubiel
 */
public class RawSmartParamDeserializerTest {

    private ParameterMetadataDeserializer configDeserializer;

    private ParameterEntryDeserializer entryDeserializer;

    private RawSmartParamDeserializer deserializer;

    @Before
    public void initialize() {
        configDeserializer = mock(ParameterMetadataDeserializer.class);
        entryDeserializer = mock(ParameterEntryDeserializer.class);
        deserializer = new RawSmartParamDeserializer(new StandardSerializationConfig(), configDeserializer, entryDeserializer);
    }

//    @Test
//    public void shouldStripCommentCharsFromParameterConfigSectionBeforeDeserialization() throws SmartParamSerializationException {
//        // given
//        String config = "#{\n"
//                + "#name: \"parameter\"\n"
//                + "#}\n";
//        StringReader stringReader = new StringReader(config);
//
//        // when
//        deserializer.deserializeMetadata(new BufferedReader(stringReader));
//
//        // then
//        verify(configDeserializer).deserialize("{name: \"parameter\"}");
//    }
}
