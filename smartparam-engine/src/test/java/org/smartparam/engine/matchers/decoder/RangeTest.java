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
}
