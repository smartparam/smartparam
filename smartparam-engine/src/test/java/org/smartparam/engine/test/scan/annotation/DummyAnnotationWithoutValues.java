package org.smartparam.engine.test.scan.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.smartparam.engine.annotations.ObjectInstance;

/**
 *
 * @author Adam Dubiel
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DummyAnnotationWithoutValues {

    ObjectInstance[] instances() default {};

}
