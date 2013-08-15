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
package org.smartparam.engine.annotations.scanner;

import org.smartparam.engine.bean.RepositoryObjectKey;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.smartparam.engine.bean.PackageList;
import org.smartparam.engine.core.exception.SmartParamInitializationException;
import org.smartparam.engine.util.reflection.ReflectionsHelper;
import org.smartparam.engine.util.reflection.ReflectionsScanner;

/**
 * Annotation scanner util supporting SmartParam* annotations. It not only scans
 * for annotated objects, but also instantiates them using hints included in
 * annotation. Annotation should define:
 * <ul>
 * <li>value() - to extract unique code of object</li>
 * <li>values() - if same object should be returned multiple times under different names</li>
 * <li>instances() - if object should be instantiated with specific constructor args</li>
 * </ul>
 * Also, if scanned annotation is {@link SmartParamSortable}, it should define
 * order() method.
 *
 * @see org.smartparam.engine.annotations.SmartParamMatcher
 * @see SmartParamObjectInstance
 *
 * Map returned by {@link #getAnnotatedObjects(java.lang.Class) } is not ordered,
 * but the key ({@link RepositoryObjectKey}) holds all information that can be
 * used for ordering.
 *
 *
 * @param <OBJECT> type of object to instantiate
 * @author Adam Dubiel
 * @since 0.1.0
 */
public class AnnotatedObjectsScanner<OBJECT> {

    private ReflectionsScanner reflectionsScanner = new ReflectionsScanner();

    private AnnotatedObjectFactory annotatedObjectFactory = new AnnotatedObjectFactory();

    /**
     * Scan for classes annotated with given annotation type, instantiate them
     * using hints from annotation and return a map of those objects.
     *
     * Might throw {@link SmartParamInitializationException} if any reflective
     * operation fails.
     *
     * @see RepositoryObjectKey
     * @param annotationClass annotation to look for
     * @param packagesToScan
     *
     * @return map of objects
     */
    @SuppressWarnings("unchecked")
    public Map<RepositoryObjectKey, OBJECT> getAnnotatedObjects(Class<? extends Annotation> annotationClass, PackageList packagesToScan) {
        Set<Class<?>> annotatedObjectClasses = reflectionsScanner.findClassesAnnotatedWith(annotationClass, packagesToScan.getPackages());
        Map<RepositoryObjectKey, OBJECT> objects = new HashMap<RepositoryObjectKey, OBJECT>();

        Annotation typeAnnotation;
        for (Class<?> type : annotatedObjectClasses) {
            typeAnnotation = type.getAnnotation(annotationClass);

            for (Entry<RepositoryObjectKey, ?> entry : annotatedObjectFactory.createObjects(type, typeAnnotation).entrySet()) {
                objects.put(entry.getKey(), (OBJECT) entry.getValue());
            }
        }

        return objects;
    }

    /**
     * Scan for classes annotated with given annotation and return instances
     * of those objects (instantiated using default constructor).
     *
     * @param annotationClass
     * @param packagesToScan
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<OBJECT> getAnnotatedObjectsWithoutName(Class<? extends Annotation> annotationClass, PackageList packagesToScan) {
        Set<Class<?>> annotatedObjectClasses = reflectionsScanner.findClassesAnnotatedWith(annotationClass, packagesToScan.getPackages());
        List<OBJECT> objects = new ArrayList<OBJECT>(annotatedObjectClasses.size());

        for (Class<?> type : annotatedObjectClasses) {
            objects.add((OBJECT) ReflectionsHelper.createObject(type));
        }

        return objects;
    }

    public void setReflectionsScanner(ReflectionsScanner reflectionsScanner) {
        this.reflectionsScanner = reflectionsScanner;
    }

    public void setAnnotatedObjectFactory(AnnotatedObjectFactory annotatedObjectFactory) {
        this.annotatedObjectFactory = annotatedObjectFactory;
    }
}
