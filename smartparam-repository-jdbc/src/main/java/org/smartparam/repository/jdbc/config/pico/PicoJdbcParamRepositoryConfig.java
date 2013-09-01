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
import org.smartparam.engine.config.pico.ComponentConfig;
import org.smartparam.repository.jdbc.config.Configuration;
import org.smartparam.repository.jdbc.config.JdbcParamRepositoryConfig;
import org.smartparam.repository.jdbc.dao.JdbcProviderDAOImpl;
import org.smartparam.repository.jdbc.core.query.JdbcQueryRunner;
import org.smartparam.repository.jdbc.query.loader.ClasspathQueryLoader;
import org.smartparam.repository.jdbc.schema.DDLSchemaManager;
import org.smartparam.repository.jdbc.core.transaction.DataSourceTransactionManager;

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
        components.add(JdbcProviderDAOImpl.class);
        components.add(JdbcQueryRunner.class);
        components.add(DDLSchemaManager.class);
        components.add(ClasspathQueryLoader.class);
        components.add(DataSourceTransactionManager.class);
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