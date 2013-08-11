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
package org.smartparam.provider.jdbc.integration;

import javax.sql.DataSource;
import org.smartparam.jdbc.dialect.Dialect;
import org.smartparam.jdbc.query.JdbcQueryRunner;
import org.smartparam.jdbc.query.JdbcQueryRunnerImpl;
import org.smartparam.jdbc.schema.SchemaDescription;
import org.smartparam.jdbc.schema.SchemaLookupResult;
import org.smartparam.jdbc.schema.SchemaManager;
import org.smartparam.jdbc.schema.SchemaManagerImpl;
import org.smartparam.jdbc.schema.loader.ClasspathSchemaDefinitionLoader;
import org.smartparam.jdbc.schema.loader.SchemaDefinitionLoader;
import org.smartparam.provider.jdbc.config.Configuration;
import org.smartparam.provider.jdbc.dao.JdbcProviderDAOImpl;
import org.smartparam.provider.jdbc.schema.SchemaDefinitionPreparer;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.smartparam.provider.jdbc.config.ConfigurationBuilder.jdbcConfiguration;
import static org.smartparam.provider.jdbc.test.assertions.Assertions.*;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
@Test(groups = {"nativeDatabase"})
public class DDLTest {

    private SchemaManager schemaManager;

    private JdbcProviderDAOImpl dao;

    private Configuration configuration;


    @DataProvider(name = "databases")
    public Object[][] databases() {
        return new Object[][] {
            {Dialect.H2, "jdbc:h2:mem:test", "smartparam", "smartparam"}
        };
    }

    private void dynamicSetUpMethod(Dialect dialect, String url, String username, String password) {
        DataSource dataSource = DataSourceFactory.create(dialect, url, username, password);

        configuration = jdbcConfiguration().withDialect(dialect)
                .withParameterTableName("parameter").withLevelTableName("level").withParameterEntryTableName("entry").build();

        JdbcQueryRunner jdbcQueryRunner = new JdbcQueryRunnerImpl(dataSource);
        schemaManager = new SchemaManagerImpl(jdbcQueryRunner);

        dao = new JdbcProviderDAOImpl(dataSource);
        dao.setConfiguration(configuration);
    }

    private void dynamicTearDownMethod(Dialect dialect) {
        SchemaDefinitionLoader schemaDefinitionLoader = new ClasspathSchemaDefinitionLoader("/ddl/", ":dialect_drop_ddl.sql", ":dialect");
        SchemaDefinitionPreparer schemaDefinitionPreparer = new SchemaDefinitionPreparer();

        String ddl = schemaDefinitionPreparer.prepareQuery(schemaDefinitionLoader.getQuery(dialect), configuration);

        schemaManager.executeDDL(ddl);
    }

    @Test(dataProvider = "databases")
    public void shouldCreateSchemaOnEmptyDatabase(Dialect dialect, String url, String username, String password) throws Exception {
        dynamicSetUpMethod(dialect, url, username, password);
        // given
        SchemaDescription description = new SchemaDescription().addTables("parameter", "level", "entry")
                .addSequences("seq_parameter", "seq_level", "seq_entry").setDialect(Dialect.H2);
        dao.createSchema();

        // when
        SchemaLookupResult lookupResult = schemaManager.schemaExists(description);

        // then
        assertThat(lookupResult).hasTable("parameter").hasTable("level").hasTable("entry")
                .hasSequence("seq_parameter").hasSequence("seq_level").hasSequence("seq_entry");

        dynamicTearDownMethod(dialect);
    }
}
