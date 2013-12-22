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

import java.util.Collection;
import org.smartparam.engine.core.type.ValueHolder;
import org.smartparam.engine.core.type.Type;

/**
 * @author Przemek Hertel
 * @since 0.9.0
 */
final class TypeDecoder {

    private TypeDecoder() {
    }

    static ValueHolder decode(Type<?> type, String text) {
        try {
            return type.decode(text != null ? text.trim() : null);
        } catch (RuntimeException exception) {
            throw new TypeDecodingException(exception, text, type);
        }
    }

    static ValueHolder convert(Type<?> type, Object obj) {
        try {
            return type.convert(obj);
        } catch (RuntimeException exception) {
            throw new TypeConversionException(exception, obj, type);
        }
    }

    static ValueHolder[] convert(Type<?> type, Object[] array) {
        ValueHolder[] result = type.newArray(array.length);
        for (int i = 0; i < result.length; i++) {
            result[i] = convert(type, array[i]);
        }
        return result;
    }

    static ValueHolder[] convert(Type<?> type, Collection<?> coll) {
        return convert(type, coll.toArray());
    }

}
