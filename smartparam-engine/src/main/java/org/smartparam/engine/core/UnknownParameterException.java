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

import org.smartparam.engine.core.exception.SmartParamException;

/**
 * No parameter with given name could be found in any registered repository.
 *
 * @author Adam Dubiel
 */
@SuppressWarnings("serial")
public class UnknownParameterException extends SmartParamException {

    UnknownParameterException(String parameterName) {
        super("UNKNOWN_PARAMETER",
                String.format("Parameter %s was not found in any of registered repositories. "
                + "Check if name is correct and repositories are properly configured and initalized.", parameterName));
    }

}
