package org.smartparam.engine.test.assertions;

import org.fest.assertions.api.AbstractAssert;
import org.smartparam.engine.core.MapRepository;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
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
