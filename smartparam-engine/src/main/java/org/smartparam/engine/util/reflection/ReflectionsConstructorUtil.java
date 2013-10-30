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

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.exception.SmartParamException;

/**
 *
 * @author Adam Dubiel
 */
public final class ReflectionsConstructorUtil {

    private ReflectionsConstructorUtil() {
    }

    public static Constructor<?> findNonSyntheticConstructor(Class<?> clazz, int argumentsCount) {
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (!constructor.isSynthetic() && constructor.getParameterTypes().length == argumentsCount) {
                constructor.setAccessible(true);
                return constructor;
            }
        }
        throw new SmartParamException("Unable to find non-synthetic constructor with " + argumentsCount + " arguments for class " + clazz.getCanonicalName()
                + " while looking for public, protected, package-level and private constructors.");
    }

    public static <A extends Annotation> List<A> findParametersAnnotation(Constructor<?> constructor, Class<A> annotationType) {
        Annotation[][] annotations = constructor.getParameterAnnotations();
        List<A> parametersAnnotation = new ArrayList<A>(annotations.length);

        for (Annotation[] annotation : annotations) {
            parametersAnnotation.add(findAnnotation(annotation, annotationType));
        }
        return parametersAnnotation;
    }

    @SuppressWarnings("unchecked")
    private static <A extends Annotation> A findAnnotation(Annotation[] annotations, Class<A> annotationType) {
        for (Annotation annotation : annotations) {
            if (annotationType.isAssignableFrom(annotation.getClass())) {
                return (A) annotation;
            }
        }
        return null;
    }

    public static <T> T createObject(Class<T> objectClass) {
        return createObject(objectClass, new Class<?>[]{}, new Object[]{});
    }

    public static <T> T createObject(Class<T> objectClass, Class<?>[] constructorArgsClasses, Object[] constructorArgs) {
        try {
            return createObject(objectClass.getConstructor(constructorArgsClasses), constructorArgs);
        } catch (IllegalArgumentException illegalArgumentException) {
            throwExceptionForObjectConstruction(illegalArgumentException, objectClass, constructorArgs);
        } catch (NoSuchMethodException noSuchMethodException) {
            throwExceptionForObjectConstruction(noSuchMethodException, objectClass, constructorArgs);
        } catch (SecurityException securityException) {
            throwExceptionForObjectConstruction(securityException, objectClass, constructorArgs);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T createObject(Constructor<?> constructor, Object[] constrcutorArgs) {
        try {
            return (T) constructor.newInstance(constrcutorArgs);
        } catch (IllegalAccessException illegalAccessException) {
            throwExceptionForObjectConstruction(illegalAccessException, constructor, constrcutorArgs);
        } catch (IllegalArgumentException illegalArgumentException) {
            throwExceptionForObjectConstruction(illegalArgumentException, constructor, constrcutorArgs);
        } catch (InstantiationException instantiationException) {
            throwExceptionForObjectConstruction(instantiationException, constructor, constrcutorArgs);
        } catch (SecurityException securityException) {
            throwExceptionForObjectConstruction(securityException, constructor, constrcutorArgs);
        } catch (InvocationTargetException invoicationTargetException) {
            throwExceptionForObjectConstruction(invoicationTargetException, constructor, constrcutorArgs);
        }
        return null;
    }

    private static void throwExceptionForObjectConstruction(Exception exception, Class<?> objectClass, Object[] construtorArgs) {
        throw new SmartParamException(SmartParamErrorCode.REFLECTIVE_OPERATION_ERROR, exception,
                String.format("no String[%d] constructor found for class %s", construtorArgs.length, objectClass.getCanonicalName()));
    }

    private static void throwExceptionForObjectConstruction(Exception exception, Constructor<?> constructor, Object[] construtorArgs) {
        throw new SmartParamException(SmartParamErrorCode.REFLECTIVE_OPERATION_ERROR, exception,
                String.format("no %d-parameter constructor found for class %s", construtorArgs.length, constructor.getDeclaringClass().getCanonicalName()));
    }
}
