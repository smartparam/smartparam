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

package org.smartparam.engine.core;

import org.smartparam.engine.core.index.IndexTraversalOverrides;
import org.smartparam.engine.core.matcher.Matcher;
import org.smartparam.engine.core.matcher.MatcherRepository;
import org.smartparam.engine.core.prepared.PreparedLevel;
import org.smartparam.engine.core.prepared.PreparedParameter;

/**
 *
 * @author Adam Dubiel
 */
public class IndexTraversalOverridesFactory {

    private final MatcherRepository matcherRepository;

    public IndexTraversalOverridesFactory(MatcherRepository matcherRepository) {
        this.matcherRepository = matcherRepository;
    }

    public IndexTraversalOverrides create(PreparedParameter parameter, ParamEvaluationOptions options) {
        int totalDepth = parameter.getInputLevelsCount();

        boolean[] greedinessMatrix = new boolean[totalDepth];
        Matcher[] overridenMatchers = new Matcher[totalDepth];

        int depth = 0;
        String levelName;
        for (PreparedLevel level : parameter.getLevels()) {
            if (depth >= totalDepth) {
                break;
            }

            levelName = level.getName();
            greedinessMatrix[depth] = options.greedy(levelName);
            overridenMatchers[depth] = options.overrideMatcher(levelName) ? matcherRepository.getMatcher(options.overridenMatcher(levelName)) : null;

            depth++;
        }

        return new IndexTraversalOverrides(greedinessMatrix, overridenMatchers);
    }

}
