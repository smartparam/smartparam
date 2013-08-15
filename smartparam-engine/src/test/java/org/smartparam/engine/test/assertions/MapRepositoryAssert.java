/*
 * Copyright 2013 Adam Dubiel, Przemek Hertel.
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
package org.smartparam.engine.test.assertions;

import org.fest.assertions.api.AbstractAssert;
import org.smartparam.engine.core.MapRepository;

/**
 *
 * @author Adam Dubiel
 */
public class MapRepositoryAssert extends AbstractAssert<MapRepositoryAssert, MapRepository<?>> {

    public MapRepositoryAssert(MapRepository<?> actual) {
        super(actual, MapRepositoryAssert.class);
    }

    public static MapRepositoryAssert assertThat(MapRepository<?> actual) {
        return new MapRepositoryAssert(actual);
    }

    public MapRepositoryAssert contains(String key) {
        Assertions.assertThat(actual.getItem(key)).isNotNull();
        return this;
    }
}
