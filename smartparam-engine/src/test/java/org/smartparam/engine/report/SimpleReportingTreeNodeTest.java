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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.smartparam.engine.report.ReportingTreeBuilder.reportingTree;

/**
 *
 * @author Adam Dubiel
 */
public class SimpleReportingTreeNodeTest {

    private static final Logger logger = LoggerFactory.getLogger(SimpleReportingTreeNodeTest.class);

    @Test
    public void shouldCopyWholeSubtreeIncludingClonedNodeWhenCopyingBranch() {
        // given
        ReportingTree<String> tree = reportingTree().withOnlyExactLevels(2).build();
        ReportingTreeNode<String> firstLevel = tree.root().addDictionaryChild("A");
        firstLevel.addDictionaryChild("A-A").parent()
                .addDictionaryChild("A-B").parent()
                .addDictionaryChild("*");
        tree.insertValue(new String[]{"A", "A-A"}, "VALUE_A");
        tree.insertValue(new String[]{"A", "A-B"}, "VALUE_B");
        tree.insertValue(new String[]{"A", "*"}, "VALUE_ANY");

        ReportingTreeNode<String> newParent = new SimpleReportingTreeNode<String>(tree, null, "NEW_ROOT_PARENT");

        // when
        logger.debug(tree.printTree());
        ReportingTreeNode<String> clone = firstLevel.cloneBranch(newParent);
        printNode(clone);

        // then
        assertThat(clone.parent()).isSameAs(newParent);
    }

    @Test
    public void shouldNotChangeParentOfClonedBranchWheNCloningRootNode() {
        // given
        ReportingTree<String> tree = reportingTree().withOnlyExactLevels(1).build();
        tree.root().addDictionaryChild("A");
        tree.insertValue(new String[]{"A"}, "VALUE_A");

        ReportingTreeNode<String> newParent = new SimpleReportingTreeNode<String>(tree, null, "NEW_PARENT");

        // when
        ReportingTreeNode<String> clone = tree.root().cloneBranch(newParent);

        // then
        assertThat(clone.parent()).isNull();
    }

    private void printNode(ReportingTreeNode<?> node) {
        StringBuilder builder = new StringBuilder(100);
        node.printNode(builder);
        logger.debug(builder.toString());
    }
}
