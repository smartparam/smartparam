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

import org.smartparam.engine.model.Level;
import org.smartparam.repository.jdbc.config.Configuration;
import org.smartparam.repository.jdbc.core.dialect.query.DialectQueryBuilder;
import org.smartparam.repository.jdbc.core.query.Query;
import org.smartparam.repository.jdbc.core.query.QueryRunner;
import org.smartparam.repository.jdbc.core.transaction.Transaction;
import org.smartparam.repository.jdbc.mapper.LevelMapper;

/**
 *
 * @author Adam Dubiel
 */
public class LevelDAO {

    private QueryRunner queryRunner;

    private final String insertQuery;

    private final String deleteFromParameterQuery;

    private final String selectQuery;

    public LevelDAO(Configuration configuration, QueryRunner queryRunner) {
        this.queryRunner = queryRunner;

        this.insertQuery = DialectQueryBuilder.insert(configuration.getDialect()).into(configuration.getParameterLevelTable())
                .id("id", "seq_level")
                .value("name", ":name")
                .value("level_creator", ":levelCreator")
                .value("type", ":type")
                .value("matcher", ":matcher")
                .value("array_flag", ":array")
                .build();

        this.deleteFromParameterQuery = "DELETE FROM " + configuration.getParameterLevelTable() + " WHERE fk_parameter = :parameterId";
        this.selectQuery = "SELECT * FROM " + configuration.getParameterLevelTable() + " where id = :id";
    }

    public void insert(Transaction transaction, Level level) {
        Query query = Query.query(insertQuery)
                .setString("name", level.getName())
                .setString("levelCreator", level.getLevelCreator())
                .setString("matcher", level.getMatcher())
                .setString("type", level.getType())
                .setBoolean("array", level.isArray());
        transaction.executeUpdate(query);
    }

    public Level getLevel(long parameterId, long id) {
        Query query = Query.query(selectQuery).setLong("id", id);
        return queryRunner.queryForObject(query, new LevelMapper(parameterId));
    }
}
