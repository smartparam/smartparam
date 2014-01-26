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
public class ReportingTreeNodeTest {

    private static final Logger logger = LoggerFactory.getLogger(ReportingTreeNodeTest.class);

    @Test
    public void shouldCopyWholeSubtreeIncludingClonedNodeWhenUsingCopyConstructor() {
        // given
        ReportingTree<String> tree = reportingTree().withOnlyExactLevels(2).build();
        tree.root().addDictionaryLevel("A")
                .addDictionaryLevel("A-A").parent()
                .addDictionaryLevel("A-B").parent()
                .addAnyLevel();
        tree.insertValue(new String[]{"A", "A-A"}, "VALUE_A");
        tree.insertValue(new String[]{"A", "A-B"}, "VALUE_B");
        tree.insertValue(new String[]{"A", "*"}, "VALUE_ANY");

        // when
        logger.debug(tree.printTree());
        ReportingTreeNode<String> clone = new ReportingTreeNode<String>(tree.root());
        printNode(clone);

        // then
        List<String> values = new ArrayList<String>();
        clone.harvestLeavesValues(values);
        assertThat(values).containsOnly("VALUE_A", "VALUE_B", "VALUE_ANY");
    }

    private void printNode(ReportingTreeNode<?> node) {
        StringBuilder builder = new StringBuilder(100);
        node.printNode(builder, 0);
        logger.debug(builder.toString());
    }
}
