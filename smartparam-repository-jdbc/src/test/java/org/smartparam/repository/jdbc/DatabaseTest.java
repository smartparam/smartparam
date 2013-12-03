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

import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.lang.ArrayUtils;
import org.picocontainer.PicoContainer;
import org.polyjdbc.core.dialect.Dialect;
import org.polyjdbc.core.dialect.DialectRegistry;
import org.polyjdbc.core.integration.DataSourceFactory;
import org.polyjdbc.core.integration.TheCleaner;
import org.polyjdbc.core.key.KeyGeneratorRegistry;
import org.polyjdbc.core.query.QueryRunner;
import org.polyjdbc.core.query.QueryRunnerFactory;
import org.polyjdbc.core.schema.SchemaManagerFactory;
import org.polyjdbc.core.transaction.DataSourceTransactionManager;
import org.polyjdbc.core.transaction.Transaction;
import org.polyjdbc.core.transaction.TransactionManager;
import org.polyjdbc.core.util.TheCloser;
import org.smartparam.repository.jdbc.config.JdbcConfig;
import org.smartparam.repository.jdbc.config.DefaultJdbcConfig;
import org.smartparam.repository.jdbc.config.JdbcConfigBuilder;
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
import static org.smartparam.repository.jdbc.config.JdbcConfigBuilder.jdbcConfig;

/**
 *
 * @author Adam Dubiel
 */
public class DatabaseTest {

    private Dialect dialect;

    private TransactionManager transactionManager;

    private QueryRunnerFactory queryRunnerFactory;

    private SchemaCreator schemaCreator;

    private PicoContainer container;

    private TheCleaner cleaner;

    protected <T> T get(Class<T> objectClass) {
        return container.getComponent(objectClass);
    }

    protected QueryRunnerFactory queryRunnerFactory() {
        return queryRunnerFactory;
    }

    protected QueryRunner queryRunner() {
        return queryRunnerFactory.create();
    }

    protected void databaseInterface() {
        Transaction transaction = null;
        try {
            transaction = transactionManager.openTransaction();
            org.h2.tools.Server.startWebServer(transaction.getConnection());
        } catch (SQLException exception) {
            throw new IllegalStateException(exception);
        } finally {
            TheCloser.close(transaction);
        }
    }

    protected DatabaseBuilder database() {
        return DatabaseBuilder.database(get(ParameterDAO.class), get(LevelDAO.class), get(ParameterEntryDAO.class), queryRunner());
    }

    protected DatabaseAssert assertDatabase() {
        return DatabaseAssert.assertThat(queryRunner(), get(ParameterDAO.class), get(LevelDAO.class), get(ParameterEntryDAO.class));
    }

    @BeforeClass(alwaysRun = true)
    public void setUpDatabase() throws Exception {
        dialect = DialectRegistry.dialect("H2");

        JdbcConfigBuilder configurationBuilder = jdbcConfig().withDialect(dialect)
                .withParameterTableName("parameter").withLevelTableName("level")
                .withParameterEntryTableName("entry");
        customizeConfiguraion(configurationBuilder);
        DefaultJdbcConfig configuration = configurationBuilder.build();

        DataSource dataSource = DataSourceFactory.create(dialect, "jdbc:h2:mem:test", "smartparam", "smartparam");
        this.transactionManager = new DataSourceTransactionManager(dataSource);
        this.queryRunnerFactory = new QueryRunnerFactory(dialect, transactionManager);

        SchemaManagerFactory schemaManagerFactory = new SchemaManagerFactory(transactionManager);
        this.schemaCreator = new DefaultSchemaCreator(configuration, schemaManagerFactory);
        schemaCreator.createSchema();

        JdbcParamRepositoryFactory factory = new JdbcParamRepositoryFactory();
        this.container = factory.createContainer(new JdbcParamRepositoryConfig(dataSource, configuration));

        this.cleaner = new TheCleaner(queryRunnerFactory);
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
        KeyGeneratorRegistry.keyGenerator(dialect).reset();
        this.container = null;
    }
}
