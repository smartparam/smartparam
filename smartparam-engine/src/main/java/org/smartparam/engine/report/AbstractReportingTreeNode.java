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
import java.util.List;
import org.smartparam.engine.util.Formatter;
import org.smartparam.engine.util.Printer;

/**
 *
 * @author Adam Dubiel
 */
public abstract class AbstractReportingTreeNode<V> implements ReportingTreeNode<V> {

    private final ReportingTree<V> tree;

    private final ReportingTreeNode<V> parent;

    private final ReportingTreeLevel levelDescriptor;

    protected final int depth;

    protected final String levelValue;

    protected V leafValue;

    public AbstractReportingTreeNode(ReportingTree<V> tree, ReportingTreeNode<V> parent, String levelValue) {
        this.tree = tree;
        this.parent = parent;
        this.depth = parent == null ? 0 : parent.depth() + 1;
        this.levelDescriptor = tree.descriptorFor(depth);
        this.levelValue = levelValue;
    }

    protected AbstractReportingTreeNode(AbstractReportingTreeNode<V> patternToClone) {
        this.tree = patternToClone.tree;
        this.parent = patternToClone.parent;
        this.levelDescriptor = patternToClone.levelDescriptor;
        this.depth = patternToClone.depth;
        this.levelValue = patternToClone.levelValue;
        this.leafValue = patternToClone.leafValue;
    }

    protected ReportingTreeLevel levelDescriptor() {
        return levelDescriptor;
    }

    @Override
    public ReportingTree<V> tree() {
        return tree;
    }

    @Override
    public int depth() {
        return depth;
    }

    @Override
    public ReportingTreeNode<V> parent() {
        return parent;
    }

    @Override
    public void harvestLeavesValues(List<V> leafBucket) {
        if (leaf()) {
            if (leafValue != null) {
                leafBucket.add(leafValue);
            }
        } else {
            for (ReportingTreeNode<V> child : children()) {
                child.harvestLeavesValues(leafBucket);
            }
        }
    }

    protected abstract Collection<ReportingTreeNode<V>> children();

    @Override
    public void printNode(StringBuilder sb) {
        String indent = Printer.repeat(' ', depth << 2);
        boolean leaf = leaf();

        sb.append(indent).append("path : ").append(levelPath());
        if (leaf) {
            sb.append("   (leaf=").append(leafValue).append(')');
        }
        sb.append(Formatter.NL);

        for (ReportingTreeNode<V> child : children()) {
            child.printNode(sb);
        }
    }

    @Override
    public String levelPath() {
        String lv = levelValue != null ? levelValue : "";
        return parent != null ? parent.levelPath() + "/" + lv : lv;
    }

}
