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

import java.util.List;
import java.util.Set;
import org.polyjdbc.core.query.QueryRunner;
import org.smartparam.editor.viewer.ParameterEntriesFilter;
import org.smartparam.editor.viewer.ParameterFilter;
import org.smartparam.engine.core.parameter.Level;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.engine.core.parameter.ParameterEntry;
import org.smartparam.repository.jdbc.model.JdbcParameter;

/**
 * @author Przemek Hertel
 * @since 0.2.0
 */
public interface JdbcRepository {

    long createParameter(QueryRunner runner, Parameter parameter);

    boolean parameterExists(QueryRunner runner, String parameterName);

    JdbcParameter getParameter(QueryRunner runner, String parameterName);

    JdbcParameter getParameterMetadata(QueryRunner runner, String parameterName);

    Set<String> listParameterNames();

    List<String> listParameterNames(ParameterFilter filter);

    Set<ParameterEntry> getParameterEntries(QueryRunner runner, String parameterName);

    List<Long> writeParameterEntries(QueryRunner runner, String parameterName, Iterable<ParameterEntry> entries);

    void deleteParameter(QueryRunner runner, String parameterName);

    void updateParameter(QueryRunner runner, String parameterName, Parameter parameter);

    long addLevel(QueryRunner runner, String parameterName, Level level);

    void updateLevel(QueryRunner runner, long levelId, Level level);

    void reorderLevels(QueryRunner runner, long[] orderedLevelIds);

    void deleteLevel(QueryRunner queryRunner, String parameterName, long levelId);

    List<ParameterEntry> getEntries(QueryRunner runner, List<Long> ids);

    List<ParameterEntry> listEntries(QueryRunner runner, String parameterName, ParameterEntriesFilter filter);

    long addParameterEntry(QueryRunner runner, String parameterName, ParameterEntry entry);

    void updateParameterEntry(QueryRunner runner, long entryId, ParameterEntry entry);

    void deleteParameterEntry(QueryRunner runner, long entryId);

    void deleteParameterEntries(QueryRunner runner, Iterable<Long> entriesIds);
}
