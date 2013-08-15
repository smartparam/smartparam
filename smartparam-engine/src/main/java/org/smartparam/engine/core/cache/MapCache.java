package org.smartparam.engine.core.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @param <T> cached object type
 *
 * @author Adam Dubiel
 */
public class MapCache<T> {

    private Map<String, T> cache = new ConcurrentHashMap<String, T>();

    public void put(String key, T object) {
        cache.put(key, object);
    }

    public void putAll(Map<String, T> otherMap) {
        cache.putAll(otherMap);
    }

    public T get(String key) {
        return cache.get(key);
    }

    public void invalidate(String key) {
        cache.remove(key);
    }

    public void invalidate() {
        cache.clear();
    }
}
