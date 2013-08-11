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

import org.smartparam.jdbc.dialect.Dialect;
import org.smartparam.jdbc.dialect.DialectProperties;
import org.smartparam.jdbc.query.JdbcQuery;
import org.smartparam.jdbc.query.JdbcQueryRunner;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class SchemaQueryHelper {

    private JdbcQueryRunner runner;

    public SchemaQueryHelper(JdbcQueryRunner runner) {
        this.runner = runner;
    }

    public boolean tableExists(Dialect dialect, String entityName) {
        JdbcQuery query = JdbcQuery.query(dialect.getProperties().tableExistsQuery().replaceAll(":tableName", "'" + entityName + "'"));
        return runner.queryForExistence(query);
    }

    public boolean sequenceExistst(Dialect dialect, String entityName) {
        DialectProperties properties = dialect.getProperties();
        if (properties.hasSequences()) {
            JdbcQuery query = JdbcQuery.query(properties.sequenceExistsQuery().replaceAll(":sequenceName", "'" + entityName + "'"));
            return runner.queryForExistence(query);
        }
        return true;
    }
}
