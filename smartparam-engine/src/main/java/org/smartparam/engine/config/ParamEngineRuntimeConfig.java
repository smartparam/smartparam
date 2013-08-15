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
import java.util.Map;
import org.smartparam.engine.core.cache.FunctionCache;
import org.smartparam.engine.core.cache.ParamCache;
import org.smartparam.engine.core.index.Matcher;
import org.smartparam.engine.core.invoker.FunctionInvoker;
import org.smartparam.engine.core.type.Type;

/**
 * Runtime configuration of SmartParam. All collections are immutable.
 *
 * @author Adam Dubiel
 */
public class ParamEngineRuntimeConfig {

    private FunctionCache functionCache;

    private ParamCache paramCache;

    private Map<String, FunctionInvoker> invokers;

    private Map<String, Type<?>> types;

    private Map<String, Matcher> matchers;

    /**
     * Constructor for configuration object - all objects are read only and
     * collections are immutable.
     *
     * @param functionCache function cache
     * @param paramCache parameter cache
     * @param invokers registered function invokers
     * @param types registered types
     * @param matchers registered matchers
     */
    public ParamEngineRuntimeConfig(FunctionCache functionCache,
            ParamCache paramCache,
            Map<String, FunctionInvoker> invokers,
            Map<String, Type<?>> types,
            Map<String, Matcher> matchers) {
        this.functionCache = functionCache;
        this.paramCache = paramCache;
        this.invokers = Collections.unmodifiableMap(invokers);
        this.types = Collections.unmodifiableMap(types);
        this.matchers = Collections.unmodifiableMap(matchers);
    }

    /**
     * Function cache.
     *
     * @return function cache
     */
    public FunctionCache getFunctionCache() {
        return functionCache;
    }

    /**
     * Parameter cache.
     *
     * @return parameter cache
     */
    public ParamCache getParamCache() {
        return paramCache;
    }

    /**
     * Registered invokers
     *
     * @return immutable map
     */
    public Map<String, FunctionInvoker> getInvokers() {
        return invokers;
    }

    /**
     * Registered matchers.
     *
     * @return matchers
     */
    public Map<String, Matcher> getMatchers() {
        return matchers;
    }

    /**
     * Registered types.
     *
     * @return types
     */
    public Map<String, Type<?>> getTypes() {
        return types;
    }
}
