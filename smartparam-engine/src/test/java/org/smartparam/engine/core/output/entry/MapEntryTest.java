/*
 * Copyright 2014 Adam Dubiel, Przemek Hertel.
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
package org.smartparam.engine.core.output.entry;

import org.smartparam.engine.core.parameter.entry.ParameterEntryKey;
import org.smartparam.engine.core.parameter.entry.TestParameterEntryKey;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author Adam Dubiel
 */
public class MapEntryTest {

    @Test
    public void shouldKeepKeyInSpecialMapEntry() {
        // given
        MapEntry map = new MapEntry(new TestParameterEntryKey("key"));

        // when
        ParameterEntryKey key = map.get(MapEntry.KEY);

        // then
        assertThat(key.value()).isEqualTo("key");
    }

    @Test
    public void shouldMergeTwoMapsIntoOneWithHostOverridingOtherMapsProperties() {
        // given
        MapEntry host = new MapEntry().put("hostLevel", "hostValue");
        MapEntry other = new MapEntry().put("hostLevel", "rougeValue")
                .put("otherLevel", "otherValue");

        // when
        MapEntry merged = host.merge(other);

        // then
        assertThat(merged).isNotSameAs(host).isNotSameAs(other);
        assertThat(merged.get("hostLevel")).isEqualTo("hostValue");
        assertThat(merged.get("otherLevel")).isEqualTo("otherValue");
    }
}
