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
package org.smartparam.repository.jdbc.dao;

import java.util.Set;
import org.smartparam.engine.model.Parameter;
import org.smartparam.repository.jdbc.config.Configuration;
import org.smartparam.repository.jdbc.core.dialect.query.DialectQueryBuilder;
import org.smartparam.repository.jdbc.core.mapper.StringMapper;
import org.smartparam.repository.jdbc.core.query.Query;
import org.smartparam.repository.jdbc.core.query.QueryRunner;
import org.smartparam.repository.jdbc.core.transaction.Transaction;
import org.smartparam.repository.jdbc.mapper.ParameterMapper;

/**
 *
 * @author Adam Dubiel
 */
public class ParameterDAO {

    private QueryRunner queryRunner;

    private final String insertQuery;

    private final String deleteQuery;

    private final String listNamesQuery;

    private final String selectQuery;

    public ParameterDAO(Configuration configuration, QueryRunner queryRunner) {
        this.queryRunner = queryRunner;

        this.insertQuery = DialectQueryBuilder.insert(configuration.getDialect()).into(configuration.getParameterTable())
                .id("id", "seq_parameter")
                .value("name", ":name")
                .value("input_levels", ":inputLevels")
                .value("cacheable", ":cacheable")
                .value("nullable", ":nullable")
                .value("array_separator", ":arraySeparator")
                .build();

        this.deleteQuery = "DELETE FROM " + configuration.getParameterTable() + " WHERE name = :name";
        this.listNamesQuery = "SELECT name FROM " + configuration.getParameterTable();
        this.selectQuery = "SELECT * FROM " + configuration.getParameterTable() + " where name = :name";
    }

    public void insert(Transaction transaction, Parameter parameter) {
        Query query = Query.query(insertQuery)
                .setString("name", parameter.getName())
                .setLong("inputLevels", parameter.getInputLevels())
                .setBoolean("cacheable", parameter.isCacheable())
                .setBoolean("nullable", parameter.isNullable())
                .setChar("arraySeparator", parameter.getArraySeparator());
        transaction.executeUpdate(query);
    }

    public void delete(Transaction transaction, String parameterName) {
        Query query = Query.query(deleteQuery).setString("name", parameterName);
        transaction.executeUpdate(query);
    }

    public Set<String> getParameterNames() {
        Query query = Query.query(listNamesQuery);
        return queryRunner.queryForSet(query, new StringMapper());
    }

    public Parameter getParameter(String parameterName) {
        Query query = Query.query(selectQuery).setString("name", parameterName);
        return queryRunner.queryForObject(query, new ParameterMapper());
    }
}
