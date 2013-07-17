package org.smartparam.engine.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks types that should be added to matcher repository during initial
 * scan.
 *
 * @see org.smartparam.engine.core.type.Type
 * @author Adam Dubiel
 * @since 0.1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ParamType {

    /**
     * Unique name of type.
     *
     * @return name
     */
    String value();

    /**
     * Returns array of type names, if it should be registered multiple
     * times under different names.
     *
     * @return names
     */
    String[] values() default {};

    /**
     * Returns data to instantiate type class with different constructor
     * arguments. Type objects will be registered under given names.
     *
     * @see SmartParamObjectInstance
     * @return instance descriptors
     */
    ObjectInstance[] instances() default {};
}
