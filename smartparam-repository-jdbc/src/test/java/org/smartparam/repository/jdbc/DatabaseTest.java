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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.sql.DataSource;
import org.picocontainer.PicoContainer;
import org.polyjdbc.core.infrastructure.PolyDatabaseTest;
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
public class DatabaseTest extends PolyDatabaseTest {

    private SchemaCreator schemaCreator;

    private PicoContainer container;

    protected <T> T get(Class<T> objectClass) {
        return container.getComponent(objectClass);
    }

    protected DatabaseBuilder database() {
        return DatabaseBuilder.database(get(ParameterDAO.class), get(LevelDAO.class), get(ParameterEntryDAO.class), queryRunner());
    }

    protected DatabaseAssert assertDatabase() {
        return DatabaseAssert.assertThat(queryRunner(), get(ParameterDAO.class), get(LevelDAO.class), get(ParameterEntryDAO.class));
    }

    @BeforeClass(alwaysRun = true)
    public void setUpDatabase() throws Exception {
        DataSource dataSource = createDatabase("H2", "jdbc:h2:mem:test", "smartparam", "smartparam");

        JdbcConfigBuilder configurationBuilder = jdbcConfig().withDialect(dialect())
                .withParameterSufix("parameter").withLevelSufix("level")
                .withParameterEntrySufix("entry");
        customizeConfiguraion(configurationBuilder);
        DefaultJdbcConfig configuration = configurationBuilder.build();

        this.schemaCreator = new DefaultSchemaCreator(configuration, schemaManagerFactory());
        schemaCreator.createSchema();

        JdbcParamRepositoryFactory factory = new JdbcParamRepositoryFactory();
        this.container = factory.createContainer(new JdbcParamRepositoryConfig(dataSource, configuration));
    }

    protected void customizeConfiguraion(JdbcConfigBuilder builder) {
    }

    @BeforeMethod(alwaysRun = true)
    public void cleanDatabase() {
        JdbcConfig config = get(JdbcConfig.class);
        List<String> relationsToDelete = Arrays.asList(config.managedEntities());
        Collections.reverse(relationsToDelete);
        super.deleteFromRelations(relationsToDelete);
    }

    @AfterClass(alwaysRun = true)
    public void tearDownDatabase() throws Exception {
        dropDatabase();
        this.container = null;
    }

    @Override
    protected void dropSchema() {
        schemaCreator.dropSchema();
    }
}
