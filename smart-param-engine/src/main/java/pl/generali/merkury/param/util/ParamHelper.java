package pl.generali.merkury.param.util;

import java.lang.reflect.Array;
import java.util.Collection;
import org.apache.commons.lang3.ClassUtils;
import pl.generali.merkury.param.core.exception.ParamException;
import pl.generali.merkury.param.core.exception.ParamException.ErrorCode;
import pl.generali.merkury.param.core.type.AbstractHolder;
import pl.generali.merkury.param.core.type.AbstractType;

/**
 * @author Przemek Hertel
 * @since 1.0.0
 */
public abstract class ParamHelper {

    //t
    public static AbstractHolder decode(AbstractType<?> type, String text) {
        try {
            return type.decode(text != null ? text.trim() : null);
        } catch (RuntimeException e) {
            throw new ParamException(
                    ErrorCode.TYPE_DECODING_FAILURE, e,
                    "Failed to decode text [" + text + "] into type [" + ClassUtils.getShortClassName(type, null) + "]");
        }
    }

    //t
    public static AbstractHolder convert(AbstractType<?> type, Object obj) {
        try {
            return type.convert(obj);
        } catch (RuntimeException e) {
            throw new ParamException(
                    ErrorCode.TYPE_CONVERSION_FAILURE, e,
                    "Failed to convert object [" + obj + "] into type [" + type.getClass() + "]");
        }
    }

    //t
    public static AbstractHolder[] convert(AbstractType<?> type, Object[] array) {
        AbstractHolder[] result = type.newArray(array.length);
        for (int i = 0; i < result.length; i++) {
            result[i] = convert(type, array[i]);
        }
        return result;
    }

    public static AbstractHolder[] convertNonObjectArray(AbstractType<?> type, Object array) {
        int arrayLen = Array.getLength(array);
        AbstractHolder[] result = type.newArray(arrayLen);
        for (int i = 0; i < result.length; i++) {
            result[i] = convert(type, Array.get(array, i));
        }
        return result;
    }

    //t
    public static AbstractHolder[] convert(AbstractType<?> type, Collection<?> coll) {
        return convert(type, coll.toArray());
    }
}
