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
import java.io.BufferedReader;
import org.smartparam.engine.core.exception.ParamBatchLoadingException;
import org.smartparam.engine.model.Parameter;
import org.smartparam.serializer.metadata.ParameterMetadataDeserializer;
import org.smartparam.engine.core.batch.ParameterEntryBatchLoader;
import org.smartparam.serializer.model.AppendableParameter;
import org.smartparam.serializer.entries.ParameterEntryDeserializer;
import org.smartparam.serializer.exception.ParamSerializationException;

/**
 *
 * @author Adam Dubiel
 */
public class StandardParamDeserializer implements ParamDeserializer {

    private static final int PARAMETER_ENTRIES_BATCH_SIZE = 1000;

    private final SerializationConfig serializationConfig;

    private final ParameterMetadataDeserializer metadataDeserializer;

    private final ParameterEntryDeserializer entriesDeserializer;

    public StandardParamDeserializer(SerializationConfig serializationConfig, ParameterMetadataDeserializer metadataDeserializer, ParameterEntryDeserializer entriesDeserializer) {
        this.serializationConfig = serializationConfig;
        this.metadataDeserializer = metadataDeserializer;
        this.entriesDeserializer = entriesDeserializer;
    }

    @Override
    public Parameter deserialize(BufferedReader reader) throws ParamSerializationException {
        AppendableParameter deserialiedParameter = metadataDeserializer.deserialize(reader);
        readEntries(deserialiedParameter, deserializeEntries(reader));

        return deserialiedParameter;
    }

    private void readEntries(AppendableParameter parameter, ParameterEntryBatchLoader loader) throws ParamSerializationException {
        while (loader.hasMore()) {
            try {
                parameter.appendEntries(loader.nextBatch(PARAMETER_ENTRIES_BATCH_SIZE));
            } catch (ParamBatchLoadingException exception) {
                throw new ParamSerializationException("error while loading batch of entries", exception);
            }
        }
    }

    @Override
    public Parameter deserializeMetadata(BufferedReader reader) throws ParamSerializationException {
        return metadataDeserializer.deserialize(reader);
    }

    @Override
    public ParameterEntryBatchLoader deserializeEntries(BufferedReader reader) throws ParamSerializationException {
        return entriesDeserializer.deserialize(reader);
    }

    @Override
    public SerializationConfig getSerializationConfig() {
        return serializationConfig;
    }
}
