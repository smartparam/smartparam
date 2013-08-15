package org.smartparam.engine.util.reflection;

import eu.infomas.annotation.AnnotationDetector;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Adam Dubiel
 */
public class AnnotatedTypeReporter implements AnnotationDetector.TypeReporter {

    private Class<? extends Annotation> reportedClass;

    private ClassLoader classLoader;

    private Set<Class<?>> annotatedClasses = new HashSet<Class<?>>();

    public AnnotatedTypeReporter(Class<? extends Annotation> reportedClass) {
        this.reportedClass = reportedClass;
        this.classLoader = reportedClass.getClassLoader();
    }

    public AnnotatedTypeReporter(ClassLoader classLoader, Class<? extends Annotation> reportedClass) {
        this.reportedClass = reportedClass;
        this.classLoader = classLoader;
    }

    @Override
    public void reportTypeAnnotation(Class<? extends Annotation> annotation, String className) {
        Class<?> classInstance = ReflectionsHelper.loadClass(classLoader, className);
        annotatedClasses.add(classInstance);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<? extends Annotation>[] annotations() {
        return new Class[]{reportedClass};
    }

    public Set<Class<?>> getAnnotatedClasses() {
        return annotatedClasses;
    }
}
