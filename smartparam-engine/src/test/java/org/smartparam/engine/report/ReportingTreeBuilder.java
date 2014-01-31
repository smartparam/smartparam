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

import java.util.ArrayList;
import java.util.List;
import org.smartparam.engine.core.matcher.Matcher;
import org.smartparam.engine.matchers.BetweenMatcher;
import org.smartparam.engine.matchers.decoder.BetweenMatcherDecoder;
import org.smartparam.engine.types.integer.IntegerType;

/**
 *
 * @author Adam Dubiel
 */
public final class ReportingTreeBuilder {

    private final List<ReportingTreeLevel> operations = new ArrayList<ReportingTreeLevel>();

    private ReportingTreeBuilder() {
        addExactLevel();
    }

    public static ReportingTreeBuilder reportingTree() {
        return new ReportingTreeBuilder();
    }

    public ReportingTree<String> build() {
        return new ReportingTree<String>(operations, new FirstWinsValueChooser<String>());
    }

    public ReportingTreeBuilder addAmbiguousIntegerLevel(String searchedValue, Matcher overridenMatcher) {
        operations.add(new ReportingTreeLevel(searchedValue, true,
                new BetweenMatcher(true, false, "~"),
                overridenMatcher,
                new IntegerType(),
                new BetweenMatcherDecoder(),
                new ContinuousSegmentsSpaceFactory()
        ));
        return this;
    }

    public ReportingTreeBuilder addExactLevel() {
        operations.add(new ReportingTreeLevel("", false, null, null, null, null, null));
        return this;
    }

    public ReportingTreeBuilder withOnlyExactLevels(int count) {
        for (int i = 0; i < count; ++i) {
            addExactLevel();
        }
        return this;
    }
}
