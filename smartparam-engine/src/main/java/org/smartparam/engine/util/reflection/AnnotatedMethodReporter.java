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
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Adam Dubiel
 */
public class AnnotatedMethodReporter implements AnnotationDetector.MethodReporter {

    private Class<? extends Annotation> reportedClass;

    private ClassLoader classLoader;

    private Set<Method> annotatedMethods = new HashSet<Method>();

    private Set<String> scannedClasses = new HashSet<String>();

    public AnnotatedMethodReporter(Class<? extends Annotation> reportedClass) {
        this.reportedClass = reportedClass;
        this.classLoader = reportedClass.getClassLoader();
    }

    public AnnotatedMethodReporter(ClassLoader classLoader, Class<? extends Annotation> reportedClass) {
        this.reportedClass = reportedClass;
        this.classLoader = classLoader;
    }

    @Override
    public void reportMethodAnnotation(Class<? extends Annotation> annotation, String className, String methodName) {
        if (!scannedClasses.contains(className)) {
            scannedClasses.add(className);

            Class<?> classInstance = ReflectionsHelper.loadClass(classLoader, className);
            annotatedMethods.addAll(ReflectionsHelper.findMethodsAnnotatedWith(annotation, classInstance));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<? extends Annotation>[] annotations() {
        return new Class[]{reportedClass};
    }

    public Set<Method> getAnnotatedMethods() {
        return annotatedMethods;
    }
}
