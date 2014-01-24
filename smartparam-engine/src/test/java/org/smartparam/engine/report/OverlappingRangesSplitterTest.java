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
package org.smartparam.engine.report;

import org.smartparam.engine.matchers.decoder.Range;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author Adam Dubiel
 */
public class OverlappingRangesSplitterTest {

    private final OverlappingRangesSplitter<Integer> disjoiner = new OverlappingRangesSplitter<Integer>();

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnUnmodifiedRangesForTwoDisjointInputs() {
        // when
        DisjointSets<Range<Integer>> sets = disjoiner.split(new Range<Integer>(0, 5), new Range<Integer>(10, 15));

        // then
        assertThat(sets.setAParts()).containsOnly(new Range<Integer>(0, 5));
        assertThat(sets.setBParts()).containsOnly(new Range<Integer>(10, 15));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnIntersectingRangeAndTwoRangesEverythingDisjointWhenRangesIntersect() {
        // when
        DisjointSets<Range<Integer>> sets = disjoiner.split(new Range<Integer>(10, 15), new Range<Integer>(0, 12));

        // then
        assertThat(sets.setAParts()).containsOnly(new Range<Integer>(12, 15));
        assertThat(sets.setBParts()).containsOnly(new Range<Integer>(0, 10));
        assertThat(sets.intersection()).isEqualTo(new Range<Integer>(10, 12));
    }

    @Test
    public void shouldReturnTwoRangesForASetAndIntersectionWhenBIsContainedInA() {
        // when
        DisjointSets<Range<Integer>> sets = disjoiner.split(new Range<Integer>(0, null), new Range<Integer>(5, 10));

        // then
        assertThat(sets.setAParts()).containsOnly(new Range<Integer>(0, 5), new Range<Integer>(10, null));
        assertThat(sets.setBParts()).isEmpty();
        assertThat(sets.intersection()).isEqualTo(new Range<Integer>(5, 10));
    }
}
