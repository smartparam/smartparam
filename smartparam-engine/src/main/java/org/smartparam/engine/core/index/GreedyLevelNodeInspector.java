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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.smartparam.engine.core.matcher.Matcher;
import org.smartparam.engine.core.type.Type;

/**
 *
 * @author Adam Dubiel
 */
public class GreedyLevelNodeInspector<T> implements LevelNodeInspector<T> {

    private final CustomizableLevelIndexWalker<T> indexCrawler;

    public GreedyLevelNodeInspector(CustomizableLevelIndexWalker<T> indexCrawler) {
        this.indexCrawler = indexCrawler;
    }

    @Override
    public List<LevelNode<T>> inspect(LevelNode<T> currentNode, String levelValue, int currentDepth) {
        List<LevelNode<T>> matchedNodes = new ArrayList<LevelNode<T>>();

        Matcher matcher = indexCrawler.matcherFor(currentDepth);
        Type<?> type = indexCrawler.typeFor(currentDepth);

        if (currentNode.getChildren() != null) {
            matchedNodes.addAll(match(currentNode, levelValue, matcher, type, currentDepth));
        }

        if (currentNode.getDefaultNode() != null) {
            matchedNodes.addAll(indexCrawler.inspect(currentNode.getDefaultNode(), currentDepth + 1));
        }

        return matchedNodes;
    }

    private List<LevelNode<T>> match(LevelNode<T> currentNode, String val, Matcher matcher, Type<?> type, int currentDepth) {
        List<LevelNode<T>> leafs = new ArrayList<LevelNode<T>>();
        Iterator<Map.Entry<String, LevelNode<T>>> childrenIterator = currentNode.getChildren().entrySet().iterator();

        Map.Entry<String, LevelNode<T>> entry;
        while (childrenIterator.hasNext()) {
            entry = childrenIterator.next();
            if (patternMatches(val, matcher, type, entry.getKey())) {
                leafs.addAll(traverseChildNode(entry.getValue(), currentDepth));
            }
        }

        return leafs;
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

    private List<LevelNode<T>> traverseChildNode(LevelNode<T> child, int currentDepth) {
        return indexCrawler.inspect(child, currentDepth + 1);
    }
}
