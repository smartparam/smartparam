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
package org.smartparam.engine.core.cache;

import org.testng.annotations.Test;

import static org.fest.assertions.api.Assertions.*;

/**
 * @author Przemek Hertel
 */
public class MapCacheTest {

    @Test
    public void shouldReturnNullIfCacheMiss() {
        // given
        MapCache<Object> cache = new MapCache<Object>();

        // when
        Object object = cache.get("TEST_KEY");

        // then
        assertThat(object).isNull();
    }

    @Test
    public void shouldReturnStoredObjectOnCacheHit() {
        // given
        MapCache<Object> cache = new MapCache<Object>();
        Object object = new Object();
        cache.put("TEST_KEY", object);

        // when
        Object retrievedObject = cache.get("TEST_KEY");

        // then
        assertThat(retrievedObject).isSameAs(object);
    }

    @Test
    public void shouldDeleteOnlyOneItemWhenInvalidatingSingleEntry() {
        // given
        MapCache<Object> cache = new MapCache<Object>();
        cache.put("TEST_KEY", new Object());
        cache.put("INVALID_KEY", new Object());

        // when
        cache.invalidate("INVALID_KEY");

        // then
        assertThat(cache.get("INVALID_KEY")).isNull();
        assertThat(cache.get("TEST_KEY")).isNotNull();
    }

    @Test
    public void shouldClearCacheOnInvalidation() {
        // given
        MapCache<Object> cache = new MapCache<Object>();
        cache.put("TEST_KEY", new Object());

        // when
        cache.invalidate();

        // then
        assertThat(cache.get("TEST_KEY")).isNull();
    }
}
