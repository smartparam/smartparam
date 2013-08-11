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
package org.smartparam.jdbc.schema;

import org.smartparam.jdbc.query.JdbcQueryRunner;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class SchemaManagerImpl implements SchemaManager {

    private JdbcQueryRunner jdbcQueryRunner;

    private SchemaQueryHelper queryHelper;

    public SchemaManagerImpl(JdbcQueryRunner jdbcQueryRunner) {
        this.jdbcQueryRunner = jdbcQueryRunner;
        this.queryHelper = new SchemaQueryHelper(jdbcQueryRunner);
    }

    @Override
    public SchemaLookupResult schemaExists(SchemaDescription description) {
        SchemaLookupResult lookupResult = new SchemaLookupResult();
        for(String tableName : description.getTables()) {
            lookupResult.addEntity(tableName, queryHelper.tableExists(description.getDialect(), tableName));
        }
        for(String tableName : description.getSequences()) {
            lookupResult.addEntity(tableName, queryHelper.sequenceExistst(description.getDialect(), tableName));
        }

        return lookupResult;
    }

    @Override
    public void createSchema(String ddl) {
        jdbcQueryRunner.execute(ddl);
    }

    @Override
    public void executeDDL(String ddl) {
        jdbcQueryRunner.execute(ddl);
    }

    @Override
    public void dropSchema(SchemaDescription description) {
        throw new UnsupportedOperationException("not supported yet");
    }
}
