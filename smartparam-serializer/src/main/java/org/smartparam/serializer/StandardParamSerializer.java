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

import org.smartparam.serializer.config.SerializationConfig;
import java.io.Writer;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.serializer.metadata.ParameterMetadataSerializer;
import org.smartparam.engine.core.parameter.entry.ParameterEntryBatchLoader;
import org.smartparam.serializer.entries.ParameterEntrySerializer;
import org.smartparam.serializer.entries.SimpleParameterEntryBatchLoader;
import org.smartparam.serializer.exception.ParamSerializationException;

/**
 *
 * @author Adam Dubiel
 */
public class StandardParamSerializer implements ParamSerializer {

    private final SerializationConfig serializationConfig;

    private final ParameterMetadataSerializer metadataSerializer;

    private final ParameterEntrySerializer entriesSerializer;

    public StandardParamSerializer(SerializationConfig serializationConfig, ParameterMetadataSerializer metadataSerializer, ParameterEntrySerializer entriesSerializer) {
        this.serializationConfig = serializationConfig;
        this.metadataSerializer = metadataSerializer;
        this.entriesSerializer = entriesSerializer;
    }

    @Override
    public void serialize(Parameter parameter, Writer writer) throws ParamSerializationException {
        ParameterEntryBatchLoader batchLoader = new SimpleParameterEntryBatchLoader(parameter);
        serialize(parameter, writer, batchLoader);
    }

    @Override
    public void serialize(Parameter parameter, Writer writer, ParameterEntryBatchLoader entryBatchLoader) throws ParamSerializationException {
        metadataSerializer.serialize(parameter, writer);
        entriesSerializer.serialize(serializationConfig, writer, parameter, entryBatchLoader);
    }

    @Override
    public SerializationConfig getSerializationConfig() {
        return serializationConfig;
    }
}
