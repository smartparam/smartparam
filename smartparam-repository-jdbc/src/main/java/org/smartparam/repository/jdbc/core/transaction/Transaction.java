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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.smartparam.repository.jdbc.exception.SmartParamJdbcException;
import org.smartparam.repository.jdbc.core.query.Query;

/**
 *
 * @author Adam Dubiel
 */
public class Transaction {

    private Connection connection;

    private List<PreparedStatement> preparedStatements = new ArrayList<PreparedStatement>();

    private List<ResultSet> resultSets = new ArrayList<ResultSet>();

    // add rollback conditions
    // private Map - transaction preparedStatement cache
    public Transaction(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public int executeUpdate(Query query) {
        try {
            query.compile();
            PreparedStatement preparedStatement = connection.prepareStatement(query.getQuery());
            registerPrepareStatement(preparedStatement);
            query.injectValues(preparedStatement);
            return preparedStatement.executeUpdate();
        }
        catch(SQLException exception) {
            rollback();
            throw new SmartParamJdbcException(String.format("Failed to execute query %n%s", query.getQuery()), exception);
        }
    }

    public ResultSet executeQuery(Query query) {
        try {
            query.compile();
            PreparedStatement preparedStatement = connection.prepareStatement(query.getQuery());
            query.injectValues(preparedStatement);

            ResultSet resultSet = preparedStatement.executeQuery();
            registerCursor(resultSet);
            return resultSet;
        }
        catch(SQLException exception) {
            rollback();
            throw new SmartParamJdbcException(String.format("Failed to execute query %n%s", query.getQuery()), exception);
        }
    }

    public void registerPrepareStatement(PreparedStatement preparedStatement) {
        preparedStatements.add(0, preparedStatement);
    }

    public void registerCursor(ResultSet resultSet) {
        resultSets.add(0, resultSet);
    }

    public void commit() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.commit();
            }
        } catch (SQLException exception) {
            throw new SmartParamJdbcException("Failed to commit transaction transaction.", exception);
        }
    }

    public void rollback() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.rollback();
            }
        } catch (SQLException exception) {
            throw new SmartParamJdbcException("Failed to rollback transaction.", exception);
        }
    }

    public void closeWithArtifacts() {
        try {
            for (ResultSet resultSet : resultSets) {
                resultSet.close();
            }
            for (PreparedStatement statement : preparedStatements) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException exception) {
            throw new SmartParamJdbcException("Failed to rollback transaction.", exception);
        }
    }
}
