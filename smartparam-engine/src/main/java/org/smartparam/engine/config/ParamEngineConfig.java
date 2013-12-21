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

import org.smartparam.engine.config.initialization.ComponentInitializer;
import org.smartparam.engine.config.initialization.ComponentInitializerRunner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.smartparam.engine.annotated.RepositoryObjectKey;
import org.smartparam.engine.config.pico.ComponentConfig;
import org.smartparam.engine.cache.MapFunctionCache;
import org.smartparam.engine.cache.MapPreparedParamCache;
import org.smartparam.engine.core.prepared.BasicLevelPreparer;
import org.smartparam.engine.core.prepared.BasicParamPreparer;
import org.smartparam.engine.core.matcher.Matcher;
import org.smartparam.engine.core.function.FunctionInvoker;
import org.smartparam.engine.core.function.FunctionRepository;
import org.smartparam.engine.core.parameter.ParamRepository;
import org.smartparam.engine.annotated.repository.ScanningInvokerRepository;
import org.smartparam.engine.annotated.repository.ScanningMatcherRepository;
import org.smartparam.engine.annotated.repository.ScanningTypeRepository;
import org.smartparam.engine.core.function.BasicFunctionManager;
import org.smartparam.engine.annotated.repository.ScanningFunctionProvider;
import org.smartparam.engine.core.parameter.BasicParameterProvider;
import org.smartparam.engine.core.type.Type;

/**
 * Configuration to build ParamEngine instance - use {@link ParamEngineConfigBuilder}
 * to create this immutable object.
 *
 * @author Adam Dubiel
 */
public class ParamEngineConfig extends ComponentConfig {

    private final List<ParamRepository> parameterRepositories = new ArrayList<ParamRepository>();

    private final Map<RepositoryObjectKey, FunctionRepository> functionRepositories = new HashMap<RepositoryObjectKey, FunctionRepository>();

    private final Map<String, FunctionInvoker> functionInvokers = new HashMap<String, FunctionInvoker>();

    private final Map<String, Type<?>> types = new HashMap<String, Type<?>>();

    private final Map<String, Matcher> matchers = new HashMap<String, Matcher>();

    private ComponentInitializerRunner initializationRunner;

    private final List<ComponentInitializer> componentInitializers = new ArrayList<ComponentInitializer>();

    @Override
    protected void injectDefaults(List<Object> components) {
        components.add(BasicParamPreparer.class);
        components.add(BasicLevelPreparer.class);
        components.add(MapPreparedParamCache.class);
        components.add(BasicFunctionManager.class);
        components.add(ScanningFunctionProvider.class);
        components.add(MapFunctionCache.class);
        components.add(ScanningInvokerRepository.class);
        components.add(BasicParameterProvider.class);
        components.add(ScanningTypeRepository.class);
        components.add(ScanningMatcherRepository.class);
    }

    public List<ParamRepository> getParameterRepositories() {
        return Collections.unmodifiableList(parameterRepositories);
    }

    protected void addParameterRepositories(List<ParamRepository> parameterRepositories) {
        this.parameterRepositories.addAll(parameterRepositories);
    }

    public Map<RepositoryObjectKey, FunctionRepository> getFunctionRepositories() {
        return Collections.unmodifiableMap(functionRepositories);
    }

    protected void addFunctionRepository(RepositoryObjectKey key, FunctionRepository functionRepository) {
        this.functionRepositories.put(key, functionRepository);
    }

    public Map<String, FunctionInvoker> getFunctionInvokers() {
        return Collections.unmodifiableMap(functionInvokers);
    }

    protected void addFunctionInvoker(String key, FunctionInvoker functionInvoker) {
        this.functionInvokers.put(key, functionInvoker);
    }

    public Map<String, Type<?>> getTypes() {
        return Collections.unmodifiableMap(types);
    }

    protected void addType(String key, Type<?> type) {
        this.types.put(key, type);
    }

    public Map<String, Matcher> getMatchers() {
        return Collections.unmodifiableMap(matchers);
    }

    protected void addMatcher(String key, Matcher matcher) {
        this.matchers.put(key, matcher);
    }

    public List<ComponentInitializer> getComponentInitializers() {
        return Collections.unmodifiableList(componentInitializers);
    }

    protected void addComponentInitializers(List<ComponentInitializer> componentInitializers) {
        this.componentInitializers.addAll(componentInitializers);
    }

    public ComponentInitializerRunner getInitializationRunner() {
        return initializationRunner;
    }

    public void setInitializationRunner(ComponentInitializerRunner initializationRunner) {
        this.initializationRunner = initializationRunner;
    }
}
