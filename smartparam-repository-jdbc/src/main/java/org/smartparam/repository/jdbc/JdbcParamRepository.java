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

import java.util.ArrayList;
import java.util.List;
import org.smartparam.repository.jdbc.batch.JdbcParameterEntryBatchLoaderFactory;
import java.util.Set;
import org.polyjdbc.core.exception.TransactionInterruptedException;
import org.polyjdbc.core.query.TransactionWrapper;
import org.polyjdbc.core.query.TransactionRunner;
import org.polyjdbc.core.query.QueryRunner;
import org.polyjdbc.core.query.VoidTransactionWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.config.InitializableComponent;
import org.smartparam.engine.core.batch.ParameterBatchLoader;
import org.smartparam.engine.core.batch.ParameterEntryBatchLoader;
import org.smartparam.engine.core.exception.ParamBatchLoadingException;
import org.smartparam.engine.core.repository.EditableParamRepository;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.engine.model.editable.LevelKey;
import org.smartparam.engine.model.editable.ParameterEntryKey;
import org.smartparam.repository.jdbc.batch.JdbcParameterEntryBatchLoader;
import org.smartparam.repository.jdbc.dao.JdbcRepository;
import org.smartparam.repository.jdbc.exception.ParameterAlreadyExistsException;
import org.smartparam.repository.jdbc.model.JdbcLevelKey;
import org.smartparam.repository.jdbc.model.JdbcParameter;
import org.smartparam.repository.jdbc.model.JdbcParameterEntryKey;
import org.smartparam.repository.jdbc.schema.SchemaCreator;

/**
 * @author Przemek Hertel
 * @since 0.2.0
 */
public class JdbcParamRepository implements EditableParamRepository, InitializableComponent {

    private static final Logger logger = LoggerFactory.getLogger(JdbcParamRepository.class);

    private static final int LOADED_BATCH_SIZE = 500;

    private final TransactionRunner transactionRunner;

    private final JdbcParameterEntryBatchLoaderFactory batchLoaderFactory;

    private final JdbcRepository dao;

    private final SchemaCreator schemaCreator;

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
                JdbcParameterEntryBatchLoader entryLoader = batchLoaderFactory.create(parameterName);

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
    public void write(final ParameterBatchLoader batchLoader) {
        transactionRunner.run(new VoidTransactionWrapper() {
            @Override
            public void performVoid(QueryRunner queryRunner) {
                String parameterName = batchLoader.getMetadata().getName();

                try {
                    write(queryRunner, batchLoader.getMetadata());
                    queryRunner.commit();

                    ParameterEntryBatchLoader entryLoader = batchLoader.getEntryLoader();
                    while (entryLoader.hasMore()) {
                        dao.writeParameterEntries(queryRunner, parameterName, entryLoader.nextBatch(LOADED_BATCH_SIZE));
                        queryRunner.commit();
                    }
                } catch (ParamBatchLoadingException batchException) {
                    queryRunner.rollback();
                    throw new TransactionInterruptedException(batchException);
                }
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

    @Override
    public void delete(final String parameterName) {
        transactionRunner.run(new VoidTransactionWrapper() {
            @Override
            public void performVoid(QueryRunner queryRunner) {
                dao.deleteParameter(queryRunner, parameterName);
            }
        });
    }

    @Override
    public void createParameter(final Parameter parameter) {
        transactionRunner.run(new VoidTransactionWrapper() {
            @Override
            public void performVoid(QueryRunner queryRunner) {
                if (dao.parameterExists(queryRunner, parameter.getName())) {
                    throw new ParameterAlreadyExistsException("Parameter with name " + parameter.getName() + " already exists in this repository.");
                }
                dao.createParameter(queryRunner, parameter);
            }
        });
    }

    @Override
    public void updateParameter(final String parameterName, final Parameter parameter) {
        transactionRunner.run(new VoidTransactionWrapper() {
            @Override
            public void performVoid(QueryRunner queryRunner) {
                dao.updateParameter(queryRunner, parameterName, parameter);
            }
        });
    }

    @Override
    public Level getLevel(final LevelKey entityKey) {
        return transactionRunner.run(new TransactionWrapper<Level>() {
            @Override
            public Level perform(QueryRunner queryRunner) {
                return dao.getLevel(queryRunner, new JdbcLevelKey(entityKey).levelId());
            }
        });
    }

    @Override
    public LevelKey addLevel(final String parameterName, final Level level) {
        return transactionRunner.run(new TransactionWrapper<LevelKey>() {
            @Override
            public LevelKey perform(QueryRunner queryRunner) {
                long levelId = dao.addLevel(queryRunner, parameterName, level);
                return new JdbcLevelKey(parameterName, levelId);
            }
        });
    }

    @Override
    public void updateLevel(final LevelKey levelKey, final Level level) {
        transactionRunner.run(new VoidTransactionWrapper() {
            @Override
            public void performVoid(QueryRunner queryRunner) {
                dao.updateLevel(queryRunner, new JdbcLevelKey(levelKey).levelId(), level);
            }
        });
    }

    @Override
    public void reorderLevels(final List<LevelKey> orderedLevels) {
        transactionRunner.run(new VoidTransactionWrapper() {
            @Override
            public void performVoid(QueryRunner queryRunner) {
                long[] orderedLevelIds = new long[orderedLevels.size()];
                for (int index = 0; index < orderedLevelIds.length; ++index) {
                    orderedLevelIds[index] = new JdbcLevelKey(orderedLevels.get(index)).levelId();
                }

                dao.reorderLevels(queryRunner, orderedLevelIds);
            }
        });
    }

    @Override
    public void deleteLevel(final String parameterName, final LevelKey levelKey) {
        transactionRunner.run(new VoidTransactionWrapper() {
            @Override
            public void performVoid(QueryRunner queryRunner) {
                dao.deleteLevel(queryRunner, parameterName, new JdbcLevelKey(levelKey).levelId());
            }
        });
    }

    @Override
    public ParameterEntryKey addEntry(final String parameterName, final ParameterEntry entry) {
        return transactionRunner.run(new TransactionWrapper<ParameterEntryKey>() {
            @Override
            public ParameterEntryKey perform(QueryRunner queryRunner) {
                long entryId = dao.addParameterEntry(queryRunner, parameterName, entry);
                return new JdbcParameterEntryKey(parameterName, entryId);
            }
        });
    }

    @Override
    public List<ParameterEntryKey> addEntries(final String parameterName, final List<ParameterEntry> entries) {
        return transactionRunner.run(new TransactionWrapper<List<ParameterEntryKey>>() {
            @Override
            public List<ParameterEntryKey> perform(QueryRunner queryRunner) {
                List<Long> entriesIds = dao.writeParameterEntries(queryRunner, parameterName, entries);

                List<ParameterEntryKey> keys = new ArrayList<ParameterEntryKey>(entries.size());
                for (Long entryId : entriesIds) {
                    keys.add(new JdbcParameterEntryKey(parameterName, entryId));
                }

                return keys;
            }
        });
    }

    @Override
    public void updateEntry(final ParameterEntryKey entryKey, final ParameterEntry entry) {
        transactionRunner.run(new VoidTransactionWrapper() {
            @Override
            public void performVoid(QueryRunner queryRunner) {
                dao.updateParameterEntry(queryRunner, new JdbcParameterEntryKey(entryKey).entryId(), entry);
            }
        });
    }

    @Override
    public void deleteEntry(final ParameterEntryKey entryKey) {
        transactionRunner.run(new VoidTransactionWrapper() {
            @Override
            public void performVoid(QueryRunner queryRunner) {
                dao.deleteParameterEntry(queryRunner, new JdbcParameterEntryKey(entryKey).entryId());
            }
        });
    }

    @Override
    public void deleteEntries(final Iterable<ParameterEntryKey> entryKeys) {
        transactionRunner.run(new VoidTransactionWrapper() {
            @Override
            public void performVoid(QueryRunner queryRunner) {
                List<Long> ids = new ArrayList<Long>();
                for (ParameterEntryKey key : entryKeys) {
                    ids.add(new JdbcParameterEntryKey(key).entryId());
                }

                dao.deleteParameterEntries(queryRunner, ids);
            }
        });
    }
}
