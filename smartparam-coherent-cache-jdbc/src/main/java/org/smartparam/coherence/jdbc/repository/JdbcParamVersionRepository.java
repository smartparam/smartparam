package org.smartparam.coherence.jdbc.repository;

import org.polyjdbc.core.query.*;
import org.polyjdbc.core.query.mapper.ObjectMapper;
import org.smartparam.coherence.jdbc.cache.JdbcConfig;
import org.smartparam.engine.config.initialization.InitializableComponent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcParamVersionRepository implements ParamVersionRepository, InitializableComponent {

    private final ParamVersionSchemaCreator schemaCreator;

    private final JdbcConfig configuration;

    private final SimpleQueryRunner queryRunner;

    private final TransactionRunner transactionRunner;

    public JdbcParamVersionRepository(ParamVersionSchemaCreator schemaCreator,
                                      JdbcConfig configuration,
                                      SimpleQueryRunner queryRunner,
                                      TransactionRunner transactionRunner) {
        this.schemaCreator = schemaCreator;
        this.configuration = configuration;
        this.queryRunner = queryRunner;
        this.transactionRunner = transactionRunner;
    }

    @Override
    public void initialize() {
        schemaCreator.createSchema();
    }

    @Override
    public Long incrementVersion(final String paramName) {
        return transactionRunner.run(new TransactionWrapper<Long>() {
            @Override
            public Long perform(QueryRunner queryRunner) {
                if (!hasVersionOfParam(paramName)) {
                    return insertNewParamVersion(queryRunner);
                }

                long newParamVersion = versionOfParam(paramName).longValue() + 1;
                updateParamVersion(queryRunner, newParamVersion);
                return newParamVersion;
            }

            private void updateParamVersion(QueryRunner queryRunner, long newParamVersion) {
                UpdateQuery query = QueryFactory.update(configuration.entityName()).where("name = :name")
                        .withArgument("name", paramName)
                        .set("version", newParamVersion);
                queryRunner.update(query);
            }

            private Long insertNewParamVersion(QueryRunner queryRunner) {
                InsertQuery query = QueryFactory.insert().into(configuration.entityName())
                        .sequence("id", configuration.sequenceName())
                        .value("name", paramName)
                        .value("version", 1);
                queryRunner.insert(query);
                return 1L;
            }
        });
    }

    private boolean hasVersionOfParam(String paramName) {
        SelectQuery query = QueryFactory.select("version").from(configuration.entityName()).where("name = :name")
                .withArgument("name", paramName);
        return queryRunner.queryExistence(query);
    }

    @Override
    public Long versionOfParam(String paramName) {
        if (!hasVersionOfParam(paramName)) {
            return null;
        }

        SelectQuery query = QueryFactory.select("version").from(configuration.entityName()).where("name = :name")
                .withArgument("name", paramName);
        return queryRunner.queryUnique(query, new LongMapper());
    }

    @Override
    public Map<String, Long> versionOfAllParams() {
        SelectQuery query = QueryFactory.selectAll().from(configuration.entityName());
        List<ParamWithVersion> paramVersions = queryRunner.queryList(query, new ParamWithVersionMapper());

        Map<String, Long> versionMapping = new HashMap<String, Long>();
        for (ParamWithVersion paramWithVersion : paramVersions) {
            versionMapping.put(paramWithVersion.getName(), paramWithVersion.getVersion());
        }
        return versionMapping;
    }

    private static class LongMapper implements ObjectMapper<Long> {

        @Override
        public Long createObject(ResultSet resultSet) throws SQLException {
            return resultSet.getLong("version");
        }
    }

    private static class ParamWithVersionMapper implements ObjectMapper<ParamWithVersion> {

        @Override
        public ParamWithVersion createObject(ResultSet resultSet) throws SQLException {
            return new ParamWithVersion(resultSet.getString("name"), resultSet.getLong("version"));
        }
    }
}
