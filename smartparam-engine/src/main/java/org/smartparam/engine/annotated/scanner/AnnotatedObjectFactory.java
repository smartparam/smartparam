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
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.smartparam.engine.annotated.annotations.ObjectInstance;
import org.smartparam.engine.annotated.annotations.Sortable;
import org.smartparam.engine.annotated.RepositoryObjectKey;
import org.smartparam.engine.util.reflection.AnnotationHelper;
import org.smartparam.engine.util.reflection.ReflectionsConstructorUtil;

/**
 *
 * @author Adam Dubiel
 */
public class AnnotatedObjectFactory {

    /**
     * For all {@link SmartParamSortable}, reasonable default value for order()
     * method.
     */
    public static final int DEFAULT_ORDER_VALUE = 100;

    /**
     * Name of annotation method holding array of {@link SmartParamObjectInstance}
     * instance descriptors.
     */
    private static final String INSTANCES_METHOD_NAME = "instances";

    /**
     * Name of annotation method holding array of names, that will be used to
     * register same object multiple times.
     */
    private static final String VALUES_METHOD_NAME = "values";

    /**
     * Name of annotation method holding order information.
     */
    private static final String ORDER_METHOD_NAME = "order";

    public <T> Map<RepositoryObjectKey, T> createObjects(Class<T> objectClass, Annotation annotation) {
        Map<String, T> rawObjectInstances;

        ObjectInstance[] instanceDescriptors = extractInstanceDescriptors(annotation);
        if (instanceDescriptors.length > 0) {
            rawObjectInstances = createObjectsFromInstanceDescriptors(objectClass, instanceDescriptors);
        } else {
            rawObjectInstances = createObject(objectClass, annotation);
        }


        int objectOrder = extractOrder(annotation);
        Map<RepositoryObjectKey, T> createdObjects = new HashMap<RepositoryObjectKey, T>();
        for (Entry<String, T> objectEntry : rawObjectInstances.entrySet()) {
            createdObjects.put(new RepositoryObjectKey(objectEntry.getKey(), objectOrder), objectEntry.getValue());
        }

        return createdObjects;
    }

    private <T> Map<String, T> createObjectsFromInstanceDescriptors(Class<T> objectClass, ObjectInstance[] instanceDescriptors) {
        Map<String, T> createdObjects = new HashMap<String, T>();

        T object;
        String objectIdentifier;
        for (ObjectInstance instanceDescriptor : instanceDescriptors) {
            objectIdentifier = extractValue(instanceDescriptor);
            object = instantiateUsingObjectDescriptor(objectClass, instanceDescriptor);
            createdObjects.put(objectIdentifier, object);
        }

        return createdObjects;
    }

    private <T> Map<String, T> createObject(Class<T> objectClass, Annotation annotation) {
        String[] objectIdentifiers = extractIdentifiers(annotation);
        T object = instantiateUsingDefaultConstructor(objectClass);

        Map<String, T> createdObjects = new HashMap<String, T>();
        for (String objectIdentifier : objectIdentifiers) {
            createdObjects.put(objectIdentifier, object);
        }

        return createdObjects;
    }

    /**
     * Create new object of given class using its default constructor. If class
     * implements {@link AnnotationScanner}, scanner properties are injected.
     *
     * Will throw {@link org.smartparam.engine.core.exception.SmartParamInitializationException} if there is no
     * default constructor or it is unreachable.
     *
     * @param objectClass class of object to create
     *
     * @return created object
     */
    @SuppressWarnings("unchecked")
    private <T> T instantiateUsingDefaultConstructor(Class<T> objectClass) {
        return ReflectionsConstructorUtil.createObject(objectClass);
    }

    /**
     * Create object instance using instructions from object descriptor.
     *
     * Will throw {@link org.smartparam.engine.core.exception.SmartParamInitializationException} if there is no
     * constructor matching descriptor.
     *
     * @see SmartParamObjectInstance
     *
     * @param objectClass      class to instantiate
     * @param objectDescriptor object instance descriptor
     *
     * @return created object
     */
    @SuppressWarnings("unchecked")
    private <T> T instantiateUsingObjectDescriptor(Class<T> objectClass, ObjectInstance objectDescriptor) {
        int constructorArgCount = objectDescriptor.constructorArgs().length;
        Class<?>[] constructorArgClasses = new Class<?>[constructorArgCount];
        Object[] constructorArgs = new Object[constructorArgCount];

        String constructorArg;
        for (int i = 0; i < constructorArgCount; ++i) {
            constructorArg = objectDescriptor.constructorArgs()[i];
            constructorArgClasses[i] = constructorArg.getClass();
            constructorArgs[i] = constructorArg;
        }

        return ReflectionsConstructorUtil.createObject(objectClass, constructorArgClasses, constructorArgs);
    }

    private String extractValue(Annotation annotation) {
        return AnnotationHelper.extractValue(annotation, "value");
    }

    /**
     * Tries to extract order defined in annotation (in order() method),
     * returns default order {@link #DEFAULT_ORDER_VALUE} if annotation is not
     * {@link SmartParamSortable}.
     *
     * @param annotation annotation instance to extract order form
     *
     * @return order read from annotation or default value
     */
    private int extractOrder(Annotation annotation) {
        if (annotation.annotationType().isAnnotationPresent(Sortable.class)) {
            // this is a JDK 6 workaround, please leave it here unless we're moving to JDK 7+
            // JDK 6 can't figure out how to go from <T>s T to primitive int, needs <? extends Object>
            Integer order = AnnotationHelper.extractValue(annotation, ORDER_METHOD_NAME);
            return order;
        }
        return DEFAULT_ORDER_VALUE;
    }

    /**
     * Returns array of identifiers defined in values() method (might be empty).
     *
     * @param annotation annotation instance to extract identifiers from
     *
     * @return array of names (identifiers), never null
     */
    private String[] extractIdentifiers(Annotation annotation) {
        String[] identifiers = AnnotationHelper.extractValue(annotation, VALUES_METHOD_NAME);
        if (identifiers.length > 0) {
            return identifiers;
        }
        return new String[]{extractValue(annotation)};
    }

    /**
     * Return array of defined instance descriptors (might be empty).
     *
     * @param annotation annotation instance
     *
     * @return array of descriptors, never null
     */
    private ObjectInstance[] extractInstanceDescriptors(Annotation annotation) {
        return AnnotationHelper.extractValue(annotation, INSTANCES_METHOD_NAME);
    }
}
