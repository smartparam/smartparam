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
package org.smartparam.engine.report.tree;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.matchers.MatchAllMatcher;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.smartparam.engine.report.tree.ReportingTreeBuilder.reportingTree;

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
        tree.root()
                .addDictionaryChild("SECOND_LEVEL")
                .addDictionaryChild("THIRD_LEVEL")
                .addDictionaryChild("FOURTH_LEVEL");

        // when
        tree.insertValue(new String[]{"SECOND_LEVEL", "THIRD_LEVEL", "FOURTH_LEVEL"}, "VALUE");
        logger.debug(tree.printTree());

        // then
        assertThat(tree.harvestRawLeavesValues()).hasSize(1).containsExactly("VALUE");
    }

    @Test
    public void shouldSilentlyIgnoreWhenTryingToAddValueOutOfDictionaryToDictionaryOnlyLevel() {
        // given
        ReportingTree<String> tree = reportingTree().withOnlyExactLevels(2).build();
        tree.root().addDictionaryChild("SECOND_LEVEL");

        // when
        tree.insertValue(new String[]{"INVALID_VALUE"}, "VALUE");
        logger.debug(tree.printTree());

        // then
        assertThat(tree.harvestRawLeavesValues()).isEmpty();
    }

    @Test
    public void shouldInsertValueWithAnyPathValuesIntoFreeFormTree() {
        // given
        ReportingTree<String> tree = reportingTree().withOnlyExactLevels(2).build();
        tree.root()
                .addAnyChild()
                .addAnyChild();

        // when
        tree.insertValue(new String[]{"SOMETHING", "ANYTHING"}, "VALUE");
        logger.debug(tree.printTree());

        // then
        assertThat(tree.harvestRawLeavesValues()).hasSize(1).containsExactly("VALUE");
    }

    @Test
    public void shouldInsertPathIntoMixedTree() {
        // given
        ReportingTree<String> tree = reportingTree().withOnlyExactLevels(2).build();
        tree.root()
                .addDictionaryChild("SECOND_LEVEL")
                .addAnyChild();

        // when
        tree.insertValue(new String[]{"SECOND_LEVEL", "ANYTHING"}, "VALUE");
        logger.debug(tree.printTree());

        // then
        assertThat(tree.harvestRawLeavesValues()).hasSize(1).containsExactly("VALUE");
    }

    @Test
    public void shouldInsertMultiplePathsIntoOneMixedTree() {
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
        assertThat(tree.harvestRawLeavesValues()).hasSize(3).containsOnly("VALUE_1", "VALUE_2", "VALUE_3");
    }

    @Test
    public void shouldSpreadDefaultPathAmongAllLevelChildren() {
        // given
        ReportingTree<String> tree = reportingTree().withOnlyExactLevels(2).build();
        tree.root()
                .addDictionaryChild("A")
                .addDictionaryChild("A-A").parent()
                .addDictionaryChild("A-B").parent()
                .addDictionaryChild("A-C");

        // when
        tree.insertValue(new String[]{"A", "*"}, "VIRAL");
        logger.debug(tree.printTree());

        // then
        assertThat(tree.harvestRawLeavesValues()).hasSize(3).containsOnly("VIRAL");
    }

    @Test
    public void shouldSpreadDefaultPathOnlyOnSameLevelAndUseConcreteMatchingLower() {
        // given
        ReportingTree<String> tree = reportingTree().withOnlyExactLevels(3).build();
        tree.root()
                .addDictionaryChild("A")
                .addDictionaryChild("A-A")
                .addDictionaryChild("A-A-A").parent().parent()
                .addDictionaryChild("A-B")
                .addDictionaryChild("A-B-A");

        // when
        tree.insertValue(new String[]{"A", "*", "A-A-A"}, "CONTAINED_VIRAL");
        logger.debug(tree.printTree());

        // then
        assertThat(tree.harvestRawLeavesValues()).hasSize(1).containsOnly("CONTAINED_VIRAL");
    }

    @Test
    public void shouldInsertAnyPathIntoLevelAcceptingDefaults() {
        // given
        ReportingTree<String> tree = reportingTree().withOnlyExactLevels(2).build();
        tree.root()
                .addAnyChild()
                .addDictionaryChild("A-A");

        // when
        tree.insertValue(new String[]{"B", "A-A"}, "ANYTHING");
        logger.debug(tree.printTree());

        // then
        assertThat(tree.harvestRawLeavesValues()).hasSize(1).containsOnly("ANYTHING");
    }

    @Test
    public void shouldCreateTreeWithAmbiguousLevelAtTheBottom() {
        // given
        ReportingTree<String> tree = reportingTree()
                .addExactLevel()
                .addExactLevel()
                .addAmbiguousIntegerLevel("4", new MatchAllMatcher())
                .build();
        tree.root().addDictionaryChild("A")
                .addDictionaryChild("A-A")
                .addAnyChild();

        // when
        tree.insertValue(new String[]{"A", "A-A", "0~10"}, "VALUE: 0-10");
        tree.insertValue(new String[]{"A", "A-A", "2~5"}, "VALUE: 2-5");
        logger.debug(tree.printTree());

        // then
        assertThat(tree.harvestRawLeavesValues()).hasSize(3).containsOnly("VALUE: 0-10");
    }

    @Test
    public void shouldCreateTreeWithAmbiguousLevelInTheMiddleAndNeverReplaceOnceSetValue() {
        // given
        ReportingTree<String> tree = reportingTree()
                .addExactLevel()
                .addAmbiguousIntegerLevel("4", new MatchAllMatcher())
                .addExactLevel()
                .build();
        tree.root().addDictionaryChild("A")
                .addAnyChild()
                .addDictionaryChild("A-A");

        // when
        tree.insertValue(new String[]{"A", "0~10", "A-A"}, "VALUE: A-A");
        tree.insertValue(new String[]{"A", "2~5", "A-B"}, "VALUE: A-B");
        logger.debug(tree.printTree());

        // then
        assertThat(tree.harvestRawLeavesValues()).hasSize(3).containsOnly("VALUE: A-A");
    }
}
