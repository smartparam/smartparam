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
package org.smartparam.serializer.config.pico;

import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import org.picocontainer.parameters.ConstantParameter;
import org.smartparam.engine.config.pico.PicoContainerUtil;
import org.smartparam.engine.model.editable.EditableLevel;
import org.smartparam.serializer.ParamDeserializer;
import org.smartparam.serializer.ParamSerializer;
import org.smartparam.serializer.config.ParamSerializerConfig;
import org.smartparam.serializer.config.ParamSerializerFactory;
import org.smartparam.serializer.config.SerializationConfig;

/**
 *
 * @author Adam Dubiel
 */
public class PicoParamSerializerFactory implements ParamSerializerFactory {

    public static ParamSerializer paramSerializer(SerializationConfig config) {
        PicoParamSerializerFactory factory = new PicoParamSerializerFactory();
        return factory.createSerializer(new PicoParamSerializerConfig(config));
    }

    public static ParamDeserializer paramDeserializer(SerializationConfig config) {
        PicoParamSerializerFactory factory = new PicoParamSerializerFactory();
        return factory.createDeserializer(new PicoParamSerializerConfig(config));
    }

    @Override
    public ParamSerializer createSerializer(ParamSerializerConfig config) {
        PicoContainer container = createContainer((PicoParamSerializerConfig) config);
        return container.getComponent(ParamSerializer.class);
    }

    @Override
    public ParamDeserializer createDeserializer(ParamSerializerConfig config) {
        PicoContainer container = createContainer((PicoParamSerializerConfig) config);
        return container.getComponent(ParamDeserializer.class);
    }

    public PicoContainer createContainer(PicoParamSerializerConfig config) {
        MutablePicoContainer container = PicoContainerUtil.createContainer();
        PicoContainerUtil.injectImplementations(container, config.getSerializationConfig());

        PicoContainerUtil.injectImplementations(container, config.getComponents());

        return container;
    }
}
