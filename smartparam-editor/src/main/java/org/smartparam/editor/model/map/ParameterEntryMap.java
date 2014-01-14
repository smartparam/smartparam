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
package org.smartparam.editor.model.map;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.smartparam.editor.model.ParameterEntryKey;

/**
 *
 * @author Adam Dubiel
 */
public class ParameterEntryMap implements Iterable<Entry<String, Object>> {

    public static final String KEY = "_key";

    private final Map<String, Object> values = new HashMap<String, Object>();

    public ParameterEntryMap() {
    }

    public ParameterEntryMap(ParameterEntryKey key) {
        values.put(KEY, key);
    }

    public ParameterEntryMap(Map<String, Object> initialValues) {
        values.putAll(initialValues);
    }

    public ParameterEntryMap merge(ParameterEntryMap other) {
        ParameterEntryMap merged = new ParameterEntryMap();
        merged.values.putAll(other.values);

        // override
        merged.values.putAll(this.values);

        return merged;
    }

    public ParameterEntryKey key() {
        return get(KEY);
    }

    public boolean hasKey() {
        return has(KEY);
    }

    public boolean has(String levelName) {
        return this.values.containsKey(levelName);
    }

    public ParameterEntryMap put(String levelName, Object value) {
        this.values.put(levelName, value);
        return this;
    }

    public Object getRaw(String levelName) {
        return this.values.get(levelName);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String levelName) {
        return (T) this.values.get(levelName);
    }

    @SuppressWarnings("unchecked")
    public <T> T getAs(String levelName, Class<T> clazz) {
        return (T) this.values.get(levelName);
    }

    @Override
    public Iterator<Entry<String, Object>> iterator() {
        return values.entrySet().iterator();
    }

    public Map<String, Object> rawValues() {
        return Collections.unmodifiableMap(values);
    }
}
