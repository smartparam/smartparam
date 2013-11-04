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
package org.smartparam.engine.test.builder;

import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.engine.model.editable.EditableParameterEntry;

/**
 *
 * @author Adam Dubiel
 */
public abstract class AbstractParameterEntryTestBuilder<T extends EditableParameterEntry, B extends AbstractParameterEntryTestBuilder<?, ?>> {

    private String[] levels;

    protected AbstractParameterEntryTestBuilder() {
    }

    protected abstract B self();

    protected abstract T buildEntry();

    public ParameterEntry build() {
        T entry = buildEntry();
        entry.setLevels(levels);

        return entry;
    }

    public B withLevels(String... levelValues) {
        this.levels = levelValues;
        return self();
    }
}