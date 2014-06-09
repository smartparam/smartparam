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
package org.smartparam.engine.core.output;

import java.math.BigDecimal;
import java.util.Date;
import org.smartparam.engine.core.type.ValueHolder;
import org.smartparam.engine.types.bool.BooleanHolder;
import org.smartparam.engine.types.date.DateHolder;
import org.smartparam.engine.types.integer.IntegerHolder;
import org.smartparam.engine.types.number.NumberHolder;
import org.smartparam.engine.types.string.StringHolder;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.smartparam.engine.core.output.RawParamValueBuilder.rawParamValue;

/**
 *
 * @author Adam Dubiel
 */
public class DefaultParamValueTest {

    @Test
    public void shouldReturnArrayOfValueHoldersFromFirstColumnOfFirstRow() {
        // given
        ValueHolder[] values = {new StringHolder("A"), new StringHolder("B")};
        ParamValue value = rawParamValue((Object) values);

        // when
        ValueHolder[] holder = value.getArray();

        // then
        assertThat(holder).hasSize(2).containsOnly(new StringHolder("A"), new StringHolder("B"));
    }

    @Test
    public void shouldReturnArrayOfStringsFromFirstColumnOfFirstRow() {
        // given
        ValueHolder[] values = {new StringHolder("A"), new StringHolder("B")};
        ParamValue value = rawParamValue((Object) values);

        // when
        String[] array = value.getStringArray();

        // then
        assertThat(array).hasSize(2).containsOnly("A", "B");
    }

    @Test
    public void shouldRaturnArrayOfBigDecimalsFromFirstColumnOfFirstRow() {
        // given
        ValueHolder[] values = {new NumberHolder(BigDecimal.ONE), new NumberHolder(BigDecimal.TEN)};
        ParamValue value = rawParamValue((Object) values);

        // when
        BigDecimal[] array = value.getBigDecimalArray();

        // then
        assertThat(array).hasSize(2).containsOnly(BigDecimal.ONE, BigDecimal.TEN);
    }

    @Test
    public void shouldRaturnArrayOfDatesFromFirstColumnOfFirstRow() {
        // given
        Date[] dates = {new Date(), new Date()};

        ValueHolder[] values = {new DateHolder(dates[0]), new DateHolder(dates[1])};
        ParamValue value = rawParamValue((Object) values);

        // when
        Date[] array = value.getDateArray();

        // then
        assertThat(array).hasSize(2).isEqualTo(dates);
    }


    @Test
    public void shouldRaturnArrayOfIntegersFromFirstColumnOfFirstRow() {
        // given
        ValueHolder[] values = {new IntegerHolder(1L), new IntegerHolder(2L)};
        ParamValue value = rawParamValue((Object) values);

        // when
        Integer[] array = value.getIntegerArray();

        // then
        assertThat(array).hasSize(2).containsOnly(1, 2);
    }

    @Test
    public void shouldRaturnArrayOfLongsFromFirstColumnOfFirstRow() {
        // given
        ValueHolder[] values = {new IntegerHolder(1L), new IntegerHolder(2L)};
        ParamValue value = rawParamValue((Object) values);

        // when
        Long[] array = value.getLongArray();

        // then
        assertThat(array).hasSize(2).containsOnly(1L, 2L);
    }

    @Test
    public void shouldRaturnArrayOfBooleansFromFirstColumnOfFirstRow() {
        // given
        ValueHolder[] values = {new BooleanHolder(false), new BooleanHolder(true)};
        ParamValue value = rawParamValue((Object) values);

        // when
        Boolean[] array = value.getBooleanArray();

        // then
        assertThat(array).hasSize(2).containsOnly(false, true);
    }
}
