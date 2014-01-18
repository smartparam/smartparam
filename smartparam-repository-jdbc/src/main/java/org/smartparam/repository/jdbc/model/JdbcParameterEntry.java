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

import java.util.Arrays;
import org.smartparam.engine.core.parameter.entry.ParameterEntry;

/**
 * @author Przemek Hertel
 * @since 0.2.0
 */
public class JdbcParameterEntry implements ParameterEntry {

    private static final int TO_STRING_LENGTH = 100;

    private final JdbcParameterEntryKey key;

    private String[] levels;

    public JdbcParameterEntry(long id, String[] levels) {
        this.key = new JdbcParameterEntryKey(id);
        this.levels = levels;
    }

    @Override
    public JdbcParameterEntryKey getKey() {
        return key;
    }

    @Override
    public String[] getLevels() {
        return Arrays.copyOf(levels, levels.length);
    }

    public long getId() {
        return key.entryId();
    }

    public void setLevels(String[] levels) {
        this.levels = Arrays.copyOf(levels, levels.length);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(TO_STRING_LENGTH);
        sb.append("JdbcParameterEntry[#").append(getId());
        sb.append(' ');
        sb.append(Arrays.toString(levels));
        sb.append(']');
        return sb.toString();
    }
}
