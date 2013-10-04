/*
 * Copyright 2013 Adam Dubiel.
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

import java.util.List;
import org.polyjdbc.core.query.DeleteQuery;
import org.polyjdbc.core.query.InsertQuery;
import org.polyjdbc.core.query.QueryFactory;
import org.polyjdbc.core.query.QueryRunner;
import org.smartparam.engine.model.Level;
import org.smartparam.repository.jdbc.config.Configuration;
import org.smartparam.repository.jdbc.model.JdbcLevel;

/**
 *
 * @author Adam Dubiel
 */
public class LevelDAO {

    private final Configuration configuration;

    public LevelDAO(Configuration configuration) {
        this.configuration = configuration;
    }

    public long insert(QueryRunner queryRunner, Level level, long parameterId) {
        InsertQuery query = QueryFactory.insert().into(configuration.getLevelTable())
                .sequence("id", "seq_level")
                .value("paramId", parameterId)
                .value("name", level.getName())
                .value("level_creator", level.getLevelCreator())
                .value("type", level.getType())
                .value("matcher", level.getMatcher())
                .value("array_flag", level.isArray());
        return queryRunner.insert(query);
    }

    public JdbcLevel getLevel(QueryRunner queryRunner, long id) {
        return queryRunner.queryUnique(QueryFactory.select().query("select * from " + configuration.getLevelTable() + " where id = :id").withArgument("id", id), new LevelMapper());
    }

    public List<JdbcLevel> getParameterLevels(QueryRunner queryRunner, long parameterId) {
        return queryRunner.queryList(QueryFactory.select().query("select * from " + configuration.getLevelTable() + " where param_id = :param_id order by order_no asc").withArgument("param_id", parameterId), new LevelMapper());
    }

    public void deleteParameterLevels(QueryRunner queryRunner, String parameterName) {
        DeleteQuery query = QueryFactory.delete().from(configuration.getLevelTable())
                .where("paramId = (select id from " + configuration.getParameterTable() + " where name = :name)")
                .withArgument("name", parameterName);
        queryRunner.delete(query);
    }
}
