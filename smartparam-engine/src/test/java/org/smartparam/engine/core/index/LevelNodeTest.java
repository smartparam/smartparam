/*
 * Copyright 2013 Adam Dubiel, Przemek Hertel.
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
package org.smartparam.engine.core.index;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.smartparam.engine.test.assertions.Assertions.*;
import static org.smartparam.engine.test.builder.LevelIndexTestBuilder.levelIndex;

/**
 * @author Przemek Hertel
 */
public class LevelNodeTest {

    @Test
    public void shouldAddNewNodeValueToLeavesWhenAddingToLastNodeInIndexTree() {
        // given
        LevelIndex<Integer> levelIndex = levelIndex().withLevelCount(0).build();
        LevelNode<Integer> node = new LevelNode<Integer>(levelIndex);

        // when
        int currentLevelNumber = 0;
        node.add(new String[]{}, 10, currentLevelNumber);

        // then
        assertThat(node).hasLeaves(1).leavesEqualTo(10);
    }

    @Test
    public void shouldAddNewNodeToChildrenListWhenCurrentNodeIsNotLastInIndexTree() {
        // given
        LevelIndex<Integer> levelindex = levelIndex().withLevelCount(1).build();
        LevelNode<Integer> root = new LevelNode<Integer>(levelindex);

        // when
        int currentLevelNumber = 0;
        root.add(new String[]{"A"}, 10, currentLevelNumber);

        // then
        assertThat(root).hasNoLeaves().hasDirectChild("A");
    }

    @Test
    public void shouldFavourConcreteValuesOverDefaultWhenLookingForValue() {
        // given
        LevelIndex<Integer> levelindex = levelIndex().withLevelCount(1).build();
        LevelNode<Integer> root = new LevelNode<Integer>(levelindex);
        root.add(new String[]{"*"}, 11, 0);
        root.add(new String[]{"A"}, 42, 0);

        // when
        LevelNode<Integer> node = root.findNode(new String[]{"A"}, 0);

        // then
        assertThat(node).leavesEqualTo(42);
    }

    @Test
    public void shouldFallBackToDefaultValueIfNoneOtherFound() {
        // given
        LevelIndex<Integer> levelindex = levelIndex().withLevelCount(1).build();
        LevelNode<Integer> root = new LevelNode<Integer>(levelindex);
        root.add(new String[]{"*"}, 42, 0);
        root.add(new String[]{"A"}, 11, 0);

        // when
        LevelNode<Integer> node = root.findNode(new String[]{"B"}, 0);

        // then
        assertThat(node).leavesEqualTo(42);
    }

    @Test
    public void shouldReturnNullIfNothingFound() {
        // given
        LevelIndex<Integer> levelindex = levelIndex().withLevelCount(1).build();
        LevelNode<Integer> root = new LevelNode<Integer>(levelindex);
        root.add(new String[]{"A"}, 10, 0);

        // when
        LevelNode<Integer> node = root.findNode(new String[]{"B"}, 0);

        // then
        assertThat(node).isNull();
    }

    @DataProvider(name = "findNodeSearchSet")
    public Object[][] provideFindNodeSearchSets() {
        return new Object[][]{
            {new String[]{"A", "B", "C"}, 1},
            {new String[]{"A", "B", "X"}, 9},
            {new String[]{"A", "E", "D"}, 11},
            {new String[]{"A", "X", "D"}, 12},
            {new String[]{"A", "X", "X"}, 13},
            {new String[]{"V", "Z", "Z"}, 21},
            {new String[]{"V", "Z", "A"}, 22},
            {new String[]{"V", "V", "V"}, 99}
        };
    }

    @Test(dataProvider = "findNodeSearchSet")
    public void shouldFindNodeFromTestSet(String[] levelValues, int expectedValue) {
        // given
        LevelIndex<Integer> levelindex = levelIndex().withLevelCount(3).build();
        LevelNode<Integer> root = new LevelNode<Integer>(levelindex);

        root.add(new String[]{"A", "B", "C"}, 1, 0);
        root.add(new String[]{"A", "B", "*"}, 9, 0);
        root.add(new String[]{"A", "E", "D"}, 11, 0);
        root.add(new String[]{"A", "*", "D"}, 12, 0);
        root.add(new String[]{"A", "*", "*"}, 13, 0);
        root.add(new String[]{"*", "Z", "Z"}, 21, 0);
        root.add(new String[]{"*", "Z", "*"}, 22, 0);
        root.add(new String[]{"*", "*", "*"}, 99, 0);

        // when
        LevelNode<Integer> node = root.findNode(levelValues, 0);

        // then
        assertThat(node).leavesEqualTo(expectedValue);
    }
}
