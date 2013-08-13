package org.smartparam.engine.core.index;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.smartparam.engine.core.type.Type;
import org.smartparam.engine.util.Formatter;

/**
 *
 * @param <T> leaf type
 *
 * @author Przemek Hertel <przemek.hertel@gmail.com>
 */
public class LevelNode<T> {

    private static final float CHILDREN_MAP_LOAD_FACTOR = 0.8f;

    private String level;

    private Map<String, LevelNode<T>> children;

    private LevelNode<T> defaultNode;

    private List<T> leafList;

    private LevelNode<T> parent;

    private LevelIndex<T> index;

    public LevelNode(LevelIndex<T> index) {
        this.index = index;
    }

    public LevelNode(String level, LevelNode<T> parent, LevelIndex<T> index) {
        this.level = level;
        this.parent = parent;
        this.index = index;
        this.leafList = null;
    }

    public void add(List<String> levels, T leafValue, Matcher[] matchers, int depth) {
        String[] levelsArray = levels.toArray(new String[levels.size()]);
        add(levelsArray, leafValue, matchers, depth);
    }

    public void add(String[] levels, T leafValue, Matcher[] matchers, int depth) {

        if (!reachedLeafDepth(depth)) {
            String levelVal = levels[depth];

            if ("*".equals(levelVal)) {
                if (defaultNode == null) {
                    defaultNode = new LevelNode<T>(levelVal, this, index);
                }
                defaultNode.add(levels, leafValue, matchers, depth + 1);
            } else {
                ensureChildrenIsReady();
                LevelNode<T> child = children.get(levelVal);
                if (child == null) {
                    child = new LevelNode<T>(levelVal, this, index);
                    children.put(levelVal, child);
                }
                child.add(levels, leafValue, matchers, depth + 1);
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

    /**
     * Finds leaf node and returns its value.
     * Recurrent search algorithm:
     * <pre>
     * 1. find tree path that matches query values descending one by one node
     * 2. if none found, try default value if defined for this level
     * </pre>
     *
     * @param levelValues query values
     * @param depth depth of current node
     * @return value
     */
    public LevelNode<T> findNode(String[] levelValues, int depth) {

        if (depth >= levelValues.length) {
            // last node reached - final station
            return this;
        }

        String levelVal = levelValues[depth];

        Matcher matcher = index.getMatcher(depth);
        Type<?> type = index.getType(depth);

        LevelNode<T> matchedLeaf = null;

        if (children != null) {
            matchedLeaf = match(levelVal, matcher, type, levelValues, depth);
        }

        if (matchedLeaf == null && defaultNode != null) {
            matchedLeaf = defaultNode.findNode(levelValues, depth + 1);
        }

        return matchedLeaf;
    }

    private LevelNode<T> match(String val, Matcher matcher, Type<?> type, String[] levelValues, int depth) {
        LevelNode<T> leaf = null;
        Iterator<Map.Entry<String, LevelNode<T>>> childrenIterator = children.entrySet().iterator();

        Map.Entry<String, LevelNode<T>> entry;
        while(leaf == null && childrenIterator.hasNext()) {
            entry = childrenIterator.next();
            if(patternMatches(val, matcher, type, entry.getKey())) {
                leaf = traverseChildNode(entry.getValue(), levelValues, depth);
            }
        }

        return leaf;
    }

    private boolean patternMatches(String value, Matcher matcher, Type<?> type, String pattern) {
        if(matcher == null) {
            if(pattern == null) {
                return value == null;
            }
            return pattern.equals(value);
        }
        else {
            return matcher.matches(value, pattern, type);
        }
    }

    private LevelNode<T> traverseChildNode(LevelNode<T> child, String[] levelValues, int depth) {
        LevelNode<T> leaf = child.findNode(levelValues, depth + 1);
        return leaf;
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

    public boolean hasChildren() {
        return (children != null && !children.isEmpty()) || defaultNode != null;
    }

    public boolean isLeaf() {
        return !hasChildren();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
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

    public String getLevel() {
        return level;
    }

    public List<T> getLeafList() {
        return leafList;
    }

    public T getLeafValue() {
        return leafList.get(0);
    }

    LevelNode<T> getParent() {
        return parent;
    }

    String getLevelPath() {
        String lv = level != null ? level : "";
        return parent != null ? parent.getLevelPath() + "/" + lv : lv;
    }

    Map<String, LevelNode<T>> getChildren() {
        return children;
    }

    LevelNode<T> getDefaultNode() {
        return defaultNode;
    }
}
