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

import org.polyjdbc.core.query.QueryRunner;
import org.polyjdbc.core.query.TransactionalQueryRunner;
import org.polyjdbc.core.transaction.TransactionManager;
import org.smartparam.engine.test.builder.ParameterTestBuilder;
import org.smartparam.repository.jdbc.dao.LevelDAO;
import org.smartparam.repository.jdbc.dao.ParameterDAO;
import org.smartparam.repository.jdbc.dao.ParameterEntryDAO;
import static org.smartparam.engine.test.builder.ParameterTestBuilder.parameter;

/**
 *
 * @author Adam Dubiel
 */
public class DatabaseBuilder {

    private ParameterDAO parameterDAO;

    private LevelDAO levelDAO;

    private ParameterEntryDAO parameterEntryDAO;

    private QueryRunner queryRunner;

    private DatabaseBuilder(ParameterDAO parameterDAO, LevelDAO levelDAO, ParameterEntryDAO parameterEntryDAO, TransactionManager tranactionManager) {
        this.parameterDAO = parameterDAO;
        this.levelDAO = levelDAO;
        this.parameterEntryDAO = parameterEntryDAO;
        this.queryRunner = new TransactionalQueryRunner(tranactionManager.openTransaction());
    }

    public static DatabaseBuilder database(ParameterDAO parameterDAO, LevelDAO levelDAO, ParameterEntryDAO parameterEntryDAO, TransactionManager tranactionManager) {
        return new DatabaseBuilder(parameterDAO, levelDAO, parameterEntryDAO, tranactionManager);
    }

    public void build() {
        queryRunner.close();
    }

    public DatabaseBuilder withParameters(int count) {
        for(int i = 0; i < count; ++i) {
            withParameter("parameter" + i);
        }
        return this;
    }

    public DatabaseBuilder withParameter(String name) {
        parameterDAO.insert(queryRunner, parameter()
                .withName(name)
                .build());
        return this;
    }
}
