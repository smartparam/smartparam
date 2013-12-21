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
package org.smartparam.editor.editor;

import java.util.List;
import org.smartparam.engine.core.parameter.ParamRepository;
import org.smartparam.engine.core.parameter.Level;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.engine.core.parameter.ParameterEntry;
import org.smartparam.editor.model.LevelKey;
import org.smartparam.editor.model.ParameterEntryKey;

/**
 * Editable repository.
 *
 * @author Adam Dubiel
 */
public interface EditableParamRepository extends ParamRepository {

    /**
     * Create new parameter based on provided instance (only interface methods
     * are used).
     */
    void createParameter(Parameter parameter);

    /**
     * Update parameter with properties of provided instance.
     */
    void updateParameter(String parameterName, Parameter parameter);

    /**
     * Delete parameter stored under given name.
     */
    void deleteParameter(String parameterName);

    /**
     * Add level to parameter.
     */
    LevelKey addLevel(String parameterName, Level level);

    /**
     * Change order of parameter levels to their order on the list.
     */
    void reorderLevels(String parameterName, List<LevelKey> orderedLevels);

    /**
     * Update level in given parameter.
     */
    void updateLevel(String parameterName, LevelKey levelKey, Level level);

    /**
     * Delete level from parameter.
     */
    void deleteLevel(String parameterName, LevelKey levelKey);

    /**
     * Add entry to parameter.
     */
    ParameterEntryKey addEntry(String parameterName, ParameterEntry entry);

    /**
     * Add entries to parameter.
     */
    List<ParameterEntryKey> addEntries(String parameterName, Iterable<ParameterEntry> entries);

    /**
     * Update given entry.
     */
    void updateEntry(String parameterName, ParameterEntryKey entryKey, ParameterEntry entry);

    /**
     * Delete entry.
     */
    void deleteEntry(String parameterName, ParameterEntryKey entryKey);

    /**
     * Delete all entries.
     */
    void deleteEntries(String parameterName, Iterable<ParameterEntryKey> entryKeys);

}
