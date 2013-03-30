package org.smartparam.engine.types.date;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import org.smartparam.engine.annotations.SmartParamType;
import org.smartparam.engine.core.type.AbstractType;
import org.smartparam.engine.util.EngineUtil;

/**
 * @author Przemek Hertel
 * @since 1.0.0
 */
@SmartParamType("date")
public class DateType extends AbstractType<DateHolder> {

    private static String defaultOutputPattern = "dd-MM-yyyy";

    @Override
    public String encode(DateHolder holder) {
        return holder.getString(defaultOutputPattern);
    }

    @Override
    public DateHolder decode(String text) {
        Date date = EngineUtil.hasText(text) ? guess(text) : null;
        return new DateHolder(date);
    }

    @Override
    public DateHolder convert(Object obj) {
        if (obj instanceof Date) {
            return new DateHolder((Date) obj);
        }

        if (obj instanceof Calendar) {
            Calendar cal = (Calendar) obj;
            return new DateHolder(cal.getTime());
        }

        if (obj == null) {
            return new DateHolder(null);
        }

        if (obj instanceof String) {
            return decode((String) obj);
        }

        throw new IllegalArgumentException("conversion not supported for: " + obj.getClass());
    }

    @Override
    public DateHolder[] newArray(int size) {
        return new DateHolder[size];
    }

    private Date guess(String text) {

        String dateStr = text.trim();
        if (dateStr.length() == DATESTR_LENGTH) {

            char c1 = dateStr.charAt(IX2);
            char c2 = dateStr.charAt(IX5);

            if (bothEqualTo(c1, c2, '-')) {
                return parse(dateStr, "dd-MM-yyyy");
            }
            if (bothEqualTo(c1, c2, '.')) {
                return parse(dateStr, "dd.MM.yyyy");
            }
            if (bothEqualTo(c1, c2, '/')) {
                return parse(dateStr, "dd/MM/yyyy");
            }

            c1 = dateStr.charAt(IX4);
            c2 = dateStr.charAt(IX7);

            if (bothEqualTo(c1, c2, '-')) {
                return parse(dateStr, "yyyy-MM-dd");
            }
            if (bothEqualTo(c1, c2, '.')) {
                return parse(dateStr, "yyyy.MM.dd");
            }
            if (bothEqualTo(c1, c2, '/')) {
                return parse(dateStr, "yyyy/MM/dd");
            }
        }

        throw new IllegalArgumentException("Unknown date format: [" + dateStr + "]");
    }
    private static final int IX2 = 2;

    private static final int IX5 = 5;

    private static final int IX4 = 4;

    private static final int IX7 = 7;

    private static final int DATESTR_LENGTH = 10;

    private Date parse(String dateStr, String pattern) {
        try {
            return SimpleDateFormatPool.get(pattern).parse(dateStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Cannot parse date string [" + dateStr + "]", e);
        }
    }

    private boolean bothEqualTo(char c1, char c2, char expected) {
        return c1 == expected && c2 == expected;
    }

    public static void setDefaultOutputPattern(String pattern) {
        defaultOutputPattern = pattern;
    }

    public static String getDefaultOutputPattern() {
        return defaultOutputPattern;
    }
}
