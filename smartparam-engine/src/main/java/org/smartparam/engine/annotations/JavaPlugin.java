package org.smartparam.engine.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks function that should be included in Java function repository as a plugin.
 *
 * @author Adam Dubiel
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JavaPlugin {

    /**
     * Unique name of plugin.
     *
     * @return plugin name
     */
    String value();
}
