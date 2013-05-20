package org.smartparam.engine.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks configuration field, that can be processed using {@link ConfigPreparer}
 * and {@link ConfigInjector}.
 *
 * @see SmartParamConfig
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigElement {

    /**
     * Class, that should be instantiated and injected into field if it has
     * no value.
     *
     * @return default class
     */
    Class<?> value();

    /**
     * Indicates class of object which should be injected with value of this field
     * (typically used when field holds collection).
     *
     * @return class of object containing this fields value
     */
    Class<?> registerAt() default EMPTY.class;

    /**
     * Default value for registerAt() field.
     */
    static final class EMPTY {};
}
