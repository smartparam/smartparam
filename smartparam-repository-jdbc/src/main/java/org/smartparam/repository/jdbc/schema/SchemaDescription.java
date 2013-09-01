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
package org.smartparam.repository.jdbc.schema;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.smartparam.repository.jdbc.config.Configuration;
import org.smartparam.repository.jdbc.core.dialect.Dialect;

/**
 *
 * @author Adam Dubiel
 */
public class SchemaDescription {

    private Configuration configuration;

    private Dialect dialect;

    private List<String> tables = new ArrayList<String>();

    private List<String> sequences = new ArrayList<String>();

    public SchemaDescription addTables(String... tableNames) {
        tables.addAll(Arrays.asList(tableNames));
        return this;
    }

    public SchemaDescription addSequences(String... sequenceNames) {
        sequences.addAll(Arrays.asList(sequenceNames));
        return this;
    }

    public List<String> getSequences() {
        return sequences;
    }

    public List<String> getTables() {
        return tables;
    }

    public Dialect getDialect() {
        return dialect;
    }

    public SchemaDescription setDialect(Dialect dialect) {
        this.dialect = dialect;
        return this;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
