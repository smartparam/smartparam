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
import org.smartparam.engine.core.output.*;
import org.smartparam.engine.core.prepared.PreparedEntry;
import org.smartparam.engine.core.prepared.PreparedParameter;

import static org.smartparam.engine.core.output.factory.ParameterEntryKeyExtractor.extractEntryKey;

/**
 *
 * @author Adam Dubiel
 */
public class DefaultParamValueFactory extends AbstractParamValueFactory<MultiValue> implements ParamValueFactory {

    @Override
    public ParamValue create(PreparedParameter parameter, PreparedEntry[] preparedEntries) {
        List<MultiValue> rows = createRows(parameter, preparedEntries);
        return new DefaultParamValue(rows, parameter.getSourceRepository());
    }

    @Override
    protected MultiValue createMultiValue(PreparedParameter parameter, PreparedEntry preparedEntry, Object[] values) {
        return new DefaultMultiValue(extractEntryKey(preparedEntry), values, parameter.getLevelNameMap());
    }

}
