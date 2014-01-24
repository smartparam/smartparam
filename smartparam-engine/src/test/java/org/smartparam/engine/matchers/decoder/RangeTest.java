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

import java.util.List;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author Adam Dubiel
 */
public class RangeTest {

    @Test
    public void shouldAlwaysProduceSortedRange() {
        // when
        Range<Integer> range = new Range<Integer>(100, -100);

        // then
        assertThat(range.from()).isEqualTo(-100);
        assertThat(range.to()).isEqualTo(100);
    }

    @Test
    public void shouldReturnTrueIfValueInsideRange() {
        // given
        Range<Integer> range = new Range<Integer>(5, 10);

        // when
        boolean contains = range.contains(6);

        // then
        assertThat(contains).isTrue();
    }

    @Test
    public void shouldReturnFalseIfValueOutsideRange() {
        // given
        Range<Integer> range = new Range<Integer>(5, 10);

        // when
        boolean contains = range.contains(11);

        // then
        assertThat(contains).isFalse();
    }

    @Test
    public void shouldReturnTrueIfTwoRangesAreDisjoint() {
        // given
        Range<Integer> rangeA = new Range<Integer>(5, 10);
        Range<Integer> rangeB = new Range<Integer>(10, 12);

        // when
        boolean disjoint = rangeA.disjoint(rangeB);

        // then
        assertThat(disjoint).isTrue();
    }

    @Test
    public void shouldReturnFalseIfTwoRangesAreNotDisjoint() {
        // given
        Range<Integer> rangeA = new Range<Integer>(5, 10);
        Range<Integer> rangeB = new Range<Integer>(9, 12);

        // when
        boolean disjoint = rangeA.disjoint(rangeB);

        // then
        assertThat(disjoint).isFalse();
    }

    @Test
    public void shouldReturnTrueIfOtherRangeIsFullyContainedInRange() {
        // given
        Range<Integer> rangeA = new Range<Integer>(0, 10);
        Range<Integer> rangeB = new Range<Integer>(5, 10);

        // when
        boolean contains = rangeA.contains(rangeB);

        // then
        assertThat(contains).isTrue();
    }

    @Test
    public void shouldReturnFalseIfOtherRangeIsNotFullyContainedInRange() {
        // given
        Range<Integer> rangeA = new Range<Integer>(0, 10);
        Range<Integer> rangeB = new Range<Integer>(5, 11);

        // when
        boolean contains = rangeA.contains(rangeB);

        // then
        assertThat(contains).isFalse();
    }

    @Test
    public void shouldReturnIntersectionOfTwoRangesWhenOtherIsHigherOnScale() {
        // given
        Range<Integer> rangeA = new Range<Integer>(0, 10);
        Range<Integer> rangeB = new Range<Integer>(5, 15);

        // when
        Range<Integer> intersection = rangeA.intersection(rangeB);

        // then
        assertThat(intersection.from()).isEqualTo(5);
        assertThat(intersection.to()).isEqualTo(10);
    }

    @Test
    public void shouldReturnIntersectionOfTwoRangesWhenOtherIsLowerOnScale() {
        // given
        Range<Integer> rangeA = new Range<Integer>(0, 10);
        Range<Integer> rangeB = new Range<Integer>(-5, 5);

        // when
        Range<Integer> intersection = rangeA.intersection(rangeB);

        // then
        assertThat(intersection.from()).isEqualTo(0);
        assertThat(intersection.to()).isEqualTo(5);
    }

    @Test
    public void shouldReturnNullWhenIntersectingRangesAreDisjoint() {
        // given
        Range<Integer> rangeA = new Range<Integer>(5, 10);
        Range<Integer> rangeB = new Range<Integer>(0, 2);

        // when
        Range<Integer> intersection = rangeA.intersection(rangeB);

        // then
        assertThat(intersection).isNull();
    }

    @Test
    public void shouldReturnIntersectionOfTwoRangesWhenOtherIsContainedInThis() {
        // given
        Range<Integer> rangeA = new Range<Integer>(0, 20);
        Range<Integer> rangeB = new Range<Integer>(5, 10);

        // when
        Range<Integer> intersection = rangeA.intersection(rangeB);

        // then
        assertThat(intersection.from()).isEqualTo(5);
        assertThat(intersection.to()).isEqualTo(10);
    }

    @Test
    public void shouldReturnIntersectionOfTwoRangesWhenThisIsContainedInOther() {
        // given
        Range<Integer> rangeA = new Range<Integer>(0, 20);
        Range<Integer> rangeB = new Range<Integer>(-100, 100);

        // when
        Range<Integer> intersection = rangeA.intersection(rangeB);

        // then
        assertThat(intersection.from()).isEqualTo(0);
        assertThat(intersection.to()).isEqualTo(20);
    }

    @Test
    public void shouldReturnOneRangeAsDifferenceBetweenThisAndOtherWhenRangesIntersectAndThisIsLesser() {
        // given
        Range<Integer> rangeA = new Range<Integer>(0, 10);
        Range<Integer> rangeB = new Range<Integer>(5, 15);

        // when
        List<Range<Integer>> difference = rangeA.subtract(rangeB);

        // then
        assertThat(difference).hasSize(1);
        assertThat(difference.get(0).from()).isEqualTo(0);
        assertThat(difference.get(0).to()).isEqualTo(5);
    }

    @Test
    public void shouldReturnOneRangeAsDifferenceBetweenThisAndOtherWhenRangesIntersectAndThisIsGreater() {
        // given
        Range<Integer> rangeA = new Range<Integer>(5, 15);
        Range<Integer> rangeB = new Range<Integer>(0, 10);

        // when
        List<Range<Integer>> difference = rangeA.subtract(rangeB);

        // then
        assertThat(difference).hasSize(1);
        assertThat(difference.get(0).from()).isEqualTo(10);
        assertThat(difference.get(0).to()).isEqualTo(15);
    }

    @Test
    public void shouldReturnTwoRangesAsSubtractionResultWhenThisContainsOther() {
        // given
        Range<Integer> rangeA = new Range<Integer>(0, 15);
        Range<Integer> rangeB = new Range<Integer>(5, 10);

        // when
        List<Range<Integer>> difference = rangeA.subtract(rangeB);

        // then
        assertThat(difference).hasSize(2);
        assertThat(difference.get(0).from()).isEqualTo(0);
        assertThat(difference.get(0).to()).isEqualTo(5);
        assertThat(difference.get(1).from()).isEqualTo(10);
        assertThat(difference.get(1).to()).isEqualTo(15);
    }

    @Test
    public void shouldReturnEmptyListAsSubtractionResultWhenOtherContainsThis() {
        // given
        Range<Integer> rangeA = new Range<Integer>(5, 10);
        Range<Integer> rangeB = new Range<Integer>(15, 5);

        // when
        List<Range<Integer>> difference = rangeA.subtract(rangeB);

        // then
        assertThat(difference).isEmpty();
    }

    @Test
    public void shouldReturnEmptyListWhenTryingTOSubtractDisjointRanges() {
        // given
        Range<Integer> rangeA = new Range<Integer>(5, 10);
        Range<Integer> rangeB = new Range<Integer>(20, 25);

        // when
        List<Range<Integer>> difference = rangeA.subtract(rangeB);

        // then
        assertThat(difference).isEmpty();
    }
}
