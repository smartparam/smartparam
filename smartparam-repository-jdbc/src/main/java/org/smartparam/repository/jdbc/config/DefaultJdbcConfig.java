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
 * <li>sp_level for parameter level</li>
 * <li>sp_parameter_entry for parameter entry</li>
 * </ul>
 *
 * Default sequence prefix is seq_.
 *
 * Default JDBC repository database structure uses denormalized entity to
 * hold parameter entries. This entity has N-columns, labeled from level0
 * to level(N-1) that hold parameter levels. If there are more levels than parameters,
 * excess levels are all concatenated and stored in last column. Default value
 * for N is 8 and default concatenated level separator is ';'.
 *
 * If you find yourself in hot-spot using JDBC repository, N value can be modified
 * to better reflect your needs. 8 is just a sensible default deducted from
 * previous experience with using SmartParam.
 *
 * @author Przemek Hertel
 */
public class DefaultJdbcConfig implements JdbcConfig {

    private static final int DEFAULT_LEVEL_COUNT = 8;

    private Dialect dialect;

    private String parameterSufix = "parameter";

    private String levelSufix = "level";

    private String parameterEntrySufix = "parameter_entry";

    private String entityPrefix = "sp_";

    private String sequencePrefix = "seq_";

    private String indexPrefix = "idx_";

    private String primaryKeyPrefix = "pk_";

    private String foreignKeyPrefix = "fk_";

    private char excessLevelsSeparator = ';';

    private int levelColumnCount = DEFAULT_LEVEL_COUNT;

    public DefaultJdbcConfig() {
    }

    public DefaultJdbcConfig(Dialect dialect) {
        this.dialect = dialect;
    }

    @Override
    public Dialect dialect() {
        return dialect;
    }

    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }

    @Override
    public String[] managedEntities() {
        return new String[]{parameterEntityName(), levelEntityName(), parameterEntryEntityName()};
    }

    public String sequencePrefix() {
        return this.sequencePrefix;
    }

    void sequencePrefix(String sequencePrefix) {
        this.sequencePrefix = sequencePrefix;
    }

    public String entityPrefix() {
        return this.entityPrefix;
    }

    void entityPrefix(String entityPrefix) {
        this.entityPrefix = entityPrefix;
    }

    @Override
    public String parameterEntityName() {
        return entityPrefix + parameterSufix;
    }

    @Override
    public String levelEntityName() {
        return entityPrefix + levelSufix;
    }

    @Override
    public String parameterEntryEntityName() {
        return entityPrefix + parameterEntrySufix;
    }

    void parameterSufix(String parameterSufix) {
        this.parameterSufix = parameterSufix;
    }

    void levelSufix(String levelSufix) {
        this.levelSufix = levelSufix;
    }

    void parameterEntrySufix(String parameterEntrySufix) {
        this.parameterEntrySufix = parameterEntrySufix;
    }

    @Override
    public String parameterSequenceName() {
        return sequencePrefix + parameterSufix;
    }

    @Override
    public String levelSequenceName() {
        return sequencePrefix + levelSufix;
    }

    @Override
    public String parameterEntrySequenceName() {
        return sequencePrefix + parameterEntrySufix;
    }

    public char excessLevelsSeparator() {
        return excessLevelsSeparator;
    }

    void excessLevelsSeparator(char excessLevelsSeparator) {
        this.excessLevelsSeparator = excessLevelsSeparator;
    }

    public int levelColumnCount() {
        return levelColumnCount;
    }

    void levelColumnCount(int levelColumnCount) {
        this.levelColumnCount = levelColumnCount;
    }

    public String indexPrefix() {
        return indexPrefix;
    }

    void indexPrefix(String indexPrefix) {
        this.indexPrefix = indexPrefix;
    }

    public String primaryKeyPrefix() {
        return primaryKeyPrefix;
    }

    void primaryKeyPrefix(String primaryKeyPrefix) {
        this.primaryKeyPrefix = primaryKeyPrefix;
    }

    public String foreignKeyPrefix() {
        return foreignKeyPrefix;
    }

    void foreignKeyPrefix(String foreignKeyPrefix) {
        this.foreignKeyPrefix = foreignKeyPrefix;
    }
}
