package org.smartparam.coherence.jdbc.repository;

import org.polyjdbc.core.schema.SchemaInspector;
import org.polyjdbc.core.schema.SchemaManager;
import org.polyjdbc.core.schema.SchemaManagerFactory;
import org.polyjdbc.core.schema.model.Schema;
import org.polyjdbc.core.util.TheCloser;
import org.smartparam.coherence.jdbc.cache.JdbcConfig;

public class ParamVersionSchemaCreator {

    private final JdbcConfig configuration;

    private final SchemaManagerFactory schemaManagerFactory;

    public ParamVersionSchemaCreator(JdbcConfig configuration, SchemaManagerFactory schemaManagerFactory) {
        this.configuration = configuration;
        this.schemaManagerFactory = schemaManagerFactory;
    }

    public void createSchema() {
        SchemaManager schemaManager = null;
        SchemaInspector schemaInspector = null;
        try {
            schemaManager = schemaManagerFactory.createManager();
            schemaInspector = schemaManagerFactory.createInspector();

            Schema schema = new Schema(configuration.dialect());
            createParamVersionsRelation(schema, schemaInspector);

            schemaManager.create(schema);
        } finally {
            TheCloser.close(schemaManager, schemaInspector);
        }
    }

    protected void createParamVersionsRelation(Schema schema, SchemaInspector schemaInspector) {
        String relationName = configuration.entityName();
        if (!schemaInspector.relationExists(relationName)) {
            schema.addRelation(relationName)
                    .withAttribute().longAttr("id").withAdditionalModifiers("AUTO_INCREMENT").notNull().and()
                    .withAttribute().string("name").withMaxLength(200).notNull().unique().and()
                    .withAttribute().integer("version").notNull().and()
                    .primaryKey(configuration.primaryKeyPrefix() + configuration.entityName()).using("id").and()
                    .build();
            schema.addSequence(configuration.sequenceName()).build();
        }
    }

    public void dropSchema() {
        SchemaManager schemaManager = null;
        try {
            schemaManager = schemaManagerFactory.createManager();

            Schema schema = new Schema(configuration.dialect());
            schema.addRelation(configuration.entityName()).build();
            schema.addSequence(configuration.sequenceName()).build();

            schemaManager.drop(schema);
        } finally {
            TheCloser.close(schemaManager);
        }
    }

}
