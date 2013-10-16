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
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.repository.jdbc.config.JdbcConfig;
import org.smartparam.repository.jdbc.model.JdbcParameter;

/**
 * @author Przemek Hertel
 * @since 0.2.0
 */
public class SimpleJdbcRepository implements JdbcRepository {

    private JdbcConfig configuration;

    private ParameterDAO parameterDAO;

    private LevelDAO levelDAO;

    private ParameterEntryDAO parameterEntryDAO;

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
        long parameterId = parameterDAO.insert(runner, parameter);
        levelDAO.insertParameterLevels(runner, parameter.getLevels(), parameterId);
        parameterEntryDAO.insert(runner, parameter.getEntries(), parameterId);
    }

    @Override
    public boolean parameterExists(QueryRunner runner, String parameterName) {
        return parameterDAO.parameterExists(parameterName);
    }

    @Override
    public JdbcParameter getParameter(QueryRunner runner, String parameterName) {
        JdbcParameter parameter = getParameterMetadata(runner, parameterName);
        Set<ParameterEntry> entries = parameterEntryDAO.getParameterEntries(runner, parameter.getId());
        parameter.setEntries(entries);
        return parameter;
    }

    @Override
    public JdbcParameter getParameterMetadata(QueryRunner runner, String parameterName) {
        JdbcParameter parameter = parameterDAO.getParameter(runner, parameterName);
        List<Level> levels = new ArrayList<Level>(levelDAO.getLevels(runner, parameter.getId()));
        parameter.setLevels(levels);

        return parameter;
    }

    @Override
    public Set<String> getParameterNames() {
        return parameterDAO.getParameterNames();
    }

    @Override
    public Set<ParameterEntry> getParameterEntries(QueryRunner runner, long parameterId) {
        return parameterEntryDAO.getParameterEntries(runner, parameterId);
    }

    @Override
    public void writeParameterEntries(QueryRunner runner, String parameterName, Iterable<ParameterEntry> entries) {
        JdbcParameter parameter = parameterDAO.getParameter(runner, parameterName);
        parameterEntryDAO.insert(runner, entries, parameter.getId());
    }

    @Override
    public void deleteParameter(QueryRunner runner, String parameterName) {
        parameterEntryDAO.deleteParameterEntries(runner, parameterName);
        levelDAO.deleteParameterLevels(runner, parameterName);
        parameterDAO.delete(runner, parameterName);
    }
}
