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
package org.smartparam.repository.jdbc.integration;

import javax.sql.DataSource;
import org.apache.commons.lang.ArrayUtils;
import org.picocontainer.PicoContainer;
import org.polyjdbc.core.dialect.Dialect;
import org.polyjdbc.core.dialect.DialectRegistry;
import org.polyjdbc.core.integration.DataSourceFactory;
import org.polyjdbc.core.integration.TheCleaner;
import org.polyjdbc.core.query.QueryRunner;
import org.polyjdbc.core.query.TransactionalQueryRunner;
import org.polyjdbc.core.transaction.DataSourceTransactionManager;
import org.polyjdbc.core.transaction.Transaction;
import org.polyjdbc.core.transaction.TransactionManager;
import org.smartparam.repository.jdbc.config.JdbcConfig;
import org.smartparam.repository.jdbc.config.DefaultJdbcConfig;
import org.smartparam.repository.jdbc.config.JdbcConfigBuilder;
import org.smartparam.repository.jdbc.config.JdbcParamRepositoryConfig;
import org.smartparam.repository.jdbc.config.JdbcParamRepositoryFactory;
import org.smartparam.repository.jdbc.dao.LevelDAO;
import org.smartparam.repository.jdbc.dao.ParameterDAO;
import org.smartparam.repository.jdbc.dao.ParameterEntryDAO;
import org.smartparam.repository.jdbc.schema.DefaultSchemaCreator;
import org.smartparam.repository.jdbc.schema.SchemaCreator;
import org.smartparam.repository.jdbc.test.assertions.DatabaseAssert;
import org.smartparam.repository.jdbc.test.builder.DatabaseBuilder;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import static org.smartparam.repository.jdbc.config.ConfigurationBuilder.defaultConfiguration;

/**
 *
 * @author Adam Dubiel
 */
public class DatabaseTest {

    private TransactionManager transactionManager;

    private SchemaCreator schemaCreator;

    private PicoContainer container;

    private TheCleaner cleaner;

    protected <T> T get(Class<T> objectClass) {
        return container.getComponent(objectClass);
    }

    protected Transaction transaction() {
        return transactionManager.openTransaction();
    }

    protected TransactionManager transactionManager() {
        return transactionManager;
    }

    protected QueryRunner queryRunner() {
        return new TransactionalQueryRunner(transaction());
    }

    protected DatabaseBuilder database() {
        return DatabaseBuilder.database(get(ParameterDAO.class), get(LevelDAO.class), get(ParameterEntryDAO.class), transactionManager);
    }

    protected DatabaseAssert assertDatabase() {
        return DatabaseAssert.assertThat(transactionManager, get(ParameterDAO.class), get(LevelDAO.class), get(ParameterEntryDAO.class));
    }

    @BeforeClass(alwaysRun = true)
    public void setUpDatabase() throws Exception {
        Dialect dialect = DialectRegistry.dialect("H2");

        JdbcConfigBuilder configurationBuilder = defaultConfiguration().withDialect(dialect)
                .withParameterTableName("parameter").withLevelTableName("level")
                .withParameterEntryTableName("entry");
        customizeConfiguraion(configurationBuilder);
        DefaultJdbcConfig configuration = configurationBuilder.build();

        DataSource dataSource = DataSourceFactory.create(dialect, "jdbc:h2:mem:test", "smartparam", "smartparam");
        this.transactionManager = new DataSourceTransactionManager(dialect, dataSource);

        this.schemaCreator = new DefaultSchemaCreator(configuration, transactionManager);
        schemaCreator.createSchema();

        JdbcParamRepositoryFactory factory = new JdbcParamRepositoryFactory();
        this.container = factory.createContainer(new JdbcParamRepositoryConfig(dataSource, configuration));

        this.cleaner = new TheCleaner(transactionManager);
    }

    protected void customizeConfiguraion(JdbcConfigBuilder builder) {
    }

    @BeforeMethod(alwaysRun = true)
    public void cleanDatabase() {
        JdbcConfig config = get(JdbcConfig.class);
        String[] relationsToDelete = config.getManagedTables();
        ArrayUtils.reverse(relationsToDelete);
        cleaner.cleanDB(relationsToDelete);
    }

    @AfterClass(alwaysRun = true)
    public void tearDownDatabase() throws Exception {
        schemaCreator.dropSchema();
        this.container = null;
    }
}
