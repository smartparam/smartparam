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
package org.smartparam.repository.jdbc;

import java.util.Set;
import javax.sql.DataSource;
import org.polyjdbc.core.query.QueryRunnerFactory;
import org.polyjdbc.core.query.TransactionRunner;
import org.polyjdbc.core.query.SimpleQueryRunner;
import org.polyjdbc.core.schema.SchemaManagerFactory;
import org.polyjdbc.core.transaction.DataSourceTransactionManager;
import org.polyjdbc.core.transaction.TransactionManager;
import org.smartparam.engine.config.pico.ComponentConfig;
import org.smartparam.engine.config.pico.ComponentDefinition;
import org.smartparam.repository.jdbc.batch.JdbcParameterEntryBatchLoaderFactory;
import org.smartparam.repository.jdbc.config.JdbcConfig;
import org.smartparam.repository.jdbc.dao.JdbcRepository;
import org.smartparam.repository.jdbc.dao.SimpleJdbcRepository;
import org.smartparam.repository.jdbc.dao.LevelDAO;
import org.smartparam.repository.jdbc.dao.ParameterDAO;
import org.smartparam.repository.jdbc.dao.ParameterEntryDAO;
import org.smartparam.repository.jdbc.schema.DefaultSchemaCreator;
import static org.smartparam.engine.config.pico.ComponentDefinition.component;

/**
 *
 * @author Adam Dubiel
 */
public class JdbcParamRepositoryConfig extends ComponentConfig {

    private final DataSource dataSource;

    private final JdbcConfig configuration;

    public JdbcParamRepositoryConfig(DataSource dataSource, JdbcConfig configuration) {
        this.dataSource = dataSource;
        this.configuration = configuration;
    }

    @Override
    protected void injectDefaults(Set<ComponentDefinition> components) {
        components.add(component(JdbcRepository.class, SimpleJdbcRepository.class));
        components.add(component(TransactionManager.class, DataSourceTransactionManager.class));
        components.add(component(QueryRunnerFactory.class, QueryRunnerFactory.class));
        components.add(component(SchemaManagerFactory.class, SchemaManagerFactory.class));
        components.add(component(ParameterDAO.class, ParameterDAO.class));
        components.add(component(LevelDAO.class, LevelDAO.class));
        components.add(component(ParameterEntryDAO.class, ParameterEntryDAO.class));
        components.add(component(SimpleQueryRunner.class, SimpleQueryRunner.class));
        components.add(component(DefaultSchemaCreator.class, DefaultSchemaCreator.class));
        components.add(component(TransactionRunner.class, TransactionRunner.class));
        components.add(component(JdbcParameterEntryBatchLoaderFactory.class, JdbcParameterEntryBatchLoaderFactory.class));
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public JdbcConfig getConfiguration() {
        return configuration;
    }
}
