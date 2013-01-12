package pl.generali.merkury.param.types.date;

import java.util.Date;
import pl.generali.merkury.param.core.type.AbstractHolder;

/**
 * in progress..
 *
 * @author Przemek Hertel
 */
public class DateHolder extends AbstractHolder {

    private Date date;

    public DateHolder(Date date) {
        this.date = date;
    }

    @Override
    public Date getValue() {
        return date;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public String getString() {
        return getString(DateType.getDefaultOutputPattern());
    }

    public String getString(String pattern) {
        return date != null ? SimpleDateFormatPool.get(pattern).format(date) : null;
    }
}
