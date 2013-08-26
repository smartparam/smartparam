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
package org.smartparam.engine.config.pico;

import org.picocontainer.PicoContainer;
import org.smartparam.engine.config.ParamEngineRuntimeConfig;
import org.smartparam.engine.config.ParamEngineRuntimeConfigBuilder;
import org.smartparam.engine.core.cache.FunctionCache;
import org.smartparam.engine.core.cache.ParamCache;
import org.smartparam.engine.core.repository.InvokerRepository;
import org.smartparam.engine.core.repository.MatcherRepository;
import org.smartparam.engine.core.repository.TypeRepository;
import org.smartparam.engine.core.service.FunctionProvider;
import org.smartparam.engine.core.service.ParameterProvider;

/**
 * Runtime config builder that extracts runtime information form parameter engine
 * "personal" PicoContainer.
 *
 * @author Adam Dubiel
 */
public class PicoParamEngineRuntimeConfigBuilder implements ParamEngineRuntimeConfigBuilder {

    private PicoContainer engineContainer;

    public PicoParamEngineRuntimeConfigBuilder(PicoContainer engineContainer) {
        this.engineContainer = engineContainer;
    }

    @Override
    public ParamEngineRuntimeConfig buildConfig() {
        return new ParamEngineRuntimeConfig(
                engineContainer.getComponent(FunctionCache.class),
                engineContainer.getComponent(ParamCache.class),
                engineContainer.getComponent(FunctionProvider.class).registeredItems(),
                engineContainer.getComponent(ParameterProvider.class).registeredItems(),
                engineContainer.getComponent(InvokerRepository.class).registeredItems(),
                engineContainer.getComponent(TypeRepository.class).registeredItems(),
                engineContainer.getComponent(MatcherRepository.class).registeredItems());

    }

    public PicoContainer getContainer() {
        return engineContainer;
    }
}
