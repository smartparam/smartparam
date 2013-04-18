package org.smartparam.engine.core.invoker;

import org.smartparam.engine.model.function.Function;

/**
 *
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface FunctionInvoker {

    Object invoke(Function function, Object... args);
}
