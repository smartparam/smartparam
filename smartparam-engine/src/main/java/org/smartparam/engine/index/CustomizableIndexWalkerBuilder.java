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

import org.smartparam.engine.core.ParamEngine;
import org.smartparam.engine.core.matcher.MatcherRepository;

/**
 *
 * @author Adam Dubiel
 */
public final class CustomizableIndexWalkerBuilder {

    private final CustomizableLevelIndexWalkerConfig config = new CustomizableLevelIndexWalkerConfig();

    private final ParamEngine engine;

    private CustomizableIndexWalkerBuilder(ParamEngine engine) {
        this.engine = engine;
    }

    public static CustomizableIndexWalkerBuilder customizableIndexWalker(ParamEngine engine) {
        return new CustomizableIndexWalkerBuilder(engine);
    }

    public CustomizableIndexWalkerFactory build() {
        MatcherRepository matcherRepository = engine.runtimeConfiguration().getMatcherRepository();
        return new CustomizableIndexWalkerFactory(matcherRepository, config);
    }

    public CustomizableIndexWalkerBuilder withGreedyLevels(String... levelNames) {
        config.withGreedyLevels(levelNames);
        return this;
    }

    public CustomizableIndexWalkerBuilder withOverridenMatcher(String levelName, String matcherCode) {
        config.withOverridenMatcher(levelName, matcherCode);
        return this;
    }
}
