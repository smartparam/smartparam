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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.polyjdbc.core.query.QueryRunner;
import org.polyjdbc.core.query.TransactionalQueryRunner;
import org.polyjdbc.core.schema.SchemaManager;
import org.polyjdbc.core.schema.SchemaManagerImpl;
import org.polyjdbc.core.schema.model.Schema;
import org.polyjdbc.core.transaction.TransactionManager;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.repository.jdbc.config.Configuration;
import org.smartparam.repository.jdbc.model.JdbcParameter;

/**
 * @author Przemek Hertel
 * @since 0.2.0
 */
public class JdbcRepositoryDAOImpl implements JdbcRepositoryDAO {

    private Configuration configuration;

    private ParameterDAO parameterDAO;

    private LevelDAO levelDAO;

    private ParameterEntryDAO parameterEntryDAO;

    private TransactionManager transactionManager;

    public JdbcRepositoryDAOImpl(Configuration configuration, TransactionManager transactionManager, ParameterDAO parameterDAO, LevelDAO levelDAO, ParameterEntryDAO parameterEntryDAO) {
        this.configuration = configuration;
        checkConfiguration();
        this.transactionManager = transactionManager;
        this.parameterDAO = parameterDAO;
        this.levelDAO = levelDAO;
        this.parameterEntryDAO = parameterEntryDAO;
    }

    private void checkConfiguration() {
        if (configuration.getDialect() == null) {
            throw new SmartParamException("Provided JDBC repository configuration has no dialect defined!");
        }
    }

    @Override
    public void createSchema() {
        SchemaManager schemaManager = new SchemaManagerImpl(transactionManager.openTransaction());
        Schema schema = new Schema(configuration.getDialect());

        schema.addRelation(configuration.getParameterTable())
                .withAttribute().longAttr("id").notNull().and()
                .withAttribute().string("name").withMaxLength(200).notNull().unique().and()
                .withAttribute().integer("input_levels").notNull().and()
                .withAttribute().booleanAttr("cacheable").notNull().withDefaultValue(true).and()
                .withAttribute().booleanAttr("nullable").notNull().withDefaultValue(false).and()
                .withAttribute().character("array_separator").notNull().withDefaultValue(';').and()
                .primaryKey("pk_" + configuration.getParameterTable()).using("id").and()
                .build();
        schema.addSequence("seq_" + configuration.getParameterTable());
        schemaManager.create(schema);
        schemaManager.close();
    }

    private QueryRunner queryRunner() {
        return new TransactionalQueryRunner(transactionManager.openTransaction());
    }

    @Override
    public void createParameter(Parameter parameter) {
        QueryRunner runner = queryRunner();

        long parameterId = parameterDAO.insert(runner, parameter);
        for (Level level : parameter.getLevels()) {
            levelDAO.insert(runner, level, parameterId);
        }

        runner.close();
    }

    @Override
    public boolean parameterExists(String parameterName) {
        QueryRunner runner = queryRunner();
        boolean exists = parameterDAO.parameterExistst(runner, parameterName);
        runner.close();

        return exists;
    }

    @Override
    public JdbcParameter getParameter(String parameterName) {
        QueryRunner runner = queryRunner();

        JdbcParameter parameter = parameterDAO.getParameter(runner, parameterName);
        List<Level> levels = new ArrayList<Level>(levelDAO.getParameterLevels(runner, parameter.getId()));
        parameter.setLevels(levels);

        runner.close();
        return parameter;
    }

    @Override
    public Set<String> getParameterNames() {
        QueryRunner runner = queryRunner();
        Set<String> parameterNames = parameterDAO.getParameterNames(runner);
        runner.close();

        return parameterNames;
    }

    @Override
    public List<Level> getParameterLevels(long parameterId) {
        QueryRunner runner = queryRunner();
        List<Level> levels = new ArrayList<Level>(levelDAO.getParameterLevels(runner, parameterId));
        runner.close();
        return levels;
    }

    @Override
    public Set<ParameterEntry> getParameterEntries(long parameterId) {
        QueryRunner runner = queryRunner();
        Set<ParameterEntry> entries = getParameterEntries(parameterId);
        runner.close();

        return entries;
    }

    @Override
    public void deleteParameter(String parameterName) {
        QueryRunner runner = queryRunner();

        parameterEntryDAO.deleteParameterEntries(runner, parameterName);
        levelDAO.deleteParameterLevels(runner, parameterName);
        parameterDAO.delete(runner, parameterName);

        runner.close();
    }
}
