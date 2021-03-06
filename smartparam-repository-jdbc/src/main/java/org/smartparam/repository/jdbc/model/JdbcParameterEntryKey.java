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
package org.smartparam.repository.jdbc.model;

import org.smartparam.engine.core.parameter.entry.ParameterEntryKey;

/**
 *
 * @author Adam Dubiel
 */
public class JdbcParameterEntryKey implements ParameterEntryKey {

    private final long entryId;

    public JdbcParameterEntryKey(long entryId) {
        this.entryId = entryId;
    }

    public JdbcParameterEntryKey(ParameterEntryKey parameterEntryKey) {
        entryId = Long.parseLong(parameterEntryKey.value());
    }

    @Override
    public String value() {
        return Long.toString(entryId);
    }

    public long entryId() {
        return entryId;
    }

}
