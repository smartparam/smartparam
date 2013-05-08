package org.smartparam.engine.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigElement {

    Class<?> value();

    Class<?> registerAt() default EMPTY.class;

    static final class EMPTY {};
}
