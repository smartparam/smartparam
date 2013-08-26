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
package org.smartparam.repository.jdbc.dao;

import org.smartparam.repository.jdbc.mapper.ParameterMapper;
import org.smartparam.repository.jdbc.mapper.StringMapper;
import java.util.List;
import java.util.Set;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.repository.jdbc.config.Configuration;
import org.smartparam.repository.jdbc.mapper.LevelMapper;
import org.smartparam.repository.jdbc.mapper.ParameterEntryMapper;
import org.smartparam.repository.jdbc.model.JdbcParameter;
import org.smartparam.repository.jdbc.query.JdbcQuery;
import org.smartparam.repository.jdbc.query.JdbcQueryRunner;
import org.smartparam.repository.jdbc.schema.SchemaDescription;
import org.smartparam.repository.jdbc.schema.SchemaLookupResult;
import org.smartparam.repository.jdbc.schema.SchemaManager;
import org.smartparam.repository.jdbc.schema.SchemaDescriptionFactory;

/**
 * @author Przemek Hertel
 * @since 0.2.0
 */
public class JdbcProviderDAOImpl implements JdbcProviderDAO {

    private Configuration configuration;

    private JdbcQueryRunner queryRunner;

    private SchemaManager schemaManager;

    public JdbcProviderDAOImpl(Configuration configuration, JdbcQueryRunner queryRunner, SchemaManager schemaManager) {
        this.configuration = configuration;
        checkConfiguration();
        this.queryRunner = queryRunner;
        this.schemaManager = schemaManager;
    }

    @Override
    public void createSchema() {
        SchemaDescription description = SchemaDescriptionFactory.createSchemaDescription(configuration);
        SchemaLookupResult result = schemaManager.schemaExists(description);

        if (result.noEntityExisting()) {
            schemaManager.createSchema(description);
        } else if (!result.noEntityMissing()) {
            throw new SmartParamException("JDBC repository detected partial SmartParam schema in database. "
                    + "This version of JDBC repository has no schema update capabilities, remove old SmartParam entities "
                    + "from the database or use different naming schema. Detected partial entities: " + result.getExistingEntities());
        }
    }

    private void checkConfiguration() {
        if (configuration.getDialect() == null) {
            throw new SmartParamException("Provided JDBC repository configuration has no dialect defined!");
        }
    }

    @Override
    public JdbcParameter getParameter(String parameterName) {
        JdbcQuery query = JdbcQuery.query(" select id, input_levels, cacheable, nullable, array_separator"
                + " from " + configuration.getParameterTable()
                + " where name = :name");
        query.setString("name", parameterName);

        return queryRunner.queryForObject(query, new ParameterMapper());
    }

    @Override
    public Set<String> getParameterNames() {
        JdbcQuery query = JdbcQuery.query("select name from " + configuration.getParameterTable());

        return queryRunner.queryForSet(query, new StringMapper());
    }

    @Override
    public List<Level> getParameterLevels(int parameterId) {
        JdbcQuery query = JdbcQuery.query(" select id, order_no, label, type, matcher, level_creator, array_flag"
                + " from " + configuration.getParameterLevelTable()
                + " where param_id = :parameterId");
        query.setInt("parameterId", parameterId);

        return queryRunner.queryForList(query, new LevelMapper(parameterId));
    }

    @Override
    public Set<ParameterEntry> getParameterEntries(int parameterId) {
        JdbcQuery query = JdbcQuery.query("select id, level1, level2, level3, level4, level5, level6, level7, level8, value"
                + " from " + configuration.getParameterEntryTable()
                + " where param_id = :parameterId");
        query.setInt("parameterId", parameterId);

        return queryRunner.queryForSet(query, new ParameterEntryMapper(parameterId));
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
