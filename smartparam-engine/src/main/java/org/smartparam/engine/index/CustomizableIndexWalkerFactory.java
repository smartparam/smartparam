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

import org.smartparam.engine.core.index.LevelIndexWalker;
import org.smartparam.engine.core.LevelIndexWalkerFactory;
import org.smartparam.engine.core.matcher.Matcher;
import org.smartparam.engine.core.matcher.MatcherRepository;
import org.smartparam.engine.core.prepared.PreparedEntry;
import org.smartparam.engine.core.prepared.PreparedLevel;
import org.smartparam.engine.core.prepared.PreparedParameter;

/**
 *
 * @author Adam Dubiel
 */
public class CustomizableIndexWalkerFactory implements LevelIndexWalkerFactory<PreparedEntry> {

    private final MatcherRepository matcherRepository;

    private final CustomizableLevelIndexWalkerConfig config;

    CustomizableIndexWalkerFactory(MatcherRepository matcherRepository, CustomizableLevelIndexWalkerConfig walkerConfig) {
        this.matcherRepository = matcherRepository;
        this.config = walkerConfig;
    }

    @Override
    public LevelIndexWalker<PreparedEntry> create(PreparedParameter preparedParameter, String... levelValues) {
        return new CustomizableLevelIndexWalker<PreparedEntry>(convert(preparedParameter), preparedParameter.getIndex(), levelValues);
    }

    private IndexTraversalOverrides convert(PreparedParameter parameter) {
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
            greedinessMatrix[depth] = config.greedy(levelName);
            overridenMatchers[depth] = config.overrideMatcher(levelName) ? matcherRepository.getMatcher(config.overridenMatcher(levelName)) : null;

            depth++;
        }

        return new IndexTraversalOverrides(greedinessMatrix, overridenMatchers);
    }
}
