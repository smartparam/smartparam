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
import org.smartparam.engine.core.matcher.MatcherRepository;
import org.smartparam.engine.core.prepared.PreparedEntry;
import org.smartparam.engine.core.prepared.PreparedLevel;
import org.smartparam.engine.core.prepared.PreparedParameter;

/**
 *
 * @author Adam Dubiel
 */
public class CustomizableIndexWalkerFactory implements LevelIndexWalkerFactory {

    private final MatcherRepository matcherRepository;

    private final CustomizableLevelIndexWalkerConfig config;

    CustomizableIndexWalkerFactory(MatcherRepository matcherRepository, CustomizableLevelIndexWalkerConfig walkerConfig) {
        this.matcherRepository = matcherRepository;
        this.config = walkerConfig;
    }

    @Override
    public LevelIndexWalker<PreparedEntry> create(PreparedParameter preparedParameter, String... levelValues) {
        return new CustomizableLevelIndexWalker<PreparedEntry>(
                convert(preparedParameter),
                config.valuesExtractor(),
                preparedParameter.getIndex(), levelValues);
    }

    private IndexTraversalConfig convert(PreparedParameter parameter) {
        IndexTraversalConfig overrides = new IndexTraversalConfig();

        for (PreparedLevel level : parameter.getLevels()) {
            String levelName = level.getName();
            overrides.addLevel(new IndexLevelDescriptor(
                    levelName,
                    config.overrideMatcher(levelName) ? matcherRepository.getMatcher(config.overridenMatcher(levelName)) : level.getMatcher(),
                    level.getMatcher(),
                    level.getMatcherName(),
                    config.greedy(levelName),
                    level.getType()));
        }

        return overrides;
    }
}
