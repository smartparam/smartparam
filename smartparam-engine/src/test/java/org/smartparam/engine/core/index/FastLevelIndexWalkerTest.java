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

import java.util.List;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.smartparam.engine.test.ParamEngineAssertions.*;
import static org.smartparam.engine.core.index.LevelIndexTestBuilder.levelIndex;

/**
 * @author Przemek Hertel
 */
public class FastLevelIndexWalkerTest {

    @Test
    public void shouldFavourConcreteValuesOverDefaultWhenLookingForValue() {
        // given
        LevelIndex<Integer> levelIndex = levelIndex().withLevelCount(1).build();
        levelIndex.add(new String[]{"*"}, 11);
        levelIndex.add(new String[]{"A"}, 42);

        FastLevelIndexWalker<Integer> crawler = new FastLevelIndexWalker<Integer>(levelIndex, "A");

        // when
        List<Integer> node = crawler.find();

        // then
        assertThat(node).containsExactly(42);
    }

    @Test
    public void shouldFallBackToDefaultValueIfNoneOtherFound() {
        // given
        LevelIndex<Integer> levelIndex = levelIndex().withLevelCount(1).build();
        levelIndex.add(new String[]{"*"}, 42);
        levelIndex.add(new String[]{"A"}, 11);

        FastLevelIndexWalker<Integer> crawler = new FastLevelIndexWalker<Integer>(levelIndex, "B");

        // when
        List<Integer> node = crawler.find();

        // then
        assertThat(node).containsExactly(42);
    }

    @Test
    public void shouldReturnNullIfNothingFound() {
        // given
        LevelIndex<Integer> levelIndex = levelIndex().withLevelCount(1).build();
        levelIndex.add(new String[]{"A"}, 10);

        FastLevelIndexWalker<Integer> crawler = new FastLevelIndexWalker<Integer>(levelIndex, "B");

        // when
        List<Integer> node = crawler.find();

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
        LevelIndex<Integer> levelIndex = levelIndex().withLevelCount(3).build();

        levelIndex.add(new String[]{"A", "B", "C"}, 1);
        levelIndex.add(new String[]{"A", "B", "*"}, 9);
        levelIndex.add(new String[]{"A", "E", "D"}, 11);
        levelIndex.add(new String[]{"A", "*", "D"}, 12);
        levelIndex.add(new String[]{"A", "*", "*"}, 13);
        levelIndex.add(new String[]{"*", "Z", "Z"}, 21);
        levelIndex.add(new String[]{"*", "Z", "*"}, 22);
        levelIndex.add(new String[]{"*", "*", "*"}, 99);

        FastLevelIndexWalker<Integer> crawler = new FastLevelIndexWalker<Integer>(levelIndex, levelValues);

        // when
        List<Integer> node = crawler.find();

        // then
        assertThat(node).containsExactly(expectedValue);
    }
}
