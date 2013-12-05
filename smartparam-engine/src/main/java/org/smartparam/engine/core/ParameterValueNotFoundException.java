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
package org.smartparam.engine.core;

import org.smartparam.engine.core.context.ParamContext;
import org.smartparam.engine.core.exception.SmartParamException;

/**
 * No value found in evaluated parameter, returned only if parameter does
 * not allow returning null values.
 *
 * @author Adam Dubiel
 */
@SuppressWarnings("serial")
public class ParameterValueNotFoundException extends SmartParamException {

    ParameterValueNotFoundException(String parameterName, ParamContext context) {
        super("PARAM_VALUE_NOT_FOUND",
                String.format("No value found for parameter [%s] using values from context %s. "
                        + "If parameter should return null values instead of throwing this exception, set nullable flag to true.", parameterName, context));
    }

}
