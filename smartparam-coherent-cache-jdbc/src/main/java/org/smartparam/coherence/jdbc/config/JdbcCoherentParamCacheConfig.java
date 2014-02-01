package org.smartparam.coherence.jdbc.config;

import org.polyjdbc.core.query.QueryRunnerFactory;
import org.polyjdbc.core.query.SimpleQueryRunner;
import org.polyjdbc.core.query.TransactionRunner;
import org.polyjdbc.core.schema.SchemaManagerFactory;
import org.polyjdbc.core.transaction.DataSourceTransactionManager;
import org.polyjdbc.core.transaction.TransactionManager;
import org.smartparam.coherence.jdbc.repository.JdbcParamVersionRepository;
import org.smartparam.coherence.jdbc.repository.ParamVersionRepository;
import org.smartparam.coherence.jdbc.repository.ParamVersionSchemaCreator;
import org.smartparam.engine.config.pico.ComponentConfig;
import org.smartparam.engine.config.pico.ComponentDefinition;

import javax.sql.DataSource;
import java.util.Set;

import static org.smartparam.engine.config.pico.ComponentDefinition.component;

public class JdbcCoherentParamCacheConfig extends ComponentConfig {

    private final DataSource dataSource;

    private final JdbcConfig configuration;

    public JdbcCoherentParamCacheConfig(DataSource dataSource, JdbcConfig configuration) {
        this.dataSource = dataSource;
        this.configuration = configuration;
    }

    @Override
    protected void injectDefaults(Set<ComponentDefinition> components) {
        components.add(component(TransactionManager.class, DataSourceTransactionManager.class));
        components.add(component(QueryRunnerFactory.class, QueryRunnerFactory.class));
        components.add(component(SchemaManagerFactory.class, SchemaManagerFactory.class));
        components.add(component(SimpleQueryRunner.class, SimpleQueryRunner.class));
        components.add(component(ParamVersionSchemaCreator.class, ParamVersionSchemaCreator.class));
        components.add(component(TransactionRunner.class, TransactionRunner.class));
        components.add(component(ParamVersionRepository.class, JdbcParamVersionRepository.class));
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public JdbcConfig getConfiguration() {
        return configuration;
    }

}
