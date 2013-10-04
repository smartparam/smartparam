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
package org.smartparam.repository.jdbc.config.pico;

import java.util.List;
import javax.sql.DataSource;
import org.polyjdbc.core.query.SimpleQueryRunner;
import org.polyjdbc.core.transaction.DataSourceTransactionManager;
import org.smartparam.engine.config.pico.ComponentConfig;
import org.smartparam.repository.jdbc.config.Configuration;
import org.smartparam.repository.jdbc.config.JdbcParamRepositoryConfig;
import org.smartparam.repository.jdbc.dao.JdbcRepositoryDAOImpl;
import org.smartparam.repository.jdbc.dao.LevelDAO;
import org.smartparam.repository.jdbc.dao.ParameterDAO;
import org.smartparam.repository.jdbc.dao.ParameterEntryDAO;
import org.smartparam.repository.jdbc.schema.DefaultSchemaCreator;

/**
 *
 * @author Adam Dubiel
 */
public class PicoJdbcParamRepositoryConfig extends ComponentConfig implements JdbcParamRepositoryConfig {

    private DataSource dataSource;

    private Configuration configuration;

    public PicoJdbcParamRepositoryConfig(DataSource dataSource, Configuration configuration) {
        this.dataSource = dataSource;
        this.configuration = configuration;
    }

    @Override
    protected void injectDefaults(List<Object> components) {
        components.add(JdbcRepositoryDAOImpl.class);
        components.add(DataSourceTransactionManager.class);
        components.add(ParameterDAO.class);
        components.add(LevelDAO.class);
        components.add(ParameterEntryDAO.class);
        components.add(SimpleQueryRunner.class);
        components.add(DefaultSchemaCreator.class);
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }
}