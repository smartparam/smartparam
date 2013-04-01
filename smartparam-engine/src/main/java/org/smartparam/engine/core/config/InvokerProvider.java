package org.smartparam.engine.core.config;

import org.smartparam.engine.core.function.FunctionInvoker;
import org.smartparam.engine.model.FunctionImpl;

/**
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public interface InvokerProvider {

    <T extends FunctionImpl> void registerInvoker(String implCode, FunctionInvoker<T> invoker);

    <T extends FunctionImpl> FunctionInvoker<T> getInvoker(T function);
}
