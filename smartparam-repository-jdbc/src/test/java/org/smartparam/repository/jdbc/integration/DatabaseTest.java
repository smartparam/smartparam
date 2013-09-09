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
import org.smartparam.repository.jdbc.config.Configuration;
import org.smartparam.repository.jdbc.config.pico.PicoJdbcParamRepositoryConfig;
import org.smartparam.repository.jdbc.config.pico.PicoJdbcParamRepositoryFactory;
import org.smartparam.repository.jdbc.core.dialect.Dialect;
import org.smartparam.repository.jdbc.core.query.QueryRunner;
import org.smartparam.repository.jdbc.dao.JdbcProviderDAO;
import org.smartparam.repository.jdbc.schema.SchemaDescription;
import org.smartparam.repository.jdbc.schema.SchemaLookupResult;
import org.smartparam.repository.jdbc.schema.SchemaManager;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import static org.smartparam.repository.jdbc.config.ConfigurationBuilder.jdbcConfiguration;
import static org.smartparam.repository.jdbc.test.assertions.Assertions.assertThat;

/**
 *
 * @author Adam Dubiel
 */
public class DatabaseTest {

    private PicoContainer container;

    private TheCleaner cleaner;

    protected <T> T get(Class<T> objectClass) {
        return container.getComponent(objectClass);
    }

    @Parameters({"dialect", "url", "user", "password"})
    @BeforeClass(alwaysRun = true)
    public void setUpDatabase(@Optional("H2") String dialectString, @Optional("jdbc:h2:mem:test") String url, @Optional("smartpara") String user, @Optional("smartpara") String password) throws Exception {
        Dialect dialect = Dialect.valueOf(dialectString);

        Configuration configuration = jdbcConfiguration().withDialect(dialect)
                .withParameterTableName("parameter").withLevelTableName("level")
                .withParameterEntryTableName("entry").build();
        DataSource dataSource = DataSourceFactory.create(dialect, url, user, password);

        PicoJdbcParamRepositoryFactory factory = new PicoJdbcParamRepositoryFactory();
        this.container = factory.createContainer(new PicoJdbcParamRepositoryConfig(dataSource, configuration));

        this.cleaner = new TheCleaner(container.getComponent(QueryRunner.class));

        createSchema(container);
    }

    private void createSchema(PicoContainer container) throws Exception {
        // given
        Configuration configuration = container.getComponent(Configuration.class);
        JdbcProviderDAO dao = container.getComponent(JdbcProviderDAO.class);
        SchemaManager schemaManager = container.getComponent(SchemaManager.class);

        SchemaDescription description = new SchemaDescription().addTables("parameter", "level", "entry")
                .addSequences("seq_parameter", "seq_level", "seq_entry").setDialect(configuration.getDialect());
        description.setConfiguration(configuration);
        dao.createSchema();

        // when
        SchemaLookupResult lookupResult = schemaManager.schemaExists(description);

        // then
        assertThat(lookupResult).hasTable("parameter").hasTable("level").hasTable("entry");
        if (configuration.getDialect().getProperties().hasSequences()) {
            assertThat(lookupResult).hasSequence("seq_parameter").hasSequence("seq_level").hasSequence("seq_entry");
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void cleanDatabase() {
        cleaner.cleanDB(get(Configuration.class));
    }

    @AfterClass(alwaysRun = true)
    public void tearDownDatabase() throws Exception {
        dropSchema(container);
        this.container = null;
    }

    private void dropSchema(PicoContainer container) {
        // given
        Configuration configuration = container.getComponent(Configuration.class);
        SchemaManager schemaManager = container.getComponent(SchemaManager.class);
        SchemaDescription description = new SchemaDescription().addTables("parameter", "level", "entry")
                .addSequences("seq_parameter", "seq_level", "seq_entry").setDialect(configuration.getDialect())
                .setConfiguration(configuration);

        // when
        schemaManager.dropSchema(description);

        // then
    }
}
