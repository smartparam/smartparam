package pl.generali.merkury.param.matchers;

import pl.generali.merkury.param.core.index.Matcher;
import pl.generali.merkury.param.core.type.AbstractHolder;
import pl.generali.merkury.param.core.type.AbstractType;

/**
 * @author Przemek Hertel
 */
public class EqMatcher implements Matcher {

    private boolean caseSensitive = true;

    public EqMatcher() {
    }

    public EqMatcher(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    @Override
    public <T extends AbstractHolder> boolean matches(String value, String pattern, AbstractType<T> type) {
        if(value != null) {
            return caseSensitive ? value.equals(pattern) : value.equalsIgnoreCase(pattern);
        }
        return false;
    }

}
