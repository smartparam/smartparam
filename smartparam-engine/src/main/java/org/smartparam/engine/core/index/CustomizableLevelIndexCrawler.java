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
package org.smartparam.engine.core.index;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.smartparam.engine.core.matcher.Matcher;
import org.smartparam.engine.core.type.Type;

/**
 *
 * @author Adam Dubiel
 */
public class CustomizableLevelIndexCrawler<T> implements LevelIndexCrawler<T> {

    private final LevelNodeInspector<T> fastNodeInspector = new FastLevelNodeInspector<T>(this);

    private final LevelNodeInspector<T> greedyNodeInspector = new GreedyLevelNodeInspector<T>(this);

    private final LevelIndex<T> index;

    private final String[] levelValues;

    private final IndexTraversalOverrides overrides;

    public CustomizableLevelIndexCrawler(IndexTraversalOverrides overrides, LevelIndex<T> index, String... levelValues) {
        this.overrides = overrides;
        this.index = index;
        this.levelValues = levelValues;
    }

    @Override
    public List<T> find() {
        List<LevelNode<T>> nodes = inspect(index.getRoot(), 0);

        List<T> values = new ArrayList<T>();
        for (LevelNode<T> node : nodes) {
            values.addAll(node.getLeafList());
        }
        return values;
    }

    @SuppressWarnings("unchecked")
    public List<LevelNode<T>> inspect(LevelNode<T> currentNode, int depth) {
        if (depth >= levelValues.length) {
            return Arrays.asList(currentNode);
        }
        return inspectorFor(depth).inspect(currentNode, levelValues[depth], depth);
    }

    private LevelNodeInspector<T> inspectorFor(int depth) {
        return overrides.isGreedy(depth) ? greedyNodeInspector : fastNodeInspector;
    }

    public Matcher matcherFor(int depth) {
        return overrides.overrideMatcher(depth) ? overrides.overridenMatcher(depth) : index.getMatcher(depth);
    }

    public Type<?> typeFor(int depth) {
        return index.getType(depth);
    }
}
