package org.smartparam.repository.fs;

import java.util.HashMap;
import java.util.Map;
import org.smartparam.engine.core.batch.ParameterBatchLoader;
import static org.mockito.Mockito.*;
import static org.fest.assertions.api.Assertions.*;
import org.smartparam.engine.model.Parameter;
import org.smartparam.serializer.ParamDeserializer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Adam Dubiel
 */
public class AbstractFSParamRepositoryTest {

    private ParamDeserializer deserializer;

    private ResourceResolver resourceResolver;

    @BeforeMethod
    public void setUp() {
        deserializer = mock(ParamDeserializer.class);
        resourceResolver = mock(ResourceResolver.class);
    }

    @Test
    public void shouldReturnParameter() {
        // given
        Map<String, String> resources = new HashMap<String, String>();
        resources.put("parameter", "resource");

        when(resourceResolver.findParameterResources()).thenReturn(resources);

        ParameterBatchLoader batchLoader = mock(ParameterBatchLoader.class);
        when(resourceResolver.loadParameterFromResource("resource")).thenReturn(batchLoader);

        AbstractFSParamRepository paramRepository = new TestFSParamRepository(resourceResolver);
        paramRepository.initialize();

        // when
        ParameterBatchLoader parameter = paramRepository.batchLoad("parameter");

        // then
        assertThat(parameter).isSameAs(batchLoader);
    }

    @Test
    public void shouldReturnNullValueForUnknownParameter() {
        // given
        Map<String, String> resources = new HashMap<String, String>();

        when(resourceResolver.findParameterResources()).thenReturn(resources);

        AbstractFSParamRepository paramRepository = new TestFSParamRepository(resourceResolver);
        paramRepository.initialize();

        // when
        Parameter parameter = paramRepository.load("INVALID_PARAM_NAME");

        // then
        assertThat(parameter).isNull();
    }

    private final class TestFSParamRepository extends AbstractFSParamRepository {

        private ResourceResolver resourceResolver;

        public TestFSParamRepository(ResourceResolver resourceResolver) {
            super("TEST", "TEST");
            this.resourceResolver = resourceResolver;
        }

        @Override
        protected ResourceResolver createResourceResolver(String basePath, String filePattern, ParamDeserializer deserializer) {
            return resourceResolver;
        }
    }
}
