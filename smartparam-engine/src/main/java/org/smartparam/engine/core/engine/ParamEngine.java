package org.smartparam.engine.core.engine;

import org.smartparam.engine.config.ParamEngineRuntimeConfig;
import org.smartparam.engine.core.context.ParamContext;
import org.smartparam.engine.core.service.FunctionManager;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface ParamEngine {

	ParamValue get(String paramName, ParamContext ctx);

    ParamValue get(String paramName, Object... inputLevels);

    Object callFunction(String functionName, Object... args);

    ParamEngineRuntimeConfig getConfiguration();

    FunctionManager getFunctionManager();

    void setFunctionManager(FunctionManager functionManager);

    ParamPreparer getParamPreparer();

    void setParamPreparer(ParamPreparer paramPreparer);
}
