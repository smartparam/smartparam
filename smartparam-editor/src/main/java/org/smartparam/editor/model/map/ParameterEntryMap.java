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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.smartparam.editor.model.ParameterEntryKey;
import org.smartparam.engine.core.parameter.Level;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.engine.core.parameter.ParameterEntry;

/**
 *
 * @author Adam Dubiel
 */
public class ParameterEntryMap implements Iterable<Entry<String, Object>> {

    private ParameterEntryKey key;

    private final Map<String, Object> values = new HashMap<String, Object>();

    public ParameterEntryMap() {
    }

    public ParameterEntryMap(ParameterEntryKey key) {
        this.key = key;
    }

    public ParameterEntryKey key() {
        return key;
    }

    public boolean hasKey() {
        return key != null;
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
}
