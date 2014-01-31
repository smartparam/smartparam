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
package org.smartparam.engine.matchers.type;

import org.smartparam.engine.matchers.type.RangeBoundary;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author Adam Dubiel
 */
public class RangeBoundaryTest {

    @Test
    public void shouldCreateBoundaryWithFiniteValueWhenNonnullValuePassed() {
        // when
        RangeBoundary<Integer> boundary = new RangeBoundary<Integer>(10);

        // then
        assertThat(boundary.value()).isEqualTo(10);
        assertThat(boundary.finite()).isTrue();
    }

    @Test
    public void shouldCreatePlusInfinitiBoundaryWhenNullValuePassed() {
        // when
        RangeBoundary<Integer> boundary = new RangeBoundary<Integer>(null);

        // then
        assertThat(boundary.isPlusInfinity()).isTrue();
    }

    @Test
    public void shouldCompareTwoPlusInfinitiesAsEqual() {
        // given
        RangeBoundary<Integer> boundaryA = new RangeBoundary<Integer>(null);
        RangeBoundary<Integer> boundaryB = new RangeBoundary<Integer>(null);

        // then
        assertThat(boundaryA.compareTo(boundaryB)).isEqualTo(0);
    }

    @Test
    public void shouldCompareTwoMinusInfinitiesAsEqual() {
        // given
        RangeBoundary<Integer> boundaryA = RangeBoundary.minusInfinity();
        RangeBoundary<Integer> boundaryB = RangeBoundary.minusInfinity();

        // then
        assertThat(boundaryA.compareTo(boundaryB)).isEqualTo(0);
    }

    @Test
    public void shouldCompareMinusInfinityAsLowerThanPlusInfinity() {
        // given
        RangeBoundary<Integer> boundaryA = RangeBoundary.minusInfinity();
        RangeBoundary<Integer> boundaryB = RangeBoundary.plusInfinity();

        // then
        assertThat(boundaryA.compareTo(boundaryB)).isLessThan(0);
    }

    @Test
    public void shouldComparePlusInfinityAsHigherThanMinusInfinity() {
        // given
        RangeBoundary<Integer> boundaryA = RangeBoundary.plusInfinity();
        RangeBoundary<Integer> boundaryB = RangeBoundary.minusInfinity();

        // then
        assertThat(boundaryA.compareTo(boundaryB)).isGreaterThan(0);
    }

    @Test
    public void shouldComparePlusInfinityAsHigherThanConcreteValue() {
        // given
        RangeBoundary<Integer> boundaryA = RangeBoundary.plusInfinity();
        RangeBoundary<Integer> boundaryB = new RangeBoundary<Integer>(5);

        // then
        assertThat(boundaryA.compareTo(boundaryB)).isGreaterThan(0);
    }

    @Test
    public void shouldCompareMinusInfinityAsLowerThanConcreteValue() {
        // given
        RangeBoundary<Integer> boundaryA = RangeBoundary.minusInfinity();
        RangeBoundary<Integer> boundaryB = new RangeBoundary<Integer>(5);

        // then
        assertThat(boundaryA.compareTo(boundaryB)).isLessThan(0);
    }

    @Test
    public void shouldCompareTwoConcreteValuesUsingTheirComparator() {
        // given
        RangeBoundary<Integer> boundaryA = new RangeBoundary<Integer>(10);
        RangeBoundary<Integer> boundaryB = new RangeBoundary<Integer>(5);

        // then
        assertThat(boundaryA.compareTo(boundaryB)).isGreaterThan(0);
    }
}
