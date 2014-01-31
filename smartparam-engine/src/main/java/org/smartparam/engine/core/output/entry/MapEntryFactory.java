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
package org.smartparam.engine.core.output.entry;

import org.smartparam.engine.core.matcher.Matcher;
import org.smartparam.engine.core.matcher.MatcherTypeRepository;
import org.smartparam.engine.core.parameter.entry.ParameterEntryKey;
import org.smartparam.engine.core.prepared.IdentifiablePreparedEntry;
import org.smartparam.engine.core.prepared.PreparedEntry;
import org.smartparam.engine.core.prepared.PreparedLevel;
import org.smartparam.engine.core.prepared.PreparedParameter;
import org.smartparam.engine.core.type.Type;

/**
 *
 * @author Adam Dubiel
 */
public class MapEntryFactory {

    private final MatcherTypeRepository matcherDecoderRepository;

    public MapEntryFactory(MatcherTypeRepository matcherDecoderRepository) {
        this.matcherDecoderRepository = matcherDecoderRepository;
    }

    public MapEntry asMap(PreparedParameter metadata, PreparedEntry preparedEntry) {
        MapEntry map = new MapEntry(extractKey(preparedEntry));

        int index = 0;
        for (PreparedLevel level : metadata.getLevels()) {
            map.put(level.getName(), asObject(level, preparedEntry.getLevel(index)));
            index++;
        }

        return map;
    }

    private ParameterEntryKey extractKey(PreparedEntry preparedEntry) {
        if (preparedEntry instanceof IdentifiablePreparedEntry) {
            return ((IdentifiablePreparedEntry) preparedEntry).getKey();
        }
        return null;
    }

    private Object asObject(PreparedLevel level, String levelValue) {
        Type<?> type = level.getType();
        Matcher matcher = level.getMatcher();

        return matcherDecoderRepository.getMatcherType(level.getMatcherName()).decode(levelValue, type, matcher);
    }
}
