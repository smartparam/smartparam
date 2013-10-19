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

import eu.infomas.annotation.AnnotationDetector;
import eu.infomas.annotation.AnnotationDetector.Reporter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.exception.SmartParamException;

/**
 *
 * @author Adam Dubiel
 */
public class ReflectionsScanner {

    public Set<Class<?>> findClassesAnnotatedWith(Class<? extends Annotation> annotationType, List<String> packagesToScan) {
        String[] packagesArray = convertPackagesToArray(packagesToScan);
        AnnotatedTypeReporter typeReporter = new AnnotatedTypeReporter(annotationType, packagesArray);
        runDetector(typeReporter, packagesArray);
        return typeReporter.getAnnotatedClasses();
    }

    public Set<Method> findMethodsAnnotatedWith(Class<? extends Annotation> annotationType, List<String> packagesToScan) {
        String[] packagesArray = convertPackagesToArray(packagesToScan);
        AnnotatedMethodReporter methodReporter = new AnnotatedMethodReporter(annotationType, packagesArray);
        runDetector(methodReporter, packagesArray);
        return methodReporter.getAnnotatedMethods();
    }

    public Set<Method> findMethodsAnnotatedWith(Class<? extends Annotation> annotationType, Class<?> parentClass) {
        return ReflectionsHelper.findMethodsAnnotatedWith(annotationType, parentClass);
    }

    public Set<Field> findFieldsAnnotatedWith(Class<? extends Annotation> annotation, Class<?> parentClass) {
        return ReflectionsHelper.findFieldsAnnotatedWith(annotation, parentClass);
    }

    private void runDetector(Reporter reporter, String... packagesToScan) {
        try {
            AnnotationDetector detector = new AnnotationDetector(reporter);
            detector.detect(packagesToScan);
        } catch (IOException exception) {
            throw new SmartParamException(SmartParamErrorCode.ANNOTATION_INITIALIZER_ERROR, exception, "failed to scan for annotation");
        }
    }

    private String[] convertPackagesToArray(List<String> packagesToScan) {
        return packagesToScan.toArray(new String[packagesToScan.size()]);
    }
}
