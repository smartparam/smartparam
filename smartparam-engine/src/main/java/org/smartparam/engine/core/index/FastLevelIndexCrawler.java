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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.smartparam.engine.core.matcher.Matcher;
import org.smartparam.engine.core.type.Type;

/**
 *
 * @author Adam Dubiel
 */
public class FastLevelIndexCrawler<T> implements LevelIndexCrawler<T> {

    private final LevelIndex<T> index;

    private final String[] levelValues;

    public FastLevelIndexCrawler(LevelIndex<T> index, String[] levelValues) {
        this.index = index;
        this.levelValues = levelValues;
    }

    @Override
    public List<T> find() {
        LevelNode<T> node = find(index.getRoot(), 0);
        return node != null ? node.getLeafList() : null;
    }

    private LevelNode<T> find(LevelNode<T> currentNode, int currentDepth) {
        if (currentDepth >= levelValues.length) {
            // last node reached - final station
            return currentNode;
        }

        String levelValue = levelValues[currentDepth];

        Matcher matcher = index.getMatcher(currentDepth);
        Type<?> type = index.getType(currentDepth);

        LevelNode<T> matchedLeaf = null;

        if (currentNode.getChildren() != null) {
            if (matcher == null) {
                matchedLeaf = currentNode.getChildren().get(levelValue);
                if (matchedLeaf != null) {
                    LevelNode<T> leaf = find(matchedLeaf, currentDepth + 1);
                    if (leaf != null) {
                        return leaf;
                    }
                }
            }

            matchedLeaf = match(currentNode, levelValue, matcher, type, currentDepth);
        }

        if (matchedLeaf == null && currentNode.getDefaultNode() != null) {
            matchedLeaf = find(currentNode.getDefaultNode(), currentDepth + 1);
        }

        return matchedLeaf;
    }

    private LevelNode<T> match(LevelNode<T> currentNode, String val, Matcher matcher, Type<?> type, int currentDepth) {
        LevelNode<T> leaf = null;
        Iterator<Map.Entry<String, LevelNode<T>>> childrenIterator = currentNode.getChildren().entrySet().iterator();

        Map.Entry<String, LevelNode<T>> entry;
        while (leaf == null && childrenIterator.hasNext()) {
            entry = childrenIterator.next();
            if (patternMatches(val, matcher, type, entry.getKey())) {
                leaf = traverseChildNode(entry.getValue(), currentDepth);
            }
        }

        return leaf;
    }

    private boolean patternMatches(String value, Matcher matcher, Type<?> type, String pattern) {
        if (matcher == null) {
            if (pattern == null) {
                return value == null;
            }
            return pattern.equals(value);
        } else {
            return matcher.matches(value, pattern, type);
        }
    }

    private LevelNode<T> traverseChildNode(LevelNode<T> child, int currentDepth) {
        return find(child, currentDepth + 1);
    }
}
