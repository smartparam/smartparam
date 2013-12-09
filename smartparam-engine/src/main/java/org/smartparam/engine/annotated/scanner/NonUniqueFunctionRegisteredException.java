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
package org.smartparam.engine.annotated.scanner;

import java.lang.reflect.Method;
import org.smartparam.engine.core.exception.SmartParamException;

/**
 *
 * @author Adam Dubiel
 */
@SuppressWarnings("serial")
public class NonUniqueFunctionRegisteredException extends SmartParamException {

    NonUniqueFunctionRegisteredException(String pluginName, Method registeredMethod, Method duplicateMethod) {
        super("NON_UNIQUE_FUNCTION",
                String.format("Can't register two methods with same name, function %s found at method %s was already registered with %s method.",
                        pluginName, duplicateMethod.toGenericString(), registeredMethod.toGenericString()));
    }

}
