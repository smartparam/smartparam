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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.polyjdbc.core.query.DeleteQuery;
import org.polyjdbc.core.query.InsertQuery;
import org.polyjdbc.core.query.Order;
import org.polyjdbc.core.query.QueryFactory;
import org.polyjdbc.core.query.QueryRunner;
import org.polyjdbc.core.query.SelectQuery;
import org.polyjdbc.core.query.UpdateQuery;
import org.polyjdbc.core.util.StringUtils;
import org.smartparam.editor.viewer.LevelSorting;
import org.smartparam.editor.viewer.ParameterEntriesFilter;
import org.smartparam.engine.core.parameter.ParameterEntry;
import org.smartparam.repository.jdbc.config.DefaultJdbcConfig;
import org.smartparam.repository.jdbc.model.JdbcParameterEntry;
import static org.smartparam.repository.jdbc.dao.FilterConverter.parseSortOrder;

/**
 *
 * @author Adam Dubiel
 */
public class ParameterEntryDAO {

    private final DefaultJdbcConfig configuration;

    public ParameterEntryDAO(DefaultJdbcConfig configuration) {
        this.configuration = configuration;
    }

    public long insert(QueryRunner queryRunner, ParameterEntry parameterEntry, long parameterId) {
        return insert(queryRunner, Arrays.asList(parameterEntry), parameterId).get(0);
    }

    public List<Long> insert(QueryRunner queryRunner, Iterable<ParameterEntry> parameterEntries, long parameterId) {
        int maxDistinctLevels = configuration.levelColumnCount();
        List<Long> insertedEntriesIds = new LinkedList<Long>();

        InsertQuery query;
        int levelIndex;
        for (ParameterEntry entry : parameterEntries) {
            query = QueryFactory.insert().into(configuration.parameterEntryEntityName())
                    .sequence("id", configuration.parameterEntrySequenceName())
                    .value("fk_parameter", parameterId);

            for (levelIndex = 0; levelIndex < maxDistinctLevels - 1 && levelIndex < entry.getLevels().length; ++levelIndex) {
                query.value(level(levelIndex), entry.getLevels()[levelIndex]);
            }

            if (entry.getLevels().length > maxDistinctLevels) {
                query.value(lastLevel(), concatenateLastLevels(entry.getLevels(), maxDistinctLevels));
            } else if (entry.getLevels().length == maxDistinctLevels) {
                query.value(lastLevel(), entry.getLevels()[maxDistinctLevels - 1]);
            }

            insertedEntriesIds.add(queryRunner.insert(query));
        }

        return insertedEntriesIds;
    }

    private String concatenateLastLevels(String[] entryLevels, int maxDistinctLevels) {
        String[] excessLevels = Arrays.copyOfRange(entryLevels, maxDistinctLevels - 1, entryLevels.length);
        return StringUtils.concatenate(configuration.excessLevelsSeparator(), (Object[]) excessLevels);
    }

    public Set<ParameterEntry> getParameterEntries(QueryRunner queryRunner, String parameterName) {
        return queryRunner.querySet(createSelectQuery(parameterName), new ParameterEntryMapper(configuration));
    }

    public Set<JdbcParameterEntry> getJdbcParameterEntries(QueryRunner queryRunner, String parameterName) {
        return queryRunner.querySet(createSelectQuery(parameterName), new JdbcParameterEntryMapper(configuration));
    }

    public List<ParameterEntry> getParameterEntriesBatch(QueryRunner queryRunner, String parameterName, long lastEntryId, int batchSize) {
        SelectQuery query = createSelectQuery(parameterName);
        query.append(" and id > :lastId ").withArgument("lastId", lastEntryId).orderBy("id", Order.ASC).limit(batchSize);
        return queryRunner.queryList(query, new ParameterEntryMapper(configuration));
    }

    private SelectQuery createSelectQuery(String parameterName) {
        return QueryFactory.selectAll().from(configuration.parameterEntryEntityName())
                .where("fk_parameter = (select id from " + configuration.parameterEntityName() + " where name = :parameterName)")
                .withArgument("parameterName", parameterName);
    }

    public void deleteParameterEntries(QueryRunner queryRunner, String parameterName) {
        DeleteQuery query = QueryFactory.delete().from(configuration.parameterEntryEntityName())
                .where("fk_parameter = (select id from " + configuration.parameterEntityName() + " where name = :parameterName)")
                .withArgument("parameterName", parameterName);
        queryRunner.delete(query);
    }

    public void delete(QueryRunner queryRunner, long entryId) {
        DeleteQuery query = QueryFactory.delete().from(configuration.parameterEntryEntityName())
                .where("id = :id").withArgument("id", entryId);
        queryRunner.delete(query);
    }

    public void delete(QueryRunner queryRunner, Iterable<Long> entriesIds) {
        DeleteQuery query = QueryFactory.delete().from(configuration.parameterEntryEntityName())
                .where("id in (:ids)").withArgument("ids", entriesIds);
        queryRunner.delete(query);
    }

    public void update(QueryRunner queryRunner, long entryId, ParameterEntry entry) {
        UpdateQuery query = QueryFactory.update(configuration.parameterEntryEntityName())
                .where("id = :id").withArgument("id", entryId);

        int maxDistinctLevels = configuration.levelColumnCount();
        int levelIndex;

        for (levelIndex = 0; levelIndex < maxDistinctLevels - 1 && levelIndex < entry.getLevels().length; ++levelIndex) {
            query.set(level(levelIndex), entry.getLevels()[levelIndex]);
        }

        if (entry.getLevels().length > maxDistinctLevels) {
            query.set(lastLevel(), concatenateLastLevels(entry.getLevels(), maxDistinctLevels));
        } else if (entry.getLevels().length == maxDistinctLevels) {
            query.set(lastLevel(), entry.getLevels()[maxDistinctLevels - 1]);
        }

        queryRunner.update(query);
    }

    public List<ParameterEntry> list(QueryRunner queryRunner, String parameterName, ParameterEntriesFilter filter) {
        SelectQuery query = QueryFactory.selectAll().from(configuration.parameterEntryEntityName())
                .where("fk_parameter = (select id from " + configuration.parameterEntityName() + " where name = :parameterName) ")
                .withArgument("parameterName", parameterName);

        int maxDistinctLevels = configuration.levelColumnCount();
        for (int levelIndex = 0; levelIndex < filter.levelFiltersLength() && levelIndex < maxDistinctLevels; ++levelIndex) {
            if (filter.hasFilter(levelIndex)) {
                query.append(" and upper(" + level(levelIndex) + ")").append(" like :" + level(levelIndex));
                query.withArgument(level(levelIndex), FilterConverter.parseAntMatcher(filter.levelFilter(levelIndex)));
            }
        }

        for (LevelSorting levelSorting : filter.sorting()) {
            if (levelSorting.levelIndex() < maxDistinctLevels) {
                query.orderBy(level(levelSorting.levelIndex()), parseSortOrder(levelSorting.direction()));
            }
        }

        if (filter.applyPaging()) {
            query.limit(filter.pageSize(), filter.offset());
        } else if (filter.applyLimits()) {
            query.limit(filter.pageSize());
        }

        return queryRunner.queryList(query, new ParameterEntryMapper(configuration));
    }

    private String lastLevel() {
        return level(configuration.levelColumnCount() - 1);
    }

    private String level(int levelIndex) {
        return "level" + levelIndex;
    }
}
