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

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.smartparam.engine.report.ReportingTreeBuilder.reportingTree;

/**
 *
 * @author Adam Dubiel
 */
public class ReportingTreeTest {

    private static final Logger logger = LoggerFactory.getLogger(ReportingTreeTest.class);

    @Test
    public void shouldInsertValueWithDictionaryPathValuesToDictionaryOnlyTree() {
        // given
        ReportingTree<String> tree = reportingTree().withOnlyExactLevels(3).build();

        ReportingTreeNode<String> firstLevel = tree.root().addDictionaryChild("FIRST_LEVEL");
        for (String secondLevel : Arrays.asList("SECOND_LEVEL_A", "SECOND_LEVEL_B", "SECOND_LEVEL_C")) {
            firstLevel.addDictionaryChild(secondLevel).addDictionaryChild("THIRD_LEVEL_X");
        }

        // when
        tree.insertValue(new String[]{"FIRST_LEVEL", "SECOND_LEVEL_B", "THIRD_LEVEL_X"}, "VALUE");
        logger.debug(tree.printTree());

        // then
        assertThat(tree.harvestLeavesValues()).hasSize(1).containsExactly("VALUE");
    }

    @Test
    public void shouldSilentlyIngoreWhenTryingToAddValueOutOfDictionaryToDictionaryOnlyLevel() {
        // given
        ReportingTree<String> tree = reportingTree().withOnlyExactLevels(1).build();
        tree.root().addDictionaryChild("FIRST_LEVEL");

        // when
        tree.insertValue(new String[]{"INVALID_VALUE"}, "VALUE");
        logger.debug(tree.printTree());

        // then
        assertThat(tree.harvestLeavesValues()).isEmpty();
    }

    @Test
    public void shouldInsertValueWithAnyPathValuesIntoFreeFormTree() {
        // given
        ReportingTree<String> tree = reportingTree().withOnlyExactLevels(2).build();
        tree.root().addDictionaryChild("*").addDictionaryChild("*");

        // when
        tree.insertValue(new String[]{"FIRST_LEVEL", "SECOND_LEVEL"}, "VALUE");
        logger.debug(tree.printTree());

        // then
        assertThat(tree.harvestLeavesValues()).hasSize(1).containsExactly("VALUE");
    }

    @Test
    public void shouldInsertValueWithPathIntoMixedTree() {
        // given
        ReportingTree<String> tree = reportingTree().withOnlyExactLevels(2).build();
        tree.root().addDictionaryChild("*");
        tree.root().addDictionaryChild("FIRST_LEVEL_A")
                .addDictionaryChild("SECOND_LEVEL_A");
        tree.root().addDictionaryChild("FIRST_LEVEL_B")
                .addDictionaryChild("*");

        // when
        tree.insertValue(new String[]{"FIRST_LEVEL_A", "SECOND_LEVEL_A"}, "VALUE");
        logger.debug(tree.printTree());

        // then
        assertThat(tree.harvestLeavesValues()).hasSize(1).containsExactly("VALUE");
    }

    @Test
    public void shouldInsertMultiplePathsIntoOneTree() {
        // given
        ReportingTree<String> tree = reportingTree().withOnlyExactLevels(4).build();
        tree.root()
                .addDictionaryChild("A")
                .addDictionaryChild("A-A")
                .addDictionaryChild("A-A-A")
                .addDictionaryChild("*").parent()
                .parent()
                .addDictionaryChild("A-A-B")
                .addDictionaryChild("*").parent()
                .parent()
                .parent()
                .addDictionaryChild("A-B")
                .addDictionaryChild("A-B-A")
                .addDictionaryChild("*");

        // when
        tree.insertValue(new String[]{"A", "A-A", "A-A-A", "C"}, "VALUE_1");
        tree.insertValue(new String[]{"A", "A-A", "A-A-A", "*"}, "VALUE_2");
        tree.insertValue(new String[]{"A", "A-B", "A-B-A", "*"}, "VALUE_3");
        logger.debug(tree.printTree());

        // then
        assertThat(tree.harvestLeavesValues()).hasSize(3).containsOnly("VALUE_1", "VALUE_2", "VALUE_3");
    }

    @Test
    public void shouldSpreadDefaultPathAmongAllLevelChildren() {
        // given
        ReportingTree<String> tree = reportingTree().withOnlyExactLevels(2).build();
        tree.root().addDictionaryChild("A")
                .addDictionaryChild("A-A").parent()
                .addDictionaryChild("A-B").parent()
                .addDictionaryChild("A-C");

        // when
        tree.insertValue(new String[]{"A", "*"}, "VIRAL");
        logger.debug(tree.printTree());

        // then
        assertThat(tree.harvestLeavesValues()).hasSize(3).containsOnly("VIRAL");
    }

    @Test
    public void shouldSpreadDefaultPathOnlyOnSameLevelAndUseConcreteMatchingLower() {
        // given
        ReportingTree<String> tree = reportingTree().withOnlyExactLevels(3).build();
        tree.root().addDictionaryChild("A")
                .addDictionaryChild("A-A")
                .addDictionaryChild("A-A-A").parent().parent()
                .addDictionaryChild("A-B")
                .addDictionaryChild("A-B-A").parent().parent()
                .addDictionaryChild("A-C")
                .addDictionaryChild("A-C-A");

        // when
        tree.insertValue(new String[]{"A", "*", "A-A-A"}, "CONTAINED_VIRAL");
        logger.debug(tree.printTree());

        // then
        assertThat(tree.harvestLeavesValues()).hasSize(1).containsOnly("CONTAINED_VIRAL");
    }

    @Test
    public void shouldInsertAnyPathIntoLevelAcceptingDefaults() {
        // given
        ReportingTree<String> tree = reportingTree().withOnlyExactLevels(3).build();
        tree.root().addAnyChild()
                .addDictionaryChild("A-A")
                .addDictionaryChild("A-A-A").parent().parent()
                .addDictionaryChild("A-B")
                .addDictionaryChild("A-B-A").parent().parent()
                .addDictionaryChild("A-C")
                .addDictionaryChild("A-C-A").parent().parent();

        // when
        tree.insertValue(new String[]{"B", "A-A", "A-C-A"}, "ANYTHING");
        logger.debug(tree.printTree());

        // then
        assertThat(tree.harvestLeavesValues()).hasSize(1).containsOnly("ANYTHING");
    }

    @Test
    public void shouldCreateTreeWithAmbiguousLevelAtTheBottom() {
        // given
        ReportingTree<String> tree = reportingTree()
                .addExactLevel()
                .addAmbiguousIntegerLevel()
                .addExactLevel()
                .build();
        tree.root().addDictionaryChild("A")
                .addDictionaryChild("A-A")
                .addAnyChild();

        // when
        tree.insertValue(new String[]{"A", "A-A", "0~10"}, "VALUE: 0-10");
        tree.insertValue(new String[]{"A", "A-A", "2~5"}, "VALUE: 2-5");
        logger.info(tree.printTree());

        // then
        assertThat(tree.harvestLeavesValues()).hasSize(3).containsOnly("VALUE: 0-10");
    }

    @Test
    public void shouldCreateTreeWithAmbiguousLevelInTheMiddleAndNeverReplaceOnceSetValue() {
        // given
        ReportingTree<String> tree = reportingTree()
                .addAmbiguousIntegerLevel()
                .addExactLevel()
                .addExactLevel()
                .build();
        tree.root().addDictionaryChild("A")
                .addAnyChild()
                .addDictionaryChild("A-A");

        // when
        tree.insertValue(new String[]{"A", "0~10", "A-A"}, "VALUE: A-A");
        tree.insertValue(new String[]{"A", "2~5", "A-B"}, "VALUE: A-B");
        logger.info(tree.printTree());

        // then
        assertThat(tree.harvestLeavesValues()).hasSize(3).containsOnly("VALUE: A-A");
    }
}
