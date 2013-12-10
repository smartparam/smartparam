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

import javax.sql.DataSource;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import org.smartparam.engine.config.pico.PicoContainerUtil;
import org.smartparam.repository.jdbc.config.JdbcConfig;

/**
 *
 * @author Adam Dubiel
 */
public class JdbcParamRepositoryFactory {

    public static JdbcParamRepository jdbcRepository(DataSource dataSource, JdbcConfig config) {
        return new JdbcParamRepositoryFactory().createRepository(dataSource, config);
    }

    public JdbcParamRepository createRepository(DataSource dataSource, JdbcConfig config) {
        return createRepository(new JdbcParamRepositoryConfig(dataSource, config));
    }

    public JdbcParamRepository createRepository(JdbcParamRepositoryConfig config) {
        PicoContainer container = createContainer(config);
        return container.getComponent(JdbcParamRepository.class);
    }

    PicoContainer createContainer(JdbcParamRepositoryConfig config) {
        MutablePicoContainer container = PicoContainerUtil.createContainer();
        PicoContainerUtil.injectImplementations(container, JdbcParamRepository.class,
                                                config.getConfiguration(), config.getConfiguration().dialect(), config.getDataSource());
        PicoContainerUtil.injectImplementations(container, config.getComponents());

        return container;
    }
}
