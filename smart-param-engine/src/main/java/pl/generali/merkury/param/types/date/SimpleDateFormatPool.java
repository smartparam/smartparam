package pl.generali.merkury.param.types.date;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Przemek Hertel
 * @since 1.0.0
 */
public abstract class SimpleDateFormatPool {

    private static ThreadLocal<Map<String, SimpleDateFormat>> pool = new ThreadLocal<Map<String, SimpleDateFormat>>() {

        @Override
        protected Map<String, SimpleDateFormat> initialValue() {
            return new HashMap<String, SimpleDateFormat>();
        }
    };

    //boost 10x (jest szybsze dokladnie 10x w stosunku do tworzenia new SimpleDateFormat)
    public static SimpleDateFormat get(String pattern) {

        Map<String, SimpleDateFormat> map = pool.get();

        SimpleDateFormat sdf = map.get(pattern);
        if (sdf == null) {
            sdf = new SimpleDateFormat(pattern);
            sdf.setLenient(false);
            map.put(pattern, sdf);
        }
        return sdf;
    }
}
