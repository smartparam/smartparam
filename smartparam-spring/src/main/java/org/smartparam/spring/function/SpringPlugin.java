package org.smartparam.spring.function;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates function inside Spring beans that should be added to {@link SpringFunctionRepository}
 * during annotation scan.
 *
 * @author Adam Dubiel
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SpringPlugin {

    /**
     * Name of function, should be unique in single repository scope (functions
     * from other repositories can shadow it).
     */
    String value();

}
