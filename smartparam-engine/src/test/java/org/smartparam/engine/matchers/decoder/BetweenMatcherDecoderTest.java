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
package org.smartparam.engine.matchers.decoder;

import org.smartparam.engine.matchers.BetweenMatcher;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author Adam Dubiel
 */
public class BetweenMatcherDecoderTest {

    private final BetweenMatcherDecoder decoder = new BetweenMatcherDecoder();

    @Test
    public void shouldDecodeValueAsRangeObjectUsingFirstSeparatorFromMatcherFoundInValue() {
        // given
        BetweenMatcher matcher = new BetweenMatcher(true, true, "~#");

        // when
        Range decoded = decoder.decode("A#B", null, matcher);

        // then
        assertThat(decoded.from()).isEqualTo("A");
        assertThat(decoded.to()).isEqualTo("B");
    }

    @Test
    public void shouldDecodeValueAsRangeWithBeginningOnlyWhenItsImpossibleToSplitValueUsingSeparators() {
        // given
        BetweenMatcher matcher = new BetweenMatcher(true, true, "~#");

        // when
        Range decoded = decoder.decode("A:B", null, matcher);

        // then
        assertThat(decoded.from()).isEqualTo("A:B");
        assertThat(decoded.to()).isEqualTo(null);
    }

    @Test
    public void shouldTrimAllWhitespacesFromRangeBeginningAndEndBeforePassingToTypeDecoding() {
        // given
        BetweenMatcher matcher = new BetweenMatcher(true, true, "~");

        // when
        Range decoded = decoder.decode("  A ~ B  ", null, matcher);

        // then
        assertThat(decoded.from()).isEqualTo("A");
        assertThat(decoded.to()).isEqualTo("B");
    }

    @Test
    public void shouldEncodeRangeAsEncodedValuesSeparatedByFirstSeparatorThatDoesNotExistInAnyEncodedValue() {
        // given
        BetweenMatcher matcher = new BetweenMatcher(true, true, "~#");

        // when
        String encoded = decoder.encode(new Range("A~", "B"), null, matcher);

        // then
        assertThat(encoded).isEqualTo("A~#B");
    }
}
