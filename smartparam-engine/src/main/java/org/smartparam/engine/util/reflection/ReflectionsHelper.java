package org.smartparam.engine.util.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.exception.SmartParamException;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class ReflectionsHelper {

    public static Class<?> loadClass(ClassLoader classLoader, String className) {
        try {
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException exception) {
            throw new SmartParamException(SmartParamErrorCode.REFLECTIVE_OPERATION_ERROR, exception, "unable to load class " + className + " using classloader " + classLoader);
        }
    }

    public static <T> T createObject(Class<T> objectClass) {
        return createObject(objectClass, new Class<?>[]{}, new Object[]{});
    }

    public static <T> T createObject(Class<T> objectClass, Class<?>[] constructorArgsClasses, Object[] construtorArgs) {
        try {
            return objectClass.getConstructor(constructorArgsClasses).newInstance(construtorArgs);
        } catch (ReflectiveOperationException exception) {
            throw new SmartParamException(SmartParamErrorCode.REFLECTIVE_OPERATION_ERROR, exception, "no String[" + construtorArgs.length + "] constructor "
                    + "found for class " + objectClass.getCanonicalName());
        }
    }

    public static Set<Method> findMethodsAnnotatedWith(Class<? extends Annotation> annotationType, Class<?> parentClass) {
        Set<Method> annotatedMethods = new HashSet<Method>();
        for (Method method : parentClass.getMethods()) {
            if (method.isAnnotationPresent(annotationType)) {
                annotatedMethods.add(method);
            }
        }
        return annotatedMethods;
    }

    public static Set<Field> findFieldsAnnotatedWith(Class<? extends Annotation> annotation, Class<?> parentClass) {
        Set<Field> annotatedFields = new HashSet<Field>();
        for (Field field : parentClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(annotation)) {
                annotatedFields.add(field);
            }
        }
        return annotatedFields;
    }
}
