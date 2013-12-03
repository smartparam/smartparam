/*
 * Copyright 2013 Adam Dubiel, Przemek Hertel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.smartparam.engine.annotated.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks assemblers that should be added to assembler repository during initial
 * scan. Not working yet.
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ParamAssembler {

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
    ObjectInstance[] instances() default {};
}
