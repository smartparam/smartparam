package org.smartparam.engine.config;

import org.smartparam.engine.core.cache.FunctionCache;
import org.smartparam.engine.core.cache.ParamCache;
import org.smartparam.engine.core.engine.ParamEngine;
import org.smartparam.engine.core.engine.ParamPreparer;
import org.smartparam.engine.core.service.FunctionManager;

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
        FunctionManager functionManager = paramEngine.getFunctionManager();
        ParamPreparer paramPreparer = paramEngine.getParamPreparer();

        FunctionCache functionCache = functionManager.getFunctionProvider().getFunctionCache();
        ParamCache paramCache = paramPreparer.getParamCache();

        ParamEngineRuntimeConfig runtmeConfig = new ParamEngineRuntimeConfig(functionCache, paramCache,
                functionManager.getInvokerRepository().registeredItems(),
                paramPreparer.getTypeRepository().registeredItems(),
                paramPreparer.getMatcherRepository().registeredItems());

        return runtmeConfig;
    }
}
