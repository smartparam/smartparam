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

import org.smartparam.engine.config.ParamEngineConfig;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.smartparam.engine.config.ComponentInitializer;
import org.smartparam.engine.config.ComponentInitializerRunner;
import org.smartparam.engine.core.cache.MapFunctionCache;
import org.smartparam.engine.core.cache.MapParamCache;
import org.smartparam.engine.core.engine.BasicLevelPreparer;
import org.smartparam.engine.core.engine.BasicParamPreparer;
import org.smartparam.engine.core.index.Matcher;
import org.smartparam.engine.core.invoker.FunctionInvoker;
import org.smartparam.engine.core.repository.FunctionRepository;
import org.smartparam.engine.core.repository.ParamRepository;
import org.smartparam.engine.core.repository.BasicInvokerRepository;
import org.smartparam.engine.core.repository.BasicMatcherRepository;
import org.smartparam.engine.core.repository.BasicTypeRepository;
import org.smartparam.engine.core.service.BasicFunctionManager;
import org.smartparam.engine.core.service.BasicFunctionProvider;
import org.smartparam.engine.core.service.BasicParameterProvider;
import org.smartparam.engine.core.type.Type;

/**
 *
 * @author Adam Dubiel
 */
public class PicoParamEngineConfig extends ComponentConfig implements ParamEngineConfig {

    private List<ParamRepository> parameterRepositories = new ArrayList<ParamRepository>();

    private Map<String, FunctionRepository> functionRepositories = new LinkedHashMap<String, FunctionRepository>();

    private Map<String, FunctionInvoker> functionInvokers = new HashMap<String, FunctionInvoker>();

    private Map<String, Type<?>> types = new HashMap<String, Type<?>>();

    private Map<String, Matcher> matchers = new HashMap<String, Matcher>();

    private ComponentInitializerRunner initializationRunner;

    private List<ComponentInitializer> componentInitializers = new ArrayList<ComponentInitializer>();

    @Override
    protected void injectDefaults(List<Object> components) {
        components.add(BasicParamPreparer.class);
        components.add(BasicLevelPreparer.class);
        components.add(MapParamCache.class);
        components.add(BasicFunctionManager.class);
        components.add(BasicFunctionProvider.class);
        components.add(MapFunctionCache.class);
        components.add(BasicInvokerRepository.class);
        components.add(BasicParameterProvider.class);
        components.add(BasicTypeRepository.class);
        components.add(BasicMatcherRepository.class);
    }

    @Override
    public List<ParamRepository> getParameterRepositories() {
        return parameterRepositories;
    }

    public void setParameterRepositories(List<ParamRepository> parameterRepositories) {
        this.parameterRepositories = parameterRepositories;
    }

    @Override
    public Map<String, FunctionRepository> getFunctionRepositories() {
        return functionRepositories;
    }

    public void setFunctionRepositories(Map<String, FunctionRepository> functionRepositories) {
        this.functionRepositories = functionRepositories;
    }

    @Override
    public Map<String, FunctionInvoker> getFunctionInvokers() {
        return functionInvokers;
    }

    public void setFunctionInvokers(Map<String, FunctionInvoker> functionInvokers) {
        this.functionInvokers = functionInvokers;
    }

    @Override
    public Map<String, Type<?>> getTypes() {
        return types;
    }

    public void setTypes(Map<String, Type<?>> types) {
        this.types = types;
    }

    @Override
    public Map<String, Matcher> getMatchers() {
        return matchers;
    }

    public void setMatchers(Map<String, Matcher> matchers) {
        this.matchers = matchers;
    }

    @Override
    public List<ComponentInitializer> getComponentInitializers() {
        return componentInitializers;
    }

    public void setComponentInitializers(List<ComponentInitializer> componentInitializers) {
        this.componentInitializers = componentInitializers;
    }

    @Override
    public ComponentInitializerRunner getInitializationRunner() {
        return initializationRunner;
    }

    public void setInitializationRunner(ComponentInitializerRunner initializationRunner) {
        this.initializationRunner = initializationRunner;
    }
}
