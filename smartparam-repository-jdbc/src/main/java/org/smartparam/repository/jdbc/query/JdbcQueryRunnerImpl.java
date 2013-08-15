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
package org.smartparam.repository.jdbc.query;

import org.smartparam.repository.jdbc.mapper.ObjectMapper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.repository.jdbc.exception.SmartParamJdbcException;
import org.smartparam.repository.jdbc.mapper.EmptyMapper;

/**
 *
 * @author Adam Dubiel
 */
public class JdbcQueryRunnerImpl implements JdbcQueryRunner {

    private static final Logger logger = LoggerFactory.getLogger(JdbcQueryRunnerImpl.class);

    private DataSource dataSource;

    public JdbcQueryRunnerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public <T> List<T> queryForList(JdbcQuery query, ObjectMapper<T> mapper) {
        List<T> objectList = new ArrayList<T>();
        query(query, mapper, objectList);
        return objectList;
    }

    @Override
    public <T> Set<T> queryForSet(JdbcQuery query, ObjectMapper<T> mapper) {
        Set<T> objects = new HashSet<T>();
        query(query, mapper, objects);
        return objects;
    }

    @Override
    public <T> T queryForObject(JdbcQuery query, ObjectMapper<T> mapper) {
        List<T> objects = queryForList(query, mapper);
        if (!objects.isEmpty()) {
            return objects.get(0);
        }
        return null;
    }

    @Override
    public boolean queryForExistence(JdbcQuery query) {
        List<Object> objects = queryForList(query, new EmptyMapper());
        return !objects.isEmpty();
    }

    private <T> void query(JdbcQuery query, ObjectMapper<T> mapper, Collection<T> objectCollection) {
        Connection connection = openConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(query.getQuery());
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                objectCollection.add(mapper.createObject(resultSet));
            }
        } catch (SQLException e) {
            throw new SmartParamJdbcException("Failed to execute query", e);
        } finally {
            closeConnection(connection, preparedStatement, resultSet);
        }
    }

    @Override
    public void execute(String ddl) {
        Connection connection = openConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(ddl);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SmartParamJdbcException("Failed to execute DDL", e);
        } finally {
            closeConnection(connection, preparedStatement, null);
        }
    }

    private Connection openConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new SmartParamJdbcException("Failed to obtain connection from datasource", e);
        }
    }

    private void closeConnection(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException exception) {
            logger.error("failed to cleanup resources", exception);
        }
    }
}
