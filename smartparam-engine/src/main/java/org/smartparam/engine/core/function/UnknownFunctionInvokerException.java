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
 * When using dynamic context without providing levelCreator function name.
 *
 * @author Adam Dubiel
 */
@SuppressWarnings("serial")
public class UnknownFunctionInvokerException extends SmartParamException {

    UnknownFunctionInvokerException(Function function) {
        super("UNKNOWN_FUNCTION_INVOKER",
                String.format("Could not find function invoker for function %s of type %s. "
                        + "To see all registered function invokers, look for MapRepository logs on INFO level during startup.", function.getName(), function.getType()));
    }

}
