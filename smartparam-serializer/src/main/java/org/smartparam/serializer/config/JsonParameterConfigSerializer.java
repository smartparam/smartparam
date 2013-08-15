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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.smartparam.engine.model.Parameter;

/**
 *
 * @author Adam Dubiel
 */
public class JsonParameterConfigSerializer implements ParameterConfigSerializer {

    private static final String[] IGNORED_PROPERTIES = new String[]{"entries"};

    private Gson gson;

    public JsonParameterConfigSerializer() {
        PropertyExclusionStrategy exclusionStrategy = new PropertyExclusionStrategy(IGNORED_PROPERTIES);

        gson = (new GsonBuilder()).setExclusionStrategies(exclusionStrategy).setPrettyPrinting().create();
    }

    @Override
    public String serialize(Parameter parameter) {
        String serializedConfig = gson.toJson(parameter);

        return serializedConfig;
    }
}
