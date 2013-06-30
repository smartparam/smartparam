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
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class ReflectionsScanner {

    public Set<Class<?>> findClassesAnnotatedWith(Class<? extends Annotation> annotationType, List<String> packagesToScan) {
        AnnotatedTypeReporter typeReporter = new AnnotatedTypeReporter(annotationType);
        runDetector(typeReporter, convertPackagesToArray(packagesToScan));
        return typeReporter.getAnnotatedClasses();
    }

    public Set<Method> findMethodsAnnotatedWith(Class<? extends Annotation> annotationType, List<String> packagesToScan) {
        AnnotatedMethodReporter methodReporter = new AnnotatedMethodReporter(annotationType);
        runDetector(methodReporter, convertPackagesToArray(packagesToScan));
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
