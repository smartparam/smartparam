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

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import org.smartparam.editor.core.capabilities.RepositoryCapabilities;
import org.smartparam.editor.core.EditableParamRepository;
import org.smartparam.engine.core.parameter.level.LevelKey;
import org.smartparam.engine.core.parameter.entry.ParameterEntryKey;
import org.smartparam.engine.core.parameter.ParameterKey;
import org.smartparam.editor.core.filters.ParameterEntriesFilter;
import org.smartparam.editor.core.filters.ParameterFilter;
import org.smartparam.editor.core.filters.SortDirection;
import org.smartparam.editor.core.ViewableParamRepository;
import org.smartparam.engine.core.parameter.level.Level;
import org.smartparam.engine.core.parameter.ParamRepository;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.engine.core.parameter.ParameterBatchLoader;
import org.smartparam.engine.core.parameter.entry.ParameterEntry;

/**
 *
 * @author Adam Dubiel
 */
public class InMemoryParamRepository implements ParamRepository, ViewableParamRepository, EditableParamRepository {

    private final Map<String, InMemoryParameter> repository = new ConcurrentHashMap<String, InMemoryParameter>();

    private final InMemoryParamRepositoryInspector inspector = new InMemoryParamRepositoryInspector(this);

    private InMemoryParameter loadRaw(String parameterName) {
        return repository.get(parameterName);
    }

    public InMemoryParamRepositoryInspector inspector() {
        return inspector;
    }

    public void clear() {
        repository.clear();
    }

    public void clearExcept(String... parameterNames) {
        final Set<String> parametersToKeep = Sets.newHashSet(parameterNames);
        Map<String, InMemoryParameter> entriesToKeep = ImmutableMap.copyOf(Maps.filterKeys(repository, new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                return parametersToKeep.contains(input);
            }
        }));
        repository.clear();
        repository.putAll(entriesToKeep);
    }

    @Override
    public Parameter load(String parameterName) {
        return loadRaw(parameterName);
    }

    @Override
    public ParameterBatchLoader batchLoad(String parameterName) {
        Parameter parameter = load(parameterName);
        return new ParameterBatchLoader(parameter, new InMemoryParameterEntryBatchLoader(parameter));
    }

    @Override
    public Set<ParameterEntry> findEntries(String parameterName, String[] levelValues) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<String> listParameters() {
        return repository.keySet();
    }

    @Override
    public RepositoryCapabilities capabilities() {
        return new RepositoryCapabilities();
    }

    @Override
    public List<String> listParameters(ParameterFilter filter) {
        if (filter.applyNameFilter()) {
            final Pattern pattern = Pattern.compile(filter.nameFilter());
            Set<String> filteredSet = Sets.filter(listParameters(), new Predicate<String>() {
                @Override
                public boolean apply(String input) {
                    return pattern.matcher(input).matches();
                }
            });

            Ordering<String> ordering = Ordering.natural();
            if (filter.sortDirection() == SortDirection.DESC) {
                ordering = ordering.reverse();
            }
            return ordering.sortedCopy(filteredSet);
        } else {
            return new ArrayList<String>(listParameters());
        }
    }

    @Override
    public Parameter getParameterMetadata(String parameterName) {
        return load(parameterName);
    }

    @Override
    public List<ParameterEntry> getParameterEntries(String parameterName, Iterable<ParameterEntryKey> parameterEntryKeys) {
        InMemoryParameter parameter = loadRaw(parameterName);
        List<ParameterEntry> entries = new ArrayList<ParameterEntry>();
        for (ParameterEntryKey entryKey : parameterEntryKeys) {
            entries.add(parameter.findEntry(new InMemoryParameterEntryKey(entryKey)));
        }

        return entries;
    }

    @Override
    public List<ParameterEntry> listEntries(String parameterName, ParameterEntriesFilter filter) {
        InMemoryParameter parameter = loadRaw(parameterName);
        return new ArrayList<ParameterEntry>(parameter.getEntries());
    }

    @Override
    public ParameterKey createParameter(Parameter parameter) {
        InMemoryParameter newParameter = new InMemoryParameter(parameter);
        repository.put(newParameter.getName(), newParameter);
        for (Level level : parameter.getLevels()) {
            newParameter.addLevel(new InMemoryLevel(level));
        }

        return newParameter.getKey();
    }

    @Override
    public void updateParameter(String parameterName, Parameter parameter) {
        InMemoryParameter memoryParameter = loadRaw(parameterName);
        memoryParameter.merge(parameter);
    }

    @Override
    public void deleteParameter(String parameterName) {
        repository.remove(parameterName);
    }

    @Override
    public LevelKey addLevel(String parameterName, Level level) {
        InMemoryParameter parameter = loadRaw(parameterName);
        return parameter.addLevel(new InMemoryLevel(level));
    }

    @Override
    public void reorderLevels(String parameterName, List<LevelKey> orderedLevels) {
        InMemoryParameter parameter = loadRaw(parameterName);
        parameter.reorderLevels(orderedLevels);
    }

    @Override
    public void updateLevel(String parameterName, LevelKey levelKey, Level level) {
        InMemoryParameter parameter = loadRaw(parameterName);
        parameter.findLevel(new InMemoryLevelKey(levelKey)).merge(level);
    }

    @Override
    public void deleteLevel(String parameterName, LevelKey levelKey) {
        InMemoryParameter parameter = loadRaw(parameterName);
        parameter.removeLevel(new InMemoryLevelKey(levelKey));
    }

    @Override
    public ParameterEntryKey addEntry(String parameterName, ParameterEntry entry) {
        InMemoryParameter parameter = loadRaw(parameterName);
        return parameter.addEntry(new InMemoryParameterEntry(entry));
    }

    @Override
    public List<ParameterEntryKey> addEntries(String parameterName, Iterable<ParameterEntry> entries) {
        InMemoryParameter parameter = loadRaw(parameterName);
        List<ParameterEntryKey> addedKeys = new ArrayList<ParameterEntryKey>();
        for (ParameterEntry entry : entries) {
            addedKeys.add(parameter.addEntry(new InMemoryParameterEntry(entry)));
        }

        return addedKeys;
    }

    @Override
    public void updateEntry(String parameterName, ParameterEntryKey entryKey, ParameterEntry entry) {
        InMemoryParameter parameter = loadRaw(parameterName);
        parameter.findEntry(new InMemoryParameterEntryKey(entryKey)).merge(entry);
    }

    @Override
    public void deleteEntry(String parameterName, ParameterEntryKey entryKey) {
        InMemoryParameter parameter = loadRaw(parameterName);
        parameter.removeEntry(new InMemoryParameterEntryKey(entryKey));
    }

    @Override
    public void deleteEntries(String parameterName, Iterable<ParameterEntryKey> entryKeys) {
        InMemoryParameter parameter = loadRaw(parameterName);
        for (ParameterEntryKey entryKey : entryKeys) {
            parameter.removeEntry(new InMemoryParameterEntryKey(entryKey));

        }
    }

    public void deleteEntries(String parameterName) {
        InMemoryParameter parameter = loadRaw(parameterName);
        parameter.removeEntries();
    }
}
