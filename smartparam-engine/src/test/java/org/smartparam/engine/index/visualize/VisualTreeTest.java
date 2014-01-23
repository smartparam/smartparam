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
package org.smartparam.engine.index.visualize;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static com.googlecode.catchexception.CatchException.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author Adam Dubiel
 */
public class VisualTreeTest {

    private static final Logger logger = LoggerFactory.getLogger(VisualTreeTest.class);

    @Test
    public void shouldInsertValueWithDictionaryPathValuesToDictionaryOnlyTree() {
        // given
        VisualTree<String> tree = new VisualTree<String>();
        VisualTreeNode<String> firstLevel = tree.root().addDictionaryLevel("FIRST_LEVEL");
        for (String secondLevel : Arrays.asList("SECOND_LEVEL_A", "SECOND_LEVEL_B", "SECOND_LEVEL_C")) {
            firstLevel.addDictionaryLevel(secondLevel).addDictionaryLevel("THIRD_LEVEL_X");
        }

        // when
        tree.insertValue(new String[]{"FIRST_LEVEL", "SECOND_LEVEL_B", "THIRD_LEVEL_X"}, "VALUE");
        logger.debug(tree.printTree());

        // then
        assertThat(tree.harvestLeavesValues()).hasSize(1).containsExactly("VALUE");
    }

    @Test
    public void shouldThrowExceptionWhenTryingToAddValueOutOfDictionaryToDictionaryOnlyLevel() {
        // given
        VisualTree<String> tree = new VisualTree<String>();
        tree.root().addDictionaryLevel("FIRST_LEVEL");

        // when
        catchException(tree).insertValue(new String[]{"INVALID_VALUE"}, "VALUE");
        logger.debug(tree.printTree());

        // then
        assertThat(caughtException()).isInstanceOf(ParameterValueDoesNotMatchDictionary.class);
    }

    @Test
    public void shouldInsertValueWithAnyPathValuesIntoFreeFormTree() {
        // given
        VisualTree<String> tree = new VisualTree<String>();
        tree.root().addAnyLevel().addAnyLevel();

        // when
        tree.insertValue(new String[]{"FIRST_LEVEL", "SECOND_LEVEL"}, "VALUE");
        logger.debug(tree.printTree());

        // then
        assertThat(tree.harvestLeavesValues()).hasSize(1).containsExactly("VALUE");
    }

    @Test
    public void shouldInsertValueWithPathIntoMixedTree() {
        // given
        VisualTree<String> tree = new VisualTree<String>();
        tree.root().addAnyLevel();
        tree.root().addDictionaryLevel("FIRST_LEVEL_A")
                .addDictionaryLevel("SECOND_LEVEL_A");
        tree.root().addDictionaryLevel("FIRST_LEVEL_B")
                .addAnyLevel();

        // when
        tree.insertValue(new String[]{"FIRST_LEVEL_A", "SECOND_LEVEL_A"}, "VALUE");
        logger.debug(tree.printTree());

        // then
        assertThat(tree.harvestLeavesValues()).hasSize(1).containsExactly("VALUE");
    }

    @Test
    public void shouldInsertMultiplePathsIntoOneTree() {
        // given
        VisualTree<String> tree = new VisualTree<String>();
        tree.root()
                .addDictionaryLevel("A")
                .addDictionaryLevel("A-A")
                .addDictionaryLevel("A-A-A")
                .addAnyLevel().parent()
                .parent()
                .addDictionaryLevel("A-A-B")
                .addAnyLevel().parent()
                .parent()
                .parent()
                .addDictionaryLevel("A-B")
                .addDictionaryLevel("A-B-A")
                .addAnyLevel();

        // when
        tree.insertValue(new String[]{"A", "A-A", "A-A-A", "C"}, "VALUE_1");
        tree.insertValue(new String[]{"A", "A-A", "A-A-A", "*"}, "VALUE_2");
        tree.insertValue(new String[]{"A", "A-B", "A-B-A", "*"}, "VALUE_3");
        logger.debug(tree.printTree());

        // then
        assertThat(tree.harvestLeavesValues()).hasSize(3).containsOnly("VALUE_1", "VALUE_2", "VALUE_3");
    }

    @Test
    public void should() {
    // given

    // when
    // then
    }
}
