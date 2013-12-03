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
package org.smartparam.serializer.config;

import java.util.List;
import org.smartparam.engine.config.pico.ComponentConfig;
import org.smartparam.serializer.StandardParamDeserializer;
import org.smartparam.serializer.StandardParamSerializer;
import org.smartparam.serializer.SerializationConfig;
import org.smartparam.serializer.entries.CsvParameterEntryDeserializer;
import org.smartparam.serializer.entries.CsvParameterEntrySerializer;
import org.smartparam.serializer.metadata.JsonParameterMetadataDeserializer;
import org.smartparam.serializer.metadata.JsonParameterMetadataSerializer;

/**
 *
 * @author Adam Dubiel
 */
public class ParamSerializerConfig extends ComponentConfig {

    private final SerializationConfig serializationConfig;

    public ParamSerializerConfig(SerializationConfig serializationConfig) {
        this.serializationConfig = serializationConfig;
    }

    @Override
    protected void injectDefaults(List<Object> components) {
        components.add(JsonParameterMetadataDeserializer.class);
        components.add(JsonParameterMetadataSerializer.class);
        components.add(CsvParameterEntryDeserializer.class);
        components.add(CsvParameterEntrySerializer.class);
        components.add(StandardParamSerializer.class);
        components.add(StandardParamDeserializer.class);
    }

    public SerializationConfig getSerializationConfig() {
        return serializationConfig;
    }
}
