package org.smartparam.engine.core.repository;

import org.smartparam.engine.model.function.Function;

/**
 * Repository of functions. ParamEngine instance can have multiple function
 * repositories defined, see {@link org.smartparam.engine.core.service.FunctionProvider}.
 *
 * Each repository is registered under unique name. Function has type, which
 * is used to choose correct {@link org.smartparam.engine.core.invoker.FunctionInvoker},
 * which will run function (invoker identifier has to match function type).
 *
 * @see Function
 * @see org.smartparam.engine.core.invoker.FunctionInvoker
 *
 * @author Adam Dubiel
 * @since 1.0.0
 */
public interface FunctionRepository {

    /**
     * Should return instance of function or null if was not found.
     *
     * @param functionName unique (repository-wide) name of function
     * @return function object
     */
    Function loadFunction(String functionName);
}
