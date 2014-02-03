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

    List<RepositoryName> repositories();

    ParameterKey createParameter(RepositoryName in, Parameter parameter);

    void updateParameter(RepositoryName in, String parameterName, Parameter parameter);

    void deleteParameter(RepositoryName in, String parameterName);

    DescribedEntity<LevelKey> addLevel(RepositoryName in, String parameterName, Level level);

    void reorderLevels(RepositoryName in, String parameterName, List<LevelKey> orderedLevels);

    void updateLevel(RepositoryName in, String parameterName, LevelKey levelKey, Level level);

    void deleteLevel(RepositoryName in, String parameterName, LevelKey levelKey);

    DescribedEntity<ParameterEntryKey> addEntry(RepositoryName in, String parameterName, MapEntry entryMap);

    DescribedCollection<ParameterEntryKey> addEntries(RepositoryName in, String parameterName, Iterable<MapEntry> entriesMaps);

    void updateEntry(RepositoryName in, String parameterName, ParameterEntryKey entryKey, MapEntry entryMap);

    void deleteEntry(RepositoryName in, String parameterName, ParameterEntryKey entryKey);

    void deleteEntries(RepositoryName in, String parameterName, Iterable<ParameterEntryKey> entryKeys);

    void deleteEntries(RepositoryName in, String parameterName);
}
