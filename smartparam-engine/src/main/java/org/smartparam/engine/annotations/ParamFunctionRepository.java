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
package org.smartparam.engine.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks function repository that should be added to function provider during
 * initial scan. Function repositories are ordered, first function repository
 * that can provide searched function is used.
 *
 * @author Adam Dubiel
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Sortable
public @interface ParamFunctionRepository {

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
