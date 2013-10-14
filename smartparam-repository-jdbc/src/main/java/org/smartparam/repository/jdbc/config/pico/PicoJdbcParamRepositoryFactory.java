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

import javax.sql.DataSource;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import org.smartparam.engine.config.pico.PicoContainerUtil;
import org.smartparam.repository.jdbc.JdbcParamRepository;
import org.smartparam.repository.jdbc.config.JdbcConfiguration;
import org.smartparam.repository.jdbc.config.JdbcParamRepositoryConfig;
import org.smartparam.repository.jdbc.config.JdbcParamRepositoryFactory;

/**
 *
 * @author Adam Dubiel
 */
public class PicoJdbcParamRepositoryFactory implements JdbcParamRepositoryFactory {

    public static JdbcParamRepository jdbcRepository(DataSource dataSource, JdbcConfiguration config) {
        return new PicoJdbcParamRepositoryFactory().createRepository(dataSource, config);
    }

    public JdbcParamRepository createRepository(DataSource dataSource, JdbcConfiguration config) {
        return createRepository(new PicoJdbcParamRepositoryConfig(dataSource, config));
    }

    @Override
    public JdbcParamRepository createRepository(JdbcParamRepositoryConfig config) {
        PicoJdbcParamRepositoryConfig picoConfig = (PicoJdbcParamRepositoryConfig) config;

        PicoContainer container = createContainer(picoConfig);

        return container.getComponent(JdbcParamRepository.class);
    }

    public PicoContainer createContainer(PicoJdbcParamRepositoryConfig config) {
        MutablePicoContainer container = PicoContainerUtil.createContainer();
        PicoContainerUtil.injectImplementations(container, JdbcParamRepository.class,
                                                config.getConfiguration(), config.getConfiguration().getDialect(), config.getDataSource());
        PicoContainerUtil.injectImplementations(container, config.getComponents());

        return container;
    }
}
