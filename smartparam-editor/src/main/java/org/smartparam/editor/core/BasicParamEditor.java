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
package org.smartparam.editor.core;

import java.util.ArrayList;
import java.util.List;
import org.smartparam.editor.core.identity.DescribedCollection;
import org.smartparam.editor.core.identity.DescribedEntity;
import org.smartparam.engine.core.repository.RepositoryName;
import org.smartparam.editor.core.store.RepositoryStore;
import org.smartparam.engine.core.ParamEngine;
import org.smartparam.engine.core.parameter.level.Level;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.engine.core.parameter.level.LevelKey;
import org.smartparam.engine.core.parameter.entry.ParameterEntryKey;
import org.smartparam.engine.core.parameter.ParameterKey;
import org.smartparam.editor.core.entry.ParameterEntryMapConverter;
import org.smartparam.engine.core.ParamEngineRuntimeConfig;
import org.smartparam.engine.core.output.entry.MapEntry;
import org.smartparam.engine.core.parameter.entry.ParameterEntry;
import org.smartparam.engine.core.prepared.PreparedParamCache;

/**
 *
 * @author Adam Dubiel
 */
public class BasicParamEditor implements ParamEditor {

    private final RepositoryStore<EditableParamRepository> repositories;

    private final PreparedParamCache parameterCache;

    private final ParameterEntryMapConverter converter;

    public BasicParamEditor(ParamEngine paramEngine, ParameterEntryMapConverter entryMapConverter) {
        ParamEngineRuntimeConfig runtimeConfig = paramEngine.runtimeConfiguration();

        repositories = new RepositoryStore<EditableParamRepository>(runtimeConfig.getParamRepositories(), EditableParamRepository.class);
        parameterCache = runtimeConfig.getParamCache();
        converter = entryMapConverter;
    }

    @Override
    public List<RepositoryName> repositories() {
        return repositories.storedRepositories();
    }

    private void clearCache(String parameterName) {
        parameterCache.invalidate(parameterName);
    }

    @Override
    public ParameterKey createParameter(RepositoryName in, Parameter parameter) {
        EditableParamRepository repository = repositories.get(in);
        return repository.createParameter(parameter);
    }

    @Override
    public void updateParameter(RepositoryName in, String parameterName, Parameter parameter) {
        EditableParamRepository repository = repositories.get(in);
        repository.updateParameter(parameterName, parameter);
        clearCache(parameterName);
    }

    @Override
    public void deleteParameter(RepositoryName in, String parameterName) {
        EditableParamRepository repository = repositories.get(in);
        repository.deleteParameter(parameterName);
        clearCache(parameterName);
    }

    @Override
    public DescribedEntity<LevelKey> addLevel(RepositoryName in, String parameterName, Level level) {
        EditableParamRepository repository = repositories.get(in);
        LevelKey addedLevelKey = repository.addLevel(parameterName, level);
        clearCache(parameterName);

        return new DescribedEntity<LevelKey>(in, addedLevelKey);
    }

    @Override
    public void reorderLevels(RepositoryName in, String parameterName, List<LevelKey> orderedLevels) {
        EditableParamRepository repository = repositories.get(in);
        repository.reorderLevels(parameterName, orderedLevels);
        clearCache(parameterName);
    }

    @Override
    public void updateLevel(RepositoryName in, String parameterName, LevelKey levelKey, Level level) {
        EditableParamRepository repository = repositories.get(in);
        repository.updateLevel(parameterName, levelKey, level);
        clearCache(parameterName);
    }

    @Override
    public void deleteLevel(RepositoryName in, String parameterName, LevelKey levelKey) {
        EditableParamRepository repository = repositories.get(in);
        repository.deleteLevel(parameterName, levelKey);
        clearCache(parameterName);
    }

    @Override
    public DescribedEntity<ParameterEntryKey> addEntry(RepositoryName in, String parameterName, MapEntry entryMap) {
        EditableParamRepository repository = repositories.get(in);
        ParameterEntry entry = convert(repository, entryMap, parameterName);

        ParameterEntryKey addedEntryKey = repository.addEntry(parameterName, entry);
        clearCache(parameterName);

        return new DescribedEntity<ParameterEntryKey>(in, addedEntryKey);
    }

    @Override
    public DescribedCollection<ParameterEntryKey> addEntries(RepositoryName in, String parameterName, Iterable<MapEntry> entries) {
        EditableParamRepository repository = repositories.get(in);
        List<ParameterEntryKey> addedEntryKeys = repository.addEntries(parameterName, convert(repository, entries, parameterName));
        clearCache(parameterName);

        return new DescribedCollection<ParameterEntryKey>(in, addedEntryKeys);
    }

    @Override
    public void updateEntry(RepositoryName in, String parameterName, ParameterEntryKey entryKey, MapEntry entryMap) {
        EditableParamRepository repository = repositories.get(in);
        ParameterEntry entry = convert(repository, entryMap, parameterName);

        repository.updateEntry(parameterName, entryKey, entry);
        clearCache(parameterName);
    }

    @Override
    public void deleteEntry(RepositoryName in, String parameterName, ParameterEntryKey entryKey) {
        EditableParamRepository repository = repositories.get(in);
        repository.deleteEntry(parameterName, entryKey);
        clearCache(parameterName);
    }

    @Override
    public void deleteEntries(RepositoryName in, String parameterName, Iterable<ParameterEntryKey> entryKeys) {
        EditableParamRepository repository = repositories.get(in);
        repository.deleteEntries(parameterName, entryKeys);
        clearCache(parameterName);
    }

    @Override
    public void deleteEntries(RepositoryName in, String parameterName) {
        EditableParamRepository repository = repositories.get(in);
        repository.deleteEntries(parameterName);
        clearCache(parameterName);
    }

    private ParameterEntry convert(EditableParamRepository repository, MapEntry entryMap, String parameterName) {
        Parameter metadata = repository.getParameterMetadata(parameterName);
        return converter.asEntry(metadata, entryMap);
    }

    private List<ParameterEntry> convert(EditableParamRepository repository, Iterable<MapEntry> entryMaps, String parameterName) {
        Parameter metadata = repository.getParameterMetadata(parameterName);
        List<ParameterEntry> entries = new ArrayList<ParameterEntry>();
        for (MapEntry entryMap : entryMaps) {
            entries.add(converter.asEntry(metadata, entryMap));
        }
        return entries;
    }

    @Override
    public MapEntry normalize(Parameter metadata, MapEntry denormalizedEntry) {
        return converter.asMap(metadata, converter.asEntry(metadata, denormalizedEntry));
    }
}
