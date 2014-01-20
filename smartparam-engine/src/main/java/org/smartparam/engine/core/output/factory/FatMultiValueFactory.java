/*
 * Copyright 2014 Adam Dubiel.
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
package org.smartparam.engine.core.output.factory;

import org.smartparam.engine.core.output.FatMultiValue;
import org.smartparam.engine.core.output.entry.MapEntryFactory;
import org.smartparam.engine.core.output.entry.MapEntry;
import org.smartparam.engine.core.prepared.PreparedEntry;
import org.smartparam.engine.core.prepared.PreparedParameter;

import static org.smartparam.engine.core.output.factory.ParameterEntryKeyExtractor.extractEntryKey;

/**
 *
 * @author Adam Dubiel
 */
public class FatMultiValueFactory implements MultiValueFactory<FatMultiValue> {

    private final MapEntryFactory entryMapFactory;

    public FatMultiValueFactory(MapEntryFactory entryMapFactory) {
        this.entryMapFactory = entryMapFactory;
    }

    @Override
    public FatMultiValue create(PreparedParameter parameter, PreparedEntry preparedEntry, Object[] values) {
        MapEntry entry = entryMapFactory.asMap(parameter, preparedEntry);
        return new FatMultiValue(entry, extractEntryKey(preparedEntry), values, parameter.getLevelNameMap());
    }

}
