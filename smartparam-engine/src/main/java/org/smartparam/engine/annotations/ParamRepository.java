package org.smartparam.engine.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Sortable
public @interface ParamRepository {

    /**
     * Unique name of repository.
     *
     * @return name
     */
    String value();

    /**
     * List of unique names, which will be used to register same instance of
     * function repository multiple times.
     *
     * @return list of names
     */
    String[] values() default {};

    /**
     * List of function repository function descriptors - each descriptor
     * creates new instance of function repository.
     *
     * @see SmartParamObjectInstance
     * @return instance descriptors
     */
    ObjectInstance[] instances() default {};

    /**
     * Order of repository on repository list, lower number means repository will
     * be used earlier.
     *
     * @return order, defaults to 100
     */
    int order() default 100;
}
