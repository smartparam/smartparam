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
import org.polyjdbc.core.query.Order;
import org.polyjdbc.core.query.QueryFactory;
import org.polyjdbc.core.query.QueryRunner;
import org.polyjdbc.core.query.SelectQuery;
import org.polyjdbc.core.query.UpdateQuery;
import org.smartparam.engine.model.Level;
import org.smartparam.repository.jdbc.config.JdbcConfig;
import org.smartparam.repository.jdbc.model.JdbcLevel;

/**
 *
 * @author Adam Dubiel
 */
public class LevelDAO {

    private final JdbcConfig configuration;

    public LevelDAO(JdbcConfig configuration) {
        this.configuration = configuration;
    }

    public void insertParameterLevels(QueryRunner queryRunner, List<Level> levels, String parameterName) {
        int order = 0;
        for (Level level : levels) {
            insert(queryRunner, level, parameterName, order);
            order++;
        }
    }

    public long insert(QueryRunner queryRunner, JdbcLevel level, String parameterName) {
        return insert(queryRunner, level, parameterName, level.getOrderNo());
    }

    public long insert(QueryRunner queryRunner, Level level, String parameterName) {
        List<Level> levels = getLevels(queryRunner, parameterName);
        return insert(queryRunner, level, parameterName, levels.size());
    }

    private long insert(QueryRunner queryRunner, Level level, String parameterName, int order) {
        InsertQuery query = QueryFactory.insert().into(configuration.getLevelTable())
                .sequence("id", configuration.getLevelSequence())
                .value("fk_parameter", parameterName)
                .value("name", level.getName())
                .value("level_creator", level.getLevelCreator())
                .value("type", level.getType())
                .value("matcher", level.getMatcher())
                .value("order_no", order)
                .value("array_flag", level.isArray());
        return queryRunner.insert(query);
    }

    public JdbcLevel getLevel(QueryRunner queryRunner, long id) {
        return queryRunner.queryUnique(QueryFactory.selectAll().from(configuration.getLevelTable()).where("id = :id").withArgument("id", id), new JdbcLevelMapper());
    }

    public List<Level> getLevels(QueryRunner queryRunner, String parameterName) {
        return queryRunner.queryList(createSelectQuery(parameterName), new LevelMapper());
    }

    public List<JdbcLevel> getJdbcLevels(QueryRunner queryRunner, String parameterName) {
        return queryRunner.queryList(createSelectQuery(parameterName), new JdbcLevelMapper());
    }

    private SelectQuery createSelectQuery(String parameterName) {
        return QueryFactory.selectAll().from(configuration.getLevelTable()).where("fk_parameter = :parameterName").orderBy("order_no", Order.ASC).withArgument("parameterName", parameterName);
    }

    public void deleteParameterLevels(QueryRunner queryRunner, String parameterName) {
        DeleteQuery query = QueryFactory.delete().from(configuration.getLevelTable())
                .where("fk_parameter = :parameterName")
                .withArgument("parameterName", parameterName);
        queryRunner.delete(query);
    }

    public void delete(QueryRunner queryRunner, String parameterName, long levelId) {
        DeleteQuery query = QueryFactory.delete().from(configuration.getLevelTable())
                .where("id = :id and fk_parameter = :parameterName")
                .withArgument("id", levelId)
                .withArgument("parameterName", parameterName);
        queryRunner.delete(query);

        List<JdbcLevel> parameterLevels = getJdbcLevels(queryRunner, parameterName);
        long[] parameterLevelsIds = new long[parameterLevels.size()];
        for (int index = 0; index < parameterLevels.size(); ++index) {
            parameterLevelsIds[index] = parameterLevels.get(index).getId();
        }
        reorder(queryRunner, parameterLevelsIds);
    }

    public void update(QueryRunner queryRunner, long levelId, Level level) {
        UpdateQuery query = QueryFactory.update(configuration.getLevelTable())
                .where("id = :id").withArgument("id", levelId)
                .set("name", level.getName())
                .set("level_creator", level.getLevelCreator())
                .set("matcher", level.getMatcher())
                .set("type", level.getType())
                .set("array_flag", level.isArray());

        queryRunner.update(query);
    }

    public void reorder(QueryRunner queryRunner, long[] reorderedLevelIds) {
        UpdateQuery query;
        int order = 0;
        for (long levelId : reorderedLevelIds) {
            query = QueryFactory.update(configuration.getLevelTable())
                    .set("order_no", order)
                    .where("id = :id").withArgument("id", levelId);
            queryRunner.update(query);
            order++;
        }
    }
}
