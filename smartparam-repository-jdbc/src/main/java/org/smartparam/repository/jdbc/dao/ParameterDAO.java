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

import java.util.HashSet;
import java.util.Set;
import org.polyjdbc.core.query.mapper.StringMapper;
import org.polyjdbc.core.query.DeleteQuery;
import org.polyjdbc.core.query.InsertQuery;
import org.polyjdbc.core.query.QueryFactory;
import org.polyjdbc.core.query.QueryRunner;
import org.polyjdbc.core.query.SelectQuery;
import org.polyjdbc.core.query.SimpleQueryRunner;
import org.polyjdbc.core.query.UpdateQuery;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.metadata.ParameterMetadata;
import org.smartparam.repository.jdbc.config.JdbcConfig;
import org.smartparam.repository.jdbc.model.JdbcParameter;

/**
 *
 * @author Adam Dubiel
 */
public class ParameterDAO {

    private final JdbcConfig configuration;

    private final SimpleQueryRunner simpleQueryRunner;

    public ParameterDAO(JdbcConfig configuration, SimpleQueryRunner simpleQueryRunner) {
        this.configuration = configuration;
        this.simpleQueryRunner = simpleQueryRunner;
    }

    public long insert(QueryRunner queryRunner, Parameter parameter) {
        InsertQuery query = createInsertQuery(parameter);
        return queryRunner.insert(query);
    }

    public long insert(QueryRunner queryRunner, JdbcParameter parameter) {
        InsertQuery query = createInsertQuery(parameter);
        if (parameter.getId() > 0) {
            query.sequenceValue(parameter.getId());
            queryRunner.insertWithoutKey(query);
            return parameter.getId();
        } else {
            return queryRunner.insert(query);
        }
    }

    private InsertQuery createInsertQuery(Parameter parameter) {
        InsertQuery query = QueryFactory.insert().into(configuration.getParameterTable())
                .sequence("id", configuration.getParameterSequence())
                .value("name", parameter.getName())
                .value("input_levels", parameter.getInputLevels())
                .value("cacheable", parameter.isCacheable())
                .value("nullable", parameter.isNullable())
                .value("array_separator", parameter.getArraySeparator());
        return query;
    }

    public void delete(QueryRunner queryRunner, String parameterName) {
        DeleteQuery query = QueryFactory.delete().from(configuration.getParameterTable()).where("name = :name").withArgument("name", parameterName);
        queryRunner.delete(query);
    }

    public Set<String> getParameterNames() {
        SelectQuery query = QueryFactory.select("name").from(configuration.getParameterTable());
        return new HashSet<String>(simpleQueryRunner.queryList(query, new StringMapper()));
    }

    public JdbcParameter getParameter(QueryRunner queryRunner, String parameterName) {
        SelectQuery query = QueryFactory.selectAll().from(configuration.getParameterTable()).where("name = :name")
                .withArgument("name", parameterName);
        return queryRunner.queryUnique(query, new ParameterMapper(), false);
    }

    public boolean parameterExists(String parameterName) {
        SelectQuery query = QueryFactory.selectAll().from(configuration.getParameterTable()).where("name = :name")
                .withArgument("name", parameterName);
        return simpleQueryRunner.queryExistence(query);
    }

    public void update(QueryRunner queryRunner, String parameterName, ParameterMetadata parameter) {
        UpdateQuery query = QueryFactory.update(configuration.getParameterTable()).where("name = :name")
                .withArgument("name", parameterName)
                .set("name", parameter.getName())
                .set("input_levels", parameter.getInputLevels())
                .set("cacheable", parameter.isCacheable())
                .set("nullable", parameter.isNullable())
                .set("array_separator", parameter.getArraySeparator());
        queryRunner.update(query);
    }
}
