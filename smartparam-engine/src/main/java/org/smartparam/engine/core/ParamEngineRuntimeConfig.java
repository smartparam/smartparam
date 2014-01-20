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
package org.smartparam.engine.core;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.smartparam.engine.core.function.FunctionCache;
import org.smartparam.engine.core.prepared.PreparedParamCache;
import org.smartparam.engine.core.matcher.Matcher;
import org.smartparam.engine.core.function.FunctionInvoker;
import org.smartparam.engine.core.function.FunctionRepository;
import org.smartparam.engine.core.matcher.MatcherDecoderRepository;
import org.smartparam.engine.core.matcher.MatcherRepository;
import org.smartparam.engine.core.parameter.NamedParamRepository;
import org.smartparam.engine.core.type.Type;

/**
 * Snapshot of runtime configuration of ParamEngine, all collections are immutable.
 * It is possible to act on exposed services, but remember that this is a snapshot, collections won't be updated. Another
 * one needs to be created after any changes.
 *
 * @author Adam Dubiel
 */
public final class ParamEngineRuntimeConfig {

    private final FunctionCache functionCache;

    private final PreparedParamCache paramCache;

    private final Map<String, FunctionInvoker> invokers;

    private final Map<String, Type<?>> types;

    private final Map<String, Matcher> matchers;

    private final Map<String, FunctionRepository> functionRepositories;

    private final MatcherRepository matcherRepository;

    private final MatcherDecoderRepository matcherDecoderRepository;

    private final List<NamedParamRepository> paramRepositories;

    private final ParamRepositoriesNaming paramRepositoriesNaming;

    /**
     * Constructor for configuration object - all objects are read only and
     * collections are immutable.
     */
    public ParamEngineRuntimeConfig(FunctionCache functionCache,
            PreparedParamCache paramCache,
            Map<String, FunctionRepository> functionRepositories,
            List<NamedParamRepository> paramRepositories,
            Map<String, FunctionInvoker> invokers,
            Map<String, Type<?>> types,
            MatcherRepository matcherRepository,
            MatcherDecoderRepository matcherDecoderRepository) {
        this.functionCache = functionCache;
        this.paramCache = paramCache;
        this.functionRepositories = Collections.unmodifiableMap(functionRepositories);
        this.paramRepositories = Collections.unmodifiableList(paramRepositories);
        this.invokers = Collections.unmodifiableMap(invokers);
        this.types = Collections.unmodifiableMap(types);
        this.matchers = Collections.unmodifiableMap(matcherRepository.registeredItems());

        this.matcherRepository = matcherRepository;
        this.matcherDecoderRepository = matcherDecoderRepository;

        this.paramRepositoriesNaming = new ParamRepositoriesNaming(paramRepositories);
    }

    public FunctionCache getFunctionCache() {
        return functionCache;
    }

    public PreparedParamCache getParamCache() {
        return paramCache;
    }

    public MatcherRepository getMatcherRepository() {
        return matcherRepository;
    }

    public MatcherDecoderRepository getMatcherDecoderRepository() {
        return matcherDecoderRepository;
    }
    
    public Map<String, FunctionRepository> getFunctionRepositories() {
        return functionRepositories;
    }

    public List<NamedParamRepository> getParamRepositories() {
        return paramRepositories;
    }

    public Map<String, FunctionInvoker> getInvokers() {
        return invokers;
    }

    public Map<String, Matcher> getMatchers() {
        return matchers;
    }

    public Map<String, Type<?>> getTypes() {
        return types;
    }

    public ParamRepositoriesNaming getParamRepositoriesNaming() {
        return paramRepositoriesNaming;
    }
}
