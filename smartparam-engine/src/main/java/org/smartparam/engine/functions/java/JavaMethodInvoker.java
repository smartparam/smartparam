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
package org.smartparam.engine.functions.java;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Adam Dubiel
 */
public class JavaMethodInvoker {

    private static final Logger logger = LoggerFactory.getLogger(JavaMethodInvoker.class);

    /**
     * Invoke method on given object instance with arguments.
     */
    public Object invokeMethod(Object instance, Method method, boolean makeAccessible, Object... args) {
        try {
            if (makeAccessible) {
                method.setAccessible(true);
            }

            if (instance instanceof Proxy) {
                InvocationHandler handler = Proxy.getInvocationHandler(instance);
                return handler.invoke(instance, method, args);
            } else {
                return method.invoke(instance, args);
            }
        } catch (Throwable exception) {
            logger.error("", exception);
            throw new JavaFunctionInvocationException(exception, instance, method);
        }
    }
}
