/*
 * Copyright 2014 Adam Dubiel.
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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Adam Dubiel
 */
public class ReportingTree<V> {

    private static final int PRINT_DEFAULT_SIZE = 200;

    private final ReportingTreeNode<V> root;

    private final List<ReportingTreeLevel> levelDescriptors;

    public ReportingTree(List<ReportingTreeLevel> levelDescriptors) {
        this.levelDescriptors = levelDescriptors;
        root = createNode(null, "ROOT");
    }

    public ReportingTreeNode<V> root() {
        return root;
    }

    public void insertValue(String[] levelValues, V value) {
        root.insertPath(new ReportingTreePath<V>(levelValues, value));
    }

    public List<V> harvestLeavesValues() {
        List<V> crops = new ArrayList<V>();
        root.harvestLeavesValues(crops);
        return crops;
    }

    final ReportingTreeNode<V> createNode(ReportingTreeNode<V> parent, String levelValue) {
        int depth = parent == null ? 0 : parent.depth() + 1;
        return descriptorFor(depth).ambiguous()
                ? new AmbiguousReportingTreeNode<V>(this, parent, levelValue)
                : new SimpleReportingTreeNode<V>(this, parent, levelValue);
    }

    ReportingTreeLevel descriptorFor(int levelIndex) {
        return levelDescriptors.get(levelIndex);
    }

    public String printTree() {
        StringBuilder builder = new StringBuilder(PRINT_DEFAULT_SIZE);
        builder.append("VisualTree ");
        root.printNode(builder);
        return builder.toString();
    }

}
