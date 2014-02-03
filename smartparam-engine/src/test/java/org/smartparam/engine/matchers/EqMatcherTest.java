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
package org.smartparam.engine.matchers;

import org.testng.annotations.Test;
import org.smartparam.engine.types.string.StringType;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Przemek Hertel
 */
public class EqMatcherTest {

    @Test
    public void shouldMatchValueWithPatternWhenEqual() {
        // given
        StringMatcher matcher = new StringMatcher(true);

        // when
        boolean equals = matcher.matches("ABC", "ABC", new StringType());

        // then
        assertThat(equals).isTrue();
    }

    @Test
    public void shouldNotMatchValueWithPatternWhenCaseDoesNotMatch() {
        // given
        StringMatcher matcher = new StringMatcher(true);

        // when
        boolean equals = matcher.matches("abc", "ABC", new StringType());

        // then
        assertThat(equals).isFalse();
    }

    @Test
    public void shouldMatchValueWhenCaseDoesNotMatchButIsCaseInsensitive() {
        // given
        StringMatcher matcher = new StringMatcher(false);

        // when
        boolean equals = matcher.matches("abc", "ABC", new StringType());

        // then
        assertThat(equals).isTrue();
    }

    @Test
    public void shouldNotMatchValueWhenValueIsNull() {
        // given
        StringMatcher matcher = new StringMatcher(false);

        // when
        boolean equals = matcher.matches(null, "ABC", new StringType());

        // then
        assertThat(equals).isFalse();
    }
}
