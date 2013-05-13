package org.smartparam.serializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import org.smartparam.engine.model.Parameter;
import org.smartparam.serializer.config.ParameterConfigSerializer;
import org.smartparam.serializer.entries.ParameterEntryPersister;
import org.smartparam.serializer.entries.ParameterEntrySerializer;
import org.smartparam.serializer.entries.ParameterEntrySupplier;
import org.smartparam.serializer.entries.StandardParameterEntryPersister;
import org.smartparam.serializer.entries.StandardParameterEntrySupplier;
import org.smartparam.serializer.exception.SmartParamSerializationException;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class RawSmartParamSerializer implements SmartParamSerializer {

    private static final int PROBABLE_CONFIG_LENGTH = 400;

    private static final int PROBABLE_COMMENT_SIGNS_COUNT = 100;

    private static final String DEFAULT_COMMENT_STRING = "#";

    private static final String CONFIG_END_MARK = "EOF-config";

    private ParameterConfigSerializer configSerializer;

    private ParameterEntrySerializer entriesSerializer;

    private String commentString;

    public RawSmartParamSerializer(ParameterConfigSerializer configSerializer, ParameterEntrySerializer entriesSerializer) {
        this.configSerializer = configSerializer;
        this.entriesSerializer = entriesSerializer;
    }

    private String getCommentString() {
        return commentString != null ? commentString : DEFAULT_COMMENT_STRING;
    }

    @Override
    public void serialize(SerializationConfig config, Parameter parameter, Writer writer) throws SmartParamSerializationException {
        StandardParameterEntrySupplier supplier = new StandardParameterEntrySupplier(parameter);
        serialize(config, parameter, writer, supplier);
    }

    @Override
    public void serialize(SerializationConfig config, Parameter parameter, Writer writer, ParameterEntrySupplier supplier) throws SmartParamSerializationException {
        String serializedConifg = configSerializer.serialize(parameter);
        serializedConifg = appendCommentToConfig(serializedConifg);
        try {
            writer.append(serializedConifg);
            entriesSerializer.serialize(config, writer, supplier);
        } catch (IOException exception) {
            throw new SmartParamSerializationException("error while serializing parameter " + parameter.getName(), exception);
        }
    }

    private String appendCommentToConfig(String serializedConfig) {
        String activeCommentString = getCommentString();
        StringBuilder commentedConfig = new StringBuilder(serializedConfig.length() + PROBABLE_COMMENT_SIGNS_COUNT * activeCommentString.length());

        for (String line : serializedConfig.split("\n")) {
            commentedConfig.append(activeCommentString).append(line).append("\n");
        }
        commentedConfig.append(activeCommentString).append(CONFIG_END_MARK).append("\n");

        return commentedConfig.toString();
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
            String configString = readConfig(reader);
            Parameter deserialiedParameter = configSerializer.deserialize(configString);

            return deserialiedParameter;
        } catch (IOException exception) {
            throw new SmartParamSerializationException("error while deserializing parameter", exception);
        }

    }

    private String readConfig(BufferedReader reader) throws IOException {
        StringBuilder config = new StringBuilder(PROBABLE_CONFIG_LENGTH);

        String endOfConfigTag = getCommentString() + CONFIG_END_MARK;

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
        entriesSerializer.deserialize(config, reader, persister);
    }

    public void setCommentString(String commentString) {
        this.commentString = commentString;
    }
}
