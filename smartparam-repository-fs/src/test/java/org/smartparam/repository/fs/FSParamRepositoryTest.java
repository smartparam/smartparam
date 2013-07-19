package org.smartparam.repository.fs;

import java.util.HashMap;
import java.util.Map;
import static org.mockito.Mockito.*;
import static org.fest.assertions.api.Assertions.*;
import org.smartparam.engine.model.Parameter;
import org.smartparam.serializer.ParamDeserializer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class FSParamRepositoryTest {

    private static final String VALID_PARAM_NAME = "testParam";

    private static final String VALID_RESOURCE_NAME = "testResource";

    private ParamDeserializer deserializer;

    private ResourceResolverFactory resourceResolverFactory;

    private ResourceResolver resourceResolver;

    @BeforeMethod
    public void setUp() {
        deserializer = mock(ParamDeserializer.class);
        resourceResolverFactory = mock(ResourceResolverFactory.class);
        resourceResolver = mock(ResourceResolver.class);
    }

    @Test
    public void shouldReturnParameter() {
        Map<String, String> resources = new HashMap<String, String>();
        resources.put(VALID_PARAM_NAME, VALID_RESOURCE_NAME);

        when(resourceResolverFactory.getResourceResolver(anyString(), anyString())).thenReturn(resourceResolver);
        when(resourceResolver.findParameterResources()).thenReturn(resources);
        when(resourceResolver.loadParameterFromResource(VALID_RESOURCE_NAME)).thenReturn(mock(Parameter.class));
        FSParamRepository fsParamRepository = new FSParamRepository("someBasePath", "someFilePattern", deserializer, resourceResolverFactory);

        Parameter parameter = fsParamRepository.load(VALID_PARAM_NAME);

        assertThat(parameter).isNotNull();
    }

    @Test
    public void shouldReturnNullValueForUnknonwParameterName() {
        Map<String, String> resources = new HashMap<String, String>();
        when(resourceResolverFactory.getResourceResolver(anyString(), anyString())).thenReturn(resourceResolver);
        when(resourceResolver.findParameterResources()).thenReturn(resources);
        FSParamRepository fsParamRepository = new FSParamRepository("someBasePath", "someFilePattern", deserializer, resourceResolverFactory);

        Parameter parameter = fsParamRepository.load("INVALID_PARAM_NAME");
        assertThat(parameter).isNull();
    }
}
