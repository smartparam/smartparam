package org.smartparam.engine.core.engine;

import org.smartparam.engine.config.ParamEngineRuntimeConfig;
import org.smartparam.engine.core.context.ParamContext;
import org.smartparam.engine.core.service.FunctionManager;
import org.smartparam.engine.core.type.AbstractHolder;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface ParamEngine {

	ParamValue get(String paramName, ParamContext ctx);

	//todo ph get(name, levelvalues...)

    Object call(String paramName, ParamContext ctx, Object... args);

    Object callFunction(String functionName, Object... args);

    AbstractHolder[] getArray(String paramName, ParamContext ctx);

	/*
	 * will be removed - use get()
	 */
	@Deprecated
    MultiRow getMultiRow(String paramName, ParamContext ctx);

	/*
	 * use get()
	 */
	@Deprecated
    MultiValue getMultiValue(String paramName, ParamContext ctx);

    AbstractHolder getValue(String paramName, ParamContext ctx);

    AbstractHolder getValue(String paramName, Object... levelValues);

    ParamEngineRuntimeConfig getConfiguration();

    FunctionManager getFunctionManager();

    void setFunctionManager(FunctionManager functionManager);

    ParamPreparer getParamPreparer();

    void setParamPreparer(ParamPreparer paramPreparer);
}
