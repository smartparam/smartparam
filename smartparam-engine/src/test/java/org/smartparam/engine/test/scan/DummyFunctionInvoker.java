package org.smartparam.engine.test.scan;

import org.smartparam.engine.annotations.ParamFunctionInvoker;
import org.smartparam.engine.core.invoker.FunctionInvoker;
import org.smartparam.engine.model.function.Function;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
@ParamFunctionInvoker(value = "", values = {"nameOne", "nameTwo"})
public class DummyFunctionInvoker implements FunctionInvoker {

    @Override
    public Object invoke(Function function, Object... args) {
        throw new UnsupportedOperationException("Dummy implementation");
    }

}
