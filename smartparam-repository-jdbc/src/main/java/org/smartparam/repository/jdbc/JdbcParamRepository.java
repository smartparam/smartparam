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
import org.smartparam.engine.core.repository.WritableParamRepository;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.repository.jdbc.dao.JdbcRepository;
import org.smartparam.repository.jdbc.model.JdbcParameter;
import org.smartparam.repository.jdbc.schema.SchemaCreator;

/**
 * @author Przemek Hertel
 * @since 0.2.0
 */
public class JdbcParamRepository implements ParamRepository, WritableParamRepository, InitializableComponent {

    private static final Logger logger = LoggerFactory.getLogger(JdbcParamRepository.class);

    private JdbcRepository dao;

    private SchemaCreator schemaCreator;

    public JdbcParamRepository(JdbcRepository dao, SchemaCreator schemaCreator) {
        this.dao = dao;
        this.schemaCreator = schemaCreator;
    }

    @Override
    public void initialize() {
        schemaCreator.createSchema();
    }

    @Override
    public Set<String> listParameters() {
        return dao.getParameterNames();
    }

    @Override
    public Parameter load(String parameterName) {
        return dao.getParameter(parameterName);
    }

    @Override
    public ParameterBatchLoader batchLoad(String parameterName) {
        JdbcParameter parameter = dao.getParameter(parameterName);
        parameter.setLevels(dao.getParameterLevels(parameter.getId()));

        return null;
    }

    //TODO #ph finish findEntries for non-cachable parameters
    @Override
    public Set<ParameterEntry> findEntries(String parameterName, String[] levelValues) {
        logger.info("trying to load parameter {}, but {} does not support non-cacheable parameters", parameterName, getClass().getSimpleName());
        return null;
    }

    @Override
    public void write(Parameter parameter) {
        String parameterName = parameter.getName();
        if (dao.parameterExists(parameterName)) {
            dao.deleteParameter(parameterName);
        }
        dao.createParameter(parameter);
    }
}
