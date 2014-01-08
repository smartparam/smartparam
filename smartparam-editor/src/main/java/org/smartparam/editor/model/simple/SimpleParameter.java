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
package org.smartparam.editor.model.simple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.smartparam.engine.core.parameter.Level;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.engine.core.parameter.ParameterEntry;

/**
 *
 * @author Adam Dubiel
 */
public class SimpleParameter implements Parameter {

    private String name;

    private final List<Level> levels = new ArrayList<Level>();

    private int inputLevels;

    private final Set<ParameterEntry> entries = new HashSet<ParameterEntry>();

    private char arraySeparator = Parameter.DEFAULT_ARRAY_SEPARATOR;

    private boolean cacheable = true;

    private boolean nullable = false;

    public SimpleParameter() {
    }

    public SimpleParameter(Parameter parameter) {
        this.name = parameter.getName();
        this.inputLevels = parameter.getInputLevels();
        this.cacheable = parameter.isCacheable();
        this.nullable = parameter.isNullable();
        this.arraySeparator = parameter.getArraySeparator();

        for (Level level : parameter.getLevels()) {
            this.levels.add(new SimpleLevel(level));
        }
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SimpleParameter withName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public List<Level> getLevels() {
        return Collections.unmodifiableList(levels);
    }

    public void setLevels(List<Level> levels) {
        this.levels.addAll(levels);
    }

    public SimpleParameter withLevel(Level level) {
        this.levels.add(level);
        return this;
    }

    @Override
    public int getInputLevels() {
        return inputLevels;
    }

    public void setInputLevels(int inputLevels) {
        this.inputLevels = inputLevels;
    }

    public SimpleParameter withInputLevels(int inputLevels) {
        this.inputLevels = inputLevels;
        return this;
    }

    @Override
    public Set<ParameterEntry> getEntries() {
        return Collections.unmodifiableSet(entries);
    }

    public void setEntries(Set<ParameterEntry> entries) {
        this.entries.addAll(entries);
    }

    @Override
    public char getArraySeparator() {
        return arraySeparator;
    }

    public void setArraySeparator(char arraySeparator) {
        this.arraySeparator = arraySeparator;
    }

    @Override
    public boolean isCacheable() {
        return cacheable;
    }

    public void setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
    }

    @Override
    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

}
