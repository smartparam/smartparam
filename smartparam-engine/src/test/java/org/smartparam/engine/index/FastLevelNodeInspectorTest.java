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
package org.smartparam.engine.index;

import java.util.List;
import org.smartparam.engine.core.index.LevelIndex;
import org.testng.annotations.Test;
import static org.smartparam.engine.core.index.LevelIndexTestBuilder.levelIndex;
import static org.smartparam.engine.test.ParamEngineAssertions.assertThat;

/**
 *
 * @author Adam Dubiel
 */
public class FastLevelNodeInspectorTest {

    @Test
    public void shouldInspectOnlyChildWithBestMatch() {
        // given
        LevelIndex<String> index = levelIndex().withLevelCount(1).build();
        index.add(new String[]{"*"}, "default");
        index.add(new String[]{"A"}, "value");
        index.add(new String[]{"C"}, "noise");

        CustomizableLevelIndexWalker<String> crawler = new CustomizableLevelIndexWalker<String>(
                new IndexTraversalOverrides(new boolean[]{false}), index, "A");

        // when
        List<String> values = crawler.find();

        // then
        assertThat(values).containsOnly("value");
    }

    @Test
    public void shouldFallBackToDefaultWhenNoBestMatchFound() {
        // given
        LevelIndex<String> index = levelIndex().withLevelCount(1).build();
        index.add(new String[]{"*"}, "default");
        index.add(new String[]{"C"}, "noise");

        CustomizableLevelIndexWalker<String> crawler = new CustomizableLevelIndexWalker<String>(
                new IndexTraversalOverrides(new boolean[]{false}), index, "A");

        // when
        List<String> values = crawler.find();

        // then
        assertThat(values).containsOnly("default");
    }

    @Test
    public void shouldReturnEmptyArrayWhenNoMatchFoundAtLevel() {
        // given
        LevelIndex<String> index = levelIndex().withLevelCount(1).build();
        index.add(new String[]{"C"}, "noise");

        CustomizableLevelIndexWalker<String> crawler = new CustomizableLevelIndexWalker<String>(
                new IndexTraversalOverrides(new boolean[]{false}), index, "A");

        // when
        List<String> values = crawler.find();

        // then
        assertThat(values).isEmpty();
    }
}
