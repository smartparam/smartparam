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

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author Adam Dubiel
 */
public class ContinuousSpaceSegmentTest {

    @Test
    public void shouldReturnTrueIfSegmentContainsPoint() {
        // given
        ContinuousSpaceSegment<Integer, String> segment = new ContinuousSpaceSegment<Integer, String>(1, 10, "A");

        // when
        boolean contains = segment.contains(5);

        // then
        assertThat(contains).isTrue();
    }

    @Test
    public void shouldReturnFalseIfSegmentDoesNotContainPoint() {
        // given
        ContinuousSpaceSegment<Integer, String> segment = new ContinuousSpaceSegment<Integer, String>(1, 10, "A");

        // when
        boolean contains = segment.contains(20);

        // then
        assertThat(contains).isFalse();
    }

    @Test
    public void shouldReturnNONEIntersectionTypeWhenNoIntersectionPresent() {
        // given
        ContinuousSpaceSegment<Integer, String> segment = new ContinuousSpaceSegment<Integer, String>(1, 10, "A");

        // when
        ContinuousSpaceSegment.IntersectionType intersection = segment.intersects(20, 30);

        // then
        assertThat(intersection).isSameAs(ContinuousSpaceSegment.IntersectionType.NONE);
    }

    @Test
    public void shouldReturnIDENTICALIntersectionTypeWhenSegmentsHaveSameStartAndEndPoints() {
        // given
        ContinuousSpaceSegment<Integer, String> segment = new ContinuousSpaceSegment<Integer, String>(1, 10, "A");

        // when
        ContinuousSpaceSegment.IntersectionType intersection = segment.intersects(1, 10);

        // then
        assertThat(intersection).isSameAs(ContinuousSpaceSegment.IntersectionType.IDENTICAL);
    }

    @Test
    public void shouldReturnBEFOREIntersectionTypeWhenSegmentsIntersectAndOtherSegmentStartsBeforeSegment() {
        // given
        ContinuousSpaceSegment<Integer, String> segment = new ContinuousSpaceSegment<Integer, String>(10, 20, "A");

        // when
        ContinuousSpaceSegment.IntersectionType intersection = segment.intersects(5, 15);

        // then
        assertThat(intersection).isSameAs(ContinuousSpaceSegment.IntersectionType.BEFORE);
    }

    @Test
    public void shouldReturnAFTERIntersectionTypeWhenSegmentsIntersectAndOtherSegmentStartsAfterSegment() {
        // given
        ContinuousSpaceSegment<Integer, String> segment = new ContinuousSpaceSegment<Integer, String>(10, 20, "A");

        // when
        ContinuousSpaceSegment.IntersectionType intersection = segment.intersects(15, 25);

        // then
        assertThat(intersection).isSameAs(ContinuousSpaceSegment.IntersectionType.AFTER);
    }

    @Test
    public void shouldReturnCONTAINSIntersectionTypeWhenOtherSegmentIsContainedWithingSegment() {
        // given
        ContinuousSpaceSegment<Integer, String> segment = new ContinuousSpaceSegment<Integer, String>(10, 20, "A");

        // when
        ContinuousSpaceSegment.IntersectionType intersection = segment.intersects(15, 18);

        // then
        assertThat(intersection).isSameAs(ContinuousSpaceSegment.IntersectionType.CONTAINS);
    }

    @Test
    public void shouldReturnCONTAINEDIntersectionTypeWhenSegmentIsContainedWithingOtherSegment() {
        // given
        ContinuousSpaceSegment<Integer, String> segment = new ContinuousSpaceSegment<Integer, String>(10, 20, "A");

        // when
        ContinuousSpaceSegment.IntersectionType intersection = segment.intersects(0, 100);

        // then
        assertThat(intersection).isSameAs(ContinuousSpaceSegment.IntersectionType.CONTAINED);
    }

    @Test
    public void shouldReturnMinusOneWhenOtherSegmentIsLesserThanSegment() {
        // given
        ContinuousSpaceSegment<Integer, String> segment = new ContinuousSpaceSegment<Integer, String>(10, 20, "A");
        ContinuousSpaceSegment<Integer, String> other = new ContinuousSpaceSegment<Integer, String>(15, 25, "A");

        // when
        int result = segment.compareTo(other);

        // then
        assertThat(result).isEqualTo(-1);
    }

    @Test
    public void shouldReturnZeroWhenOtherSegmentIsSameAsSegment() {
        // given
        ContinuousSpaceSegment<Integer, String> segment = new ContinuousSpaceSegment<Integer, String>(10, 20, "A");
        ContinuousSpaceSegment<Integer, String> other = new ContinuousSpaceSegment<Integer, String>(10, 20, "A");

        // when
        int result = segment.compareTo(other);

        // then
        assertThat(result).isEqualTo(0);
    }

    @Test
    public void shouldReturnPlusOneWhenOtherSegmentIsGreaterThanSegment() {
        // given
        ContinuousSpaceSegment<Integer, String> segment = new ContinuousSpaceSegment<Integer, String>(10, 20, "A");
        ContinuousSpaceSegment<Integer, String> other = new ContinuousSpaceSegment<Integer, String>(5, 15, "A");

        // when
        int result = segment.compareTo(other);

        // then
        assertThat(result).isEqualTo(1);
    }
}
