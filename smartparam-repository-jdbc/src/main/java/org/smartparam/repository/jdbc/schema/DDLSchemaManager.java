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
package org.smartparam.repository.jdbc.schema;

import org.smartparam.repository.jdbc.core.dialect.Dialect;
import org.smartparam.repository.jdbc.core.dialect.DialectProperties;
import org.smartparam.repository.jdbc.core.query.Query;
import org.smartparam.repository.jdbc.core.query.QueryRunner;
import org.smartparam.repository.jdbc.query.loader.QueryLoader;

/**
 *
 * @author Adam Dubiel
 */
public class DDLSchemaManager implements SchemaManager {

    private static final String DDL_QUERY_LOCATION = "/ddl/:dialect_ddl.sql";

    private static final String DDL_DROP_QUERY_LOCATION = "/ddl/:dialect_drop_ddl.sql";

    private static final String DDL_DIALECT_PLACEHOLDER = ":dialect";

    private QueryRunner jdbcQueryRunner;

    private QueryLoader queryLoader;

    public DDLSchemaManager(QueryRunner jdbcQueryRunner, QueryLoader queryLoader) {
        this.jdbcQueryRunner = jdbcQueryRunner;
        this.queryLoader = queryLoader;
    }

    @Override
    public SchemaLookupResult schemaExists(SchemaDescription description) {
        SchemaLookupResult lookupResult = new SchemaLookupResult();
        lookupTables(description, lookupResult);
        lookupSequences(description, lookupResult);

        return lookupResult;
    }

    private SchemaLookupResult lookupTables(SchemaDescription description, SchemaLookupResult lookupResult) {
        for (String tableName : description.getTables()) {
            lookupResult.addEntity(tableName, tableExists(description.getDialect(), tableName));
        }
        return lookupResult;
    }

    private void lookupSequences(SchemaDescription description, SchemaLookupResult lookupResult) {
        boolean hasSequences = description.getDialect().getProperties().hasSequences();
        for (String sequenceName : description.getSequences()) {
            if (hasSequences) {
                lookupResult.addEntity(sequenceName, sequenceExistst(description.getDialect(), sequenceName));
            } else {
                lookupResult.addExistingEntity(sequenceName);
            }
        }
    }

    private boolean tableExists(Dialect dialect, String entityName) {
        Query query = Query.query(dialect.getProperties().tableExistsQuery().replaceAll(":tableName", "'" + entityName + "'"));
        return jdbcQueryRunner.queryForExistence(query);
    }

    private boolean sequenceExistst(Dialect dialect, String entityName) {
        DialectProperties properties = dialect.getProperties();
        if (properties.hasSequences()) {
            Query query = Query.query(properties.sequenceExistsQuery().replaceAll(":sequenceName", "'" + entityName + "'"));
            return jdbcQueryRunner.queryForExistence(query);
        }
        return true;
    }

    @Override
    public void createSchema(SchemaDescription description) {
        String resource = createDefinitionResourceName(DDL_QUERY_LOCATION, description.getDialect());
        Query schemaQueries = queryLoader.getQuery(resource);
        SchemaDefinitionPreparer.prepareQuery(schemaQueries, description.getConfiguration());

        jdbcQueryRunner.execute(schemaQueries);
    }

    private String createDefinitionResourceName(String resourceNameTemplate, Dialect dialect) {
        return resourceNameTemplate.replaceFirst(DDL_DIALECT_PLACEHOLDER, dialect.name().toLowerCase());
    }

    @Override
    public void dropSchema(SchemaDescription description) {
        String resource = createDefinitionResourceName(DDL_DROP_QUERY_LOCATION, description.getDialect());
        Query schemaQueries = queryLoader.getQuery(resource);
        SchemaDefinitionPreparer.prepareQuery(schemaQueries, description.getConfiguration());

        jdbcQueryRunner.execute(schemaQueries);
    }
}
