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
package org.smartparam.repository.jdbc.config;

/**
 *
 * @author Adam Dubiel
 */
public class JdbcConfigBuilder extends AbstractConfigurationBuilder<DefaultJdbcConfig, JdbcConfigBuilder> {

    private JdbcConfigBuilder() {
        super(new DefaultJdbcConfig());
    }

    public static JdbcConfigBuilder jdbcConfig() {
        return new JdbcConfigBuilder();
    }

    @Override
    protected JdbcConfigBuilder self() {
        return this;
    }

    public JdbcConfigBuilder withLevelColumnCount(int levelColumnCount) {
        configuration().setLevelColumnCount(levelColumnCount);
        return this;
    }

    public JdbcConfigBuilder withExcessLevelSeparator(char separator) {
        configuration().setExcessLevelsSeparator(separator);
        return this;
    }
}