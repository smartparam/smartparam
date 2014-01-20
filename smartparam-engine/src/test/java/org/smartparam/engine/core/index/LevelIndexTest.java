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

import org.smartparam.engine.core.matcher.Matcher;
import org.smartparam.engine.core.type.Type;
import org.smartparam.engine.util.Formatter;
import org.testng.annotations.Test;
import static org.mockito.Mockito.*;
import static org.smartparam.engine.test.ParamEngineAssertions.assertThat;

/**
 *
 * @author Przemek Hertel
 */
public class LevelIndexTest {

    @Test
    public void shouldConstructIndexWithTypesAndMatchersForEachLevel() {
        // given
        Type<?>[] types = {mock(Type.class), mock(Type.class)};
        Matcher[] matchers = {mock(Matcher.class), mock(Matcher.class)};

        // when
        LevelIndex<String> index = new LevelIndex<String>(2, types, matchers);

        // then
        assertThat(index.getLevelCount()).isEqualTo(2);
        assertThat(index.getTypes()).isNotSameAs(types);
        assertThat(index.getTypes()).containsExactly(types);
        assertThat(index.getMatchers()).containsExactly(matchers);
    }

    @Test
    public void shouldCreateArraysOfNullMatchersAndTypesWithLevelCountLengthAndUseThemInsteadOfNullTypesOrMatchersArrays() {
        // when
        LevelIndex<String> index = new LevelIndex<String>(2, null, (Matcher[]) null);

        // then
        assertThat(index.getMatchers()).containsExactly(null, null);
        assertThat(index.getTypes()).containsExactly(null, null);
    }

    @Test
    public void shouldFillTypesAndMatchersArraysWithNullToAchieveLevelCountLengthWhenPassedArraysAreShorter() {
        // given
        Type<?>[] types = {mock(Type.class), mock(Type.class)};
        Matcher[] matchers = {mock(Matcher.class)};

        // when
        LevelIndex<String> index = new LevelIndex<String>(3, types, matchers);

        // then
        assertThat(index.getTypes()).containsExactly(types[0], types[1], null);
        assertThat(index.getMatchers()).containsExactly(matchers[0], null, null);
    }

    @Test
    public void shouldCreateLevelIndexWithNullTypesAndMatchersWhenUsingSimplifiedConstructor() {
        // when
        LevelIndex<String> index = new LevelIndex<String>(2);

        // then
        assertThat(index.getTypes()).containsExactly(null, null);
        assertThat(index.getMatchers()).containsExactly(null, null);
    }

    @Test
    public void shouldPrettyPrintWholeLevelIndex() {
        // given
        LevelIndex<Integer> index = new LevelIndex<Integer>(2);
        index.add(new String[]{"A", "X"}, 11);
        index.add(new String[]{"B", "X"}, 22);
        index.add(new String[]{"B", "*"}, 33);

        // when
        String result = index.printTree();

        // then
        String expectedPrefix = "path : " + Formatter.NL;
        String expectedForA = ""
                + "    path : /A" + Formatter.NL
                + "        path : /A/X   (leaf=[11])" + Formatter.NL;
        String expectedForB = ""
                + "    path : /B" + Formatter.NL
                + "        path : /B/X   (leaf=[22])" + Formatter.NL
                + "        path : /B/*   (leaf=[33])" + Formatter.NL;

        assertThat(result).startsWith(expectedPrefix);
        assertThat(result).contains(expectedForA, expectedForB);
    }
}
