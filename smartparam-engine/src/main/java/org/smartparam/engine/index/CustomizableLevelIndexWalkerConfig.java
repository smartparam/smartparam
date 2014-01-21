/*
 * Copyright 2014 Adam Dubiel, Przemek Hertel.
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

import java.util.*;

/**
 *
 * @author Adam Dubiel
 */
public class CustomizableLevelIndexWalkerConfig {

    private final Set<String> greedyLevels = new HashSet<String>();

    private final Map<String, String> overridenMatchers = new HashMap<String, String>();

    public CustomizableLevelIndexWalkerConfig withGreedyLevels(String... levelNames) {
        greedyLevels.addAll(Arrays.asList(levelNames));
        return this;
    }

    public CustomizableLevelIndexWalkerConfig withOverridenMatcher(String levelName, String matcherCode) {
        overridenMatchers.put(levelName, matcherCode);
        return this;
    }

    public boolean greedy(String levelName) {
        return greedyLevels.contains(levelName);
    }

    public boolean overrideMatcher(String levelName) {
        return overridenMatchers.containsKey(levelName);
    }

    public String overridenMatcher(String levelName) {
        return overridenMatchers.get(levelName);
    }
}
