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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.sql.DataSource;
import org.picocontainer.PicoContainer;
import org.smartparam.repository.jdbc.config.Configuration;
import org.smartparam.repository.jdbc.config.pico.PicoJdbcParamRepositoryConfig;
import org.smartparam.repository.jdbc.config.pico.PicoJdbcParamRepositoryFactory;
import org.smartparam.repository.jdbc.core.dialect.Dialect;
import org.testng.annotations.DataProvider;
import static org.smartparam.repository.jdbc.config.ConfigurationBuilder.jdbcConfiguration;

/**
 *
 * @author Adam Dubiel
 */
public class ContainerDataProvider {

    private static final String[][] databases = new String[][]{
        {"H2", "jdbc:h2:mem:test", "smartparam", "smartparam"}
//        {Dialect.POSTGRESQL, "jdbc:postgresql://localhost/smartparam", "smartparam", "smartparam"},
//        {Dialect.MYSQL, "jdbc:mysql://localhost/smartparam?characterEncoding=UTF-8&allowMultiQueries=true", "smartparam", "smartparam"}
    };

    private static final ContainerDataProvider provider;

    private PicoContainer[] containers;

    static {
        provider = new ContainerDataProvider();
        provider.initializeContainers();
    }

    public ContainerDataProvider() {
    }

    @DataProvider(name = "containers")
    public static Iterator<Object[]> databaseProvider() {
        List<Object[]> testData = new ArrayList<Object[]>();
        for (PicoContainer container : provider.getContainers()) {
            testData.add(new Object[]{container});
        }

        return testData.iterator();
    }

    public void initializeContainers() {
        List<PicoContainer> containerList = new ArrayList<PicoContainer>(databases.length);
        for (String[] database : databases) {
            Dialect dialect = Dialect.valueOf(database[0]);

            Configuration configuration = jdbcConfiguration().withDialect(dialect)
                    .withParameterTableName("parameter").withLevelTableName("level")
                    .withParameterEntryTableName("entry").build();
            DataSource dataSource = DataSourceFactory.create(dialect, database[1], database[2], database[3]);

            PicoJdbcParamRepositoryFactory factory = new PicoJdbcParamRepositoryFactory();
            PicoContainer container = factory.createContainer(new PicoJdbcParamRepositoryConfig(dataSource, configuration));

            containerList.add(container);
        }

        this.containers = containerList.toArray(new PicoContainer[databases.length]);
    }

    public PicoContainer[] getContainers() {
        return containers;
    }
}
