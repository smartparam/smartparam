package org.smartparam.engine.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks assemblers that should be added to assembler repository during initial
 * scan.
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SmartParamAssembler {

    /**
     * Name of assembler (unique).
     *
     * @return name of assembler
     */
    String value();

    /**
     * Returns array of assembler names, if assembler should be registered
     * multiple times under different names.
     *
     * @return names
     */
    String[] values() default {};

    /**
     * Returns data to instantiate assembler class with different constructor
     * arguments. Assembler objects will be registered under given names.
     *
     * @see SmartParamObjectInstance
     * @return assembler instance descriptors
     */
    SmartParamObjectInstance[] instances() default {};
}
