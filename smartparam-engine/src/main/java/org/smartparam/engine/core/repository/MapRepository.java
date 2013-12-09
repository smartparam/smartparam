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
package org.smartparam.engine.core.repository;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.annotated.RepositoryObjectKey;

/**
 *
 * @param <V>
 * @author Adam Dubiel
 */
public class MapRepository<V> {

    private static final Logger logger = LoggerFactory.getLogger(MapRepository.class);

    private final Class<?> containedClass;

    private final Map<RepositoryObjectKey, V> repositoryMap;

    public MapRepository(Class<?> containedClass) {
        this(containedClass, new HashMap<RepositoryObjectKey, V>());
    }

    public MapRepository(Class<?> containedClass, Map<RepositoryObjectKey, V> repositoryMapInstance) {
        this.containedClass = containedClass;
        repositoryMap = repositoryMapInstance;
    }

    public boolean contains(RepositoryObjectKey key) {
        return repositoryMap.containsKey(key);
    }

    public boolean contains(String key) {
        return contains(RepositoryObjectKey.withKey(key));
    }

    public V getItem(RepositoryObjectKey key) {
        return repositoryMap.get(key);
    }

    public V getItem(String key) {
        return repositoryMap.get(RepositoryObjectKey.withKey(key));
    }

    public void register(RepositoryObjectKey key, V value) {
        logger.info("{} repository: registering {} under key {}", new Object[]{containedClass.getSimpleName(), value.getClass().getSimpleName(), key});
        repositoryMap.put(key, value);
    }

    public void register(String key, V value) {
        register(RepositoryObjectKey.withKey(key), value);
    }

    public void registerUnique(RepositoryObjectKey key, V value) {
        if (repositoryMap.containsKey(key)) {
            throw new NonUniqueItemRegisteredException(containedClass, key);
        }
        register(key, value);
    }

    public void registerUnique(String key, V value) {
        registerUnique(RepositoryObjectKey.withKey(key), value);
    }

    public void registerAll(Map<RepositoryObjectKey, V> items) {
        for (Entry<RepositoryObjectKey, V> item : items.entrySet()) {
            register(item.getKey(), item.getValue());
        }
    }

    public void registerAllOrdered(Map<String, V> items) {
        int index = 0;
        for (Entry<String, V> item : items.entrySet()) {
            register(new RepositoryObjectKey(item.getKey(), index), item.getValue());
            index++;
        }
    }

    public void registerAllUnordered(Map<String, V> items) {
        for (Entry<String, V> item : items.entrySet()) {
            register(RepositoryObjectKey.withKey(item.getKey()), item.getValue());
        }
    }

    public Map<String, V> getItemsUnordered() {
        return injectItemsUnwrapped(new HashMap<String, V>());
    }

    public Map<String, V> getItemsOrdered() {
        return injectItemsUnwrapped(new LinkedHashMap<String, V>());
    }

    private Map<String, V> injectItemsUnwrapped(Map<String, V> containerInstance) {
        for (Entry<RepositoryObjectKey, V> item : repositoryMap.entrySet()) {
            containerInstance.put(item.getKey().getKey(), item.getValue());
        }
        return containerInstance;
    }
}
