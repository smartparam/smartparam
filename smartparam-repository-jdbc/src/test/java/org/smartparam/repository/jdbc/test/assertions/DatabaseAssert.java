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
package org.smartparam.repository.jdbc.test.assertions;

import org.fest.assertions.api.AbstractAssert;
import org.polyjdbc.core.query.QueryRunner;
import org.polyjdbc.core.query.TransactionalQueryRunner;
import org.polyjdbc.core.transaction.TransactionManager;
import org.smartparam.engine.test.assertions.Assertions;
import org.smartparam.repository.jdbc.dao.LevelDAO;
import org.smartparam.repository.jdbc.dao.ParameterDAO;
import org.smartparam.repository.jdbc.dao.ParameterEntryDAO;

/**
 *
 * @author Adam Dubiel
 */
public class DatabaseAssert extends AbstractAssert<DatabaseAssert, Object> {

    private ParameterDAO parameterDAO;

    private LevelDAO levelDAO;

    private ParameterEntryDAO parameterEntryDAO;

    private QueryRunner queryRunner;

    private DatabaseAssert(TransactionManager transactionManager, ParameterDAO parameterDAO, LevelDAO levelDAO, ParameterEntryDAO parameterEntryDAO) {
        super(new Object(), DatabaseAssert.class);
        this.parameterDAO = parameterDAO;
        this.levelDAO = levelDAO;
        this.parameterEntryDAO = parameterEntryDAO;
        this.queryRunner = new TransactionalQueryRunner(transactionManager.openTransaction());
    }

    public static DatabaseAssert assertThat(TransactionManager transactionManager, ParameterDAO parameterDAO, LevelDAO levelDAO, ParameterEntryDAO parameterEntryDAO) {
        return new DatabaseAssert(transactionManager, parameterDAO, levelDAO, parameterEntryDAO);
    }

    public void close() {
        queryRunner.close();
    }

    private DatabaseAssert performOperation(Operation operation) {
        try {
            operation.run();
        } catch (Exception exception) {
            queryRunner.close();
            Assertions.fail(exception.toString());
        }
        return this;
    }

    public DatabaseAssert hasParameter(final String name) {
        return performOperation(new Operation() {
            @Override
            public void run() {
                boolean exists = parameterDAO.parameterExists(name);
                Assertions.assertThat(exists).isTrue();
            }
        });
    }

    public DatabaseAssert hasNoParameter(final String name) {
        return performOperation(new Operation() {
            @Override
            public void run() {
                boolean exists = parameterDAO.parameterExists(name);
                Assertions.assertThat(exists).isFalse();
            }
        });
    }

    private static interface Operation {

        void run();
    }
}
