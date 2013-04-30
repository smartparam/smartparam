package org.smartparam.engine.core.repository;

import org.smartparam.engine.core.Repository;
import org.smartparam.engine.core.invoker.FunctionInvoker;
import org.smartparam.engine.model.function.Function;

/**
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public interface InvokerRepository extends Repository<FunctionInvoker> {

    FunctionInvoker getInvoker(Function function);
}
