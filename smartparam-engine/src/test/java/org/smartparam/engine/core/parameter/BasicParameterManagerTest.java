/*
 * Copyright 2014 Adam Dubiel, Przemek Hertel.
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
package org.smartparam.engine.core.parameter;

import org.smartparam.engine.core.parameter.request.SimpleParameterRequestQueue;
import org.smartparam.engine.core.prepared.ParamPreparer;
import org.smartparam.engine.core.prepared.PreparedParamCache;
import org.smartparam.engine.core.prepared.PreparedParameter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.smartparam.engine.core.parameter.ParameterFromRepositoryBuilder.repositoryParameter;
import static org.smartparam.engine.core.parameter.ParameterTestBuilder.parameter;
import static org.smartparam.engine.core.prepared.PreparedParameterTestBuilder.preparedParameter;
import static org.smartparam.engine.test.ParamEngineAssertions.assertThat;

/**
 *
 * @author Adam Dubiel
 */
public class BasicParameterManagerTest {

    private BasicParameterManager manager;

    private ParameterProvider paramProvider;

    private ParamPreparer preparer;

    private PreparedParamCache cache;

    @BeforeMethod
    public void initialize() {
        preparer = mock(ParamPreparer.class);
        paramProvider = mock(ParameterProvider.class);
        cache = mock(PreparedParamCache.class);

        manager = new BasicParameterManager(preparer, paramProvider, cache, new SimpleParameterRequestQueue());
    }

    @Test
    public void shouldPrepareParameterOnlyOnceAndUseCacheInConsequentTries() {
        // given
        Parameter parameter = parameter().withEntries().build();
        when(cache.get("param")).thenReturn(null).thenReturn(preparedParameter().forParameter(parameter).build());
        when(paramProvider.load("param")).thenReturn(repositoryParameter(parameter).build());

        // when
        manager.getPreparedParameter("param");
        manager.getPreparedParameter("param");

        // then
        verify(cache, times(2)).get("param");
        verify(cache, times(1)).put(eq("param"), any(PreparedParameter.class));
    }

    @Test
    public void shouldReturnPreparedParameter() {
        // given
        Parameter parameter = parameter().withEntries().build();
        when(cache.get("param")).thenReturn(null);
        when(paramProvider.load("param")).thenReturn(repositoryParameter(parameter).build());
        when(preparer.prepare(any(ParameterFromRepository.class))).thenReturn(preparedParameter().forParameter(parameter).build());

        // when
        PreparedParameter preparedParameter = manager.getPreparedParameter("param");

        // then
        assertThat(preparedParameter).isNotNull();
    }

    @Test
    public void shouldReturnNullWhenParameterNotFound() {
        // given
        when(paramProvider.load("param")).thenReturn(null);

        // when
        PreparedParameter preparedParameter = manager.getPreparedParameter("param");

        // then
        assertThat(preparedParameter).isNull();
    }

}
