package org.smartparam.engine.core.repository;

import org.smartparam.engine.core.function.FunctionInvoker;
import org.smartparam.engine.model.function.Function;

/**
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public interface InvokerRepository {

    <T extends Function> void registerInvoker(String typeCode, FunctionInvoker<T> invoker);

    <T extends Function> FunctionInvoker<T> getInvoker(T function);

}
