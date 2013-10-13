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

import java.io.IOException;
import java.io.Writer;
import org.smartparam.engine.model.Parameter;
import org.smartparam.serializer.config.ParameterConfigSerializer;
import org.smartparam.engine.core.batch.ParameterEntryBatchLoader;
import org.smartparam.serializer.entries.ParameterEntrySerializer;
import org.smartparam.serializer.entries.SimpleParameterEntryBatchLoader;
import org.smartparam.serializer.exception.SmartParamSerializationException;

/**
 *
 * @author Adam Dubiel
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
//        serializedConifg = appendCommentToConfig(serializationConfig.getCommentChar(), serializedConifg);
        try {
            writer.append(serializedConifg);
            entriesSerializer.serialize(serializationConfig, writer, parameter, entryBatchLoader);
        } catch (IOException exception) {
            throw new SmartParamSerializationException("error while serializing parameter " + parameter.getName(), exception);
        }
    }

//    private String appendCommentToConfig(char commentChar, String serializedConfig) {
//        StringBuilder commentedConfig = new StringBuilder(serializedConfig.length() + PROBABLE_COMMENT_SIGNS_COUNT);
//
//        for (String line : serializedConfig.split("\n")) {
//            commentedConfig.append(commentChar).append(line).append("\n");
//        }
//        commentedConfig.append(commentChar).append(commentChar).append("\n");
//
//        return commentedConfig.toString();
//    }

    @Override
    public SerializationConfig getSerializationConfig() {
        return serializationConfig;
    }
}
