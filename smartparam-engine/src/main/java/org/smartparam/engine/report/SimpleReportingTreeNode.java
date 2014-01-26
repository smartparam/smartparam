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
public class SimpleReportingTreeNode<V> extends AbstractReportingTreeNode<V> {

    private final Map<String, ReportingTreeNode<V>> children = new HashMap<String, ReportingTreeNode<V>>();

    private boolean dictionaryOnlyLevel = true;

    public SimpleReportingTreeNode(ReportingTree<V> tree, ReportingTreeNode<V> parent, String levelValue) {
        super(tree, parent, levelValue);
    }

    private SimpleReportingTreeNode(SimpleReportingTreeNode<V> patternToClone) {
        super(patternToClone);
    }

    @Override
    public ReportingTreeNode<V> addChild(String levelValue) {
        ReportingTreeNode<V> child = tree().createNode(this, levelValue);
        children.put(levelValue, child);

        if ("*".equals(levelValue)) {
            dictionaryOnlyLevel = false;
        }

        return child;
    }

    @Override
    public void insertPath(ReportingTreePath<V> path) {
        if (leaf()) {
            if(leafValue == null) {
                this.leafValue = path.value();
            }
            return;
        }

        String valueToInsert = path.segmentAt(depth);
        if ("*".equals(valueToInsert)) {
            insertPathToAllChildren(path);
        } else {
            ReportingTreeNode<V> childNode = children.get(valueToInsert);
            if (childNode == null && !dictionaryOnlyLevel) {
                childNode = addChild(valueToInsert);
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
    public boolean leaf() {
        return children.isEmpty();
    }

    @Override
    public ReportingTreeNode<V> cloneBranch() {
        SimpleReportingTreeNode<V> offspringRoot = new SimpleReportingTreeNode<V>(this);
        for (Entry<String, ReportingTreeNode<V>> entry : children.entrySet()) {
            offspringRoot.children.put(entry.getKey(), entry.getValue().cloneBranch());
        }

        return offspringRoot;
    }

    @Override
    protected Collection<ReportingTreeNode<V>> children() {
        return children.values();
    }
}
