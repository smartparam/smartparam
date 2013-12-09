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
package org.smartparam.engine.core.output;

import org.smartparam.engine.core.exception.SmartParamException;

/**
 * Trying to extract wrong type from {@link org.smartparam.engine.core.type.AbstractHolder}.
 *
 * @author Adam Dubiel
 */
@SuppressWarnings("serial")
public class GettingWrongTypeException extends SmartParamException {

    private static final String EXCEPTION_CODE = "GETTING_WRONG_TYPE";

    GettingWrongTypeException(int position, String expected, Object found) {
        super(EXCEPTION_CODE,
                String.format("Expected %s but found %s at position %d.", expected, printClass(found), position));
    }

    GettingWrongTypeException(IllegalArgumentException baseException, Class<?> enumClass, String invalidValue) {
        super(EXCEPTION_CODE, baseException,
                String.format("Requested enum of class %s has no value %s defnied.", enumClass.getSimpleName(), invalidValue));
    }

    private static String printClass(Object obj) {
        return obj != null ? obj.getClass().getSimpleName() : null;
    }
}
