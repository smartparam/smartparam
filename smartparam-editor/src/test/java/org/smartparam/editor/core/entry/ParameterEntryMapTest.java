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
package org.smartparam.editor.core.entry;

import org.smartparam.editor.core.entry.ParameterEntryMap;
import org.smartparam.editor.core.model.ParameterEntryKey;
import org.smartparam.editor.model.simple.SimpleParameterEntryKey;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author Adam Dubiel
 */
public class ParameterEntryMapTest {

    @Test
    public void shouldKeepKeyInSpecialMapEntry() {
        // given
        ParameterEntryMap map = new ParameterEntryMap(new SimpleParameterEntryKey("key"));
        
        // when
        ParameterEntryKey key = map.get(ParameterEntryMap.KEY);

        // then
        assertThat(key.value()).isEqualTo("key");
    }

    @Test
    public void shouldMergeTwoMapsIntoOneWithHostOverridingOtherMapsProperties() {
        // given
        ParameterEntryMap host = new ParameterEntryMap().put("hostLevel", "hostValue");
        ParameterEntryMap other = new ParameterEntryMap().put("hostLevel", "rougeValue")
                .put("otherLevel", "otherValue");

        // when
        ParameterEntryMap merged = host.merge(other);

        // then
        assertThat(merged).isNotSameAs(host).isNotSameAs(other);
        assertThat(merged.get("hostLevel")).isEqualTo("hostValue");
        assertThat(merged.get("otherLevel")).isEqualTo("otherValue");
    }
}
