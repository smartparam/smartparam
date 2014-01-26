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
package org.smartparam.engine.report;

import org.smartparam.engine.core.matcher.Matcher;
import org.smartparam.engine.core.matcher.MatcherAwareDecoder;
import org.smartparam.engine.core.type.Type;

/**
 *
 * @author Adam Dubiel
 */
public class ReportingTreeLevelOperations {

    private final boolean potentiallyAmbiguous;

    private final Matcher matcher;

    private final Type<?> type;

    private final MatcherAwareDecoder matcherDecoder;

    private final OverlappingSetsSplitter splitter;

    public ReportingTreeLevelOperations(boolean potentiallyAmbiguous, Matcher matcher,
            Type<?> type, MatcherAwareDecoder<?> matcherDecoder,
            OverlappingSetsSplitter<?> splitter) {
        this.potentiallyAmbiguous = potentiallyAmbiguous;
        this.matcher = matcher;
        this.type = type;
        this.matcherDecoder = matcherDecoder;
        this.splitter = splitter;
    }

    public static ReportingTreeLevelOperations exact() {
        return new ReportingTreeLevelOperations(false, null, null, null, null);
    }

    boolean ambiguous() {
        return potentiallyAmbiguous;
    }

    @SuppressWarnings("unchecked")
    DisjointSets<String> split(String existingSet, String incomingSet) {
        DisjointSets<Object> splitProduct = splitter.split(
                matcherDecoder.decode(existingSet, type, matcher),
                matcherDecoder.decode(incomingSet, type, matcher)
        );

        DisjointSets<String> splitSets = new DisjointSets<String>();
        for (Object object : splitProduct.setAParts()) {
            splitSets.withPartOfSetA(matcherDecoder.encode(object, type, matcher));
        }
        if (splitSets.intersection() != null) {
            splitSets.withIntersection(matcherDecoder.encode(splitSets.intersection(), type, matcher));
        }
        for (Object object : splitProduct.setBParts()) {
            splitSets.withPartOfSetB(matcherDecoder.encode(object, type, matcher));
        }

        return splitSets;
    }

    <T> T chooseValue(T existing, T incoming) {
        if (existing != null) {
            return existing;
        }
        return incoming;
    }
}
