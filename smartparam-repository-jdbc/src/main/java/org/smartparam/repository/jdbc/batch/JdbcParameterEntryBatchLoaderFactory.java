/*
 * Copyright 2013 Adam Dubiel.
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
package org.smartparam.repository.jdbc.batch;

import org.polyjdbc.core.transaction.TransactionManager;
import org.smartparam.repository.jdbc.batch.JdbcParameterEntryBatchLoader;
import org.smartparam.repository.jdbc.dao.ParameterEntryDAO;

/**
 *
 * @author Adam Dubiel
 */
public class JdbcParameterEntryBatchLoaderFactory {

    private TransactionManager transactionManager;

    private ParameterEntryDAO parameterEntryDAO;

    public JdbcParameterEntryBatchLoaderFactory(TransactionManager transactionManager, ParameterEntryDAO parameterEntryDAO) {
        this.transactionManager = transactionManager;
        this.parameterEntryDAO = parameterEntryDAO;
    }

    public JdbcParameterEntryBatchLoader create(long parameterId) {
        return new JdbcParameterEntryBatchLoader(transactionManager, parameterEntryDAO, parameterId);
    }
}
