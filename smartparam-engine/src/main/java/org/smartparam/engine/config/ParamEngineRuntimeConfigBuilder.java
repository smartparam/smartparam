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

import org.smartparam.engine.core.engine.ParamEngine;

/**
 * Traverses SmartParamEngine service tree and returns runtime configuration of
 * engine in form of immutable object.
 *
 * @author Adam Dubiel
 */
public class ParamEngineRuntimeConfigBuilder {

    /**
     * Creates runtime configuration descriptor for given param engine.
     *
     * @param paramEngine engine
     * @return configuration
     */
    public ParamEngineRuntimeConfig buildConfig(ParamEngine paramEngine) {
//        FunctionManager functionManager = paramEngine.getFunctionManager();
//        ParamPreparer paramPreparer = paramEngine.getParamPreparer();
//
//        FunctionCache functionCache = functionManager.getFunctionProvider().getFunctionCache();
//        ParamCache paramCache = paramPreparer.getParamCache();
//
//        ParamEngineRuntimeConfig runtmeConfig = new ParamEngineRuntimeConfig(functionCache, paramCache,
//                functionManager.getInvokerRepository().registeredItems(),
//                paramPreparer.getLevelPreparer().getTypeRepository().registeredItems(),
//                paramPreparer.getLevelPreparer().getMatcherRepository().registeredItems());
//
//        return runtmeConfig;
        return null;
    }
}
