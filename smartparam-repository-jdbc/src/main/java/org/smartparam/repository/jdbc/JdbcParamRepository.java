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
package org.smartparam.repository.jdbc;

import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.config.InitializableComponent;
import org.smartparam.engine.core.batch.ParameterBatchLoader;
import org.smartparam.engine.core.repository.ParamRepository;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.repository.jdbc.dao.JdbcProviderDAO;
import org.smartparam.repository.jdbc.model.JdbcParameter;

/**
 * @author Przemek Hertel
 * @since 0.2.0
 */
public class JdbcParamRepository implements ParamRepository, InitializableComponent {

    private static final Logger logger = LoggerFactory.getLogger(JdbcParamRepository.class);

    private JdbcProviderDAO dao;

    /**
     * This variable will be used to set the fetchSize property on jdbc statements.
     * ...
     * If this variable is set to a non-zero value, it will be used for setting the
     * fetchSize property on statements used for query processing.
     */
    //TODO #ph rethink default and comment
    private int fetchSize = 100;

    @Override
    public void initialize() {
        dao.createSchema();
    }

    @Override
    public Set<String> listParameters() {
        return dao.getParameterNames();
    }

    @Override
    public Parameter load(String parameterName) {
        JdbcParameter parameter = loadMetadata(parameterName);
        parameter.setEntries(dao.getParameterEntries(parameter.getId()));

        return parameter;
    }

    @Override
    public ParameterBatchLoader batchLoad(String parameterName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private JdbcParameter loadMetadata(String parameterName) {
        JdbcParameter parameter = dao.getParameter(parameterName);
        parameter.setLevels(dao.getParameterLevels(parameter.getId()));

        return parameter;
    }

    //TODO #ph finish findEntries for non-cachable parameters
    @Override
    public Set<ParameterEntry> findEntries(String parameterName, String[] levelValues) {
        logger.info("trying to load parameter {}, but {} does not support non-cacheable parameters", parameterName, getClass().getSimpleName());
        return null;
    }

    public int getFetchSize() {
        return fetchSize;
    }

    public void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
    }

    public void setJdbcProviderDao(JdbcProviderDAO dao) {
        this.dao = dao;
    }
}
