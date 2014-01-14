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

import org.smartparam.editor.model.EditableParameterEntry;
import org.smartparam.editor.model.ParameterEntryKey;
import org.smartparam.engine.core.ParamEngineRuntimeConfig;
import org.smartparam.engine.core.parameter.Level;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.engine.core.parameter.ParameterEntry;
import org.smartparam.engine.core.type.Type;

/**
 *
 * @author Adam Dubiel
 */
class EntryToMapConverter {

    private final ParamEngineRuntimeConfig engineConfig;

    EntryToMapConverter(ParamEngineRuntimeConfig engineConfig) {
        this.engineConfig = engineConfig;
    }

    ParameterEntryMap asMap(Parameter metadata, ParameterEntry parameterEntry) {
        ParameterEntryMap map = new ParameterEntryMap(keyOf(parameterEntry));

        int index = 0;
        for (Level level : metadata.getLevels()) {
            map.put(level.getName(), asObject(level, parameterEntry.getLevels()[index]));
            index++;
        }

        return map;
    }

    private ParameterEntryKey keyOf(ParameterEntry entry) {
        if (entry instanceof EditableParameterEntry) {
            return ((EditableParameterEntry) entry).getKey();
        }
        return null;
    }

    private Object asObject(Level level, String levelValue) {
        if (levelValue != null && levelValue.equals("*")) {
            return Star.star();
        }

        Type<?> type = engineConfig.getTypes().get(level.getType());
        if (type != null) {
            return type.decode(levelValue).getValue();
        }

        return levelValue;
    }
}
