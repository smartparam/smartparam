package org.smartparam.engine.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class ReflectionsHelper {

    public static Set<Method> getMethodsAnnotatedBy(Class<?> targetClass, Class<? extends Annotation> annotation) {
        Set<Method> annotatedMethods = new HashSet<Method>();
        for (Method method : targetClass.getMethods()) {
            if (method.isAnnotationPresent(annotation)) {
                annotatedMethods.add(method);
            }
        }
        return annotatedMethods;
    }

    public static Set<Field> getFieldsAnnotatedBy(Class<?> targetClass, Class<? extends Annotation> annotation) {
        Set<Field> annotatedFields = new HashSet<Field>();
        for (Field field : targetClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(annotation)) {
                annotatedFields.add(field);
            }
        }
        return annotatedFields;
    }
}
