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
package org.smartparam.repository.memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.smartparam.editor.core.model.EditableParameter;
import org.smartparam.editor.core.model.LevelKey;
import org.smartparam.editor.core.model.ParameterKey;
import org.smartparam.engine.core.parameter.Level;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.engine.core.parameter.ParameterEntry;

/**
 *
 * @author Adam Dubiel
 */
public class InMemoryParameter implements EditableParameter {

    private final InMemoryParameterKey key;

    private String name;

    private final List<Level> levels = new ArrayList<Level>();

    private int inputLevels;

    private final Map<String, InMemoryParameterEntry> entries = new HashMap<String, InMemoryParameterEntry>();

    private char arraySeparator = Parameter.DEFAULT_ARRAY_SEPARATOR;

    private boolean cacheable = true;

    private boolean nullable = false;

    InMemoryParameter() {
        this.key = new InMemoryParameterKey();
    }

    InMemoryParameter(Parameter parameter) {
        this();
        merge(parameter);
    }

    final void merge(Parameter parameter) {
        this.name = parameter.getName();
        this.inputLevels = parameter.getInputLevels();
        this.arraySeparator = parameter.getArraySeparator();
        this.cacheable = parameter.isCacheable();
        this.nullable = parameter.isNullable();
    }

    InMemoryLevelKey addLevel(InMemoryLevel level) {
        this.levels.add(level);
        return level.getRawKey();
    }

    InMemoryLevel findLevel(InMemoryLevelKey levelKey) {
        InMemoryLevel inMemoryLevel;
        for (Level level : levels) {
            inMemoryLevel = (InMemoryLevel) level;
            if (inMemoryLevel.getKey().value().equals(levelKey.value())) {
                return inMemoryLevel;
            }
        }
        return null;
    }

    void reorderLevels(List<LevelKey> newOrderKeys) {
        List<Level> orderedLevels = new ArrayList<Level>(this.levels.size());
        for (LevelKey orderKey : newOrderKeys) {
            InMemoryLevel inMemoryLevel = findLevel(new InMemoryLevelKey(orderKey));
            orderedLevels.add(inMemoryLevel);
        }
        levels.clear();
        levels.addAll(orderedLevels);
    }

    void removeLevel(InMemoryLevelKey levelKey) {
        InMemoryLevel inMemoryLevel;
        Iterator<Level> iterator = levels.iterator();
        while (iterator.hasNext()) {
            inMemoryLevel = (InMemoryLevel) iterator.next();
            if (inMemoryLevel.getRawKey().value().equals(levelKey.value())) {
                iterator.remove();
                break;
            }
        }
    }

    InMemoryParameterEntryKey addEntry(InMemoryParameterEntry entry) {
        entries.put(entry.getKey().value(), entry);
        return entry.getRawKey();
    }

    InMemoryParameterEntry findEntry(InMemoryParameterEntryKey entryKey) {
        return entries.get(entryKey.value());
    }

    void removeEntry(InMemoryParameterEntryKey entryKey) {
        entries.remove(entryKey.value());
    }

    @Override
    public ParameterKey getKey() {
        return key;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Level> getLevels() {
        return Collections.unmodifiableList(levels);
    }

    @Override
    public int getInputLevels() {
        return inputLevels;
    }

    @Override
    public Set<ParameterEntry> getEntries() {
        return Collections.unmodifiableSet(new HashSet<ParameterEntry>(entries.values()));
    }

    @Override
    public boolean isCacheable() {
        return cacheable;
    }

    @Override
    public boolean isNullable() {
        return nullable;
    }

    @Override
    public char getArraySeparator() {
        return arraySeparator;
    }

}
