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
package org.smartparam.engine.report.tree;

import java.util.List;
import org.smartparam.engine.util.Formatter;
import org.smartparam.engine.util.Printer;

/**
 *
 * @author Adam Dubiel
 */
public abstract class ReportingTreeNode<V> {

    private final ReportingTree<V> tree;

    private final ReportingTreeNode<V> parent;

    private final ReportingTreeLevelDescriptor levelDescriptor;

    private final int depth;

    private String levelValue;

    private V leafValue;

    public ReportingTreeNode(ReportingTree<V> tree, ReportingTreeNode<V> parent, String levelValue) {
        this.tree = tree;
        this.parent = parent;
        this.depth = parent == null ? 0 : parent.depth() + 1;
        this.levelDescriptor = tree.leafLevel(depth) ? null : tree.descriptorFor(depth);
        this.levelValue = levelValue;
    }

    protected ReportingTreeNode(ReportingTreeNode<V> patternToClone) {
        this.tree = patternToClone.tree;
        this.parent = patternToClone.parent;
        this.levelDescriptor = patternToClone.levelDescriptor;
        this.depth = patternToClone.depth;
        this.levelValue = patternToClone.levelValue;
        this.leafValue = patternToClone.leafValue;
    }

    protected ReportingTreeNode(ReportingTreeNode<V> patternToClone, ReportingTreeNode<V> newParent) {
        this.tree = patternToClone.tree;
        this.parent = patternToClone.root() ? null : newParent;
        this.levelDescriptor = patternToClone.levelDescriptor;
        this.depth = patternToClone.depth;
        this.levelValue = patternToClone.levelValue;
        this.leafValue = patternToClone.leafValue;
    }

    public abstract ReportingTreeNode<V> child(String levelValue);

    public abstract ReportingTreeNode<V> childStar();

    public abstract void insertPath(ReportingTreePath<V> path);

    public abstract ReportingTreeNode<V> cloneBranch(ReportingTreeNode<V> newParent);

    public abstract ReportingTreeNode<V> allowAnyValues();

    protected abstract boolean dictionaryOnly();

    protected ReportingTreeLevelDescriptor levelDescriptor() {
        return levelDescriptor;
    }

    protected int treeHeight() {
        return tree.height();
    }

    protected boolean leaf() {
        return depth >= treeHeight();
    }

    protected boolean root() {
        return parent == null;
    }

    protected ReportingTree<V> tree() {
        return tree;
    }

    protected int depth() {
        return depth;
    }

    protected String levelValue() {
        return levelValue;
    }

    protected void chooseLeafValue(V incomingValue) {
        this.leafValue = tree().valueChooser().choose(tree.outputValueDescriptor(), leafValue, incomingValue);
    }

    protected Object decodeLevelValue(String levelValue) {
        return tree.descriptorFor(depth).decode(levelValue);
    }

    protected String encodeLevelValue(Object levelValue) {
        return tree.descriptorFor(depth).encode(levelValue);
    }

    public ReportingTreeNode<V> parent() {
        return parent;
    }

    public void updateLevelValue(String newValue) {
        this.levelValue = newValue;
    }

    public void harvestLeavesValues(List<ReportingTreePath<V>> leafBucket) {
        if (leaf()) {
            if (leafValue != null) {
                // unoptimized go up&down algorithm, should collect all during one
                // descend
                ReportingTreePath<V> path = new ReportingTreePath<V>(leafValue);
                path.addSegment(levelValue);

                ReportingTreeNode<V> ascendParent = parent;
                while (ascendParent != null) {
                    if (ascendParent.parent != null) {
                        path.pushSegment(ascendParent.levelValue);
                    }
                    ascendParent = ascendParent.parent();
                }

                leafBucket.add(path);
            }
        } else {
            for (ReportingTreeNode<V> child : matchingChildren()) {
                child.harvestLeavesValues(leafBucket);
            }
        }
    }

    protected abstract Iterable<ReportingTreeNode<V>> allChildren();

    protected abstract Iterable<ReportingTreeNode<V>> matchingChildren();

    @Override
    public String toString() {
        return "[ReportingTreeNode depth: " + depth + " path: " + levelPath() + " dictionary: " + dictionaryOnly() + " value: " + leafValue + "]";
    }

    public void printNode(StringBuilder sb) {
        String indent = Printer.repeat(' ', depth << 2);
        boolean leaf = leaf();

        sb.append(indent).append("path : ").append(levelPath())
                .append("  dictionary: ").append(dictionaryOnly());
        if (leaf) {
            sb.append("   (leaf=").append(leafValue).append(')');
        }
        sb.append(Formatter.NL);

        for (ReportingTreeNode<V> child : allChildren()) {
            child.printNode(sb);
        }
    }

    public String levelPath() {
        String lv = levelValue != null ? levelValue : "";
        return parent != null ? parent.levelPath() + "/" + lv : lv;
    }
}
