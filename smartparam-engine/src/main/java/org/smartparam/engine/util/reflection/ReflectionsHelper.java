package org.smartparam.engine.util.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.exception.SmartParamException;

/**
 *
 * @author Adam Dubiel
 */
public class ReflectionsHelper {

    public static Class<?> loadClass(ClassLoader classLoader, String className) {
        try {
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException exception) {
            throw new SmartParamException(SmartParamErrorCode.REFLECTIVE_OPERATION_ERROR, exception, String.format("Unable to load class %s using %s classloader.", className, classLoader));
        }
    }

    public static <T> T createObject(Class<T> objectClass) {
        return createObject(objectClass, new Class<?>[]{}, new Object[]{});
    }

    public static <T> T createObject(Class<T> objectClass, Class<?>[] constructorArgsClasses, Object[] construtorArgs) {
        try {
            return objectClass.getConstructor(constructorArgsClasses).newInstance(construtorArgs);
        } catch (IllegalAccessException illegalAccessException) {
            throwSmartParamException(illegalAccessException, objectClass, construtorArgs);
        } catch (IllegalArgumentException illegalArgumentException) {
            throwSmartParamException(illegalArgumentException, objectClass, construtorArgs);
        } catch (InstantiationException instantiationException) {
            throwSmartParamException(instantiationException, objectClass, construtorArgs);
        } catch (NoSuchMethodException noSuchMethodException) {
            throwSmartParamException(noSuchMethodException, objectClass, construtorArgs);
        } catch (SecurityException securityException) {
            throwSmartParamException(securityException, objectClass, construtorArgs);
        } catch (InvocationTargetException invoicationTargetException) {
            throwSmartParamException(invoicationTargetException, objectClass, construtorArgs);
        }
        return null;
    }

    private static void throwSmartParamException(Exception exception, Class<?> objectClass, Object[] construtorArgs) {
        throw new SmartParamException(SmartParamErrorCode.REFLECTIVE_OPERATION_ERROR, exception,
                String.format("no String[%d] constructor found for class %s", construtorArgs.length, objectClass.getCanonicalName()));
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
