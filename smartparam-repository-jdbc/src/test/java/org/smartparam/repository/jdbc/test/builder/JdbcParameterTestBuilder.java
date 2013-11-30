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
package org.smartparam.repository.jdbc.test.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.repository.jdbc.model.JdbcParameter;

/**
 *
 * @author Adam Dubiel
 */
public class JdbcParameterTestBuilder {

    private String name;

    private final List<Level> levels = new ArrayList<Level>();

    private final Set<ParameterEntry> entries = new HashSet<ParameterEntry>();

    private int inputLevels;

    private boolean nullable;

    private boolean cacheable = true;

    private char arraySeparator;

    public static JdbcParameterTestBuilder jdbcParameter() {
        return new JdbcParameterTestBuilder();
    }

    public JdbcParameter build() {
        JdbcParameter parameter = new JdbcParameter(name, inputLevels);
        parameter.setLevels(levels);
        parameter.setEntries(entries);
        parameter.setNullable(nullable);
        parameter.setCacheable(cacheable);
        parameter.setArraySeparator(arraySeparator);

        return parameter;
    }

    public JdbcParameterTestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public JdbcParameterTestBuilder noncacheable() {
        this.cacheable = false;
        return this;
    }

    public JdbcParameterTestBuilder nullable() {
        this.nullable = true;
        return this;
    }

    public JdbcParameterTestBuilder withInputLevels(int inputLevels) {
        this.inputLevels = inputLevels;
        return this;
    }

    public JdbcParameterTestBuilder withArraySeparator(char separator) {
        this.arraySeparator = separator;
        return this;
    }

    public JdbcParameterTestBuilder withLevels(Level... levels) {
        this.levels.addAll(Arrays.asList(levels));
        return this;
    }

    public JdbcParameterTestBuilder withEntries(ParameterEntry... entries) {
        this.entries.addAll(Arrays.asList(entries));
        return this;
    }
}
