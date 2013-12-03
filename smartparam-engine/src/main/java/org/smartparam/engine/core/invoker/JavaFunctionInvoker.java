/*
 * Copyright 2013 Adam Dubiel, Przemek Hertel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.smartparam.engine.core.invoker;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.smartparam.engine.annotated.annotations.ParamFunctionInvoker;
import org.smartparam.engine.core.function.Function;
import org.smartparam.engine.core.function.JavaFunction;
import org.smartparam.engine.util.reflection.ReflectionsConstructorUtil;

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
            obj = ReflectionsConstructorUtil.createObject(objectClass);
            instanceMap.put(objectClass, obj);
        }
        return obj;
    }
}
