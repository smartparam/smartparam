package org.smartparam.engine.core.config;

import org.smartparam.engine.core.cache.FunctionCache;
import org.smartparam.engine.core.cache.ParamCache;
import org.smartparam.engine.core.engine.ParamEngine;
import org.smartparam.engine.core.engine.ParamPreparer;
import org.smartparam.engine.core.service.FunctionManager;

/**
 * Traverses SmartParamEngine service tree and returns runtime configuration of
 * engine in form of immutable object.
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class SmartParamConfigBuilder {

    /**
     * Creates runtime configuration descriptor for given param engine.
     *
     * @param paramEngine engine
     * @return configuration
     */
    public SmartParamConfig buildConfig(ParamEngine paramEngine) {
        FunctionManager functionManager = paramEngine.getFunctionManager();
        ParamPreparer paramPreparer = paramEngine.getParamPreparer();

        FunctionCache functionCache = functionManager.getFunctionProvider().getFunctionCache();
        ParamCache paramCache = paramPreparer.getParamCache();

        SmartParamConfig runtmeConfig = new SmartParamConfig(functionCache, paramCache,
                functionManager.getInvokerRepository().registeredInvokers(),
                paramPreparer.getTypeRepository().registeredTypes(),
                paramPreparer.getMatcherRepository().registeredMatchers());

        return runtmeConfig;
    }
}
