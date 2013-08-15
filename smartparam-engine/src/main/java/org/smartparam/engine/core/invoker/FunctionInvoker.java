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
package org.smartparam.engine.core.invoker;

import org.smartparam.engine.model.function.Function;

/**
 * Invoker is able to run function (from Java class, script, etc...) described by
 * {@link Function} object with given parameters and return its value
 * (null if function is void).
 *
 * Unique name under which invoker is registered in
 * {@link org.smartparam.engine.core.repository.InvokerRepository} should be the
 * the same as type of functions invoked by this invoker. For example:
 * <pre>
 * function[type = java]  ---(invoked by)--> invoker[name = java]
 * function[type = groovy]  ---(invoked by)--> invoker[name = groovy]
 * </pre>
 * There can be only one function invoker registered per function type.
 *
 * @author Adam Dubiel
 */
public interface FunctionInvoker {

    /**
     * Invoke function, it is a good practice to wrap any outgoing exceptions
     * with {@link org.smartparam.engine.core.exception.SmartParamException}
     * with {@link org.smartparam.engine.core.exception.SmartParamErrorCode#FUNCTION_INVOKE_ERROR}
     * reason.
     *
     * @param function description of function to invoke
     * @param args arguments
     * @return evaluated value (can be null)
     */
    Object invoke(Function function, Object... args);
}
