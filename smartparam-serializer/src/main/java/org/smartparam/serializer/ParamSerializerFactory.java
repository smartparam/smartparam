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

import org.smartparam.serializer.config.SerializationConfig;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import org.smartparam.engine.config.pico.PicoContainerUtil;

/**
 *
 * @author Adam Dubiel
 */
public class ParamSerializerFactory {

    public static ParamSerializer paramSerializer(SerializationConfig config) {
        ParamSerializerFactory factory = new ParamSerializerFactory();
        return factory.createSerializer(new ParamSerializerConfig(config));
    }

    public static ParamDeserializer paramDeserializer(SerializationConfig config) {
        ParamSerializerFactory factory = new ParamSerializerFactory();
        return factory.createDeserializer(new ParamSerializerConfig(config));
    }

    public ParamSerializer createSerializer(ParamSerializerConfig config) {
        PicoContainer container = createContainer(config);
        return container.getComponent(ParamSerializer.class);
    }

    public ParamDeserializer createDeserializer(ParamSerializerConfig config) {
        PicoContainer container = createContainer(config);
        return container.getComponent(ParamDeserializer.class);
    }

    private PicoContainer createContainer(ParamSerializerConfig config) {
        MutablePicoContainer container = PicoContainerUtil.createContainer();
        PicoContainerUtil.injectImplementations(container, config.getSerializationConfig());

        PicoContainerUtil.injectImplementations(container, config.getComponents());

        return container;
    }
}
