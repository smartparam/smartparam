package org.smartparam.serializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import org.smartparam.engine.model.Parameter;
import org.smartparam.serializer.config.ParameterConfigDeserializer;
import org.smartparam.serializer.entries.ParameterEntryBatchLoader;
import org.smartparam.serializer.entries.ParameterEntryDeserializer;
import org.smartparam.serializer.exception.SmartParamSerializationException;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class RawSmartParamDeserializer implements ParamDeserializer {

    private static final int PROBABLE_CONFIG_LENGTH = 400;

    private static final int PARAMETER_ENTRIES_BATCH_SIZE = 500;

    private SerializationConfig serializationConfig;

    private ParameterConfigDeserializer configDeserializer;

    private ParameterEntryDeserializer entriesDeserializer;

    public RawSmartParamDeserializer(SerializationConfig serializationConfig, ParameterConfigDeserializer configDeserializer, ParameterEntryDeserializer entriesDeserializer) {
        this.serializationConfig = serializationConfig;
        this.configDeserializer = configDeserializer;
        this.entriesDeserializer = entriesDeserializer;
    }

    @Override
    public Parameter deserialize(Reader reader) throws SmartParamSerializationException {
        BufferedReader bufferedReader = new BufferedReader(reader);

        Parameter deserialiedParameter = deserializeConfig(bufferedReader);
        readEntries(deserialiedParameter, deserializeEntries(bufferedReader));

        return deserialiedParameter;
    }

    private void readEntries(Parameter parameter, ParameterEntryBatchLoader loader) throws SmartParamSerializationException {
        while(loader.hasMore()) {
            parameter.getEntries().addAll(loader.nextBatch(PARAMETER_ENTRIES_BATCH_SIZE));
        }
    }

    @Override
    public Parameter deserializeConfig(BufferedReader reader) throws SmartParamSerializationException {
        try {
            String configString = readConfig(serializationConfig.getCommentChar(), reader);
            Parameter deserialiedParameter = configDeserializer.deserialize(configString);

            return deserialiedParameter;
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
