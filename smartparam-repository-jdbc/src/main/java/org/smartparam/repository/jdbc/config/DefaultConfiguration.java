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
package org.smartparam.repository.jdbc.config;

import org.smartparam.repository.jdbc.core.dialect.Dialect;

/**
 * Java bean implementation of JDBC configuration, provides default values for
 * table names (dialect has no default and is mandatory).
 *
 * Default table names are:
 * <ul>
 * <li>sp_parameter for parameter</li>
 * <li>sp_parameter_level for parameter level</li>
 * <li>sp_parameter_entry for parameter entry</li>
 * </ul>
 *
 *
 * @author Przemek Hertel
 */
public class DefaultConfiguration implements Configuration {

    private Dialect dialect;

    private String parameterTable = "sp_parameter";

    private String parameterLevelTable = "sp_parameter_level";

    private String parameterEntryTable = "sp_parameter_entry";

    public DefaultConfiguration() {
    }

    public DefaultConfiguration(Dialect dialect) {
        this.dialect = dialect;
    }

    @Override
    public Dialect getDialect() {
        return dialect;
    }

    @Override
    public String getParameterTable() {
        return parameterTable;
    }

    @Override
    public String getParameterLevelTable() {
        return parameterLevelTable;
    }

    @Override
    public String getParameterEntryTable() {
        return parameterEntryTable;
    }

    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }

    public void setParameterTable(String parameterTable) {
        this.parameterTable = parameterTable;
    }

    public void setParameterLevelTable(String parameterLevelTable) {
        this.parameterLevelTable = parameterLevelTable;
    }

    public void setParameterEntryTable(String parameterEntryTable) {
        this.parameterEntryTable = parameterEntryTable;
    }
}
