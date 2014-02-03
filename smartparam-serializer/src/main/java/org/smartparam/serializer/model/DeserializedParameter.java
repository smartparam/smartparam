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
package org.smartparam.serializer.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.smartparam.engine.core.parameter.level.Level;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.engine.core.parameter.ParameterKey;
import org.smartparam.engine.core.parameter.entry.ParameterEntry;
import org.smartparam.engine.core.parameter.identity.EmptyEntityKey;

/**
 *
 * @author Adam Dubiel
 */
public class DeserializedParameter implements AppendableParameter {

    private String name;

    private final List<Level> levels = new ArrayList<Level>();

    private int inputLevels;

    private final Set<ParameterEntry> entries = new HashSet<ParameterEntry>();

    private boolean cacheable = true;

    private boolean nullable;

    private boolean identifyEntries;

    private char arraySeparator = Parameter.DEFAULT_ARRAY_SEPARATOR;

    public DeserializedParameter() {
    }

    public DeserializedParameter(Parameter parameter) {
        this.name = parameter.getName();
        this.inputLevels = parameter.getInputLevels();
        this.cacheable = parameter.isCacheable();
        this.nullable = parameter.isNullable();
        this.identifyEntries = parameter.isIdentifyEntries();
        this.arraySeparator = parameter.getArraySeparator();

        for (Level level : parameter.getLevels()) {
            this.levels.add(new DeserializedLevel(level));
        }
    }

    @Override
    public ParameterKey getKey() {
        return EmptyEntityKey.emptyKey();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Level> getLevels() {
        return Collections.unmodifiableList(levels);
    }

    @Override
    public int getInputLevels() {
        return inputLevels;
    }

    @Override
    public Set<ParameterEntry> getEntries() {
        return Collections.unmodifiableSet(entries);
    }

    @Override
    public void appendEntries(Collection<ParameterEntry> entries) {
        this.entries.addAll(entries);
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
    public boolean isIdentifyEntries() {
        return identifyEntries;
    }

    @Override
    public char getArraySeparator() {
        return arraySeparator;
    }

}
