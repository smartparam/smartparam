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
