package org.smartparam.engine.annotations.handler;

import java.lang.annotation.Annotation;

/**
 *
 * @author Adam Dubiel
 */
public interface AnnotationHandler {

    String extractIdentifier(Annotation annotation);

    Class<? extends Annotation> handledAnnotation();
}
