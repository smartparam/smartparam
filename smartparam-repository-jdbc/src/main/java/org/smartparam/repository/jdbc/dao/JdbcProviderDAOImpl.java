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
import org.smartparam.repository.jdbc.core.mapper.StringMapper;
import java.util.List;
import java.util.Set;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.repository.jdbc.config.Configuration;
import org.smartparam.repository.jdbc.mapper.LevelMapper;
import org.smartparam.repository.jdbc.mapper.ParameterEntryMapper;
import org.smartparam.repository.jdbc.model.JdbcParameter;
import org.smartparam.repository.jdbc.core.query.Query;
import org.smartparam.repository.jdbc.core.query.QueryRunner;
import org.smartparam.repository.jdbc.schema.SchemaDescription;
import org.smartparam.repository.jdbc.schema.SchemaLookupResult;
import org.smartparam.repository.jdbc.schema.SchemaManager;
import org.smartparam.repository.jdbc.schema.SchemaDescriptionFactory;
import org.smartparam.repository.jdbc.core.transaction.Transaction;
import org.smartparam.repository.jdbc.core.transaction.TransactionManager;

/**
 * @author Przemek Hertel
 * @since 0.2.0
 */
public class JdbcProviderDAOImpl implements JdbcProviderDAO {

    private Configuration configuration;

    private QueryRunner queryRunner;

    private ParameterDAO parameterDAO;

    private SchemaManager schemaManager;

    private TransactionManager transactionManager;

    public JdbcProviderDAOImpl(Configuration configuration, QueryRunner queryRunner, SchemaManager schemaManager, TransactionManager transactionManager, ParameterDAO parameterDAO) {
        this.configuration = configuration;
        checkConfiguration();
        this.queryRunner = queryRunner;
        this.schemaManager = schemaManager;
        this.transactionManager = transactionManager;
        this.parameterDAO = parameterDAO;
    }

    private void checkConfiguration() {
        if (configuration.getDialect() == null) {
            throw new SmartParamException("Provided JDBC repository configuration has no dialect defined!");
        }
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

    @Override
    public void createParameter(Parameter parameter) {
        Transaction transaction = transactionManager.openTransaction();
        try {
            parameterDAO.insert(transaction, parameter);
            transaction.commit();
        }
        finally {
            transaction.closeWithArtifacts();
        }
    }

    @Override
    public boolean parameterExists(String parameterName) {
        Query query = Query.query("select * from " + configuration.getParameterTable() + " where name = :name");
        query.setString("name", parameterName);
        return queryRunner.queryForExistence(query);
    }

    @Override
    public JdbcParameter getParameter(String parameterName) {
        Query query = Query.query(" select id, input_levels, cacheable, nullable, array_separator"
                + " from " + configuration.getParameterTable()
                + " where name = :name");
        query.setString("name", parameterName);

        return queryRunner.queryForObject(query, new ParameterMapper());
    }

    @Override
    public Set<String> getParameterNames() {
        Query query = Query.query("select name from " + configuration.getParameterTable());

        return queryRunner.queryForSet(query, new StringMapper());
    }

    @Override
    public List<Level> getParameterLevels(int parameterId) {
        Query query = Query.query(" select id, order_no, label, type, matcher, level_creator, array_flag"
                + " from " + configuration.getParameterLevelTable()
                + " where param_id = :parameterId");
        query.setInt("parameterId", parameterId);

        return queryRunner.queryForList(query, new LevelMapper(parameterId));
    }

    @Override
    public Set<ParameterEntry> getParameterEntries(int parameterId) {
        Query query = Query.query("select id, level1, level2, level3, level4, level5, level6, level7, level8, value"
                + " from " + configuration.getParameterEntryTable()
                + " where param_id = :parameterId");
        query.setInt("parameterId", parameterId);

        return queryRunner.queryForSet(query, new ParameterEntryMapper(parameterId));
    }

    @Override
    public void dropParameter(String parameterName) {
        JdbcParameter parameter = getParameter(parameterName);

        Query dropEntriesQuery = Query.query("delete from " + configuration.getParameterEntryTable() + " where param_id = :parameterId");
        dropEntriesQuery.setInt("parameterId", parameter.getId());
        Query dropLevelsQuery = Query.query("delete from " + configuration.getParameterLevelTable() + " where param_id = :parameterId");
        dropLevelsQuery.setInt("parameterId", parameter.getId());
        Query dropParameterQuery = Query.query("delete from " + configuration.getParameterTable() + " where id = :id");
        dropParameterQuery.setInt("id", parameter.getId());

        queryRunner.execute(dropEntriesQuery, dropLevelsQuery, dropParameterQuery);
    }
}
