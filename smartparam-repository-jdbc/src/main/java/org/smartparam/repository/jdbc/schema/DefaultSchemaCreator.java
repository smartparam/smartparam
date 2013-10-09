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

import org.polyjdbc.core.schema.SchemaInspector;
import org.polyjdbc.core.schema.SchemaInspectorImpl;
import org.polyjdbc.core.schema.SchemaManager;
import org.polyjdbc.core.schema.SchemaManagerImpl;
import org.polyjdbc.core.schema.model.RelationBuilder;
import org.polyjdbc.core.schema.model.Schema;
import org.polyjdbc.core.transaction.Transaction;
import org.polyjdbc.core.transaction.TransactionManager;
import org.smartparam.repository.jdbc.config.DefaultConfiguration;
import static org.smartparam.repository.jdbc.schema.SchemaNamePolicy.*;

/**
 *
 * @author Adam Dubiel
 */
public class DefaultSchemaCreator implements SchemaCreator {

    private DefaultConfiguration configuration;

    private TransactionManager transactionManager;

    public DefaultSchemaCreator(DefaultConfiguration configuration, TransactionManager transactionManager) {
        this.configuration = configuration;
        this.transactionManager = transactionManager;
    }

    @Override
    public void createSchema() {
        Transaction transaction = transactionManager.openTransaction();
        try {
            SchemaManager manager = new SchemaManagerImpl(transaction);
            SchemaInspector inspector = new SchemaInspectorImpl(transaction);

            Schema schema = new Schema(configuration.getDialect());
            createParameterRelation(schema, inspector);
            createLevelRelation(schema, inspector);
            createParameterEntryRelation(schema, inspector);

            manager.create(schema);
        } finally {
            transaction.commit();
            transaction.closeWithArtifacts();
        }
    }

    protected void createParameterRelation(Schema schema, SchemaInspector schemaInspector) {
        String relationName = configuration.getParameterTable();
        if (!schemaInspector.relationExists(relationName)) {
            schema.addRelation(relationName)
                    .withAttribute().longAttr("id").withAdditionalModifiers("AUTO_INCREMENT").notNull().and()
                    .withAttribute().string("name").withMaxLength(200).notNull().unique().and()
                    .withAttribute().integer("input_levels").notNull().and()
                    .withAttribute().booleanAttr("cacheable").notNull().withDefaultValue(true).and()
                    .withAttribute().booleanAttr("nullable").notNull().withDefaultValue(false).and()
                    .withAttribute().character("array_separator").notNull().withDefaultValue(';').and()
                    .primaryKey(primaryKey(relationName)).using("id").and()
                    .build();
            schema.addSequence(configuration.getParameterSequence()).build();
        }
    }

    protected void createLevelRelation(Schema schema, SchemaInspector schemaInspector) {
        String relationName = configuration.getLevelTable();
        if (!schemaInspector.relationExists(relationName)) {
            schema.addRelation(relationName)
                    .withAttribute().longAttr("id").withAdditionalModifiers("AUTO_INCREMENT").notNull().and()
                    .withAttribute().string("name").withMaxLength(200).notNull().and()
                    .withAttribute().string("type").withMaxLength(100).notNull().and()
                    .withAttribute().string("matcher").withMaxLength(100).and()
                    .withAttribute().string("level_creator").withMaxLength(200).and()
                    .withAttribute().booleanAttr("array_flag").notNull().withDefaultValue(false).and()
                    .withAttribute().integer("order_no").notNull().and()
                    .withAttribute().longAttr(foreignKey("parameter")).notNull().and()
                    .primaryKey(primaryKey(relationName)).using("id").and()
                    .foreignKey(foreignKey(configuration.getParameterTable() + "_id")).references(configuration.getParameterTable(), "id").on(foreignKey("parameter")).and()
                    .build();
            schema.addSequence(configuration.getLevelSequence()).build();
        }
    }

    protected void createParameterEntryRelation(Schema schema, SchemaInspector schemaInspector) {
        String relationName = configuration.getParameterEntryTable();
        if (!schemaInspector.relationExists(relationName)) {
            RelationBuilder builder = RelationBuilder.relation(schema, relationName);
            builder.withAttribute().longAttr("id").withAdditionalModifiers("AUTO_INCREMENT").notNull().and()
                    .withAttribute().longAttr(foreignKey("parameter")).notNull().and();

            for (int levelIndex = 0; levelIndex < configuration.getLevelColumnCount(); ++levelIndex) {
                builder.string("level" + (levelIndex + 1)).withMaxLength(255).and();
            }

            builder.primaryKey(primaryKey(relationName)).using("id").and()
                    .foreignKey(foreignKey(configuration.getParameterTable() + "id")).references(configuration.getParameterTable(), "id").on(foreignKey("parameter")).and()
                    .build();

            schema.addSequence(configuration.getParameterEntrySequence()).build();
        }
    }

    @Override
    public void dropSchema() {
        SchemaManager manager = new SchemaManagerImpl(transactionManager.openTransaction());
        try {
            Schema schema = new Schema(configuration.getDialect());
            schema.addRelation(configuration.getParameterTable()).build();
            schema.addSequence(configuration.getParameterSequence()).build();

            schema.addRelation(configuration.getLevelTable()).build();
            schema.addSequence(configuration.getLevelSequence()).build();

            schema.addRelation(configuration.getParameterEntryTable()).build();
            schema.addSequence(configuration.getParameterEntrySequence()).build();

            manager.drop(schema);
        } finally {
            manager.close();
        }
    }
}
