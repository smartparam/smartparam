package org.smartparam.engine.annotations.scanner;

import java.lang.annotation.Annotation;
import org.smartparam.engine.core.exception.SmartParamInitializationException;
import org.smartparam.engine.util.reflection.ReflectionsHelper;

/**
 * Helper class providing methods for common annotation scanner tasks.
 * TODO #ad any ideas on how to rename it?
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 * @since 0.1.0
 */
abstract class AbstractAnnotationScanner {

    /**
     * Default name of "value" method, that holds entity name in SmartParam*
     * annotations.
     */
    private static final String VALUE_METHOD_NAME = "value";

    /**
     * Extract value from {@link AbstractAnnotationScanner#VALUE_METHOD_NAME}
     * annotation method, uses {@link #extractValue(java.lang.annotation.Annotation, java.lang.String) }
     * internally.
     *
     * @param annotation source annotation
     *
     * @return value of value()
     */
    protected String extractValue(Annotation annotation) {
        return (String) extractValue(annotation, VALUE_METHOD_NAME);
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
    protected Object extractValue(Annotation annotation, String methodName) {
        return ReflectionsHelper.extractValue(annotation, methodName);
    }
}
