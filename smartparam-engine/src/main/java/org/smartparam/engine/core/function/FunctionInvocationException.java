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
package org.smartparam.engine.core.function;

import org.smartparam.engine.core.exception.SmartParamException;

/**
 *
 * @author Adam Dubiel
 */
@SuppressWarnings("serial")
public class FunctionInvocationException extends SmartParamException {

    private final Function function;

    FunctionInvocationException(Throwable cause, Function function) {
        super("FUNCTION_INVOKE_ERROR", cause,
                String.format("Failed to invoke function %s of type %s.", function.getName(), function.getType()));
        this.function = function;
    }

    public Function function() {
        return function;
    }
}
