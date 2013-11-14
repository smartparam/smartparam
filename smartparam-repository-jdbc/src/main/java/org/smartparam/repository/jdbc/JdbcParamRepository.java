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
import org.smartparam.engine.model.EntityKey;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.repository.jdbc.batch.JdbcParameterEntryBatchLoader;
import org.smartparam.repository.jdbc.dao.JdbcRepository;
import org.smartparam.repository.jdbc.model.JdbcEntityKey;
import org.smartparam.repository.jdbc.model.JdbcParameter;
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
    public Level getLevel(EntityKey entityKey) {
        final long levelId = JdbcEntityKey.parseKey(entityKey).getId();
        return transactionRunner.run(new TransactionWrapper<Level>() {
            @Override
            public Level perform(QueryRunner queryRunner) {
                return dao.getLevel(queryRunner, levelId);
            }
        });
    }

    @Override
    public EntityKey addLevel(final String parameterName, final Level level) {
        return transactionRunner.run(new TransactionWrapper<EntityKey>() {
            @Override
            public EntityKey perform(QueryRunner queryRunner) {
                long levelId = dao.addLevel(queryRunner, parameterName, level);
                return new JdbcEntityKey(levelId, parameterName);
            }
        });
    }

    @Override
    public void updateLevel(final EntityKey levelKey, final Level level) {
        transactionRunner.run(new VoidTransactionWrapper() {
            @Override
            public void performVoid(QueryRunner queryRunner) {
                dao.updateLevel(queryRunner, levelKey.asNumber(), level);
            }
        });
    }

    @Override
    public void reorderLevels(final List<EntityKey> orderedLevels) {
        transactionRunner.run(new VoidTransactionWrapper() {
            @Override
            public void performVoid(QueryRunner queryRunner) {
                long[] orderedLevelIds = new long[orderedLevels.size()];
                for (int index = 0; index < orderedLevelIds.length; ++index) {
                    orderedLevelIds[index] = orderedLevels.get(index).asNumber();
                }

                dao.reorderLevels(queryRunner, orderedLevelIds);
            }
        });
    }

    @Override
    public void deleteLevel(final String parameterName, final EntityKey levelKey) {
        transactionRunner.run(new VoidTransactionWrapper() {
            @Override
            public void performVoid(QueryRunner queryRunner) {
                dao.deleteLevel(queryRunner, parameterName, levelKey.asNumber());
            }
        });
    }

    @Override
    public EntityKey addEntry(final String parameterName, final ParameterEntry entry) {
        return transactionRunner.run(new TransactionWrapper<EntityKey>() {
            @Override
            public EntityKey perform(QueryRunner queryRunner) {
                long entryId = dao.addParameterEntry(queryRunner, parameterName, entry);
                return new JdbcEntityKey(entryId, parameterName);
            }
        });
    }

    @Override
    public List<EntityKey> addEntries(final String parameterName, final List<ParameterEntry> entries) {
        return transactionRunner.run(new TransactionWrapper<List<EntityKey>>() {
            @Override
            public List<EntityKey> perform(QueryRunner queryRunner) {
                List<Long> entriesIds = dao.writeParameterEntries(queryRunner, parameterName, entries);

                List<EntityKey> keys = new ArrayList<EntityKey>(entries.size());
                for (Long entryId : entriesIds) {
                    keys.add(new JdbcEntityKey(entryId, parameterName));
                }

                return keys;
            }
        });
    }

    @Override
    public void updateEntry(final EntityKey entryKey, final ParameterEntry entry) {
        transactionRunner.run(new VoidTransactionWrapper() {
            @Override
            public void performVoid(QueryRunner queryRunner) {
                dao.updateParameterEntry(queryRunner, entryKey.asNumber(), entry);
            }
        });
    }

    @Override
    public void deleteEntry(final EntityKey entryKey) {
        transactionRunner.run(new VoidTransactionWrapper() {
            @Override
            public void performVoid(QueryRunner queryRunner) {
                dao.deleteParameterEntry(queryRunner, entryKey.asNumber());
            }
        });
    }

    @Override
    public void deleteEntries(final Iterable<EntityKey> entryKeys) {
        throw new UnsupportedOperationException("Please start with implementing IN(...) SQL syntax in PolyJDBC before using this one!");
//        transactionRunner.run(new VoidTransactionWrapper() {
//            @Override
//            public void performVoid(QueryRunner queryRunner) {
//                List<Long> ids = new ArrayList<Long>();
//                for (EntityKey key : entryKeys) {
//                    ids.add(key.asNumber());
//                }
//
//                dao.deleteParameterEntries(queryRunner, ids);
//            }
//        });
    }
}
