package org.smartparam.engine.core.function;

import org.smartparam.engine.model.function.Function;

/**
 *
 * @param <FUNCTION>
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public interface FunctionInvoker<FUNCTION extends Function> {

    Object invoke(FUNCTION function, Object... args);
}
