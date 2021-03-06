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
package org.smartparam.engine.annotated.scanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.kielo.annotationscanner.ReflectionsScanner;
import org.smartparam.engine.annotated.PackageList;
import org.smartparam.engine.util.reflection.AnnotationHelper;

/**
 *
 * @author Adam Dubiel
 */
public class PackageMethodScanner implements MethodScanner {

    private final ReflectionsScanner reflectionsScanner = new ReflectionsScanner();

    private final PackageList packagesToScan;

    public PackageMethodScanner(PackageList packagesToScan) {
        this.packagesToScan = packagesToScan;
    }

    @Override
    public Map<String, Method> scanMethods(Class<? extends Annotation> annotationClass) {
        Map<String, Method> methods = new HashMap<String, Method>();

        String pluginName;
        for (Method method : reflectionsScanner.findMethodsAnnotatedWith(annotationClass, packagesToScan.getPackages())) {
            pluginName = AnnotationHelper.extractValue(method.getAnnotation(annotationClass), "value");
            checkForDuplicates(methods, pluginName, method);
            methods.put(pluginName, method);
        }

        return methods;
    }

    private void checkForDuplicates(Map<String, Method> methods, String newPluginName, Method newPluginMethod) {
        if (methods.containsKey(newPluginName)) {
            throw new NonUniqueFunctionRegisteredException(newPluginName, methods.get(newPluginName), newPluginMethod);
        }
    }
}
