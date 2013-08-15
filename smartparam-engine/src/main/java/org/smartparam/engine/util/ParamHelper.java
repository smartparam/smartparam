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
package org.smartparam.engine.util;

import java.lang.reflect.Array;
import java.util.Collection;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.type.AbstractHolder;
import org.smartparam.engine.core.type.Type;

/**
 * @author Przemek Hertel
 */
public abstract class ParamHelper {

    public static AbstractHolder decode(Type<?> type, String text) {
        try {
            return type.decode(text != null ? text.trim() : null);
        } catch (RuntimeException exception) {
            throw new SmartParamException(
                    SmartParamErrorCode.TYPE_DECODING_FAILURE, exception,
                    String.format("Failed to decode text [%s] into type [%s], check if level type is set correctly.",
                    text, type != null ? type.getClass().getSimpleName() : null));
        }
    }

    public static AbstractHolder convert(Type<?> type, Object obj) {
        try {
            return type.convert(obj);
        } catch (RuntimeException e) {
            throw new SmartParamException(
                    SmartParamErrorCode.TYPE_CONVERSION_FAILURE, e,
                    String.format("Failed to convert object [%s] into type [%s], check if level type is set correctly.",
                    obj, type.getClass().getSimpleName()));
        }
    }

    public static AbstractHolder[] convert(Type<?> type, Object[] array) {
        AbstractHolder[] result = type.newArray(array.length);
        for (int i = 0; i < result.length; i++) {
            result[i] = convert(type, array[i]);
        }
        return result;
    }

    public static AbstractHolder[] convertNonObjectArray(Type<?> type, Object array) {
        int arrayLen = Array.getLength(array);
        AbstractHolder[] result = type.newArray(arrayLen);
        for (int i = 0; i < result.length; i++) {
            result[i] = convert(type, Array.get(array, i));
        }
        return result;
    }

    public static AbstractHolder[] convert(Type<?> type, Collection<?> coll) {
        return convert(type, coll.toArray());
    }
}
