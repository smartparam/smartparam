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

import org.polyjdbc.core.query.QueryRunnerFactory;
import org.smartparam.repository.jdbc.dao.ParameterEntryDAO;

/**
 *
 * @author Adam Dubiel
 */
public class JdbcParameterEntryBatchLoaderFactory {

    private final QueryRunnerFactory queryRunnerFactory;

    private final ParameterEntryDAO parameterEntryDAO;

    public JdbcParameterEntryBatchLoaderFactory(QueryRunnerFactory queryRunnerFactory, ParameterEntryDAO parameterEntryDAO) {
        this.queryRunnerFactory = queryRunnerFactory;
        this.parameterEntryDAO = parameterEntryDAO;
    }

    public JdbcParameterEntryBatchLoader create(String parameterName) {
        return new JdbcParameterEntryBatchLoader(queryRunnerFactory.create(), parameterEntryDAO, parameterName);
    }
}
