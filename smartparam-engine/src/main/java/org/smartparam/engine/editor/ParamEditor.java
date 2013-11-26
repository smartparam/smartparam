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
package org.smartparam.engine.editor;

import java.util.List;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.engine.model.editable.EditableLevel;
import org.smartparam.engine.model.editable.LevelKey;
import org.smartparam.engine.model.editable.ParameterEntryKey;

/**
 *
 * @author Adam Dubiel
 */
public interface ParamEditor {

    List<RepositoryName> repositories();

    void createParameter(RepositoryName in, Parameter parameter);

    void updateParameter(RepositoryName in, String parameterName, Parameter parameter);

    DescribedEntity<EditableLevel> getLevel(RepositoryName from, LevelKey levelKey);

    DescribedEntity<LevelKey> addLevel(RepositoryName in, String parameterName, Level level);

    void reorderLevels(RepositoryName in, List<LevelKey> orderedLevels);

    void updateLevel(RepositoryName in, LevelKey levelKey, Level level);

    void deleteLevel(RepositoryName in, String parameterName, LevelKey levelKey);

    DescribedEntity<ParameterEntryKey> addEntry(RepositoryName in, String parameterName, ParameterEntry entry);

    DescribedCollection<ParameterEntryKey> addEntries(RepositoryName in, String parameterName, List<ParameterEntry> entries);

    void updateEntry(RepositoryName in, ParameterEntryKey entryKey, ParameterEntry entry);

    void deleteEntry(RepositoryName in, ParameterEntryKey entryKey);

    void deleteEntries(RepositoryName in, Iterable<ParameterEntryKey> entryKeys);
}
