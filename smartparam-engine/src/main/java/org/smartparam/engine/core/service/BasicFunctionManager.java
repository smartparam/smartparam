package org.smartparam.engine.core.service;

import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.core.invoker.FunctionInvoker;
import org.smartparam.engine.core.repository.InvokerRepository;
import org.smartparam.engine.model.function.Function;

/**
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class BasicFunctionManager implements FunctionManager {

    private InvokerRepository invokerRepository;

    private FunctionProvider functionProvider;

    @Override
    public Object invokeFunction(String name, Object... args) {
        Function function = functionProvider.getFunction(name);
        return invokeFunction(function, args);
    }

    @Override
    public Object invokeFunction(Function function, Object... args) {
        FunctionInvoker invoker = invokerRepository.getInvoker(function);

        if (invoker == null) {
            throw new SmartParamException(SmartParamErrorCode.UNDEFINED_FUNCTION_INVOKER,
                    String.format("Could not find function invoker for function %s of type %s. "
                    + "To see all registered function invokers, look for MapRepository logs on INFO level during startup.",
                    function.getName(), function.getType()));
        }

        try {
            return invoker.invoke(function, args);
        } catch (RuntimeException e) {
            throw new SmartParamException(SmartParamErrorCode.FUNCTION_INVOKE_ERROR, e, "Failed to invoke function: " + function);
        }
    }

    @Override
    public FunctionProvider getFunctionProvider() {
        return functionProvider;
    }

    @Override
    public void setFunctionProvider(FunctionProvider functionProvider) {
        this.functionProvider = functionProvider;
    }

    @Override
    public InvokerRepository getInvokerRepository() {
        return invokerRepository;
    }

    @Override
    public void setInvokerRepository(InvokerRepository invokerRepository) {
        this.invokerRepository = invokerRepository;
    }
}
