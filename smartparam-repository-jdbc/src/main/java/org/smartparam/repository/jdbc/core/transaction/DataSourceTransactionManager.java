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
package org.smartparam.repository.jdbc.core.transaction;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.repository.jdbc.exception.SmartParamJdbcException;

/**
 *
 * @author Adam Dubiel
 */
public class DataSourceTransactionManager implements TransactionManager {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceTransactionManager.class);

    private DataSource dataSource;

    public DataSourceTransactionManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Transaction openTransaction() {
        return openTransaction(false);
    }

    @Override
    public Transaction openTransaction(boolean autoCommit) {
        Connection connection;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(autoCommit);
            return new Transaction(connection);
        } catch (SQLException e) {
            throw new SmartParamJdbcException("Failed to obtain connection from datasource.", e);
        }
    }
}
