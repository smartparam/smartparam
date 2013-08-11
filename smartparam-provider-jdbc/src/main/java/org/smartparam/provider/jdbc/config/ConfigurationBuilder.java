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
package org.smartparam.provider.jdbc.config;

import org.smartparam.jdbc.dialect.Dialect;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class ConfigurationBuilder {

    private DefaultConfiguration configuration = new DefaultConfiguration();

    private ConfigurationBuilder() {
    }

    public static ConfigurationBuilder jdbcConfiguration() {
        return new ConfigurationBuilder();
    }

    public Configuration build() {
        return configuration;
    }

    public ConfigurationBuilder withDialect(Dialect dialect) {
        configuration.setDialect(dialect);
        return this;
    }

    public ConfigurationBuilder withParameterTableName(String parameterTableName) {
        configuration.setParameterTable(parameterTableName);
        return this;
    }

    public ConfigurationBuilder withParameterEntryTableName(String parameterEntryTableName) {
        configuration.setParameterEntryTable(parameterEntryTableName);
        return this;
    }

    public ConfigurationBuilder withLevelTableName(String levelTableName) {
        configuration.setParameterLevelTable(levelTableName);
        return this;
    }
}
