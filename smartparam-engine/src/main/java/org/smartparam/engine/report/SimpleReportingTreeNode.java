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
package org.smartparam.engine.report;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Adam Dubiel
 */
public class SimpleReportingTreeNode<V> extends ReportingTreeNode<V> {

    private final Map<String, ReportingTreeNode<V>> children = new HashMap<String, ReportingTreeNode<V>>();

    private boolean dictionaryOnlyLevel = true;

    public SimpleReportingTreeNode(ReportingTree<V> tree, ReportingTreeNode<V> parent, String levelValue) {
        super(tree, parent, levelValue);
    }

    private SimpleReportingTreeNode(ReportingTreeNode<V> patternToClone, ReportingTreeNode<V> newParent) {
        super(patternToClone, newParent);
    }

    @Override
    protected void allowAnyValues(boolean state) {
        this.dictionaryOnlyLevel = !state;
    }

    @Override
    public ReportingTreeNode<V> addDictionaryChild(String levelValue) {
        return addChild(levelValue, false);
    }

    @Override
    public ReportingTreeNode<V> addAnyChild() {
        return addChild("*", true);
    }

    private ReportingTreeNode<V> addChild(String levelValue, boolean forceAllowAnyValues) {
        ReportingTreeNode<V> child = tree().createNode(this, levelValue);
        children.put(levelValue, child);

        if ("*".equals(levelValue)) {
            dictionaryOnlyLevel = false;
        }
        child.allowAnyValues(forceAllowAnyValues);

        return child;
    }

    @Override
    public void insertPath(ReportingTreePath<V> path) {
        if (leaf()) {
            this.leafValue = chooseValue(leafValue, path.value());
            return;
        }

        String valueToInsert = path.segmentAt(depth);
        if ("*".equals(valueToInsert) && dictionaryOnlyLevel) {
            insertPathToAllChildren(path);
        } else {
            ReportingTreeNode<V> childNode = children.get(valueToInsert);
            if (childNode == null && !dictionaryOnlyLevel) {
                childNode = addChild(valueToInsert, true);
            }
            if (childNode != null) {
                childNode.insertPath(path);
            }
        }
    }

    private void insertPathToAllChildren(ReportingTreePath<V> path) {
        for (ReportingTreeNode<V> child : children.values()) {
            child.insertPath(path);
        }
    }

    @Override
    public ReportingTreeNode<V> cloneBranch(ReportingTreeNode<V> newParent) {
        SimpleReportingTreeNode<V> offspringRoot = new SimpleReportingTreeNode<V>(this, newParent);
        for (Entry<String, ReportingTreeNode<V>> entry : children.entrySet()) {
            offspringRoot.children.put(entry.getKey(), entry.getValue().cloneBranch(offspringRoot));
        }

        return offspringRoot;
    }

    @Override
    protected Collection<ReportingTreeNode<V>> allChildren() {
        return children.values();
    }

    @Override
    protected Iterable<ReportingTreeNode<V>> matchingChildren() {
        return allChildren();
    }
}
