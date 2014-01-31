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
package org.smartparam.engine.index;

import org.smartparam.engine.core.matcher.Matcher;

/**
 *
 * @author Adam Dubiel
 */
public class IndexTraversalOverridesTestBuilder {

    private boolean[] greedinessMatrix;

    private Matcher[] overridenMatchers;

    private IndexTraversalOverridesTestBuilder() {
    }

    public static IndexTraversalOverridesTestBuilder indexTraversalOverrides() {
        return new IndexTraversalOverridesTestBuilder();
    }

    public IndexTraversalOverrides build() {
        return new IndexTraversalOverrides(greedinessMatrix,
                overridenMatchers == null ? new Matcher[greedinessMatrix.length] : overridenMatchers,
                new String[greedinessMatrix.length],
                new String[greedinessMatrix.length]);
    }

    public IndexTraversalOverridesTestBuilder withGreediness(boolean... greediness) {
        this.greedinessMatrix = greediness;
        return this;
    }

    public IndexTraversalOverridesTestBuilder overridingMatchers(Matcher... matchers) {
        this.overridenMatchers = matchers;
        return this;
    }
}
