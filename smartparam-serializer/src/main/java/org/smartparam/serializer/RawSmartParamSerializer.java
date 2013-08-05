package org.smartparam.serializer;

import java.io.IOException;
import java.io.Writer;
import org.smartparam.engine.model.Parameter;
import org.smartparam.serializer.config.ParameterConfigSerializer;
import org.smartparam.serializer.entries.ParameterEntryBatchLoader;
import org.smartparam.serializer.entries.ParameterEntrySerializer;
import org.smartparam.serializer.entries.SimpleParameterEntryBatchLoader;
import org.smartparam.serializer.exception.SmartParamSerializationException;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class RawSmartParamSerializer implements ParamSerializer {

    private static final int PROBABLE_COMMENT_SIGNS_COUNT = 50;

    private SerializationConfig serializationConfig;

    private ParameterConfigSerializer configSerializer;

    private ParameterEntrySerializer entriesSerializer;

    public RawSmartParamSerializer(SerializationConfig serializationConfig, ParameterConfigSerializer configSerializer, ParameterEntrySerializer entriesSerializer) {
        this.serializationConfig = serializationConfig;
        this.configSerializer = configSerializer;
        this.entriesSerializer = entriesSerializer;
    }

    @Override
    public void serialize(Parameter parameter, Writer writer) throws SmartParamSerializationException {
        ParameterEntryBatchLoader batchLoader = new SimpleParameterEntryBatchLoader(parameter);
        serialize(parameter, writer, batchLoader);
    }

    @Override
    public void serialize(Parameter parameter, Writer writer, ParameterEntryBatchLoader entryBatchLoader) throws SmartParamSerializationException {
        String serializedConifg = configSerializer.serialize(parameter);
        serializedConifg = appendCommentToConfig(serializationConfig.getCommentChar(), serializedConifg);
        try {
            writer.append(serializedConifg);
            entriesSerializer.serialize(serializationConfig, writer, parameter, entryBatchLoader);
        } catch (IOException exception) {
            throw new SmartParamSerializationException("error while serializing parameter " + parameter.getName(), exception);
        }
    }

    private String appendCommentToConfig(char commentChar, String serializedConfig) {
        StringBuilder commentedConfig = new StringBuilder(serializedConfig.length() + PROBABLE_COMMENT_SIGNS_COUNT);

        for (String line : serializedConfig.split("\n")) {
            commentedConfig.append(commentChar).append(line).append("\n");
        }
        commentedConfig.append(commentChar).append(commentChar).append("\n");

        return commentedConfig.toString();
    }

    @Override
    public SerializationConfig getSerializationConfig() {
        return serializationConfig;
    }
}
