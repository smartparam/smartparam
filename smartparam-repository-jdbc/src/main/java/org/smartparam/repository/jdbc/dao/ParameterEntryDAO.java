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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.polyjdbc.core.query.DeleteQuery;
import org.polyjdbc.core.query.InsertQuery;
import org.polyjdbc.core.query.Order;
import org.polyjdbc.core.query.QueryFactory;
import org.polyjdbc.core.query.QueryRunner;
import org.polyjdbc.core.query.SelectQuery;
import org.polyjdbc.core.util.StringUtils;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.repository.jdbc.config.DefaultJdbcConfig;
import org.smartparam.repository.jdbc.model.JdbcParameterEntry;

/**
 *
 * @author Adam Dubiel
 */
public class ParameterEntryDAO {

    private final DefaultJdbcConfig configuration;

    public ParameterEntryDAO(DefaultJdbcConfig configuration) {
        this.configuration = configuration;
    }

    public void insert(QueryRunner queryRunner, Iterable<ParameterEntry> parameterEntries, long parameterId) {
        int maxDistinctLevels = configuration.getLevelColumnCount();

        InsertQuery query;
        int levelIndex;
        for (ParameterEntry entry : parameterEntries) {
            query = QueryFactory.insert().into(configuration.getParameterEntryTable())
                    .sequence("id", configuration.getParameterEntrySequence())
                    .value("fk_parameter", parameterId);

            for (levelIndex = 0; levelIndex < maxDistinctLevels - 1 && levelIndex < entry.getLevels().length; ++levelIndex) {
                query.value("level" + (levelIndex + 1), entry.getLevels()[levelIndex]);
            }

            if (entry.getLevels().length > maxDistinctLevels) {
                query.value("level" + maxDistinctLevels, concatenateLastLevels(entry.getLevels(), maxDistinctLevels));
            } else if (entry.getLevels().length == maxDistinctLevels) {
                query.value("level" + maxDistinctLevels, entry.getLevels()[maxDistinctLevels - 1]);
            }

            queryRunner.insert(query);
        }
    }

    private String concatenateLastLevels(String[] entryLevels, int maxDistinctLevels) {
        String[] excessLevels = Arrays.copyOfRange(entryLevels, maxDistinctLevels - 1, entryLevels.length);
        String lastLevelValue = StringUtils.concatenate(configuration.getExcessLevelsSeparator(), (Object[]) excessLevels);
        return lastLevelValue;
    }

    public Set<ParameterEntry> getParameterEntries(QueryRunner queryRunner, long parameterId) {
        return queryRunner.querySet(createSelectQuery(parameterId), new ParameterEntryMapper(configuration));
    }

    public Set<JdbcParameterEntry> getJdbcParameterEntries(QueryRunner queryRunner, long parameterId) {
        return queryRunner.querySet(createSelectQuery(parameterId), new JdbcParameterEntryMapper(configuration));
    }

    public List<ParameterEntry> getParameterEntriesBatch(QueryRunner queryRunner, long parameterId, long lastEntryId, int batchSize) {
        SelectQuery query = createSelectQuery(parameterId);
        query.append(" and id > :lastId ").withArgument("lastId", lastEntryId).orderBy("id", Order.ASC).limit(batchSize);
        return queryRunner.queryList(query, new ParameterEntryMapper(configuration));
    }

    private SelectQuery createSelectQuery(long parameterId) {
        return QueryFactory.select().query("select * from " + configuration.getParameterEntryTable() + " where fk_parameter = :parameterId")
                .withArgument("parameterId", parameterId);
    }

    public void deleteParameterEntries(QueryRunner queryRunner, String parameterName) {
        DeleteQuery query = QueryFactory.delete().from(configuration.getParameterEntryTable())
                .where("fk_parameter = (select id from " + configuration.getParameterTable() + " where name = :name)")
                .withArgument("name", parameterName);
        queryRunner.delete(query);
    }
}
