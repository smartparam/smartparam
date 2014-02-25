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
package org.smartparam.serializer.metadata;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import org.smartparam.engine.core.parameter.level.Level;
import org.smartparam.serializer.model.AppendableParameter;
import org.smartparam.serializer.model.DeserializedParameter;
import org.smartparam.serializer.exception.ParamSerializationException;
import org.smartparam.serializer.util.StreamPartReader;

/**
 *
 * @author Adam Dubiel
 */
public class JsonParameterMetadataDeserializer implements ParameterMetadataDeserializer {

    private final Gson gson;

    public JsonParameterMetadataDeserializer() {
        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(Level.class, new LevelJsonDeserializer());
        gson = builder.create();
    }

    @Override
    public AppendableParameter deserialize(BufferedReader reader) throws ParamSerializationException {
        String jsonConfig = null;
        try {
            jsonConfig = StreamPartReader.readPart(reader, '{', '}');
        } catch (IOException exception) {
            throw new ParamSerializationException("Unable to read config part from stream.", exception);
        }

        DeserializedParameter parameter = gson.fromJson(jsonConfig, DeserializedParameter.class);
        parameter.normalizeLevels();

        return parameter;
    }
}
