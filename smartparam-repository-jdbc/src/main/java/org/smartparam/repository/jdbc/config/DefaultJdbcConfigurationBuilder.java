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
public class DefaultJdbcConfigurationBuilder extends AbstractConfigurationBuilder<DefaultJdbcConfiguration, DefaultJdbcConfigurationBuilder> {

    private DefaultJdbcConfigurationBuilder() {
        super(new DefaultJdbcConfiguration());
    }

    public static DefaultJdbcConfigurationBuilder defaultJdbcConfiguration() {
        return new DefaultJdbcConfigurationBuilder();
    }

    @Override
    protected DefaultJdbcConfigurationBuilder self() {
        return this;
    }

    public DefaultJdbcConfigurationBuilder withLevelColumnCount(int levelColumnCount) {
        configuration().setLevelColumnCount(levelColumnCount);
        return this;
    }

    public DefaultJdbcConfigurationBuilder withExcessLevelSeparator(char separator) {
        configuration().setExcessLevelsSeparator(separator);
        return this;
    }
}
