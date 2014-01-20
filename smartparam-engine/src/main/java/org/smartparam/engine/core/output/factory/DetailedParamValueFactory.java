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

import java.util.List;
import org.smartparam.engine.core.output.DetailedMultiValue;
import org.smartparam.engine.core.output.DetailedParamValue;
import org.smartparam.engine.core.output.DetailedParamValueImpl;
import org.smartparam.engine.core.output.entry.MapEntry;
import org.smartparam.engine.core.output.entry.MapEntryFactory;
import org.smartparam.engine.core.prepared.PreparedEntry;
import org.smartparam.engine.core.prepared.PreparedParameter;

import static org.smartparam.engine.core.output.factory.ParameterEntryKeyExtractor.extractEntryKey;

/**
 *
 * @author Adam Dubiel
 */
public class DetailedParamValueFactory extends AbstractParamValueFactory<DetailedMultiValue> implements ParamValueFactory {

    private final MapEntryFactory entryMapFactory;

    public DetailedParamValueFactory(MapEntryFactory entryMapFactory) {
        this.entryMapFactory = entryMapFactory;
    }

    @Override
    public DetailedParamValue create(PreparedParameter parameter, PreparedEntry[] preparedEntries) {
        List<DetailedMultiValue> rows = createRows(parameter, preparedEntries);
        return new DetailedParamValueImpl(rows, parameter.getSourceRepository());
    }

    @Override
    protected DetailedMultiValue createMultiValue(PreparedParameter parameter, PreparedEntry preparedEntry, Object[] values) {
        MapEntry entry = entryMapFactory.asMap(parameter, preparedEntry);
        return new DetailedMultiValue(entry, extractEntryKey(preparedEntry), values, parameter.getLevelNameMap());
    }

}
