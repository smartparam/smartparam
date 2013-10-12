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

import org.smartparam.repository.jdbc.batch.JdbcParameterEntryBatchLoaderFactory;
import java.util.Set;
import org.polyjdbc.core.query.TransactionWrapper;
import org.polyjdbc.core.query.TransactionRunner;
import org.polyjdbc.core.query.QueryRunner;
import org.polyjdbc.core.query.VoidTransactionWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.config.InitializableComponent;
import org.smartparam.engine.core.batch.ParameterBatchLoader;
import org.smartparam.engine.core.repository.ParamRepository;
import org.smartparam.engine.core.repository.WritableParamRepository;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.repository.jdbc.batch.JdbcParameterEntryBatchLoader;
import org.smartparam.repository.jdbc.dao.JdbcRepository;
import org.smartparam.repository.jdbc.model.JdbcParameter;
import org.smartparam.repository.jdbc.schema.SchemaCreator;

/**
 * @author Przemek Hertel
 * @since 0.2.0
 */
public class JdbcParamRepository implements ParamRepository, WritableParamRepository, InitializableComponent {

    private static final Logger logger = LoggerFactory.getLogger(JdbcParamRepository.class);

    private TransactionRunner transactionRunner;

    private JdbcParameterEntryBatchLoaderFactory batchLoaderFactory;

    private JdbcRepository dao;

    private SchemaCreator schemaCreator;

    public JdbcParamRepository(TransactionRunner operationRunner, JdbcParameterEntryBatchLoaderFactory batchLoaderFactory,
                               JdbcRepository dao, SchemaCreator schemaCreator) {
        this.dao = dao;
        this.schemaCreator = schemaCreator;
        this.batchLoaderFactory = batchLoaderFactory;
        this.transactionRunner = operationRunner;
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
    public Parameter load(final String parameterName) {
        return transactionRunner.run(new TransactionWrapper<Parameter>() {
            @Override
            public Parameter perform(QueryRunner queryRunner) {
                return dao.getParameter(queryRunner, parameterName);
            }
        });
    }

    @Override
    public ParameterBatchLoader batchLoad(final String parameterName) {
        return transactionRunner.run(new TransactionWrapper<ParameterBatchLoader>() {
            @Override
            public ParameterBatchLoader perform(QueryRunner queryRunner) {
                JdbcParameter metadata = dao.getParameterMetadata(queryRunner, parameterName);
                JdbcParameterEntryBatchLoader entryLoader = batchLoaderFactory.create(metadata.getId());

                return new ParameterBatchLoader(metadata, entryLoader);
            }
        });
    }

    @Override
    public Set<ParameterEntry> findEntries(String parameterName, String[] levelValues) {
        logger.info("trying to load parameter {}, but {} does not support non-cacheable parameters", parameterName, getClass().getSimpleName());
        return null;
    }

    @Override
    public void write(final Parameter parameter) {
        transactionRunner.run(new VoidTransactionWrapper() {
            @Override
            public void performVoid(QueryRunner queryRunner) {
                write(queryRunner, parameter);
            }
        });
    }

    @Override
    public void writeAll(final Iterable<Parameter> parameters) {
        transactionRunner.run(new VoidTransactionWrapper() {
            @Override
            public void performVoid(QueryRunner queryRunner) {
                for (Parameter parameter : parameters) {
                    write(queryRunner, parameter);
                }
            }
        });
    }

    private void write(QueryRunner queryRunner, Parameter parameter) {
        String parameterName = parameter.getName();
        if (dao.parameterExists(queryRunner, parameterName)) {
            dao.deleteParameter(queryRunner, parameterName);
        }
        dao.createParameter(queryRunner, parameter);
    }

    @Override
    public void writeParameterEntries(final String parameterName, final Iterable<ParameterEntry> parameterEntries) {
        transactionRunner.run(new VoidTransactionWrapper() {
            @Override
            public void performVoid(QueryRunner queryRunner) {
                dao.writeParameterEntries(queryRunner, parameterName, parameterEntries);
            }
        });
    }
}
