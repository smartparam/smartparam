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
package org.smartparam.engine.util.reflection;

import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility for efficient setter invocation. Useful, when same setters should be
 * find by reflection and invoked multiple times. Whole search magic is hidden
 * in {@link #findSetter(java.lang.Class, java.lang.Object) } method.
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class ReflectionSetterInvoker {

    private Map<Class<?>, Map<Class<?>, Setter>> setterCache = new ConcurrentHashMap<Class<?>, Map<Class<?>, Setter>>();

    /**
     * Find and invoke setter on provided object.
     *
     * @param setterHostObject
     * @param forArg
     * @return true if setter for argument found, false otherwise
     */
    public boolean invokeSetter(Object setterHostObject, Object forArg) {
        Method setter = findSetter(setterHostObject.getClass(), forArg);
        if(setter == null) {
            return false;
        }
        ReflectionsHelper.runSetter(setter, setterHostObject, forArg);
        return true;
    }

    /**
     * Find setter method on host class (and its supertypes), that can handle
     * setting provided argument. Setter does not have to be conventional
     * JavaBeans setter, it is enough to be a single-argument void method that
     * can accept provided argument, no naming convention is applied. This method
     * uses setter caching, so subsequent calls to retrieve same setter are fast.
     *
     * Caution! Because search method does not follow JavaBeans convention,
     * using it to find different setters for objects of same type will result
     * in nondeterministic results!
     *
     * @param setterHostClass
     * @param forArg
     * @return
     */
    public Method findSetter(Class<?> setterHostClass, Object forArg) {
        Class<?> argClass = forArg.getClass();
        Map<Class<?>, Setter> settersMap = setterCache.get(setterHostClass);
        if (settersMap == null) {
            settersMap = new ConcurrentHashMap<Class<?>, Setter>();
            setterCache.put(getClass(), settersMap);
        }

        Setter setter = settersMap.get(argClass);
        if (setter == null) {
            Method method = lookupSetter(setterHostClass, argClass);
            setter = new Setter(method);
            settersMap.put(argClass, setter);
        }

        return setter.getMethod();
    }

    private Method lookupSetter(Class<?> setterHostClass, Class<?> propertyClass) {
        Class<?> clazz = setterHostClass;

        while (clazz != null) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (isSetter(method, propertyClass)) {
                    AccessController.doPrivileged(new AccessibleSetter(method));
                    return method;
                }
            }
            clazz = clazz.getSuperclass();
        }

        return null;
    }

    private boolean isSetter(Method method, Class<?> propertyClass) {
        Class<?>[] methodArgumentTypes = method.getParameterTypes();
        return method.getReturnType() == Void.TYPE && methodArgumentTypes.length == 1 && methodArgumentTypes[0].isAssignableFrom(propertyClass);
    }

    private static final class Setter {

        private Method method;

        Setter(Method method) {
            this.method = method;
        }

        Method getMethod() {
            return method;
        }
    }

    private static final class AccessibleSetter implements PrivilegedAction<Object> {

        private Method method;

        private AccessibleSetter(Method method) {
            this.method = method;
        }

        @Override
        public Object run() {
            method.setAccessible(true);
            return null;
        }
    }
}
