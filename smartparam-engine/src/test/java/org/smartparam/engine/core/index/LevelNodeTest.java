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
package org.smartparam.engine.core.index;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.smartparam.engine.test.ParamEngineAssertions.*;
import static org.smartparam.engine.core.index.LevelIndexTestBuilder.levelIndex;

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
}
