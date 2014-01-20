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

import java.util.List;
import org.smartparam.engine.core.index.LevelIndex;
import org.smartparam.engine.core.matcher.Matcher;
import org.smartparam.engine.core.type.Type;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.smartparam.engine.core.index.LevelIndexTestBuilder.levelIndex;

/**
 *
 * @author Adam Dubiel
 */
public class FastLevelIndexWalkerIntegrationTest {

    @Test
    public void shouldFindBestMatchPathToLeafAndReturnLeafValueUsingDefaultMatchers() {
        // given

        LevelIndex<String> index = levelIndex().withLevelCount(3).build();
        index.add(new String[]{"A", "C", "Z"}, "noise");
        index.add(new String[]{"A", "B", "C"}, "bestMatch");
        index.add(new String[]{"A", "B", "Z"}, "noise");
        index.add(new String[]{"C", "B", "C"}, "noise");

        FastLevelIndexWalker<String> walker = new FastLevelIndexWalker<String>(index, "A", "B", "C");

        // when
        List<String> value = walker.find();

        // then
        assertThat(value).containsExactly("bestMatch");
    }

    @Test
    public void shouldUseDefaultStarValueWhenNoOtherMatchFoundAtTheSameLevelAndDefaultIsDefined() {
        // given
        LevelIndex<String> index = levelIndex().withLevelCount(3).build();
        index.add(new String[]{"A", "B", "*"}, "bestMatch");
        index.add(new String[]{"A", "B", "Z"}, "noise");

        FastLevelIndexWalker<String> walker = new FastLevelIndexWalker<String>(index, "A", "B", "C");

        // when
        List<String> value = walker.find();

        // then
        assertThat(value).containsExactly("bestMatch");
    }

    @Test
    public void shouldAlwaysFavorConcreteResultToDefaultWhenBothFoundAtSameLevel() {
        // given
        LevelIndex<String> index = levelIndex().withLevelCount(3).build();
        index.add(new String[]{"A", "B", "*"}, "default");
        index.add(new String[]{"A", "B", "C"}, "bestMatch");

        FastLevelIndexWalker<String> walker = new FastLevelIndexWalker<String>(index, "A", "B", "C");

        // when
        List<String> value = walker.find();

        // then
        assertThat(value).containsExactly("bestMatch");
    }

    @Test
    public void shouldStayWithDefaultPathWhenNothingElseMatchesQuery() {
        // given
        LevelIndex<String> index = levelIndex().withLevelCount(3).build();
        index.add(new String[]{"*", "*", "*"}, "defaultPath");
        index.add(new String[]{"A", "B", "C"}, "noise");

        FastLevelIndexWalker<String> walker = new FastLevelIndexWalker<String>(index, "Z", "B", "C");

        // when
        List<String> value = walker.find();

        // then
        assertThat(value).containsExactly("defaultPath");
    }

    @Test
    public void shouldReturnNullWhenNoMatchingPathFound() {
        // given
        LevelIndex<String> index = levelIndex().withLevelCount(3).build();
        index.add(new String[]{"A", "*", "*"}, "noise");
        index.add(new String[]{"A", "B", "C"}, "noise");

        FastLevelIndexWalker<String> walker = new FastLevelIndexWalker<String>(index, "Z", "B", "C");

        // when
        List<String> value = walker.find();

        // then
        assertThat(value).isNull();
    }

    @Test
    public void shouldReturnMultipleValuesWhenThereAreMultipleSameMatches() {
        // given
        LevelIndex<String> index = levelIndex().withLevelCount(3).build();
        index.add(new String[]{"A", "B", "C"}, "match1");
        index.add(new String[]{"A", "B", "C"}, "match2");
        index.add(new String[]{"A", "B", "*"}, "defaultMatch");

        FastLevelIndexWalker<String> walker = new FastLevelIndexWalker<String>(index, "A", "B", "C");

        // when
        List<String> value = walker.find();

        // then
        assertThat(value).containsOnly("match1", "match2");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldUseProvidedMatchersInsteadOfDefaultsToFindValue() {
        // given
        Matcher matcher1 = mock(Matcher.class);
        when(matcher1.matches(eq("A"), eq("valid-A"), any(Type.class))).thenReturn(true);

        Matcher matcher2 = mock(Matcher.class);
        when(matcher2.matches(eq("B"), eq("valid-B"), any(Type.class))).thenReturn(true);

        LevelIndex<String> index = levelIndex().withLevelCount(3).withMatchers(matcher1, matcher2).build();
        index.add(new String[]{"A", "invalid-B", "C"}, "noise");
        index.add(new String[]{"valid-A", "valid-B", "C"}, "match");
        index.add(new String[]{"valid-A", "valid-B", "*"}, "defaultMatch");

        FastLevelIndexWalker<String> walker = new FastLevelIndexWalker<String>(index, "A", "B", "C");

        // when
        List<String> value = walker.find();

        // then
        assertThat(value).containsExactly("match");
    }

}
