package org.smartparam.engine.core.exception;

import org.smartparam.engine.core.context.LevelValues;

/**
 *
 * @author Adam Dubiel
 */
public enum SmartParamErrorCode {

    /**
     * Trying to extract wrong type from {@link org.smartparam.engine.core.type.AbstractHolder}.
     */
    GETTING_WRONG_TYPE,
    /**
     * Deprecated.
     */
    ILLEGAL_ASSEMBLER_DEFINITION,
    /**
     * Deprecated.
     */
    ASSEMBLER_NOT_FOUND,
    /**
     * Deprecated.
     */
    ASSEMBLER_INVOKE_ERROR,
    /**
     * Trying to register an item under same code (for strictly unique repositories).
     */
    NON_UNIQUE_ITEM_CODE,
    /**
     * Conversion to declared level type failed.
     */
    TYPE_CONVERSION_FAILURE,
    /**
     * Decoding value failed
     * {@link org.smartparam.engine.core.type.AbstractType#decode(java.lang.String)}.
     */
    TYPE_DECODING_FAILURE,
    /**
     * No parameter with given name could be found in any registered repository.
     */
    UNKNOWN_PARAMETER,
    /**
     * No function with given name could be found in any registered repository.
     */
    UNKNOWN_FUNCTION,
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
