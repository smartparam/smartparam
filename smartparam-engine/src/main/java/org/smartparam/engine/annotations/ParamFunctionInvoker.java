package org.smartparam.engine.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks function invokers that should be added to repository during initial
 * scan.
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ParamFunctionInvoker {

    /**
     * Name of function invoker (unique), also marks the type of function
     * that should be invoked using it.
     *
     * @return name
     */
    String value();

    /**
     * Returns array of function invoker names, if it should be registered
     * multiple times under different names.
     *
     * @return names
     */
    String[] values() default {};

    /**
     * Returns data to instantiate function invoker class with different constructor
     * arguments. Function invoker objects will be registered under given names.
     *
     * @see SmartParamObjectInstance
     * @return instance descriptors
     */
    ObjectInstance[] instances() default {};
}
