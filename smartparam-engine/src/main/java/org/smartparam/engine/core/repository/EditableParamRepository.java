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

import java.util.List;
import org.smartparam.engine.editor.ParameterEntriesFilter;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.engine.model.editable.EditableParameterEntry;
import org.smartparam.engine.model.editable.LevelKey;
import org.smartparam.engine.model.editable.ParameterEntryKey;

/**
 * Warning! This interface will be undergoing big changes in near future
 * (adding new methods most probably) to integrate with editor.
 *
 * @author Adam Dubiel
 */
public interface EditableParamRepository extends ParamRepository {

    void createParameter(Parameter parameter);

    void updateParameter(String parameterName, Parameter parameter);

    Level getLevel(LevelKey entityKey);

    LevelKey addLevel(String parameterName, Level level);

    void reorderLevels(List<LevelKey> orderedLevels);

    void updateLevel(LevelKey levelKey, Level level);

    void deleteLevel(String parameterName, LevelKey levelKey);

    ParameterEntryKey addEntry(String parameterName, ParameterEntry entry);

    List<ParameterEntryKey> addEntries(String parameterName, List<ParameterEntry> entries);

    void updateEntry(ParameterEntryKey entryKey, ParameterEntry entry);

    void deleteEntry(ParameterEntryKey entryKey);

    void deleteEntries(Iterable<ParameterEntryKey> entryKeys);

}
