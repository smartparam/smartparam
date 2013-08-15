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
import java.lang.reflect.Method;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.exception.SmartParamInitializationException;

/**
 *
 * @author Adam Dubiel
 */
public class AnnotationHelper {

    /**
     * Extract value from given annotation method, if anything goes wrong
     * throws {@link SmartParamInitializationException} with real reason as
     * cause.
     *
     * @param <T> type of returned value
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
        } catch (Exception exception) {
            throw new SmartParamInitializationException(SmartParamErrorCode.REFLECTIVE_OPERATION_ERROR,
                    exception, String.format("no %s method found on annotation %s", methodName, annotation.annotationType().getCanonicalName()));
        }
    }
}
