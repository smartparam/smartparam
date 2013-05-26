package org.smartparam.serializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import org.smartparam.engine.model.Parameter;
import org.smartparam.serializer.config.ParameterConfigDeserializer;
import org.smartparam.serializer.entries.ParameterEntryDeserializer;
import org.smartparam.serializer.entries.ParameterEntryPersister;
import org.smartparam.serializer.entries.StandardParameterEntryPersister;
import org.smartparam.serializer.exception.SmartParamSerializationException;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class RawSmartParamDeserializer implements SmartParamDeserializer {

    private static final int PROBABLE_CONFIG_LENGTH = 400;

    private static final String CONFIG_END_MARK = "EOF-config";

    private ParameterConfigDeserializer configDeserializer;

    private ParameterEntryDeserializer entriesDeserializer;

    public RawSmartParamDeserializer(ParameterConfigDeserializer configDeserializer, ParameterEntryDeserializer entriesDeserializer) {
        this.configDeserializer = configDeserializer;
        this.entriesDeserializer = entriesDeserializer;
    }

    @Override
    public Parameter deserialize(SerializationConfig config, Reader reader) throws SmartParamSerializationException {
        BufferedReader bufferedReader = new BufferedReader(reader);

        Parameter deserialiedParameter = deserializeConfig(config, bufferedReader);
        StandardParameterEntryPersister persister = new StandardParameterEntryPersister(deserialiedParameter);
        deserializeEntries(config, bufferedReader, persister);

        return deserialiedParameter;
    }

    @Override
    public Parameter deserializeConfig(SerializationConfig config, BufferedReader reader) throws SmartParamSerializationException {
        try {
            String configString = readConfig(config.getCommentChar(), reader);
            Parameter deserialiedParameter = configDeserializer.deserialize(configString);

            return deserialiedParameter;
        } catch (IOException exception) {
            throw new SmartParamSerializationException("error while deserializing parameter", exception);
        }

    }

    private String readConfig(char commentChar, BufferedReader reader) throws IOException {
        StringBuilder config = new StringBuilder(PROBABLE_CONFIG_LENGTH);

        String endOfConfigTag = commentChar + CONFIG_END_MARK;

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
    public void deserializeEntries(SerializationConfig config, BufferedReader reader, ParameterEntryPersister persister) throws SmartParamSerializationException {
        entriesDeserializer.deserialize(config, reader, persister);
    }
}
