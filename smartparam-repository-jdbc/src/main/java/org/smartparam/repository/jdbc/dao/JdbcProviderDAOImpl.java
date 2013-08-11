package org.smartparam.repository.jdbc.dao;

import org.smartparam.repository.jdbc.mapper.ParameterMapper;
import org.smartparam.jdbc.mapper.StringMapper;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.repository.jdbc.config.Configuration;
import org.smartparam.repository.jdbc.config.DefaultConfiguration;
import org.smartparam.repository.jdbc.mapper.LevelMapper;
import org.smartparam.repository.jdbc.mapper.ParameterEntryMapper;
import org.smartparam.repository.jdbc.model.JdbcParameter;
import org.smartparam.jdbc.query.JdbcQuery;
import org.smartparam.jdbc.query.JdbcQueryRunner;
import org.smartparam.jdbc.query.JdbcQueryRunnerImpl;
import org.smartparam.jdbc.schema.SchemaDescription;
import org.smartparam.jdbc.schema.SchemaLookupResult;
import org.smartparam.jdbc.schema.SchemaManager;
import org.smartparam.jdbc.schema.SchemaManagerImpl;
import org.smartparam.jdbc.schema.loader.ClasspathSchemaDefinitionLoader;
import org.smartparam.jdbc.schema.loader.SchemaDefinitionLoader;
import org.smartparam.repository.jdbc.config.SchemaDescriptionFactory;
import org.smartparam.repository.jdbc.schema.SchemaDefinitionPreparer;

/**
 * @author Przemek Hertel
 * @since 0.2.0
 */
public class JdbcProviderDAOImpl implements JdbcProviderDAO {

    private Configuration configuration = new DefaultConfiguration();

    private JdbcQueryRunner queryRunner;

    private SchemaManager schemaManager;

    private SchemaDefinitionLoader schemaDefinitionLoader = new ClasspathSchemaDefinitionLoader("/ddl/", ":dialect_ddl.sql", ":dialect");

    private SchemaDefinitionPreparer schemaDefinitionPreparer = new SchemaDefinitionPreparer();

    public JdbcProviderDAOImpl(DataSource dataSource) {
        this.queryRunner = new JdbcQueryRunnerImpl(dataSource);
        schemaManager = new SchemaManagerImpl(queryRunner);
    }

    @Override
    public void createSchema() {
        SchemaDescription description = SchemaDescriptionFactory.createSchemaDescription(configuration);
        SchemaLookupResult result = schemaManager.schemaExists(description);

        if (result.noEntityExisting()) {
            String rawDDL = schemaDefinitionLoader.getQuery(configuration.getDialect());
            String ddl = schemaDefinitionPreparer.prepareQuery(rawDDL, configuration);
            schemaManager.createSchema(ddl);
        }
        else if(!result.noEntityMissing()) {
            throw new SmartParamException("JDBC repository detected partial SmartParam schema in database. "
                    + "This version of JDBC repository has no schema update capabilities, remove old SmartParam entities "
                    + "from the database or use different naming schema. Detected partial entities: " + result.getExistingEntities());
        }
    }

    @Override
    public JdbcParameter getParameter(String parameterName) {
        JdbcQuery query = JdbcQuery.query(" select id, input_levels, cacheable, nullable, array_separator"
                + " from " + configuration.getParameterTable()
                + " where name = :name");
        query.setString("name", parameterName);

        return queryRunner.queryForObject(query, new ParameterMapper());
    }

    @Override
    public Set<String> getParameterNames() {
        JdbcQuery query = JdbcQuery.query("select name from " + configuration.getParameterTable());

        return queryRunner.queryForSet(query, new StringMapper());
    }

    @Override
    public List<Level> getParameterLevels(int parameterId) {
        JdbcQuery query = JdbcQuery.query(" select id, order_no, label, type, matcher, level_creator, array_flag"
                + " from " + configuration.getParameterLevelTable()
                + " where param_id = :parameterId");
        query.setInt("parameterId", parameterId);

        return queryRunner.queryForList(query, new LevelMapper(parameterId));
    }

    @Override
    public Set<ParameterEntry> getParameterEntries(int parameterId) {
        JdbcQuery query = JdbcQuery.query("select id, level1, level2, level3, level4, level5, level6, level7, level8, value"
                + " from " + configuration.getParameterEntryTable()
                + " where param_id = :parameterId");
        query.setInt("parameterId", parameterId);

        return queryRunner.queryForSet(query, new ParameterEntryMapper(parameterId));
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
