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
package org.smartparam.engine.core.output;

import java.util.Map;
import org.smartparam.engine.core.output.entry.MapEntry;
import org.smartparam.engine.core.parameter.entry.ParameterEntryKey;

/**
 *
 * @author Adam Dubiel
 */
public class DetailedMultiValue extends DefaultMultiValue implements MultiValue {

    private final MapEntry entry;

    public DetailedMultiValue(MapEntry entry, Object[] values, Map<String, Integer> indexMap) {
        super(values, indexMap);
        this.entry = entry;
    }

    public DetailedMultiValue(MapEntry entry, ParameterEntryKey key, Object[] values, Map<String, Integer> indexMap) {
        super(key, values, indexMap);
        this.entry = entry;
    }

    public MapEntry entry() {
        return entry;
    }
}
