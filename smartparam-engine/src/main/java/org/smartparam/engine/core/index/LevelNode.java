package org.smartparam.engine.core.index;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.smartparam.engine.core.type.AbstractType;
import org.smartparam.engine.util.Formatter;

/**
 * in progress
 *
 * @param <T> typ indeksowanych wartosci, czyli typ liscia
 *
 * @author Przemek Hertel
 * @since 1.0.0
 */
public class LevelNode<T> {

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

    /**
     * buduje poddrzewo (galaz) biezacego wezla
     */
    public void add(String[] levels, T leafValue, Matcher[] matchers, int depth) {

        if (depth < index.getLevelCount()) {
            //pobieramy kolejna wartosc levelu
            String levelVal = levels[depth];	//wartosc lub *

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

            //wezel staje sie lisciem, zawiera ostateczna wartosc
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

    private void ensureChildrenIsReady() {
        if (children == null) {
            children = new LinkedHashMap<String, LevelNode<T>>(2, LOAD_FACTOR);
        }
    }
    private static final float LOAD_FACTOR = 0.8f;

    /**
     * znajduje wezel-lisc i zwraca jego wartosc, czyli obiekt CustomParameter.
     * szukanie odbywa sie wedlug algorytmu: 1. znajdz sciezke w drzewie
     * odpowiadajaca podanym wartosciom poziomow 2. jesli nie mozna znalezc
     * sciezki w oparciu do konkretne wartosci - szukamy sciezek oznaczonych
     * jako gwiazdki
     */
    public LevelNode<T> findNode(String[] levelValues, int depth) {

        if (depth >= levelValues.length) {
            //osiagnelismy ostatni wezel na szukanej sciezce
            return this;
        }

        //wartosc biezacego poziomu na sciezce
        String levelVal = levelValues[depth];

        //szukamy wezla-dziecka dla wartosci levelVal

        Matcher matcher = index.getMatcher(depth);
        AbstractType<?> type = index.getType(depth);

        LevelNode<T> child = null;

        if (children != null) {
            if (matcher == null) {
                child = children.get(levelVal);
            } else {
                child = match(levelVal, matcher, type);
            }
        }

        if (child != null) {
            LevelNode<T> leaf = child.findNode(levelValues, depth + 1);		//podazamy sciezka w kierunku liscia
            if (leaf != null) {
                return leaf;								//jesli znalezlismy lisc, zwracamy jego wartosc
            }
        }

        //jesli nie znalezlismy wezla-dziecka lub nie znalezlismy dla niego poprawnej sciezki - sprawdzamy wezel domyslny (*)
        if (defaultNode != null) {
            return defaultNode.findNode(levelValues, depth + 1);
        }

        //z biezacego wezla nie ma sciezki zgodnej z podlista levelValues(depth)
        return null;
    }

    private LevelNode<T> match(String val, Matcher matcher, AbstractType<?> type) {
        for (Map.Entry<String, LevelNode<T>> e : children.entrySet()) {
            String pattern = e.getKey();
            if (matcher.matches(val, pattern, type)) {
                return e.getValue();
            }
        }
        return null;
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

    /**
     * Buduje string skladajacy sie z <tt>count</tt> znakow <tt>c</tt>
     *
     * @param c     znak
     * @param count liczba powtorzen znaku <tt>c</tt>
     * @return string o dlugosci <tt>count</tt>
     */
    private String repeat(char c, int count) {
        char[] str = new char[count];
        Arrays.fill(str, c);
        return new String(str);
    }

    public String getLevel() {
        return level;
    }

    public LevelIndex<T> getIndex() {
        return index;
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
