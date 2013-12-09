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
package org.smartparam.repository.fs;

import org.smartparam.repository.fs.resolver.ResourceResolver;
import java.util.HashMap;
import java.util.Map;
import org.smartparam.engine.core.parameter.ParameterBatchLoader;
import static org.mockito.Mockito.*;
import static org.fest.assertions.api.Assertions.*;
import org.smartparam.engine.core.parameter.Parameter;
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
        when(resourceResolver.batchLoadParameterFromResource("resource")).thenReturn(batchLoader);

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
