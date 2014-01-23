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
package org.smartparam.engine.index.visualize;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.smartparam.engine.util.Formatter;
import org.smartparam.engine.util.Printer;

/**
 *
 * @author Adam Dubiel
 */
public class VisualTreeNode<T> {

    private final Map<String, VisualTreeNode<T>> children = new HashMap<String, VisualTreeNode<T>>();

    private final VisualTreeNode<T> parent;

    private final String level;

    private T value;

    private boolean containsDefault = false;

    public VisualTreeNode(VisualTreeNode<T> parent, String level) {
        this.parent = parent;
        this.level = level;
    }

    public T value() {
        return value;
    }

    public VisualTreeNode<T> parent() {
        return parent;
    }

    public VisualTreeNode<T> addDictionaryLevel(String levelValue) {
        return addLevel(levelValue);
    }

    public VisualTreeNode<T> addAnyLevel() {
        containsDefault = true;
        return addLevel("*");
    }

    private VisualTreeNode<T> addLevel(String levelValue) {
        VisualTreeNode<T> child = new VisualTreeNode<T>(this, levelValue);
        children.put(levelValue, child);

        return child;
    }

    public void appendPath(String[] levelValues, int currentDepth, T value) {
        if (currentDepth >= levelValues.length) {
            // matcher
            this.value = value;
            return;
        }

        String levelValue = levelValues[currentDepth];
        if ("*".equals(levelValue) && containsDefault) {
            children.get("*").appendPath(levelValues, currentDepth + 1, value);
        } else {
            if (!children.containsKey(levelValue)) {
                if (containsDefault) {
                    // merging dates etc
                    addLevelFromPath(levelValue, currentDepth, levelValues.length);
                } else {
                    throw new ParameterValueDoesNotMatchDictionary(this.level, currentDepth, levelValue);
                }
            }
            children.get(levelValue).appendPath(levelValues, currentDepth + 1, value);
        }
    }

    private void addLevelFromPath(String levelValue, int currentDepth, int levelsTotal) {
        VisualTreeNode<T> addedLevel = addLevel(levelValue);
        if (currentDepth < levelsTotal - 1) {
            addedLevel.addAnyLevel();
        }
    }

    public void harvestLeavesValues(List<T> appendTo) {
        if (isLeaf()) {
            if (value != null) {
                appendTo.add(value);
            }
        } else {
            for (VisualTreeNode<T> child : children.values()) {
                child.harvestLeavesValues(appendTo);
            }
        }
    }

    private boolean isLeaf() {
        return children.isEmpty();
    }

    public void printNode(StringBuilder sb, int level) {
        String indent = Printer.repeat(' ', level << 2);
        boolean leaf = isLeaf();

        sb.append(indent).append("path : ").append(getLevelPath());
        if (leaf) {
            sb.append("   (leaf=").append(value).append(')');
        }
        sb.append(Formatter.NL);

        if (children != null) {
            for (VisualTreeNode<T> child : children.values()) {
                child.printNode(sb, level + 1);
            }
        }
    }

    private String getLevelPath() {
        String lv = level != null ? level : "";
        return parent != null ? parent.getLevelPath() + "/" + lv : lv;
    }
}
