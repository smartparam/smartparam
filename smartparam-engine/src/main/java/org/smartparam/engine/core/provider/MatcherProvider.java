package org.smartparam.engine.core.provider;

import org.smartparam.engine.core.index.Matcher;

/**
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public interface MatcherProvider {

    void registerMatcher(String code, Matcher matcher);

    Matcher getMatcher(String code);
}
