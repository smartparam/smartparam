package org.smartparam.coherence.jdbc;

import org.picocontainer.PicoContainer;
import org.polyjdbc.core.dialect.H2Dialect;
import org.polyjdbc.core.infrastructure.PolyDatabaseTest;
import org.smartparam.coherence.jdbc.config.JdbcCoherentParamCacheConfig;
import org.smartparam.coherence.jdbc.config.JdbcCoherentParamCacheFactory;
import org.smartparam.coherence.jdbc.config.JdbcConfig;
import org.smartparam.coherence.jdbc.repository.ParamVersionSchemaCreator;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import javax.sql.DataSource;
import java.util.Arrays;

public class DatabaseTest extends PolyDatabaseTest {

    private ParamVersionSchemaCreator schemaCreator;

    private PicoContainer container;

    protected <T> T get(Class<T> objectClass) {
        return container.getComponent(objectClass);
    }

    @BeforeClass(alwaysRun = true)
    public void setUpDatabase() throws Exception {
        DataSource dataSource = createDatabase("H2", "jdbc:h2:mem:test", "smartparam", "smartparam");
        JdbcConfig config = new JdbcConfig(new H2Dialect());

        schemaCreator = new ParamVersionSchemaCreator(config, schemaManagerFactory());
        schemaCreator.createSchema();

        JdbcCoherentParamCacheFactory factory = new JdbcCoherentParamCacheFactory();
        container = factory.createContainer(new JdbcCoherentParamCacheConfig(dataSource, config));
    }

    @BeforeMethod(alwaysRun = true)
    public void cleanDatabase() {
        super.deleteFromRelations(Arrays.asList(get(JdbcConfig.class).entityName()));
    }

    @AfterClass(alwaysRun = true)
    public void tearDownDatabase() throws Exception {
        dropDatabase();
        container = null;
    }

    @Override
    protected void dropSchema() {
        schemaCreator.dropSchema();
    }

}
