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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author Adam Dubiel
 */
public class RangeComparatorTest {

    @Test
    public void shouldCompareRangesInAscendingOrderUsingLowerBoundByDefault() {
        // given
        List<Range<Integer>> ranges = Arrays.asList(
                new Range<Integer>(1, 12),
                new Range<Integer>(10, 12),
                new Range<Integer>(5, 6)
        );

        // when
        Collections.sort(ranges, new RangeComparator());

        // then
        assertThat(ranges).containsExactly(new Range<Integer>(1, 12), new Range<Integer>(5, 6), new Range<Integer>(10, 12));
    }

    @Test
    public void shouldCompareRangesInDescendingOrderUsingLowerBound() {
        // given
        List<Range<Integer>> ranges = Arrays.asList(
                new Range<Integer>(1, 12),
                new Range<Integer>(10, 12),
                new Range<Integer>(5, 6)
        );

        // when
        Collections.sort(ranges, new RangeComparator(false));

        // then
        assertThat(ranges).containsExactly(new Range<Integer>(10, 12), new Range<Integer>(5, 6), new Range<Integer>(1, 12));
    }

    @Test
    public void shouldCompareRangesInAscendingOrderUsingUpperBound() {
        // given
        List<Range<Integer>> ranges = Arrays.asList(
                new Range<Integer>(1, 12),
                new Range<Integer>(10, 14),
                new Range<Integer>(5, 6)
        );

        // when
        Collections.sort(ranges, new RangeComparator(false, true));

        // then
        assertThat(ranges).containsExactly(new Range<Integer>(5, 6), new Range<Integer>(1, 12), new Range<Integer>(10, 14));
    }

    @Test
    public void shouldCompareRangesInDescendingOrderUsingUpperBound() {
        // given
        List<Range<Integer>> ranges = Arrays.asList(
                new Range<Integer>(1, 12),
                new Range<Integer>(10, 14),
                new Range<Integer>(5, 6)
        );

        // when
        Collections.sort(ranges, new RangeComparator(false, false));

        // then
        assertThat(ranges).containsExactly(new Range<Integer>(10, 14), new Range<Integer>(1, 12), new Range<Integer>(5, 6));
    }
}
