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
public class ReportingTree<T> {

    private static final int PRINT_DEFAULT_SIZE = 200;

    private final ReportingTreeNode<T> root = new ReportingTreeNode<T>(this, null, null);

    private final List<ReportingTreeLevelOperations> operations;

    public ReportingTree(List<ReportingTreeLevelOperations> operations) {
        this.operations = operations;
    }

    public ReportingTreeNode<T> root() {
        return root;
    }

    public void insertValue(String[] levelValues, T value) {
        root.insertPath(levelValues, 0, value);
    }

    public List<T> harvestLeavesValues() {
        List<T> crops = new ArrayList<T>();
        root.harvestLeavesValues(crops);
        return crops;
    }

    boolean isPotentiallyAmbiguous(int levelIndex) {
        return operations.get(levelIndex).ambiguous();
    }

    ReportingTreeLevelOperations operationsFor(int levelIndex) {
        return operations.get(levelIndex);
    }

    public String printTree() {
        StringBuilder builder = new StringBuilder(PRINT_DEFAULT_SIZE);
        builder.append("VisualTree ");
        root.printNode(builder, 0);
        return builder.toString();
    }

}
