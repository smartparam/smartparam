/*
 * Copyright 2014 Adam Dubiel, Przemek Hertel.
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
package org.smartparam.engine.core.output;

import org.smartparam.engine.core.exception.SmartParamException;

/**
 * Trying to get entry key from parameter which is not identifiable, see
 * {@link org.smartparam.engine.core.parameter.Parameter#isIdentifyEntries() }.
 *
 * @author Adam Dubiel
 */
@SuppressWarnings("serial")
public class GettingKeyNotIdentifiableParameterException extends SmartParamException {

    private static final String EXCEPTION_CODE = "GETTING_KEY_FROM_UNIDENTIFIED_PARAM";

    GettingKeyNotIdentifiableParameterException() {
        super(EXCEPTION_CODE, "Requested entry key from parameter that is not identifiable. If you want to be able to "
                + "identify specific entries please set 'identifyEntries' parameter flag to true (defaults to false). Make"
                + "sure to read flag javadoc, as using it might hurt memory efficiency.");
    }

}
