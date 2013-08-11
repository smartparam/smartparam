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
public class SchemaCreatorImpl implements SchemaCreator {

    private JdbcQueryRunner jdbcQueryRunner;

    public SchemaCreatorImpl(JdbcQueryRunner jdbcQueryRunner) {
        this.jdbcQueryRunner = jdbcQueryRunner;
    }

    @Override
    public boolean schemaExists() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

//    private boolean tableExists(Dialect dialect, String tableName) {
//        JdbcQuery query = JdbcQuery.query(dialect.getProperties().tableExistsQuery());
//        query.setString(":tableName", tableName);
//        jdbcQueryRunner.queryForObject(query, null);
//    }

    @Override
    public void createSchema() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void dropSchema() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
