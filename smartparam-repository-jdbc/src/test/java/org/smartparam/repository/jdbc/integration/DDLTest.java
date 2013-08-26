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
import org.smartparam.repository.jdbc.dialect.Dialect;
import org.smartparam.repository.jdbc.query.JdbcQueryRunner;
import org.smartparam.repository.jdbc.query.JdbcQueryRunnerImpl;
import org.smartparam.repository.jdbc.schema.SchemaDescription;
import org.smartparam.repository.jdbc.schema.SchemaLookupResult;
import org.smartparam.repository.jdbc.schema.SchemaManager;
import org.smartparam.repository.jdbc.schema.DDLSchemaManager;
import org.smartparam.repository.jdbc.config.Configuration;
import org.smartparam.repository.jdbc.dao.JdbcProviderDAOImpl;
import org.smartparam.repository.jdbc.query.loader.ClasspathQueryLoader;
import org.smartparam.repository.jdbc.query.loader.QueryLoader;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.smartparam.repository.jdbc.config.ConfigurationBuilder.jdbcConfiguration;
import static org.smartparam.repository.jdbc.test.assertions.Assertions.*;

/**
 *
 * @author Adam Dubiel
 */
@Test(groups = {"nativeDatabase"})
public class DDLTest {

    private SchemaManager schemaManager;

    private JdbcProviderDAOImpl dao;

    private Configuration configuration;

    @DataProvider(name = "databases")
    public Object[][] databases() {
        return new Object[][]{
            {Dialect.H2, "jdbc:h2:mem:test", "smartparam", "smartparam"},
            {Dialect.POSTGRESQL, "jdbc:postgresql://localhost/smartparam", "smartparam", "smartparam"},
            {Dialect.MYSQL, "jdbc:mysql://localhost/smartparam?characterEncoding=UTF-8&allowMultiQueries=true", "smartparam", "smartparam"}
        };
    }

    private void dynamicSetUpMethod(Dialect dialect, String url, String username, String password) {
        DataSource dataSource = DataSourceFactory.create(dialect, url, username, password);

        configuration = jdbcConfiguration().withDialect(dialect)
                .withParameterTableName("parameter").withLevelTableName("level").withParameterEntryTableName("entry").build();

        JdbcQueryRunner jdbcQueryRunner = new JdbcQueryRunnerImpl(dataSource);
        QueryLoader queryLoader = new ClasspathQueryLoader();
        schemaManager = new DDLSchemaManager(jdbcQueryRunner, queryLoader);

        dao = new JdbcProviderDAOImpl(configuration, dataSource);
    }

    private void dynamicTearDownMethod(SchemaDescription description) {
        schemaManager.dropSchema(description);
    }

    @Test(dataProvider = "databases")
    public void shouldCreateSchemaOnEmptyDatabase(Dialect dialect, String url, String username, String password) throws Exception {
        dynamicSetUpMethod(dialect, url, username, password);
        // given
        SchemaDescription description = new SchemaDescription().addTables("parameter", "level", "entry")
                .addSequences("seq_parameter", "seq_level", "seq_entry").setDialect(dialect);
        description.setConfiguration(configuration);
        dao.createSchema();

        // when
        SchemaLookupResult lookupResult = schemaManager.schemaExists(description);

        // then
        assertThat(lookupResult).hasTable("parameter").hasTable("level").hasTable("entry");
        if (dialect.getProperties().hasSequences()) {
            assertThat(lookupResult).hasSequence("seq_parameter").hasSequence("seq_level").hasSequence("seq_entry");
        }

        dynamicTearDownMethod(description);
    }
}
