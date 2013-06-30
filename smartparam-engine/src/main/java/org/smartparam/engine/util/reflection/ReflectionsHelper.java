package org.smartparam.engine.util.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.core.exception.SmartParamInitializationException;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class ReflectionsHelper {

    public static Class<?> loadClass(ClassLoader classLoader, String className) {
        try {
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException exception) {
            throw new SmartParamException(SmartParamErrorCode.ANNOTATION_INITIALIZER_ERROR, exception, "unable to load class " + className + " using classloader " + classLoader);
        }
    }

    /**
     * Extract value from given annotation method, if anything goes wrong
     * throws {@link SmartParamInitializationException} with real reason as
     * cause.
     *
     * @param annotation source annotation
     * @param methodName annotation method to look for
     *
     * @return value returned from annotation method
     */
    public static Object extractValue(Annotation annotation, String methodName) {
        try {
            Method defaultValueMethod = annotation.annotationType().getMethod(methodName);
            return defaultValueMethod.invoke(annotation);
        } catch (Exception exception) {
            throw new SmartParamInitializationException(SmartParamErrorCode.ANNOTATION_INITIALIZER_ERROR,
                    exception, "no " + methodName + " field found on annotation " + annotation.annotationType().getSimpleName());
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
