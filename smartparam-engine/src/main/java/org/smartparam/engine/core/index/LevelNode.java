/*
 * Copyright 2013 Adam Dubiel, Przemek Hertel.
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.smartparam.engine.util.Formatter;

/**
 *
 * @param <T> leaf type
 *
 * @author Przemek Hertel
 */
public class LevelNode<T> {

    private static final int TO_STRING_INITIAL_LENGTH = 100;

    private static final float CHILDREN_MAP_LOAD_FACTOR = 0.8f;

    private String level;

    private Map<String, LevelNode<T>> children;

    private LevelNode<T> defaultNode;

    private List<T> leafList;

    private LevelNode<T> parent;

    private final LevelIndex<T> index;

    public LevelNode(LevelIndex<T> index) {
        this.index = index;
    }

    public LevelNode(String level, LevelNode<T> parent, LevelIndex<T> index) {
        this.level = level;
        this.parent = parent;
        this.index = index;
        this.leafList = null;
    }

    void add(List<String> levels, T leafValue, int depth) {
        String[] levelsArray = levels.toArray(new String[levels.size()]);
        add(levelsArray, leafValue, depth);
    }

    void add(String[] levels, T leafValue, int depth) {

        if (!reachedLeafDepth(depth)) {
            String levelVal = levels[depth];

            if ("*".equals(levelVal)) {
                if (defaultNode == null) {
                    defaultNode = new LevelNode<T>(levelVal, this, index);
                }
                defaultNode.add(levels, leafValue, depth + 1);
            } else {
                ensureChildrenIsReady();
                LevelNode<T> child = children.get(levelVal);
                if (child == null) {
                    child = new LevelNode<T>(levelVal, this, index);
                    children.put(levelVal, child);
                }
                child.add(levels, leafValue, depth + 1);
            }
        } else {
            if (leafList == null) {
                leafList = new ArrayList<T>(1);
                leafList.add(leafValue);
            } else {
                List<T> newlist = new ArrayList<T>(leafList.size() + 1);
                newlist.addAll(leafList);
                newlist.add(leafValue);
                leafList = newlist;
            }

        }
    }

    private boolean reachedLeafDepth(int depth) {
        return depth >= index.getLevelCount();
    }

    private void ensureChildrenIsReady() {
        if (children == null) {
            children = new HashMap<String, LevelNode<T>>(2, CHILDREN_MAP_LOAD_FACTOR);
        }
    }

    public boolean hasChildren() {
        return (children != null && !children.isEmpty()) || defaultNode != null;
    }

    public boolean isLeaf() {
        return !hasChildren();
    }

    public Map<String, LevelNode<T>> getChildren() {
        return children;
    }

    public LevelNode<T> getDefaultNode() {
        return defaultNode;
    }

    public List<T> getLeafList() {
        return leafList;
    }

    public T getLeafValue() {
        return leafList.get(0);
    }

    public void printNode(StringBuilder sb, int level) {
        String indent = repeat(' ', level << 2);
        boolean leaf = isLeaf();

        sb.append(indent).append("path : ").append(getLevelPath());
        if (leaf) {
            sb.append("   (leaf=").append(leafList).append(')');
        }
        sb.append(Formatter.NL);

        if (children != null) {
            for (LevelNode<T> child : children.values()) {
                child.printNode(sb, level + 1);
            }
        }

        if (defaultNode != null) {
            defaultNode.printNode(sb, level + 1);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(TO_STRING_INITIAL_LENGTH);
        sb.append("LevelNode[");
        sb.append("level=").append(level);
        sb.append(", path=").append(getLevelPath());
        if (!isLeaf()) {
            sb.append(", children=").append(children != null ? children.keySet() : null);
        } else {
            sb.append(", leaf=").append(leafList);
        }
        sb.append(']');
        return sb.toString();
    }

    private String repeat(char c, int count) {
        char[] str = new char[count];
        Arrays.fill(str, c);
        return new String(str);
    }

    private String getLevelPath() {
        String lv = level != null ? level : "";
        return parent != null ? parent.getLevelPath() + "/" + lv : lv;
    }
}
