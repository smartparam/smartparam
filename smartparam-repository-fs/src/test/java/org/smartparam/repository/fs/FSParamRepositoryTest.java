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
        // given
        Map<String, String> resources = new HashMap<String, String>();
        resources.put("parameter", "resource");

        when(resourceResolverFactory.getResourceResolver(anyString(), anyString())).thenReturn(resourceResolver);
        when(resourceResolver.findParameterResources()).thenReturn(resources);
        when(resourceResolver.loadParameterFromResource("resource")).thenReturn(mock(Parameter.class));

        FSParamRepository fsParamRepository = new FSParamRepository("TEST", "TEST", deserializer, resourceResolverFactory);
        fsParamRepository.initialize();

        // when
        Parameter parameter = fsParamRepository.load("parameter");

        // then
        assertThat(parameter).isNotNull();
    }

    @Test
    public void shouldReturnNullValueForUnknownParameter() {
        // given
        Map<String, String> resources = new HashMap<String, String>();

        when(resourceResolverFactory.getResourceResolver(anyString(), anyString())).thenReturn(resourceResolver);
        when(resourceResolver.findParameterResources()).thenReturn(resources);

        FSParamRepository fsParamRepository = new FSParamRepository("TEST", "TEST", deserializer, resourceResolverFactory);
        fsParamRepository.initialize();

        // when
        Parameter parameter = fsParamRepository.load("INVALID_PARAM_NAME");

        // then
        assertThat(parameter).isNull();
    }
}
