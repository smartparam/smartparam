package org.smartparam.engine.core.repository;

import java.util.Map;
import org.smartparam.engine.core.invoker.FunctionInvoker;
import org.smartparam.engine.model.function.Function;

/**
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public interface InvokerRepository {

    void registerInvoker(String typeCode, FunctionInvoker invoker);

    FunctionInvoker getInvoker(Function function);

    void setInvokers(Map<String, FunctionInvoker> invokers);
}
