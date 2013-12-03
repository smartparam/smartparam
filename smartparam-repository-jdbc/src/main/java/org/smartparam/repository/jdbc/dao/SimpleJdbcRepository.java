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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.polyjdbc.core.query.QueryRunner;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.editor.viewer.ParameterEntriesFilter;
import org.smartparam.editor.viewer.ParameterFilter;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.repository.jdbc.config.JdbcConfig;
import org.smartparam.repository.jdbc.model.JdbcLevel;
import org.smartparam.repository.jdbc.model.JdbcParameter;

/**
 * @author Przemek Hertel
 * @since 0.2.0
 */
public class SimpleJdbcRepository implements JdbcRepository {

    private final JdbcConfig configuration;

    private final ParameterDAO parameterDAO;

    private final LevelDAO levelDAO;

    private final ParameterEntryDAO parameterEntryDAO;

    public SimpleJdbcRepository(JdbcConfig configuration, ParameterDAO parameterDAO, LevelDAO levelDAO, ParameterEntryDAO parameterEntryDAO) {
        this.configuration = configuration;
        checkConfiguration();
        this.parameterDAO = parameterDAO;
        this.levelDAO = levelDAO;
        this.parameterEntryDAO = parameterEntryDAO;
    }

    private void checkConfiguration() {
        if (configuration.getDialect() == null) {
            throw new SmartParamException("Provided JDBC repository configuration has no dialect defined!");
        }
    }

    @Override
    public void createParameter(QueryRunner runner, Parameter parameter) {
        parameterDAO.insert(runner, parameter);
        levelDAO.insertParameterLevels(runner, parameter.getLevels(), parameter.getName());
        parameterEntryDAO.insert(runner, parameter.getEntries(), parameter.getName());
    }

    @Override
    public boolean parameterExists(QueryRunner runner, String parameterName) {
        return parameterDAO.parameterExists(parameterName);
    }

    @Override
    public JdbcParameter getParameter(QueryRunner runner, String parameterName) {
        JdbcParameter parameter = getParameterMetadata(runner, parameterName);
        Set<ParameterEntry> entries = parameterEntryDAO.getParameterEntries(runner, parameterName);
        parameter.setEntries(entries);
        return parameter;
    }

    @Override
    public JdbcParameter getParameterMetadata(QueryRunner runner, String parameterName) {
        JdbcParameter parameter = parameterDAO.getParameter(runner, parameterName);
        List<Level> levels = new ArrayList<Level>(levelDAO.getLevels(runner, parameterName));
        parameter.setLevels(levels);

        return parameter;
    }

    @Override
    public Set<String> listParameterNames() {
        return parameterDAO.getParameterNames();
    }

    @Override
    public List<String> listParameterNames(ParameterFilter filter) {
        return parameterDAO.getParameterNames(filter);
    }

    @Override
    public Set<ParameterEntry> getParameterEntries(QueryRunner runner, String parameterName) {
        return parameterEntryDAO.getParameterEntries(runner, parameterName);
    }

    @Override
    public List<Long> writeParameterEntries(QueryRunner runner, String parameterName, Iterable<ParameterEntry> entries) {
        return parameterEntryDAO.insert(runner, entries, parameterName);
    }

    @Override
    public void deleteParameter(QueryRunner runner, String parameterName) {
        parameterEntryDAO.deleteParameterEntries(runner, parameterName);
        levelDAO.deleteParameterLevels(runner, parameterName);
        parameterDAO.delete(runner, parameterName);
    }

    @Override
    public void updateParameter(QueryRunner runner, String parameterName, Parameter parameter) {
        parameterDAO.update(runner, parameterName, parameter);
    }

    @Override
    public JdbcLevel getLevel(QueryRunner runner, long id) {
        return levelDAO.getLevel(runner, id);
    }

    @Override
    public long addLevel(QueryRunner runner, String parameterName, Level level) {
        return levelDAO.insert(runner, level, parameterName);
    }

    @Override
    public void updateLevel(QueryRunner runner, long levelId, Level level) {
        levelDAO.update(runner, levelId, level);
    }

    @Override
    public void reorderLevels(QueryRunner runner, long[] orderedLevelIds) {
        levelDAO.reorder(runner, orderedLevelIds);
    }

    @Override
    public void deleteLevel(QueryRunner queryRunner, String parameterName, long levelId) {
        levelDAO.delete(queryRunner, parameterName, levelId);
    }

    @Override
    public List<ParameterEntry> listEntries(QueryRunner runner, String parameterName, ParameterEntriesFilter filter) {
        return parameterEntryDAO.list(runner, parameterName, filter);
    }

    @Override
    public long addParameterEntry(QueryRunner runner, String parameterName, ParameterEntry entry) {
        return parameterEntryDAO.insert(runner, entry, parameterName);
    }

    @Override
    public void updateParameterEntry(QueryRunner runner, long entryId, ParameterEntry entry) {
        parameterEntryDAO.update(runner, entryId, entry);
    }

    @Override
    public void deleteParameterEntry(QueryRunner runner, long entryId) {
        parameterEntryDAO.delete(runner, entryId);
    }

    @Override
    public void deleteParameterEntries(QueryRunner runner, Iterable<Long> entriesIds) {
        parameterEntryDAO.delete(runner, entriesIds);
    }
}
