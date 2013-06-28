package org.smartparam.engine.core.cache;

import org.junit.Test;

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
    public void shouldCleanCacheOnInvalidation() {
        // given
        MapCache<Object> cache = new MapCache<Object>();
        cache.put("TEST_KEY", new Object());

        // when
        cache.invalidate();

        // then
        assertThat(cache.get("TEST_KEY")).isNull();
    }
}
