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
package org.smartparam.engine.model.editable;

import java.util.List;
import java.util.Set;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.engine.model.SimpleParameter;

/**
 *
 * @author Adam Dubiel
 */
public class SimpleEditableParameter extends SimpleParameter implements EditableParameter {

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setLevels(List<Level> levels) {
        this.levels = levels;
    }

    @Override
    public void setInputLevels(int inputLevels) {
        this.inputLevels = inputLevels;
    }

    @Override
    public void setEntries(Set<ParameterEntry> entries) {
        this.entries = entries;
    }

    @Override
    public void setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
    }

    @Override
    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    @Override
    public void setArraySeparator(char arraySeparator) {
        this.arraySeparator = arraySeparator;
    }
}
