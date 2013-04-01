package org.smartparam.engine.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Adam Dubiel
 * @sicne 0.1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SmartParamMatcher {

    String value();

    SmartParamObjectInstance[] instances() default {};
}
