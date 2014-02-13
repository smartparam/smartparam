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
import java.util.Set;
import org.smartparam.engine.annotated.RepositoryObjectKey;
import org.smartparam.engine.annotated.repository.*;
import org.smartparam.engine.config.pico.ComponentConfig;
import org.smartparam.engine.cache.MapFunctionCache;
import org.smartparam.engine.cache.MapPreparedParamCache;
import org.smartparam.engine.core.prepared.BasicLevelPreparer;
import org.smartparam.engine.core.prepared.BasicParamPreparer;
import org.smartparam.engine.core.matcher.Matcher;
import org.smartparam.engine.core.function.FunctionInvoker;
import org.smartparam.engine.core.function.FunctionRepository;
import org.smartparam.engine.core.function.BasicFunctionManager;
import org.smartparam.engine.config.pico.ComponentDefinition;
import org.smartparam.engine.core.function.FunctionCache;
import org.smartparam.engine.core.function.FunctionManager;
import org.smartparam.engine.core.function.FunctionProvider;
import org.smartparam.engine.core.function.InvokerRepository;
import org.smartparam.engine.core.matcher.MatcherType;
import org.smartparam.engine.core.matcher.MatcherTypeRepository;
import org.smartparam.engine.core.matcher.MatcherRepository;
import org.smartparam.engine.core.output.entry.MapEntryFactory;
import org.smartparam.engine.core.output.factory.DefaultParamValueFactory;
import org.smartparam.engine.core.output.factory.DetailedParamValueFactory;
import org.smartparam.engine.core.parameter.BasicParameterManager;
import org.smartparam.engine.core.parameter.BasicParameterProvider;
import org.smartparam.engine.core.parameter.NamedParamRepository;
import org.smartparam.engine.core.parameter.ParameterManager;
import org.smartparam.engine.core.parameter.ParameterProvider;
import org.smartparam.engine.core.prepared.LevelPreparer;
import org.smartparam.engine.core.prepared.ParamPreparer;
import org.smartparam.engine.core.prepared.PreparedParamCache;
import org.smartparam.engine.core.type.Type;
import org.smartparam.engine.core.type.TypeRepository;
import org.smartparam.engine.report.tree.ReportLevelValuesSpaceRepository;
import static org.smartparam.engine.config.pico.ComponentDefinition.component;

/**
 * Configuration to build ParamEngine instance - use {@link ParamEngineConfigBuilder}
 * to create this immutable object.
 *
 * @author Adam Dubiel
 */
public class ParamEngineConfig extends ComponentConfig {

    private final List<NamedParamRepository> parameterRepositories = new ArrayList<NamedParamRepository>();

    private final Map<RepositoryObjectKey, FunctionRepository> functionRepositories = new HashMap<RepositoryObjectKey, FunctionRepository>();

    private final Map<String, FunctionInvoker> functionInvokers = new HashMap<String, FunctionInvoker>();

    private final Map<String, Type<?>> types = new HashMap<String, Type<?>>();

    private final Map<String, Matcher> matchers = new HashMap<String, Matcher>();

    private final Map<String, MatcherType<?>> matcherTypes = new HashMap<String, MatcherType<?>>();

    private PreparedParamCache parameterCache;

    private FunctionCache functionCache;

    private ComponentInitializerRunner initializationRunner;

    private final List<ComponentInitializer> componentInitializers = new ArrayList<ComponentInitializer>();

    @Override
    protected void injectDefaults(Set<ComponentDefinition> components) {
        components.add(component(ParameterManager.class, BasicParameterManager.class));
        components.add(component(ParamPreparer.class, BasicParamPreparer.class));
        components.add(component(LevelPreparer.class, BasicLevelPreparer.class));
        components.add(component(PreparedParamCache.class, MapPreparedParamCache.class));
        components.add(component(FunctionManager.class, BasicFunctionManager.class));
        components.add(component(FunctionProvider.class, ScanningFunctionProvider.class));
        components.add(component(FunctionCache.class, MapFunctionCache.class));
        components.add(component(InvokerRepository.class, ScanningInvokerRepository.class));
        components.add(component(ParameterProvider.class, BasicParameterProvider.class));
        components.add(component(TypeRepository.class, ScanningTypeRepository.class));
        components.add(component(MatcherRepository.class, ScanningMatcherRepository.class));
        components.add(component(MatcherTypeRepository.class, ScanningMatcherTypeRepository.class));
        components.add(component(MapEntryFactory.class, MapEntryFactory.class));
        components.add(component(DefaultParamValueFactory.class, DefaultParamValueFactory.class));
        components.add(component(DetailedParamValueFactory.class, DetailedParamValueFactory.class));
        components.add(component(ReportLevelValuesSpaceRepository.class, ScanningReportLevelValuesSpaceRepository.class));
    }

    public List<NamedParamRepository> getParameterRepositories() {
        return Collections.unmodifiableList(parameterRepositories);
    }

    protected void addParameterRepositories(List<NamedParamRepository> parameterRepositories) {
        this.parameterRepositories.addAll(parameterRepositories);
    }

    protected void addParameterRepository(NamedParamRepository parameterRepository) {
        this.parameterRepositories.add(parameterRepository);
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

    public Map<String, MatcherType<?>> getMatcherTypes() {
        return Collections.unmodifiableMap(matcherTypes);
    }

    protected void addMatcherType(String key, MatcherType<?> matcherType) {
        this.matcherTypes.put(key, matcherType);
    }

    public FunctionCache getFunctionCache() {
        return functionCache;
    }

    protected void setFunctionCache(FunctionCache functionCache) {
        this.functionCache = functionCache;
        addComponent(component(FunctionCache.class, functionCache));
    }

    public PreparedParamCache getParameterCache() {
        return parameterCache;
    }

    protected void setParameterCache(PreparedParamCache parameterCache) {
        this.parameterCache = parameterCache;
        addComponent(component(PreparedParamCache.class, parameterCache));
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
