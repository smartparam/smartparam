package org.smartparam.engine.annotations.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.apache.commons.lang3.ClassUtils;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.exception.SmartParamInitializationException;

/**
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public class GenericAnnotationHandler implements AnnotationHandler {

    private final static String DEFAULT_VALUE_METHOD = "value";

    private Class<? extends Annotation> annotationClass;

    public GenericAnnotationHandler(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    public String extractIdentifier(Annotation annotation) {
        try {
            Method defaultValueMethod = annotation.annotationType().getMethod(DEFAULT_VALUE_METHOD);
            return (String) defaultValueMethod.invoke(annotation);
        } catch (Exception exception) {
            throw new SmartParamInitializationException(SmartParamErrorCode.ANNOTATION_INITIALIZER_ERROR,
                    exception, "no " + DEFAULT_VALUE_METHOD + " field found on annotation " + ClassUtils.getShortCanonicalName(annotationClass));
        }
    }

    public Class<? extends Annotation> handledAnnotation() {
        return annotationClass;
    }
}
