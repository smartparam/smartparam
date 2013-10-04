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
import org.picocontainer.PicoContainer;
import org.polyjdbc.core.dialect.Dialect;
import org.polyjdbc.core.dialect.DialectRegistry;
import org.polyjdbc.core.integration.DataSourceFactory;
import org.polyjdbc.core.integration.TestSchemaManager;
import org.polyjdbc.core.integration.TheCleaner;
import org.polyjdbc.core.query.QueryRunner;
import org.polyjdbc.core.query.TransactionalQueryRunner;
import org.polyjdbc.core.transaction.DataSourceTransactionManager;
import org.polyjdbc.core.transaction.Transaction;
import org.polyjdbc.core.transaction.TransactionManager;
import org.smartparam.repository.jdbc.config.Configuration;
import org.smartparam.repository.jdbc.config.pico.PicoJdbcParamRepositoryConfig;
import org.smartparam.repository.jdbc.config.pico.PicoJdbcParamRepositoryFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import static org.smartparam.repository.jdbc.config.ConfigurationBuilder.jdbcConfiguration;

/**
 *
 * @author Adam Dubiel
 */
public class DatabaseTest {

    private TransactionManager transactionManager;

    private TestSchemaManager schemaManager;

    private PicoContainer container;

    private TheCleaner cleaner;

    protected <T> T get(Class<T> objectClass) {
        return container.getComponent(objectClass);
    }

    protected Transaction transaction() {
        return transactionManager.openTransaction();
    }

    protected QueryRunner queryRunner() {
        return new TransactionalQueryRunner(transaction());
    }

    @Parameters({"dialect", "url", "user", "password"})
    @BeforeClass(alwaysRun = true)
    public void setUpDatabase() throws Exception {
        Dialect dialect = DialectRegistry.dialect("H2");

        Configuration configuration = jdbcConfiguration().withDialect(dialect)
                .withParameterTableName("parameter").withLevelTableName("level")
                .withParameterEntryTableName("entry").build();
        DataSource dataSource = DataSourceFactory.create(dialect, "jdbc:h2:mem:test", "smartparam", "smartparam");
        this.transactionManager = new DataSourceTransactionManager(dialect, dataSource);
        this.schemaManager = new TestSchemaManager(dialect);

        PicoJdbcParamRepositoryFactory factory = new PicoJdbcParamRepositoryFactory();
        this.container = factory.createContainer(new PicoJdbcParamRepositoryConfig(dataSource, configuration));

        this.cleaner = new TheCleaner(transactionManager);
        this.schemaManager.createSchema(transactionManager);
    }

    @BeforeMethod(alwaysRun = true)
    public void cleanDatabase() {
        Configuration config = get(Configuration.class);
        cleaner.cleanDB(config.getParameterEntryTable(), config.getLevelTable(), config.getParameterTable());
    }

    @AfterClass(alwaysRun = true)
    public void tearDownDatabase() throws Exception {
        dropSchema(container);
        this.container = null;
    }

    private void dropSchema(PicoContainer container) {
        schemaManager.dropSchema(transactionManager);
    }
}
