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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Adam Dubiel
 */
public class AmbiguousReportingTreeNode<V> extends AbstractReportingTreeNode<V> {

    private final Map<Object, ReportingTreeNode<V>> children = new TreeMap<Object, ReportingTreeNode<V>>();

    public AmbiguousReportingTreeNode(ReportingTree<V> tree, ReportingTreeNode<V> parent, String levelValue) {
        super(tree, parent, levelValue);
    }

    private AmbiguousReportingTreeNode(AmbiguousReportingTreeNode<V> patternToClone) {
        super(patternToClone);
    }

    @Override
    protected Collection<ReportingTreeNode<V>> children() {
        return children.values();
    }

    @Override
    public ReportingTreeNode<V> addChild(String levelValue) {
        ReportingTreeNode<V> child = tree().createNode(this, levelValue);
        children.put(levelValue, child);

        return child;
    }

    @Override
    public void insertPath(ReportingTreePath<V> path) {
        Object incomingKey = levelDescriptor().decode(path.segmentAt(depth()));
        disjointPut(incomingKey, path);
    }

    private void disjointPut(Object incomingKey, ReportingTreePath<V> path) {
        Map<Object, ReportingTreeNode<V>> nodesAfterPut = new HashMap<Object, ReportingTreeNode<V>>();

        boolean putViaIntersection = false;

        Iterator<Object> existingKeys = children.keySet().iterator();
        while (existingKeys.hasNext()) {
            Object existingKey = existingKeys.next();
            ReportingTreeNode<V> branch = children.get(existingKey);

            DisjointSets<Object> disjoint = levelDescriptor().split(existingKey, incomingKey);
            if (disjoint.intersectionExists()) {
                putViaIntersection = true;

                plantCloneBranches(children, disjoint.setAParts(), branch);
                plantNewBranches(children, disjoint.setBParts(), path);
                plantMergedBranch(children, existingKey, branch, path);
            } else {
                nodesAfterPut.put(existingKey, children.get(existingKey));
            }
        }

        if (!putViaIntersection) {
            plantNewBranch(children, incomingKey, path);
        }

        children.clear();
        children.putAll(nodesAfterPut);
    }

    private void plantCloneBranches(Map<Object, ReportingTreeNode<V>> target, List<Object> keys, ReportingTreeNode<V> value) {
        for (Object key : keys) {
            target.put(key, value.cloneBranch());
        }
    }

    private void plantNewBranches(Map<Object, ReportingTreeNode<V>> target, List<Object> keys, ReportingTreePath<V> withPath) {
        for (Object key : keys) {
            plantNewBranch(target, key, withPath);
        }
    }

    private void plantNewBranch(Map<Object, ReportingTreeNode<V>> target, Object key, ReportingTreePath<V> withPath) {
        ReportingTreeNode<V> offspring = tree().createNode(this, levelValue);
        offspring.insertPath(withPath);
        target.put(key, offspring);
    }

    private void plantMergedBranch(Map<Object, ReportingTreeNode<V>> target, Object key, ReportingTreeNode<V> value, ReportingTreePath<V> pathToAdd) {
        target.put(key, value);
        value.insertPath(pathToAdd);
    }

    @Override
    public ReportingTreeNode<V> cloneBranch() {
        AmbiguousReportingTreeNode<V> offspringRoot = new AmbiguousReportingTreeNode<V>(this);
        for (Map.Entry<Object, ReportingTreeNode<V>> entry : children.entrySet()) {
            offspringRoot.children.put(entry.getKey(), entry.getValue().cloneBranch());
        }

        return offspringRoot;
    }

    @Override
    public boolean leaf() {
        return children.isEmpty();
    }
}
