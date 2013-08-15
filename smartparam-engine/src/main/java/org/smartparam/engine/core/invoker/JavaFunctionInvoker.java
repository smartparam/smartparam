package org.smartparam.engine.core.invoker;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.smartparam.engine.annotations.ParamFunctionInvoker;
import org.smartparam.engine.model.function.Function;
import org.smartparam.engine.model.function.JavaFunction;
import org.smartparam.engine.util.reflection.ReflectionsHelper;

/**
 *
 * @author Adam Dubiel
 */
@ParamFunctionInvoker("java")
public class JavaFunctionInvoker extends AbstractJavaFunctionInvoker {

    private final Map<Class<?>, Object> instanceMap = new ConcurrentHashMap<Class<?>, Object>();

    @Override
    public Object invoke(Function function, Object... args) {
        JavaFunction javaFunction = (JavaFunction) function;

        Class<?> clazz = javaFunction.getMethod().getDeclaringClass();
        Method method = javaFunction.getMethod();

        Object instance = null;

        if (!Modifier.isStatic(method.getModifiers())) {
            instance = findInstance(clazz);
        }

        return invokeMethod(instance, method, args);
    }

    private Object findInstance(Class<?> objectClass) {
        Object obj = instanceMap.get(objectClass);
        if (obj == null) {
            obj = ReflectionsHelper.createObject(objectClass);
            instanceMap.put(objectClass, obj);
        }
        return obj;
    }
}
