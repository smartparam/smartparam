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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.engine.model.editable.EditableParameter;
import static org.smartparam.engine.test.builder.ParameterTestBuilder.parameter;

/**
 *
 * @author Adam Dubiel
 */
public abstract class AbstractParameterTestBuilder<T extends EditableParameter, B extends AbstractParameterTestBuilder<?, ?>> {

    private String name;

    private boolean cacheable = true;

    private boolean nullable;

    private int inputLevels;

    private char separator = ';';

    private List<Level> levels;

    private Set<ParameterEntry> entries;

    protected AbstractParameterTestBuilder() {
    }

    protected abstract B self();

    protected abstract T buildParameter();

    public T build() {
        T parameter = buildParameter();
        parameter.setName(name);
        parameter.setCacheable(cacheable);
        parameter.setNullable(nullable);
        parameter.setInputLevels(inputLevels);
        parameter.setArraySeparator(separator);
        parameter.setLevels(levels);
        parameter.setEntries(entries);

        return parameter;
    }

    public B withName(String name) {
        this.name = name;
        return self();
    }

    public B noncacheable() {
        this.cacheable = false;
        return self();
    }

    public B nullable() {
        this.nullable = true;
        return self();
    }

    public B withInputLevels(int inputLevels) {
        this.inputLevels = inputLevels;
        return self();
    }

    public B withArraySeparator(char separator) {
        this.separator = separator;
        return self();
    }

    public B withLevels(Level... levels) {
        this.levels = Arrays.asList(levels);
        return self();
    }

    public B withEntries(ParameterEntry... entries) {
        this.entries = new HashSet<ParameterEntry>(Arrays.asList(entries));
        return self();
    }
}
