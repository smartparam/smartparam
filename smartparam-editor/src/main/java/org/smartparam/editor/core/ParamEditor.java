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

import java.util.List;
import org.smartparam.editor.core.identity.DescribedCollection;
import org.smartparam.editor.core.identity.DescribedEntity;
import org.smartparam.engine.core.output.entry.MapEntry;
import org.smartparam.engine.core.repository.RepositoryName;
import org.smartparam.engine.core.parameter.level.Level;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.engine.core.parameter.level.LevelKey;
import org.smartparam.engine.core.parameter.entry.ParameterEntryKey;
import org.smartparam.engine.core.parameter.ParameterKey;

/**
 *
 * @author Adam Dubiel
 */
public interface ParamEditor {

    /**
     * Return names of all editable repositories registered.
     */
    List<RepositoryName> repositories();

    /**
     * Create new parameter in repository.
     */
    ParameterKey createParameter(RepositoryName in, Parameter parameter);

    /**
     * Update metadata of parameter with given name (any parameter entries present in provided metadata should
     * be ignored).
     */
    void updateParameter(RepositoryName in, String parameterName, Parameter parameter);

    /**
     * Delete parameter from repository.
     */
    void deleteParameter(RepositoryName in, String parameterName);

    /**
     * Add level to parameter in repository.
     */
    DescribedEntity<LevelKey> addLevel(RepositoryName in, String parameterName, Level level);

    /**
     * Set new order of levels. List of level keys has to contain all levels in new order.
     */
    void reorderLevels(RepositoryName in, String parameterName, List<LevelKey> orderedLevels);

    /**
     * Update level values.
     */
    void updateLevel(RepositoryName in, String parameterName, LevelKey levelKey, Level level);

    /**
     * Delete level from parameter in repository.
     */
    void deleteLevel(RepositoryName in, String parameterName, LevelKey levelKey);

    /**
     * Add entry to parameter.
     */
    DescribedEntity<ParameterEntryKey> addEntry(RepositoryName in, String parameterName, MapEntry entryMap);

    /**
     * Add multiple entries in one transaction.
     */
    DescribedCollection<ParameterEntryKey> addEntries(RepositoryName in, String parameterName, Iterable<MapEntry> entriesMaps);

    /**
     * Update values of entry.
     */
    void updateEntry(RepositoryName in, String parameterName, ParameterEntryKey entryKey, MapEntry entryMap);

    /**
     * Delete entry.
     */
    void deleteEntry(RepositoryName in, String parameterName, ParameterEntryKey entryKey);

    /**
     * Delete multiple entries in one transaction.
     */
    void deleteEntries(RepositoryName in, String parameterName, Iterable<ParameterEntryKey> entryKeys);

    /**
     * Delete all entries from parameter.
     */
    void deleteEntries(RepositoryName in, String parameterName);

    /**
     * Normalize given denormalized MapEntry. Returned normalized MapEntry is same as entry saved&read from repository.
     */
    MapEntry normalize(Parameter metadata, MapEntry denormalizedEntry);
}
