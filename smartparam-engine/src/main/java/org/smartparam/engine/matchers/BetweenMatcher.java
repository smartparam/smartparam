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
package org.smartparam.engine.matchers;

import org.smartparam.engine.annotated.annotations.ParamMatcher;
import org.smartparam.engine.annotated.annotations.ObjectInstance;
import org.smartparam.engine.core.matcher.Matcher;
import org.smartparam.engine.core.type.ValueHolder;
import org.smartparam.engine.core.type.Type;
import org.smartparam.engine.util.EngineUtil;

/**
 * Range matcher, checks if value fits in range defined in pattern. Value type
 * and pattern type must also match. It is possible to define range inclusiveness
 * or exclusiveness, separately for each of range border value.
 *
 * Between matcher has a set of default separators, that will be used to
 * separate values for beginning and end of range. These separators are (order matters):
 * <pre>
 * : - ,
 * </pre>
 * First separator that was found in pattern string is used to split it.
 * Use {@link #setSeparators(java.lang.String) } to override defaults.
 *
 * @author Przemek Hertel
 * @since 0.9.0
 */
@ParamMatcher(value = "", instances = {
    @ObjectInstance(value = "between/ie", constructorArgs = {"true", "false"}),
    @ObjectInstance(value = "between/ei", constructorArgs = {"false", "true"}),
    @ObjectInstance(value = "between/ii", constructorArgs = {"true", "true"}),
    @ObjectInstance(value = "between/ee", constructorArgs = {"false", "false"})
})
public class BetweenMatcher implements Matcher {

    private static final char[] DEFAULT_SEPARATORS = {':', '-', ','};

    private boolean lowerInclusive = true;

    private boolean upperInclusive = false;

    private char[] separators = DEFAULT_SEPARATORS;

    public BetweenMatcher() {
    }

    public BetweenMatcher(String lowerInclusive, String upperInclusive) {
        setLowerInclusive(Boolean.parseBoolean(lowerInclusive));
        setUpperInclusive(Boolean.parseBoolean(upperInclusive));
    }

    public BetweenMatcher(String lowerInclusive, String upperInclusive, String separators) {
        this(Boolean.parseBoolean(lowerInclusive), Boolean.parseBoolean(upperInclusive), separators);
    }

    /**
     * @param lowerInclusive range lower end should be inclusive?
     * @param upperInclusive range upper end should be inclusive?
     * @param separators     separators to use
     */
    public BetweenMatcher(boolean lowerInclusive, boolean upperInclusive, String separators) {
        setLowerInclusive(lowerInclusive);
        setUpperInclusive(upperInclusive);
        setSeparators(separators);
    }

    @Override
    public <T extends ValueHolder> boolean matches(String value, String pattern, Type<T> type) {
        char separator = findSeparator(pattern);

        String[] tokens = EngineUtil.split2(pattern, separator);
        String lower = tokens[0].trim();
        String upper = tokens[1].trim();

        T v = type.decode(value);

        return lowerCondition(v, lower, type) && upperCondition(v, upper, type);
    }

    private char findSeparator(String pattern) {
        for (char ch : separators) {
            if (pattern.indexOf(ch) >= 0) {
                return ch;
            }
        }
        return DEFAULT_SEPARATORS[0];
    }

    private <T extends ValueHolder> boolean lowerCondition(T v, String lower, Type<T> type) {
        if ("*".equals(lower) || "".equals(lower)) {
            return true;
        }

        T l = type.decode(lower);

        return lowerInclusive ? l.compareTo(v) <= 0 : l.compareTo(v) < 0;
    }

    private <T extends ValueHolder> boolean upperCondition(T v, String upper, Type<T> type) {
        if ("*".equals(upper) || "".equals(upper)) {
            return true;
        }

        T u = type.decode(upper);

        return upperInclusive ? v.compareTo(u) <= 0 : v.compareTo(u) < 0;
    }

    public final void setLowerInclusive(boolean lowerInclusive) {
        this.lowerInclusive = lowerInclusive;
    }

    public final void setUpperInclusive(boolean upperInclusive) {
        this.upperInclusive = upperInclusive;
    }

    /**
     * Override default separators. Provided string is split into char array and
     * each character is treated as a single separator.
     */
    public final void setSeparators(String separators) {
        if (separators != null) {
            this.separators = separators.toCharArray();
        }
    }
}
