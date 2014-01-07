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
package org.smartparam.engine.config;

import org.smartparam.engine.config.initialization.ComponentInitializerRunner;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import org.smartparam.engine.config.initialization.BasicComponentInitializerRunner;
import org.smartparam.engine.config.pico.ComponentDefinition;
import org.smartparam.engine.config.pico.PicoContainerUtil;
import org.smartparam.engine.config.pico.PicoParamEngineRuntimeConfigBuilder;
import org.smartparam.engine.core.ParamEngine;
import org.smartparam.engine.core.SmartParamEngine;
import org.smartparam.engine.core.function.InvokerRepository;
import org.smartparam.engine.core.matcher.MatcherRepository;
import org.smartparam.engine.core.type.TypeRepository;
import org.smartparam.engine.core.function.FunctionProvider;
import org.smartparam.engine.core.parameter.ParameterProvider;
import static org.smartparam.engine.config.pico.ComponentDefinition.component;

/**
 * Creates ParamEngine instance using provided configuration. Use {@link ParamEngineConfigBuilder}
 * to construct configuration object.
 *
 * Under the hood PicoContainer is used to create ParamEngine dependency tree.
 *
 * @author Adam Dubiel
 */
public class ParamEngineFactory {

    /**
     * Create instance of ParamEngine, use {@link ParamEngineConfigBuilder}
     * to construct configuration object.
     */
    public static ParamEngine paramEngine(ParamEngineConfig config) {
        return new ParamEngineFactory().createParamEngine(config);
    }

    /**
     * Create instance of ParamEngine, use {@link ParamEngineConfigBuilder}
     * to construct configuration object.
     */
    public ParamEngine createParamEngine(ParamEngineConfig config) {
        ComponentInitializerRunner initializerRunner = prepareInitializerRunner(config);

        config.addComponent(component(ParamEngine.class, SmartParamEngine.class));

        MutablePicoContainer picoContainer = PicoContainerUtil.createContainer();
        PicoContainerUtil.injectImplementations(picoContainer, config.getComponents());
        picoContainer.addComponent(new PicoParamEngineRuntimeConfigBuilder(picoContainer));

        ParamEngine engine = picoContainer.getComponent(ParamEngine.class);

        initializeRepositories(picoContainer, config, initializerRunner);
        initializerRunner.runInitializersOnList(picoContainer.getComponents());

        return engine;
    }

    private ComponentInitializerRunner prepareInitializerRunner(ParamEngineConfig config) {
        if (config.getInitializationRunner() == null) {
            ComponentInitializerRunner initializerRunner = new BasicComponentInitializerRunner();
            initializerRunner.registerInitializers(config.getComponentInitializers());
            config.setInitializationRunner(initializerRunner);
        }
        return config.getInitializationRunner();
    }

    private void initializeRepositories(PicoContainer container, ParamEngineConfig config, ComponentInitializerRunner initializerRunner) {
        initializerRunner.runInitializersOnList(config.getParameterRepositories());
        container.getComponent(ParameterProvider.class).registerAll(config.getParameterRepositories());

        initializerRunner.runInitializersOnList(config.getFunctionRepositories().values());
        container.getComponent(FunctionProvider.class).registerWithKeys(config.getFunctionRepositories());

        container.getComponent(InvokerRepository.class).registerAll(config.getFunctionInvokers());
        container.getComponent(TypeRepository.class).registerAll(config.getTypes());
        container.getComponent(MatcherRepository.class).registerAll(config.getMatchers());
    }
}
