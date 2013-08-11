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

import java.util.EnumMap;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.smartparam.repository.jdbc.dialect.Dialect;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class DataSourceFactory {

    private static final Map<Dialect, String> DIALECT_DRIVER_CLASS = new EnumMap<Dialect, String>(Dialect.class);

    static {
        DIALECT_DRIVER_CLASS.put(Dialect.H2, "org.h2.Driver");
        DIALECT_DRIVER_CLASS.put(Dialect.POSTGRESQL, "org.postgresql.Driver");
    }

    public static DataSource create(Dialect dialect, String databaseUrl, String user, String password) {
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName(DIALECT_DRIVER_CLASS.get(dialect));
        dataSource.setUrl(databaseUrl);
        dataSource.setUsername(user);
        dataSource.setPassword(password);

        return dataSource;
    }
}
