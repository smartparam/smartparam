package org.smartparam.repository.fs.resolver;

import org.smartparam.repository.fs.ResourceResolver;
import org.smartparam.repository.fs.ResourceResolverFactory;
import static org.fest.assertions.api.Assertions.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class ResourceResolverFactoryTest {

    private ResourceResolverFactory factory;

    @DataProvider(name = "resourceResolvers")
    public Object[][] resourceResolversProvider() {
        return new Object[][]{
            {"file://home/test/path", FileResourceResolver.class},
            {"classpath://test/path", ClasspathResourceResolver.class}
        };
    }

    @BeforeMethod
    public void setUp() {
        factory = new ResourceResolverFactory(null);
    }

    @Test(dataProvider = "resourceResolvers")
    public void shouldProduceFileResolver(String basePath, Class<?> resolverClass) {
        ResourceResolver resolver = factory.getResourceResolver(basePath, "");

        assertThat(resolver).isInstanceOf(resolverClass);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionForUnsupportedResolverPrefix() {
        factory.getResourceResolver("UNKNOWN_PREFIX://", "");
    }
}
