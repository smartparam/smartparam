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
package org.smartparam.engine.model;

import org.smartparam.engine.core.parameter.Level;
import org.smartparam.engine.core.parameter.ParameterEntry;
import org.smartparam.engine.core.parameter.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Adam Dubiel
 */
class TestParameter implements Parameter {

    String name;

    List<Level> levels = new ArrayList<Level>();

    int inputLevels;

    Set<ParameterEntry> entries = new HashSet<ParameterEntry>();

    char arraySeparator = ';';

    boolean cacheable = true;

    boolean nullable = false;

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
}
