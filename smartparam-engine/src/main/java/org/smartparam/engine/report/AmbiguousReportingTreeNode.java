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

import org.smartparam.engine.report.space.ReportLevelValuesSpace;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Adam Dubiel
 */
public class AmbiguousReportingTreeNode<V> extends ReportingTreeNode<V> {

    private final ReportLevelValuesSpace<V> space;

    private final Map<Object, ReportingTreeNode<V>> children = new TreeMap<Object, ReportingTreeNode<V>>();

    public AmbiguousReportingTreeNode(ReportingTree<V> tree, ReportingTreeNode<V> parent, String levelValue) {
        super(tree, parent, levelValue);
        space = tree().descriptorFor(depth).createSpace();
    }

    private AmbiguousReportingTreeNode(AmbiguousReportingTreeNode<V> patternToClone, ReportingTreeNode<V> newParent, ReportLevelValuesSpace<V> spaceToClone) {
        super(patternToClone, newParent);
        space = spaceToClone.cloneSpace(newParent);
    }

    @Override
    protected void allowAnyValues(boolean state) {
        // noop - non-dictionary only by default and can't be changed
    }

    @Override
    protected Iterable<ReportingTreeNode<V>> children() {
        return space.values();
    }

    @Override
    public ReportingTreeNode<V> addDictionaryChild(String levelValue) {
        ReportingTreeNode<V> child = tree().createNode(this, levelValue);
        space.unsafePut(decodeLevelValue(levelValue), child);
        children.put(levelValue, child);

        return child;
    }

    @Override
    public ReportingTreeNode<V> addAnyChild() {
        return addDictionaryChild("*");

    }

    @Override
    public void insertPath(ReportingTreePath<V> path) {
        if (leaf()) {
            if (leafValue == null) {
                this.leafValue = path.value();
            }
            return;
        }

        Object incomingKey = levelDescriptor().decode(path.segmentAt(depth()));
        boolean added = space.insertPath(incomingKey, path, levelDescriptor());
        if (!added) {
            plantNewBranch(incomingKey, path);
        }
    }

    private void plantNewBranch(Object key, ReportingTreePath<V> withPath) {
        ReportingTreeNode<V> offspring = tree().createNode(this, levelValue);
        offspring.insertPath(withPath);
        space.unsafePut(key, offspring);
    }

    @Override
    public ReportingTreeNode<V> cloneBranch(ReportingTreeNode<V> newParent) {
        AmbiguousReportingTreeNode<V> offspringRoot = new AmbiguousReportingTreeNode<V>(this, newParent, this.space);
        return offspringRoot;
    }
}
