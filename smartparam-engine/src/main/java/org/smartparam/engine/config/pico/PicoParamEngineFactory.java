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

import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import org.smartparam.engine.config.initialization.BasicComponentInitializerRunner;
import org.smartparam.engine.config.ComponentInitializerRunner;
import org.smartparam.engine.config.ParamEngineConfig;
import org.smartparam.engine.config.ParamEngineFactory;
import org.smartparam.engine.core.engine.ParamEngine;
import org.smartparam.engine.core.engine.SmartParamEngine;
import org.smartparam.engine.core.repository.InvokerRepository;
import org.smartparam.engine.core.repository.MatcherRepository;
import org.smartparam.engine.core.repository.TypeRepository;
import org.smartparam.engine.core.service.FunctionProvider;
import org.smartparam.engine.core.service.ParameterProvider;

/**
 *
 * @author Adam Dubiel
 */
public class PicoParamEngineFactory implements ParamEngineFactory {

    public static ParamEngine paramEngine(ParamEngineConfig config) {
        if (config instanceof PicoParamEngineConfig) {
            return new PicoParamEngineFactory().createParamEngine(config);

        } else {
            throw new IllegalArgumentException("Expected instance of PicoParamEngineConfig but got " + config.getClass().getCanonicalName());
        }
    }

    public static ParamEngine paramEngine(PicoParamEngineConfig config) {
        return new PicoParamEngineFactory().createParamEngine(config);
    }

    @Override
    public ParamEngine createParamEngine(ParamEngineConfig config) {
        PicoParamEngineConfig picoConfig = (PicoParamEngineConfig) config;

        ComponentInitializerRunner initializerRunner = prepareInitializerRunner(picoConfig);

        MutablePicoContainer picoContainer = PicoContainerUtil.createContainer();
        picoContainer.addComponent(SmartParamEngine.class);
        PicoContainerUtil.injectImplementations(picoContainer, picoConfig.getComponents());
        picoContainer.addComponent(new PicoParamEngineRuntimeConfigBuilder(picoContainer));

        ParamEngine engine = picoContainer.getComponent(ParamEngine.class);

        initializeRepositories(picoContainer, picoConfig, initializerRunner);
        initializerRunner.runInitializersOnList(picoContainer.getComponents());

        return engine;
    }

    private ComponentInitializerRunner prepareInitializerRunner(PicoParamEngineConfig config) {
        if (config.getInitializationRunner() == null) {
            ComponentInitializerRunner initializerRunner = new BasicComponentInitializerRunner(config.getComponentInitializers());
            config.setInitializationRunner(initializerRunner);
        }
        return config.getInitializationRunner();
    }

    private void initializeRepositories(PicoContainer container, PicoParamEngineConfig config, ComponentInitializerRunner initializerRunner) {
        initializerRunner.runInitializersOnList(config.getParameterRepositories());
        container.getComponent(ParameterProvider.class).registerAll(config.getParameterRepositories());

        initializerRunner.runInitializersOnList(config.getFunctionRepositories().values());
        container.getComponent(FunctionProvider.class).registerAll(config.getFunctionRepositories());

        container.getComponent(InvokerRepository.class).registerAll(config.getFunctionInvokers());
        container.getComponent(TypeRepository.class).registerAll(config.getTypes());
        container.getComponent(MatcherRepository.class).registerAll(config.getMatchers());
    }
}
