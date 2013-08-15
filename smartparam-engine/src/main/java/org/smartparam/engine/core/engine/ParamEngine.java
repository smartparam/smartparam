package org.smartparam.engine.core.engine;

import org.smartparam.engine.config.ParamEngineRuntimeConfig;
import org.smartparam.engine.core.context.ParamContext;
import org.smartparam.engine.core.service.FunctionManager;

/**
 * Single point of entry to get all parameters and call functions managed by
 * parameter engine.
 *
 * @author Adam Dubiel
 */
public interface ParamEngine {

    /**
     * Return submatrix of parameter rows that match values from context.
     * Remember, that to use dynamic context levels need to have defined
     * levelCreators.
     *
     * @param parameterName name of parameter to search
     * @param context evaluation context
     * @return resulting submatrix
     */
	ParamValue get(String parameterName, ParamContext context);

    /**
     * Return submatrix of parameter rows that match provided query values.
     * Input levels array should have length equal to parameters input levels count.
     *
     * @param parameterName name of parameter to search
     * @param inputLevels values to match against each input level
     * @return resulting submatrix
     */
    ParamValue get(String paramName, Object... inputLevels);

    /**
     * Use function engine to call function registered under provided name,
     * passing provided invocation arguments.
     *
     * @param functionName name of function to run
     * @param args function invocation arguments
     * @return invocation result, null for void functions
     */
    Object callFunction(String functionName, Object... args);

    /**
     * Return runtime configuration of this instance of parameter engine.
     *
     * @return runtime config
     */
    ParamEngineRuntimeConfig getConfiguration();

    FunctionManager getFunctionManager();

    void setFunctionManager(FunctionManager functionManager);

    ParamPreparer getParamPreparer();

    void setParamPreparer(ParamPreparer paramPreparer);
}
