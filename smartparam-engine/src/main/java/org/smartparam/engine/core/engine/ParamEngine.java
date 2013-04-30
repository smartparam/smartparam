package org.smartparam.engine.core.engine;

import org.smartparam.engine.core.config.SmartParamRuntimeConfig;
import org.smartparam.engine.core.context.ParamContext;
import org.smartparam.engine.core.service.FunctionManager;
import org.smartparam.engine.core.type.AbstractHolder;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface ParamEngine {

    Object call(String paramName, ParamContext ctx, Object... args);

    Object callFunction(String functionName, Object... args);

    AbstractHolder[] getArray(String paramName, ParamContext ctx);

    MultiRow getMultiRow(String paramName, ParamContext ctx);

    MultiValue getMultiValue(String paramName, ParamContext ctx);

    AbstractHolder getValue(String paramName, ParamContext ctx);

    AbstractHolder getValue(String paramName, Object... levelValues);

    SmartParamRuntimeConfig getConfiguration();

    FunctionManager getFunctionManager();

    ParamPreparer getParamPreparer();
}
