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

import java.util.Set;
import org.smartparam.engine.config.pico.ComponentConfig;
import org.smartparam.engine.config.pico.ComponentDefinition;
import org.smartparam.serializer.config.SerializationConfig;
import org.smartparam.serializer.entries.CsvParameterEntryDeserializer;
import org.smartparam.serializer.entries.CsvParameterEntrySerializer;
import org.smartparam.serializer.entries.ParameterEntryDeserializer;
import org.smartparam.serializer.entries.ParameterEntrySerializer;
import org.smartparam.serializer.metadata.JsonParameterMetadataDeserializer;
import org.smartparam.serializer.metadata.JsonParameterMetadataSerializer;
import org.smartparam.serializer.metadata.ParameterMetadataDeserializer;
import org.smartparam.serializer.metadata.ParameterMetadataSerializer;
import static org.smartparam.engine.config.pico.ComponentDefinition.component;

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
    protected void injectDefaults(Set<ComponentDefinition> components) {
        components.add(component(ParameterMetadataDeserializer.class, JsonParameterMetadataDeserializer.class));
        components.add(component(ParameterMetadataSerializer.class, JsonParameterMetadataSerializer.class));
        components.add(component(ParameterEntryDeserializer.class, CsvParameterEntryDeserializer.class));
        components.add(component(ParameterEntrySerializer.class, CsvParameterEntrySerializer.class));
        components.add(component(ParamSerializer.class, StandardParamSerializer.class));
        components.add(component(ParamDeserializer.class, StandardParamDeserializer.class));
    }

    public SerializationConfig getSerializationConfig() {
        return serializationConfig;
    }
}
