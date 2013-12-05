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
 *
 * @author Adam Dubiel
 */
@SuppressWarnings("serial")
public class InvalidRowIndexException extends SmartParamException {

    InvalidRowIndexException(int rowIndex, Object[] values) {
        super("INVALID_ROW_INDEX",
                String.format("Trying to get non-existing row: %d. Available rows: %d..%d", rowIndex, 0, values.length - 1));
    }

}
