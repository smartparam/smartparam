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

import java.util.List;
import java.util.Set;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;

/**
 * @author Przemek Hertel
 * @since 0.2.0
 */
public class JdbcParameter implements Parameter {

    /**
     * Default value for {@link #arraySeparator} field.
     */
    public static final char DEFAULT_ARRAY_SEPARATOR = ',';

    /**
     * Unique identifier.
     */
    private long id;

    /**
     * Unique parameter name (code).
     */
    private String name;

    private List<Level> levels;

    private Set<ParameterEntry> entries;

    private int inputLevels;

    private boolean nullable;

    private boolean cacheable = true;

    private char arraySeparator = DEFAULT_ARRAY_SEPARATOR;

    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Level> getLevels() {
        return levels;
    }

    public int getLevelCount() {
        return levels != null ? levels.size() : 0;
    }

    @Override
    public int getInputLevels() {
        return inputLevels;
    }

    @Override
    public Set<ParameterEntry> getEntries() {
        return entries;
    }

    @Override
    public char getArraySeparator() {
        return arraySeparator;
    }

    @Override
    public boolean isCacheable() {
        return cacheable;
    }

    @Override
    public boolean isNullable() {
        return nullable;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Parameter#").append(id);
        sb.append('[').append(name);
        sb.append(", levels=").append(getLevelCount());
        sb.append(", inputLevels=").append(getInputLevels());
        sb.append(nullable ? ", nullable" : ", notnull");

        if (!cacheable) {
            sb.append(", nocache");
        }

        sb.append(']');
        return sb.toString();
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLevels(List<Level> levels) {
        this.levels = levels;
    }

    public void setEntries(Set<ParameterEntry> entries) {
        this.entries = entries;
    }

    public void setInputLevels(int inputLevels) {
        this.inputLevels = inputLevels;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public void setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
    }

    public void setArraySeparator(char arraySeparator) {
        this.arraySeparator = arraySeparator;
    }
}
