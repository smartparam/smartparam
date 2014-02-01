/*
 * Copyright 2014 Adam Dubiel.
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
package org.smartparam.engine.report.tree;

import org.smartparam.engine.core.matcher.Matcher;
import org.smartparam.engine.core.matcher.MatcherType;
import org.smartparam.engine.core.type.Type;

/**
 *
 * @author Adam Dubiel
 */
public class ReportingTreeLevelDescriptor {

    private final String searchedValue;

    private final boolean ambiguous;

    private final Matcher originalMatcher;

    private final Matcher overridenMatcher;

    private final Type<?> type;

    private final MatcherType matcherType;

    private final ReportLevelValuesSpaceFactory ambiguousSpaceFactory;

    public ReportingTreeLevelDescriptor(String searchedValue, boolean ambiguous,
            Matcher originalMatcher,
            Matcher overridenMatcher,
            Type<?> type, MatcherType<?> matcherType,
            ReportLevelValuesSpaceFactory ambiguousSpaceFactory) {
        this.searchedValue = searchedValue;
        this.ambiguous = ambiguous;
        this.originalMatcher = originalMatcher;
        this.overridenMatcher = overridenMatcher;
        this.type = type;
        this.matcherType = matcherType;
        this.ambiguousSpaceFactory = ambiguousSpaceFactory;
    }

    boolean ambiguous() {
        return ambiguous;
    }

    @SuppressWarnings("unchecked")
    public <T> T decode(String string) {
        return (T) matcherType.decode(string, type, originalMatcher);
    }

    @SuppressWarnings("unchecked")
    public <T> String encode(T object) {
        return matcherType.encode(object, type, originalMatcher);
    }

    public <V> ReportLevelValuesSpace<V> createSpace() {
        return ambiguousSpaceFactory.createSpace();
    }

    public boolean matches(String pattern) {
        return overridenMatcher.matches(searchedValue, pattern, type);
    }
}
