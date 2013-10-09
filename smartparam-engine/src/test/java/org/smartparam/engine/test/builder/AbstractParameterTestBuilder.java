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
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.engine.model.editable.EditableParameter;

/**
 *
 * @author Adam Dubiel
 */
public abstract class AbstractParameterTestBuilder<T extends EditableParameter, B extends AbstractParameterTestBuilder<?, ?>> {

    protected T parameter;

    protected AbstractParameterTestBuilder(T parameter) {
        this.parameter = parameter;
    }

    public T build() {
        return parameter;
    }

    protected abstract B self();

    public B withName(String name) {
        parameter.setName(name);
        return self();
    }

    public B noncacheable() {
        parameter.setCacheable(false);
        return self();
    }

    public B nullable() {
        parameter.setNullable(true);
        return self();
    }

    public B withInputLevels(int inputLevels) {
        parameter.setInputLevels(inputLevels);
        return self();
    }

    public B withArraySeparator(char separator) {
        parameter.setArraySeparator(separator);
        return self();
    }

    public B withLevels(Level... levels) {
        parameter.setLevels(Arrays.asList(levels));
        return self();
    }

    public B withEntries(ParameterEntry... entries) {
        parameter.setEntries(new HashSet<ParameterEntry>(Arrays.asList(entries)));
        return self();
    }
}
