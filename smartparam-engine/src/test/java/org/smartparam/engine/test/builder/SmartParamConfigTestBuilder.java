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
package org.smartparam.engine.test.builder;

import java.util.ArrayList;
import java.util.HashMap;
import org.smartparam.engine.config.ParamEngineConfig;
import org.smartparam.engine.core.cache.FunctionCache;
import org.smartparam.engine.core.repository.ParamRepository;
import org.smartparam.engine.core.type.Type;

/**
 *
 * @author Adam Dubiel
 */
public class SmartParamConfigTestBuilder {

    private ParamEngineConfig config;

    private SmartParamConfigTestBuilder() {
        config = new ParamEngineConfig();
    }

    public static SmartParamConfigTestBuilder config() {
        return new SmartParamConfigTestBuilder();
    }

    public ParamEngineConfig build() {
        return config;
    }

    public SmartParamConfigTestBuilder withRepository(ParamRepository repository) {
        if(config.getParameterRepositories() == null) {
            config.setParameterRepositories(new ArrayList<ParamRepository>());
        }
        config.getParameterRepositories().add(repository);
        return this;
    }

    public SmartParamConfigTestBuilder withType(String key, Type<?> type) {
        if (config.getTypes() == null) {
            config.setTypes(new HashMap<String, Type<?>>());
        }
        config.getTypes().put(key, type);
        return this;
    }

    public SmartParamConfigTestBuilder withFunctionCache(FunctionCache functionCache) {
        config.setFunctionCache(functionCache);
        return this;
    }
}
