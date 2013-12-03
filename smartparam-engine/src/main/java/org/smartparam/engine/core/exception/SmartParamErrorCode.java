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
package org.smartparam.engine.core.exception;

/**
 *
 * @author Adam Dubiel
 */
public enum SmartParamErrorCode {

    /**
     * No value found in evaluated parameter, returned only if parameter does
     * not allow returning null values.
     */
    PARAM_VALUE_NOT_FOUND,
    /**
     * When using {@link org.smartparam.engine.core.context.LevelValues} and
     * provided less level values than there are levels defined in parameter.
     */
    ILLEGAL_LEVEL_VALUES,
    /**
     * If function invocation thrown any error.
     */
    FUNCTION_INVOKE_ERROR,
    /**
     * When initializing dynamic context.
     *
     * @see
     * org.smartparam.engine.core.context.DefaultContext#initialize(java.lang.Object[]).
     */
    ERROR_FILLING_CONTEXT,
    /**
     * Misused API, probably passed incompatible arguments.
     */
    ILLEGAL_API_USAGE,
    /**
     * When using dynamic context without providing levelCreator function name.
     */
    UNDEFINED_LEVEL_CREATOR,
    /**
     * When trying to call function of type that has no associated invoker.
     */
    UNDEFINED_FUNCTION_INVOKER,
    /**
     * Trying to use level value of unknown {@link org.smartparam.engine.core.type.Type}.
     */
    UNKNOWN_PARAM_TYPE,
    /**
     * Trying to use unknown level {@link org.smartparam.engine.core.index.Matcher}.
     */
    UNKNOWN_MATCHER,
    /**
     * When trying to get item from array/list that doesn't exist.
     */
    INDEX_OUT_OF_BOUNDS,
    /**
     * Error while initializing SmartParam using annotation aware classes.
     */
    ANNOTATION_INITIALIZER_ERROR,
    /**
     * When error comes from any reflective operation (class instantiation,
     * method call etc).
     */
    REFLECTIVE_OPERATION_ERROR;

}
