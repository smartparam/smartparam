package org.smartparam.engine.test.scan;

import org.smartparam.engine.annotations.ParamMatcher;
import org.smartparam.engine.core.index.Matcher;
import org.smartparam.engine.core.type.AbstractHolder;
import org.smartparam.engine.core.type.Type;

/**
 *
 * @author Adam Dubiel
 */
@ParamMatcher("dummyMatcher")
public class DummyMatcher implements Matcher {

    @Override
    public <T extends AbstractHolder> boolean matches(String value, String pattern, Type<T> type) {
        throw new UnsupportedOperationException("Dummy implementation");
    }
}
