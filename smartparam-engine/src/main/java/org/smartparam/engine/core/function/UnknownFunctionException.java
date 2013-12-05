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
 * No function with given name could be found in any registered repository.
 *
 * @author Adam Dubiel
 */
@SuppressWarnings("serial")
public class UnknownFunctionException extends SmartParamException {

    public UnknownFunctionException(String functionName) {
        super("UNKNOWN_FUNCTION",
                String.format("Could not find function %s in any registered repository. "
                        + "Check if all repositories are properly configured."
                        + "To see all functions registered, follow logs from MapRepository on INFO level.", functionName));
    }

}
