package org.smartparam.engine.annotations.handler;

import java.lang.annotation.Annotation;
import org.smartparam.engine.annotations.SmartParamType;

/**
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public class SmartParamTypeAnnotationHandler implements AnnotationHandler {

    public String extractIdentifier(Annotation annotation) {
        return ((SmartParamType) annotation).value();
    }

    public Class<? extends Annotation> handledAnnotation() {
        return SmartParamType.class;
    }
}
