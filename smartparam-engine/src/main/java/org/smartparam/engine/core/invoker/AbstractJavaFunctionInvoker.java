package org.smartparam.engine.core.invoker;

import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.exception.SmartParamException;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public abstract class AbstractJavaFunctionInvoker implements FunctionInvoker {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected Object invokeMethod(Object instance, Method method, Object... args) {
        try {
            return method.invoke(instance, args);
        } catch (Exception e) {
            logger.error("", e);
            throw new SmartParamException(SmartParamErrorCode.FUNCTION_INVOKE_ERROR, e,
                    String.format("Error invoking method %s on object %s.",
                    method.getName(), instance != null ? instance.getClass().getSimpleName() : "<null>"));
        }
    }
}
