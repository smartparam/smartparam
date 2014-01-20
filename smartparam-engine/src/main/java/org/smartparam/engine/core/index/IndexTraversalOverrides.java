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
package org.smartparam.engine.core.index;

import org.smartparam.engine.core.matcher.Matcher;
import org.smartparam.engine.core.matcher.MatcherRepository;
import org.smartparam.engine.core.prepared.PreparedLevel;
import org.smartparam.engine.core.prepared.PreparedParameter;

/**
 *
 * @author Adam Dubiel
 */
public class IndexTraversalOverrides {

    private final boolean[] greedinessMatrix;

    private final Matcher[] overridenMatchers;

    public IndexTraversalOverrides(PreparedParameter parameter, IndexTraversalOptions options, MatcherRepository repository) {
        int totalDepth = parameter.getInputLevelsCount();

        this.greedinessMatrix = new boolean[totalDepth];
        this.overridenMatchers = new Matcher[totalDepth];

        int depth = 0;
        String levelName;
        for (PreparedLevel level : parameter.getLevels()) {
            if (depth >= totalDepth) {
                break;
            }

            levelName = level.getName();
            greedinessMatrix[depth] = options.greedy(levelName);
            overridenMatchers[depth] = options.overrideMatcher(levelName) ? repository.getMatcher(options.overridenMatcher(levelName)) : null;

            depth++;
        }
    }

    public IndexTraversalOverrides(boolean[] greedinessMatrix, Matcher[] overridenMatchers) {
        this.greedinessMatrix = greedinessMatrix;
        this.overridenMatchers = overridenMatchers;
    }

    public IndexTraversalOverrides(boolean[] greedinessMatrix) {
        this.greedinessMatrix = greedinessMatrix;
        this.overridenMatchers = new Matcher[greedinessMatrix.length];
    }

    public boolean isGreedy(int levelDepth) {
        return greedinessMatrix[levelDepth];
    }

    public boolean overrideMatcher(int levelDepth) {
        return overridenMatchers[levelDepth] != null;
    }

    public Matcher overridenMatcher(int levelDepth) {
        return overridenMatchers[levelDepth];
    }

}
