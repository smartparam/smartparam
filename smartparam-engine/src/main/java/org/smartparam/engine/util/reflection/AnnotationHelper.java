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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * @author Adam Dubiel
 */
public final class AnnotationHelper {

    private AnnotationHelper() {
    }

    /**
     * Extract value from given annotation method, if anything goes wrong
     * throws {@link InnerReflectiveOperationException} with real reason as
     * cause.
     *
     * @param <T>        type of returned value
     * @param annotation source annotation
     * @param methodName annotation method to look for
     *
     * @return value returned from annotation method
     */
    @SuppressWarnings("unchecked")
    public static <T> T extractValue(Annotation annotation, String methodName) {
        try {
            Method defaultValueMethod = annotation.annotationType().getMethod(methodName);
            return (T) defaultValueMethod.invoke(annotation);
        } catch (IllegalAccessException illegalAccessException) {
            throw reflectiveException(illegalAccessException, methodName, annotation);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw reflectiveException(illegalArgumentException, methodName, annotation);
        } catch (NoSuchMethodException noSuchMethodException) {
            throw reflectiveException(noSuchMethodException, methodName, annotation);
        } catch (SecurityException securityException) {
            throw reflectiveException(securityException, methodName, annotation);
        } catch (InvocationTargetException invocationTargetException) {
            throw reflectiveException(invocationTargetException, methodName, annotation);
        }
    }

    private static InnerReflectiveOperationException reflectiveException(Exception exception, String methodName, Annotation annotation) {
        return new InnerReflectiveOperationException(exception,
                String.format("No %s method found on annotation %s.", methodName, annotation.annotationType().getCanonicalName()));
    }
}
