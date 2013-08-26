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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.smartparam.engine.core.cache.FunctionCache;
import org.smartparam.engine.core.cache.ParamCache;
import org.smartparam.engine.core.index.Matcher;
import org.smartparam.engine.core.invoker.FunctionInvoker;
import org.smartparam.engine.core.repository.FunctionRepository;
import org.smartparam.engine.core.repository.ParamRepository;
import org.smartparam.engine.core.type.Type;

/**
 * Runtime configuration of ParamEngine, all collections are immutable.
 *
 * @author Adam Dubiel
 */
public class ParamEngineRuntimeConfig {

    private FunctionCache functionCache;

    private ParamCache paramCache;

    private Map<String, FunctionInvoker> invokers;

    private Map<String, Type<?>> types;

    private Map<String, Matcher> matchers;

    private Map<String, FunctionRepository> functionRepositories;

    private List<ParamRepository> paramRepositories;

    /**
     * Constructor for configuration object - all objects are read only and
     * collections are immutable.
     */
    public ParamEngineRuntimeConfig(FunctionCache functionCache,
            ParamCache paramCache,
            Map<String, FunctionRepository> functionRepositories,
            List<ParamRepository> paramRepositories,
            Map<String, FunctionInvoker> invokers,
            Map<String, Type<?>> types,
            Map<String, Matcher> matchers) {
        this.functionCache = functionCache;
        this.paramCache = paramCache;
        this.functionRepositories = Collections.unmodifiableMap(functionRepositories);
        this.paramRepositories = Collections.unmodifiableList(paramRepositories);
        this.invokers = Collections.unmodifiableMap(invokers);
        this.types = Collections.unmodifiableMap(types);
        this.matchers = Collections.unmodifiableMap(matchers);
    }

    public FunctionCache getFunctionCache() {
        return functionCache;
    }

    public ParamCache getParamCache() {
        return paramCache;
    }

    public Map<String, FunctionRepository> getFunctionRepositories() {
        return functionRepositories;
    }

    public List<ParamRepository> getParamRepositories() {
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
}
