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
package org.smartparam.repository.jdbc.batch;

import java.util.Collection;
import java.util.List;
import org.polyjdbc.core.query.QueryRunner;
import org.smartparam.engine.core.parameter.ParameterEntryBatchLoader;
import org.smartparam.engine.core.parameter.ParameterEntry;
import org.smartparam.repository.jdbc.dao.ParameterEntryDAO;
import org.smartparam.repository.jdbc.model.JdbcParameterEntry;

/**
 *
 * @author Adam Dubiel
 */
public class JdbcParameterEntryBatchLoader implements ParameterEntryBatchLoader {

    private final QueryRunner queryRunner;

    private final ParameterEntryDAO parameterEntryDAO;

    private final String parameterName;

    private long lastEntryId;

    private boolean hasMore = true;

    public JdbcParameterEntryBatchLoader(QueryRunner queryRunner, ParameterEntryDAO parameterEntryDAO, String parameterName) {
        this.parameterEntryDAO = parameterEntryDAO;
        this.parameterName = parameterName;
        this.queryRunner = queryRunner;
    }

    @Override
    public boolean hasMore() {
        return hasMore;
    }

    @Override
    public Collection<ParameterEntry> nextBatch(int batchSize) {
        List<ParameterEntry> entries = parameterEntryDAO.getParameterEntriesBatch(queryRunner, parameterName, lastEntryId, batchSize);
        queryRunner.commit();

        JdbcParameterEntry lastEntry = getLastEntry(entries);
        if (lastEntry != null) {
            lastEntryId = lastEntry.getId();
        }

        hasMore = entries.size() == batchSize;

        return entries;
    }

    private JdbcParameterEntry getLastEntry(List<ParameterEntry> entries) {
        if (entries.isEmpty()) {
            return null;
        }
        return (JdbcParameterEntry) entries.get(entries.size() - 1);
    }

    @Override
    public void close() {
        queryRunner.close();
    }
}
