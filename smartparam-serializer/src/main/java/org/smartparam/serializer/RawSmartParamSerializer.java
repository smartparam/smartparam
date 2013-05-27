package org.smartparam.serializer;

import java.io.IOException;
import java.io.Writer;
import org.smartparam.engine.model.Parameter;
import org.smartparam.serializer.config.ParameterConfigSerializer;
import org.smartparam.serializer.entries.ParameterEntrySerializer;
import org.smartparam.serializer.entries.ParameterEntrySupplier;
import org.smartparam.serializer.entries.StandardParameterEntrySupplier;
import org.smartparam.serializer.exception.SmartParamSerializationException;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class RawSmartParamSerializer implements SmartParamSerializer {

    private static final int PROBABLE_COMMENT_SIGNS_COUNT = 50;

    private ParameterConfigSerializer configSerializer;

    private ParameterEntrySerializer entriesSerializer;

    public RawSmartParamSerializer(ParameterConfigSerializer configSerializer, ParameterEntrySerializer entriesSerializer) {
        this.configSerializer = configSerializer;
        this.entriesSerializer = entriesSerializer;
    }

    @Override
    public void serialize(SerializationConfig config, Parameter parameter, Writer writer) throws SmartParamSerializationException {
        StandardParameterEntrySupplier supplier = new StandardParameterEntrySupplier(parameter);
        serialize(config, parameter, writer, supplier);
    }

    @Override
    public void serialize(SerializationConfig config, Parameter parameter, Writer writer, ParameterEntrySupplier supplier) throws SmartParamSerializationException {
        String serializedConifg = configSerializer.serialize(parameter);
        serializedConifg = appendCommentToConfig(config.getCommentChar(), serializedConifg);
        try {
            writer.append(serializedConifg);
            entriesSerializer.serialize(config, writer, supplier);
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
}
