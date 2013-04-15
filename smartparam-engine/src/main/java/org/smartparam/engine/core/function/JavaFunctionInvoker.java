package org.smartparam.engine.core.function;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.annotations.SmartParamFunctionInvoker;
import org.smartparam.engine.core.exception.SmartParamDefinitionException;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.model.function.JavaFunction;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
@SmartParamFunctionInvoker("java")
public class JavaFunctionInvoker extends AbstractJavaFunctionInvoker<JavaFunction> implements FunctionInvoker<JavaFunction> {

    /**
     * Logger.
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Map<Class<?>, Object> instanceMap = new ConcurrentHashMap<Class<?>, Object>();

    @Override
    public Object invoke(JavaFunction function, Object... args) {
        Class<?> clazz = function.getMethod().getDeclaringClass();
        Method method = function.getMethod();

        Object instance = null;

        if (!Modifier.isStatic(method.getModifiers())) {
            instance = findInstance(clazz);
        }

        return invoke(instance, method, args);
    }

    private Object findInstance(Class<?> clazz) {
        Object obj = instanceMap.get(clazz);
        if (obj == null) {
            obj = createInstance(clazz);
            instanceMap.put(clazz, obj);
        }
        return obj;
    }

    private Object createInstance(Class<?> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            logger.error("", e);
            throw new SmartParamDefinitionException(
                    SmartParamErrorCode.FUNCTION_INVOKE_ERROR, e,
                    "Error instantiating class: " + clazz + ", msg=" + e.getMessage());
        }
    }
}
