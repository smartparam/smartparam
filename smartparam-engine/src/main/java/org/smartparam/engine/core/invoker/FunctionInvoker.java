package org.smartparam.engine.core.invoker;

import org.smartparam.engine.model.function.Function;

/**
 * Invoker is able to run function (from Java class, script, etc...) described by
 * {@link Function} object with given parameters and return its value
 * (null if function is void).
 *
 * Unique name under which invoker is registered in
 * {@link org.smartparam.engine.core.repository.InvokerRepository} should be the
 * the same as type of functions invoked by this invoker. For example:
 * <pre>
 * function[type = java]  ---(invoked by)--> invoker[name = java]
 * function[type = groovy]  ---(invoked by)--> invoker[name = groovy]
 * </pre>
 * There can be only one function invoker registered per function type.
 *
 * @author Adam Dubiel
 */
public interface FunctionInvoker {

    /**
     * Invoke function, it is a good practice to wrap any outgoing exceptions
     * with {@link org.smartparam.engine.core.exception.SmartParamException}
     * with {@link org.smartparam.engine.core.exception.SmartParamErrorCode#FUNCTION_INVOKE_ERROR}
     * reason.
     *
     * @param function description of function to invoke
     * @param args arguments
     * @return evaluated value (can be null)
     */
    Object invoke(Function function, Object... args);
}
