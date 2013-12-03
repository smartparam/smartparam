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
package org.smartparam.repository.jdbc.test.builder;

import java.util.ArrayList;
import java.util.List;
import org.polyjdbc.core.query.QueryRunner;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.repository.jdbc.dao.LevelDAO;
import org.smartparam.repository.jdbc.dao.ParameterDAO;
import org.smartparam.repository.jdbc.dao.ParameterEntryDAO;
import static org.smartparam.engine.model.LevelTestBuilder.level;
import static org.smartparam.repository.jdbc.test.builder.JdbcParameterEntryTestBuilder.jdbcParameterEntry;
import static org.smartparam.repository.jdbc.test.builder.JdbcParameterTestBuilder.jdbcParameter;

/**
 *
 * @author Adam Dubiel
 */
public class DatabaseBuilder {

    private final ParameterDAO parameterDAO;

    private final LevelDAO levelDAO;

    private final ParameterEntryDAO parameterEntryDAO;

    private final QueryRunner queryRunner;

    private DatabaseBuilder(ParameterDAO parameterDAO, LevelDAO levelDAO, ParameterEntryDAO parameterEntryDAO, QueryRunner queryRunner) {
        this.parameterDAO = parameterDAO;
        this.levelDAO = levelDAO;
        this.parameterEntryDAO = parameterEntryDAO;
        this.queryRunner = queryRunner;
    }

    public static DatabaseBuilder database(ParameterDAO parameterDAO, LevelDAO levelDAO, ParameterEntryDAO parameterEntryDAO, QueryRunner queryRunner) {
        return new DatabaseBuilder(parameterDAO, levelDAO, parameterEntryDAO, queryRunner);
    }

    public void build() {
        queryRunner.close();
    }

    public DatabaseBuilder withParameters(int count) {
        for (int i = 0; i < count; ++i) {
            withParameter("parameter" + i);
        }
        return this;
    }

    public DatabaseBuilder withParameter(String name) {
        parameterDAO.insert(queryRunner, jdbcParameter()
                .withName(name)
                .build());
        return this;
    }

    public DatabaseBuilder withParameterEntries(String parameterName, int count) {
        return withParameterEntries(parameterName, -1, count);
    }

    public DatabaseBuilder withParameterEntries(String parameterName, long baseId, int count) {
        List<ParameterEntry> entries = new ArrayList<ParameterEntry>();

        boolean definedIds = baseId >= 0;
        JdbcParameterEntryTestBuilder entryBuilder;
        for (int i = 0; i < count; ++i) {
            entryBuilder = jdbcParameterEntry().withLevels("entry" + i);
            if (definedIds) {
                entryBuilder.withId(baseId + i);
            }
            entries.add(entryBuilder.build());
        }
        parameterEntryDAO.insert(queryRunner, entries, parameterName);
        return this;
    }

    public DatabaseBuilder withLevels(String parameterName, int count) {
        List<Level> levels = new ArrayList<Level>();
        for (int i = 0; i < count; ++i) {
            levels.add(level().withName("level" + i).withType("string").build());
            }
        levelDAO.insertParameterLevels(queryRunner, levels, parameterName);
        return this;
    }
}
