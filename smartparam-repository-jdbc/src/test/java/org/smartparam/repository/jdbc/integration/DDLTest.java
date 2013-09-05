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

import org.picocontainer.PicoContainer;
import org.smartparam.repository.jdbc.schema.SchemaDescription;
import org.smartparam.repository.jdbc.schema.SchemaLookupResult;
import org.smartparam.repository.jdbc.schema.SchemaManager;
import org.smartparam.repository.jdbc.config.Configuration;
import org.smartparam.repository.jdbc.dao.JdbcProviderDAO;
import org.testng.annotations.Test;
import static org.smartparam.repository.jdbc.test.assertions.Assertions.*;

/**
 *
 * @author Adam Dubiel
 */
public class DDLTest {

    @Test(dataProvider = "databases", dataProviderClass = TestIntegrationDataProvider.class, groups = "integration.setUp")
    public void shouldCreateSchemaOnEmptyDatabase(PicoContainer container) throws Exception {
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

    @Test(dataProvider = "databases", dataProviderClass = TestIntegrationDataProvider.class, groups = "integration.tearDown", dependsOnGroups = "integration")
    public void shouldDropSchemaFromPopoulatedDatabase(PicoContainer container) {
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
