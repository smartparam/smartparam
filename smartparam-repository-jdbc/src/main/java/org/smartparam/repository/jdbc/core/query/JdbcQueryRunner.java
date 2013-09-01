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
package org.smartparam.repository.jdbc.core.query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.smartparam.repository.jdbc.exception.SmartParamJdbcException;
import org.smartparam.repository.jdbc.core.mapper.EmptyMapper;
import org.smartparam.repository.jdbc.core.mapper.ObjectMapper;
import org.smartparam.repository.jdbc.core.transaction.Transaction;
import org.smartparam.repository.jdbc.core.transaction.TransactionManager;

/**
 *
 * @author Adam Dubiel
 */
public class JdbcQueryRunner implements QueryRunner {

    private TransactionManager transactionManager;

    public JdbcQueryRunner(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public <T> List<T> queryForList(Query query, ObjectMapper<T> mapper) {
        List<T> objectList = new ArrayList<T>();
        query(query, mapper, objectList);
        return objectList;
    }

    @Override
    public <T> Set<T> queryForSet(Query query, ObjectMapper<T> mapper) {
        Set<T> objects = new HashSet<T>();
        query(query, mapper, objects);
        return objects;
    }

    @Override
    public <T> T queryForObject(Query query, ObjectMapper<T> mapper) {
        List<T> objects = queryForList(query, mapper);
        if (!objects.isEmpty()) {
            return objects.get(0);
        }
        return null;
    }

    @Override
    public boolean queryForExistence(Query query) {
        List<Object> objects = queryForList(query, new EmptyMapper());
        return !objects.isEmpty();
    }

    private <T> void query(Query query, ObjectMapper<T> mapper, Collection<T> objectCollection) {
        Transaction transaction = transactionManager.openTransaction();
        try {
            ResultSet resultSet = transaction.executeQuery(query);
            while (resultSet.next()) {
                objectCollection.add(mapper.createObject(resultSet));
            }
            transaction.commit();
        } catch (SQLException exception) {
            transaction.rollback();
            throw new SmartParamJdbcException("Failed to execute query", exception);
        } finally {
            transaction.closeWithArtifacts();
        }
    }

    @Override
    public void execute(Query... queries) {
        Transaction transaction = transactionManager.openTransaction();
        try {
            execute(transaction, queries);
        } finally {
            transaction.closeWithArtifacts();
        }
    }

    @Override
    public void execute(Transaction transaction, Query... queries) {
        for (Query query : queries) {
            transaction.executeUpdate(query);
        }
        transaction.commit();
    }
}
