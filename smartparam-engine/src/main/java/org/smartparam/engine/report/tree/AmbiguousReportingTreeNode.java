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

import java.util.ArrayList;
import java.util.List;
import org.smartparam.engine.core.index.Star;

/**
 *
 * @author Adam Dubiel
 */
public class AmbiguousReportingTreeNode<V> extends ReportingTreeNode<V> {

    private final ReportLevelValuesSpace<V> space;

    public AmbiguousReportingTreeNode(ReportingTree<V> tree, ReportingTreeNode<V> parent, String levelValue) {
        super(tree, parent, levelValue);
        space = levelDescriptor().createSpace();
    }

    private AmbiguousReportingTreeNode(AmbiguousReportingTreeNode<V> patternToClone, ReportingTreeNode<V> newParent, ReportLevelValuesSpace<V> spaceToClone) {
        super(patternToClone, newParent);
        space = spaceToClone.cloneSpace(newParent);
    }

    @Override
    public AmbiguousReportingTreeNode<V> allowAnyValues() {
        return this;
    }

    @Override
    protected boolean dictionaryOnly() {
        return false;
    }

    @Override
    protected Iterable<ReportingTreeNode<V>> allChildren() {
        return space.values();
    }

    @Override
    protected Iterable<ReportingTreeNode<V>> matchingChildren() {
        List<ReportingTreeNode<V>> matchingNodes = new ArrayList<ReportingTreeNode<V>>();
        for (ReportingTreeNode<V> node : allChildren()) {
            if (levelDescriptor().matches(node.levelValue())) {
                matchingNodes.add(node);
            }
        }
        return matchingNodes;
    }

    @Override
    public ReportingTreeNode<V> child(String levelValue) {
        ReportingTreeNode<V> child = tree().createNode(this, levelValue);
        space.uncheckedPut(decodeLevelValue(levelValue), child);

        return child;
    }

    @Override
    public ReportingTreeNode<V> childStar() {
        return child(Star.SYMBOL).allowAnyValues();
    }

    @Override
    public void insertPath(ReportingTreePath<V> path) {
        if (leaf()) {
            chooseLeafValue(path.value());
            return;
        }

        String incomingKeyString = path.segmentAt(depth());
        Object incomingKey = levelDescriptor().decode(incomingKeyString);
        boolean added = space.insertPath(incomingKey, path, levelDescriptor());
        if (!added) {
            plantNewBranch(incomingKey, incomingKeyString, path);
        }
    }

    private void plantNewBranch(Object key, String incomingKeyString, ReportingTreePath<V> withPath) {
        ReportingTreeNode<V> offspring = tree().createNode(this, incomingKeyString);
        offspring.insertPath(withPath);
        space.uncheckedPut(key, offspring);
    }

    @Override
    public ReportingTreeNode<V> cloneBranch(ReportingTreeNode<V> newParent) {
        return new AmbiguousReportingTreeNode<V>(this, newParent, this.space);
    }
}
