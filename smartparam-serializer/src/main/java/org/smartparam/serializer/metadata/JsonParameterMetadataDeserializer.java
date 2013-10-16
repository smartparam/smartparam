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
import java.util.HashSet;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.engine.model.editable.EditableParameter;
import org.smartparam.serializer.config.SerializationConfig;
import org.smartparam.serializer.exception.ParamSerializationException;
import org.smartparam.serializer.util.StreamPartReader;

/**
 *
 * @author Adam Dubiel
 */
public class JsonParameterMetadataDeserializer implements ParameterMetadataDeserializer {

    private Class<? extends EditableParameter> parameterInstanceClass;

    private Gson gson;

    public JsonParameterMetadataDeserializer(SerializationConfig serializationConfig) {
        this.parameterInstanceClass = serializationConfig.parameterInstanceClass();

        LevelSerializationAdapter levelAdapter = new LevelSerializationAdapter(serializationConfig.levelInstanceClass());

        gson = (new GsonBuilder()).registerTypeAdapter(Level.class, levelAdapter).create();

        levelAdapter.setGson(gson);
    }

    @Override
    public Parameter deserialize(BufferedReader reader) throws ParamSerializationException {
        String jsonConfig = null;
        try {
            jsonConfig = StreamPartReader.readPart(reader, '{', '}');
        } catch (IOException exception) {
            throw new ParamSerializationException("Unable to read config part from stream.", exception);
        }

        EditableParameter parameter = gson.fromJson(jsonConfig, parameterInstanceClass);
        parameter.setEntries(new HashSet<ParameterEntry>());

        return parameter;
    }
}
