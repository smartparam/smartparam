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
package org.smartparam.engine.core.type.decode;

import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.core.type.Type;
import org.smartparam.engine.core.type.Type;

/**
 * Decoding value failed
 * {@link org.smartparam.engine.core.type.AbstractType#decode(java.lang.String)}.
 *
 * @author Adam Dubiel
 */
@SuppressWarnings("serial")
public class TypeDecodingException extends SmartParamException {

    TypeDecodingException(RuntimeException cause, String value, Type<?> targetType) {
        super("TYPE_DECODING_FAILURE", cause,
                String.format("Failed to decode text [%s] into type [%s], check if level type is set correctly.",
                        value, targetType != null ? targetType.getClass().getSimpleName() : null));
    }

}
