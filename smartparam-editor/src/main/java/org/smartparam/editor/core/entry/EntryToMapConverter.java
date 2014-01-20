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
package org.smartparam.editor.core.entry;

import org.smartparam.engine.core.ParamEngineRuntimeConfig;
import org.smartparam.engine.core.matcher.Matcher;
import org.smartparam.engine.core.matcher.MatcherDecoderRepository;
import org.smartparam.engine.core.parameter.level.Level;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.engine.core.parameter.entry.ParameterEntry;
import org.smartparam.engine.core.type.Type;

/**
 *
 * @author Adam Dubiel
 */
class EntryToMapConverter {

    private final ParamEngineRuntimeConfig engineConfig;

    private final MatcherDecoderRepository decoderRepository;

    EntryToMapConverter(ParamEngineRuntimeConfig engineConfig) {
        this.engineConfig = engineConfig;
        this.decoderRepository = engineConfig.getMatcherDecoderRepository();
    }

    ParameterEntryMap asMap(Parameter metadata, ParameterEntry parameterEntry) {
        ParameterEntryMap map = new ParameterEntryMap(parameterEntry.getKey());

        int index = 0;
        for (Level level : metadata.getLevels()) {
            map.put(level.getName(), asObject(level, parameterEntry.getLevels()[index]));
            index++;
        }

        return map;
    }

    private Object asObject(Level level, String levelValue) {
        Type<?> type = engineConfig.getTypes().get(level.getType());
        Matcher matcher = engineConfig.getMatchers().get(level.getMatcher());

        return decoderRepository.getDecoder(level.getMatcher()).decode(levelValue, type, matcher);
    }
}
