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
