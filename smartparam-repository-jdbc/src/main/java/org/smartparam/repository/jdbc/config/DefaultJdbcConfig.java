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

import org.polyjdbc.core.dialect.Dialect;

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
public class DefaultJdbcConfig implements JdbcConfig {

    private Dialect dialect;

    private String parameterTable = "sp_parameter";

    private String levelTable = "sp_parameter_level";

    private String parameterEntryTable = "sp_parameter_entry";

    private String sequencePrefix = "seq_";

    private char excessLevelsSeparator = ';';

    private int levelColumnCount = 8;

    public DefaultJdbcConfig() {
    }

    public DefaultJdbcConfig(Dialect dialect) {
        this.dialect = dialect;
    }

    @Override
    public Dialect getDialect() {
        return dialect;
    }

    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }

    @Override
    public String[] getManagedTables() {
        return new String[] { parameterTable, levelTable, parameterEntryTable };
    }

    @Override
    public String getParameterTable() {
        return parameterTable;
    }

    public void setParameterTable(String parameterTable) {
        this.parameterTable = parameterTable;
    }

    @Override
    public String getLevelTable() {
        return levelTable;
    }

    public void setLevelTable(String parameterLevelTable) {
        this.levelTable = parameterLevelTable;
    }

    @Override
    public String getParameterEntryTable() {
        return parameterEntryTable;
    }

    @Override
    public String getSequencePrefix() {
        return sequencePrefix;
    }

    @Override
    public String getParameterSequence() {
        return sequencePrefix + parameterTable;
    }

    @Override
    public String getLevelSequence() {
        return sequencePrefix + levelTable;
    }

    @Override
    public String getParameterEntrySequence() {
        return sequencePrefix + parameterEntryTable;
    }

    public void setSequencePrefix(String sequencePrefix) {
        this.sequencePrefix = sequencePrefix;
    }

    public void setParameterEntryTable(String parameterEntryTable) {
        this.parameterEntryTable = parameterEntryTable;
    }

    public char getExcessLevelsSeparator() {
        return excessLevelsSeparator;
    }

    public void setExcessLevelsSeparator(char excessLevelsSeparator) {
        this.excessLevelsSeparator = excessLevelsSeparator;
    }

    public int getLevelColumnCount() {
        return levelColumnCount;
    }

    public void setLevelColumnCount(int levelColumnCount) {
        this.levelColumnCount = levelColumnCount;
    }
}
