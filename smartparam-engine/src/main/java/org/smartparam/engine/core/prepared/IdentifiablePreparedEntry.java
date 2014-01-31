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
package org.smartparam.engine.core.prepared;

import org.smartparam.engine.core.parameter.entry.ParameterEntry;
import org.smartparam.engine.core.parameter.entry.ParameterEntryKey;

/**
 *
 * @author Adam Dubiel
 */
public class IdentifiablePreparedEntry extends PreparedEntry {

    private final String key;

    public IdentifiablePreparedEntry(ParameterEntry parameterEntry) {
        super(parameterEntry);
        this.key = parameterEntry.getKey().value();
    }

    public IdentifiablePreparedEntry(ParameterEntryKey key, String[] values) {
        super(values);
        this.key = key.value();
    }

    public ParameterEntryKey getKey() {
        return new PreparedEntryKey(key);
    }
}
