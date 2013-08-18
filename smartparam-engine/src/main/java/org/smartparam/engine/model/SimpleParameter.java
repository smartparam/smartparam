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

import java.util.List;
import java.util.Set;

/**
 *
 * @author Adam Dubiel
 */
public class SimpleParameter implements Parameter {

    protected String name;

    protected List<Level> levels;

    protected int inputLevels;

    protected Set<ParameterEntry> entries;

    protected char arraySeparator;

    protected boolean cacheable = true;

    protected boolean nullable = false;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Level> getLevels() {
        return levels;
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
}
