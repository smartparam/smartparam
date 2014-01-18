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
import org.smartparam.engine.core.parameter.level.Level;
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

    public void insertParameterLevels(QueryRunner queryRunner, List<Level> levels, long parameterId) {
        int order = 0;
        for (Level level : levels) {
            insert(queryRunner, level, parameterId, order);
            order++;
        }
    }

    public long insert(QueryRunner queryRunner, Level level, long parameterId) {
        List<Level> levels = getLevels(queryRunner, parameterId);
        return insert(queryRunner, level, parameterId, levels.size());
    }

    private long insert(QueryRunner queryRunner, Level level, long parameterId, int order) {
        InsertQuery query = QueryFactory.insert().into(configuration.levelEntityName())
                .sequence("id", configuration.levelSequenceName())
                .value("fk_parameter", parameterId)
                .value("name", level.getName())
                .value("level_creator", level.getLevelCreator())
                .value("type", level.getType())
                .value("matcher", level.getMatcher())
                .value("order_no", order)
                .value("array_flag", level.isArray());
        return queryRunner.insert(query);
    }

    public JdbcLevel getLevel(QueryRunner queryRunner, long id) {
        return queryRunner.queryUnique(QueryFactory.selectAll().from(configuration.levelEntityName()).where("id = :id").withArgument("id", id), new JdbcLevelMapper());
    }

    public List<Level> getLevels(QueryRunner queryRunner, long parameterId) {
        return queryRunner.queryList(createSelectQuery(parameterId), new LevelMapper());
    }

    public List<JdbcLevel> getJdbcLevels(QueryRunner queryRunner, long parameterId) {
        return queryRunner.queryList(createSelectQuery(parameterId), new JdbcLevelMapper());
    }

    private SelectQuery createSelectQuery(long parameterId) {
        return QueryFactory.selectAll().from(configuration.levelEntityName())
                .where("fk_parameter = :parameterId")
                .orderBy("order_no", Order.ASC)
                .withArgument("parameterId", parameterId);
    }

    public void deleteParameterLevels(QueryRunner queryRunner, String parameterName) {
        DeleteQuery query = QueryFactory.delete().from(configuration.levelEntityName())
                .where("fk_parameter = (select id from " + configuration.parameterEntityName() + " where name = :parameterName)")
                .withArgument("parameterName", parameterName);
        queryRunner.delete(query);
    }

    public void delete(QueryRunner queryRunner, long parameterId, long levelId) {
        DeleteQuery query = QueryFactory.delete().from(configuration.levelEntityName())
                .where("id = :id")
                .withArgument("id", levelId);
        queryRunner.delete(query);

        List<JdbcLevel> parameterLevels = getJdbcLevels(queryRunner, parameterId);
        long[] parameterLevelsIds = new long[parameterLevels.size()];
        for (int index = 0; index < parameterLevels.size(); ++index) {
            parameterLevelsIds[index] = parameterLevels.get(index).getId();
        }
        reorder(queryRunner, parameterLevelsIds);
    }

    public void update(QueryRunner queryRunner, long levelId, Level level) {
        UpdateQuery query = QueryFactory.update(configuration.levelEntityName())
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
            query = QueryFactory.update(configuration.levelEntityName())
                    .set("order_no", order)
                    .where("id = :id").withArgument("id", levelId);
            queryRunner.update(query);
            order++;
        }
    }
}
