package org.smartparam.engine.matchers;

import org.smartparam.engine.annotations.SmartParamMatcher;
import org.smartparam.engine.core.index.Matcher;
import org.smartparam.engine.core.type.AbstractHolder;
import org.smartparam.engine.core.type.AbstractType;

/**
 * @author Przemek Hertel
 */
@SmartParamMatcher("eqMatcher")
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
