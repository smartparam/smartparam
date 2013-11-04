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

import java.util.Collection;
import java.util.List;
import org.smartparam.engine.model.EntityKey;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;

/**
 * Warning! This interface will be undergoing big changes in near future
 * (adding new methods most probably) to integrate with editor.
 *
 * @author Adam Dubiel
 */
public interface EditableParamRepository extends ParamRepository {

    void updateParameter(String parameterName, Parameter parameter);

    Level getLevel(EntityKey entityKey);

    EntityKey addLevel(Level level);

    void updateLevel(EntityKey levelKey, Level level);

    void deleteLevel(EntityKey levelKey);

    EntityKey addEntry(ParameterEntry entry);

    List<EntityKey> addEntries(List<ParameterEntry> entries);

    void updateEntry(EntityKey entryKey, ParameterEntry entry);

    void deleteEntry(EntityKey entryKey);

    void deleteEntries(Collection<EntityKey> entryKeys);

}
