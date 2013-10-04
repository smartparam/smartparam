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
import org.smartparam.engine.model.Parameter;
import org.smartparam.repository.jdbc.config.Configuration;
import org.smartparam.repository.jdbc.model.JdbcParameter;

/**
 *
 * @author Adam Dubiel
 */
public class ParameterDAO {

    private final Configuration configuration;

    public ParameterDAO(Configuration configuration) {
        this.configuration = configuration;
    }

    public long insert(QueryRunner queryRunner, Parameter parameter) {
        InsertQuery query = QueryFactory.insert().into(configuration.getParameterTable())
                .sequence("id", "seq_parameter")
                .value("name", ":name")
                .value("input_levels", ":inputLevels")
                .value("cacheable", ":cacheable")
                .value("nullable", ":nullable")
                .value("array_separator", ":arraySeparator");
        return queryRunner.insert(query);
    }

    public void delete(QueryRunner queryRunner, String parameterName) {
        DeleteQuery query = QueryFactory.delete().from(configuration.getParameterTable()).where("name = :name").withArgument("name", parameterName);
        queryRunner.delete(query);
    }

    public Set<String> getParameterNames(QueryRunner queryRunner) {
        SelectQuery query = QueryFactory.select().query("select name from " + configuration.getParameterTable());
        return new HashSet<String>(queryRunner.queryList(query, new StringMapper()));
    }

    public JdbcParameter getParameter(QueryRunner queryRunner, String parameterName) {
        SelectQuery query = QueryFactory.select().query("select * from " + configuration.getParameterTable() + " where name = :name")
                .withArgument("name", parameterName);
        return queryRunner.queryUnique(query, new ParameterMapper());
    }

    public boolean parameterExistst(QueryRunner queryRunner, String parameterName) {
        SelectQuery query = QueryFactory.select().query("select * from " + configuration.getParameterTable() + " where name = :name")
                .withArgument("name", parameterName);
        return queryRunner.queryExistence(query);
    }
}
