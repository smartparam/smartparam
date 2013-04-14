package org.smartparam.engine.util;

import java.lang.reflect.Array;
import java.util.Collection;
import org.apache.commons.lang3.ClassUtils;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.type.AbstractHolder;
import org.smartparam.engine.core.type.Type;

/**
 * @author Przemek Hertel
 * @since 1.0.0
 */
public abstract class ParamHelper {

    //t
    public static AbstractHolder decode(Type<?> type, String text) {
        try {
            return type.decode(text != null ? text.trim() : null);
        } catch (RuntimeException e) {
            throw new SmartParamException(
                    SmartParamErrorCode.TYPE_DECODING_FAILURE, e,
                    "Failed to decode text [" + text + "] into type [" + ClassUtils.getShortClassName(type, null) + "]");
        }
    }

    //t
    public static AbstractHolder convert(Type<?> type, Object obj) {
        try {
            return type.convert(obj);
        } catch (RuntimeException e) {
            throw new SmartParamException(
                    SmartParamErrorCode.TYPE_CONVERSION_FAILURE, e,
                    "Failed to convert object [" + obj + "] into type [" + type.getClass() + "]");
        }
    }

    //t
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

    //t
    public static AbstractHolder[] convert(Type<?> type, Collection<?> coll) {
        return convert(type, coll.toArray());
    }
}
