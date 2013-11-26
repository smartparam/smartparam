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

import org.smartparam.editor.model.AbstractEntityKey;
import org.smartparam.editor.model.ParameterEntryKey;
import static org.smartparam.repository.jdbc.model.JdbcLevelKey.SYMBOL;

/**
 *
 * @author Adam Dubiel
 */
public class JdbcParameterEntryKey extends AbstractEntityKey implements ParameterEntryKey {

    private final String value;

    private final String parameterName;

    private final long entryId;

    public JdbcParameterEntryKey(String parameterName, long entryId) {
        this.value = format(SYMBOL, parameterName, Long.toString(entryId));
        this.parameterName = parameterName;
        this.entryId = entryId;
    }

    public JdbcParameterEntryKey(ParameterEntryKey parameterKey) {
        String[] segments = parse(SYMBOL, parameterKey.value());
        value = parameterKey.value();
        parameterName = segments[0];
        entryId = Long.parseLong(segments[1]);
    }

    @Override
    public String value() {
        return value;
    }

    public String parameterName() {
        return parameterName;
    }

    public long entryId() {
        return entryId;
    }

}
