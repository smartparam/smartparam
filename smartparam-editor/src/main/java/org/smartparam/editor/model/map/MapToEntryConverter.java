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

import org.smartparam.editor.model.simple.SimpleParameterEntry;
import org.smartparam.engine.core.ParamEngineRuntimeConfig;
import org.smartparam.engine.core.parameter.Level;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.engine.core.parameter.ParameterEntry;
import org.smartparam.engine.core.type.Type;

/**
 *
 * @author Adam Dubiel
 */
class MapToEntryConverter {

    private final ParamEngineRuntimeConfig engineConfig;

    MapToEntryConverter(ParamEngineRuntimeConfig engineConfig) {
        this.engineConfig = engineConfig;
    }

    ParameterEntry asEntry(Parameter metadata, ParameterEntryMap entryMap) {
        String[] levelValues = new String[metadata.getLevels().size()];

        int index = 0;
        for (Level level : metadata.getLevels()) {
            levelValues[index] = asString(level, entryMap.get(level.getName()));
            index++;
        }

        return new SimpleParameterEntry(levelValues);
    }

    @SuppressWarnings("unchecked")
    private String asString(Level level, Object object) {
        if (object != null && object instanceof Star) {
            return "*";
        }

        Type type = engineConfig.getTypes().get(level.getType());
        if (type != null) {
            return type.encode(type.convert(object));
        }
        return object == null ? "" : object.toString();
    }

}
