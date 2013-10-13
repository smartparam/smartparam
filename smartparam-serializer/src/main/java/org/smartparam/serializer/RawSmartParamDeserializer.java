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

import java.io.BufferedReader;
import java.io.IOException;
import org.smartparam.engine.core.exception.ParamBatchLoadingException;
import org.smartparam.engine.model.Parameter;
import org.smartparam.serializer.config.ParameterConfigDeserializer;
import org.smartparam.engine.core.batch.ParameterEntryBatchLoader;
import org.smartparam.serializer.entries.ParameterEntryDeserializer;
import org.smartparam.serializer.exception.SmartParamSerializationException;

/**
 *
 * @author Adam Dubiel
 */
public class RawSmartParamDeserializer implements ParamDeserializer {

    private static final int PROBABLE_CONFIG_LENGTH = 400;

    private static final int PARAMETER_ENTRIES_BATCH_SIZE = 1000;

    private SerializationConfig serializationConfig;

    private ParameterConfigDeserializer configDeserializer;

    private ParameterEntryDeserializer entriesDeserializer;

    public RawSmartParamDeserializer(SerializationConfig serializationConfig, ParameterConfigDeserializer configDeserializer, ParameterEntryDeserializer entriesDeserializer) {
        this.serializationConfig = serializationConfig;
        this.configDeserializer = configDeserializer;
        this.entriesDeserializer = entriesDeserializer;
    }

    @Override
    public Parameter deserialize(BufferedReader reader) throws SmartParamSerializationException {
        BufferedReader bufferedReader = new BufferedReader(reader);

        Parameter deserialiedParameter = deserializeConfig(bufferedReader);
        readEntries(deserialiedParameter, deserializeEntries(bufferedReader));

        return deserialiedParameter;
    }

    private void readEntries(Parameter parameter, ParameterEntryBatchLoader loader) throws SmartParamSerializationException {
        while (loader.hasMore()) {
            try {
                parameter.getEntries().addAll(loader.nextBatch(PARAMETER_ENTRIES_BATCH_SIZE));
            } catch (ParamBatchLoadingException exception) {
                throw new SmartParamSerializationException("error while loading batch of entries", exception);
            }
        }
    }

    @Override
    public Parameter deserializeConfig(BufferedReader reader) throws SmartParamSerializationException {
        try {
            String configString = readConfig(serializationConfig.getCommentChar(), reader);
            Parameter deserializedParameter = configDeserializer.deserialize(configString);

            return deserializedParameter;
        } catch (IOException exception) {
            throw new SmartParamSerializationException("error while deserializing parameter", exception);
        }

    }

    private String readConfig(char commentChar, BufferedReader reader) throws IOException {
        StringBuilder config = new StringBuilder(PROBABLE_CONFIG_LENGTH);

        String endOfConfigTag = commentChar + String.valueOf(commentChar);

        String line = reader.readLine();
        while (line != null) {
            if (line.startsWith(endOfConfigTag)) {
                break;
            }

            config.append(line.substring(1));
            line = reader.readLine();
        }

        return config.toString();
    }

    @Override
    public ParameterEntryBatchLoader deserializeEntries(BufferedReader reader) throws SmartParamSerializationException {
        return entriesDeserializer.deserialize(serializationConfig, reader);
    }

    @Override
    public SerializationConfig getSerializationConfig() {
        return serializationConfig;
    }
}
