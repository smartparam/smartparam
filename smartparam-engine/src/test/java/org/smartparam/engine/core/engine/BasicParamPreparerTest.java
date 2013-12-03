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
package org.smartparam.engine.core.engine;

import org.testng.annotations.BeforeMethod;
import org.smartparam.engine.core.cache.ParamCache;
import org.smartparam.engine.model.Parameter;

import static org.mockito.Mockito.*;
import org.smartparam.engine.core.service.ParameterProvider;
import org.smartparam.engine.model.Level;
import org.testng.annotations.Test;
import static org.smartparam.engine.test.assertions.Assertions.*;
import static org.smartparam.engine.model.LevelTestBuilder.level;
import static org.smartparam.engine.model.ParameterTestBuilder.parameter;
import static org.smartparam.engine.test.builder.PreparedLevelTestBuilder.preparedLevel;
import static org.smartparam.engine.test.builder.PreparedParameterTestBuilder.preparedParameter;

/**
 * @author Przemek Hertel
 */
public class BasicParamPreparerTest {

    private ParamCache cache;

    private BasicParamPreparer paramPreparer;

    private ParameterProvider paramProvider;

    private LevelPreparer levelPreparer;

    @BeforeMethod
    public void initialize() {
        levelPreparer = mock(LevelPreparer.class);
        paramProvider = mock(ParameterProvider.class);
        cache = mock(ParamCache.class);

        paramPreparer = new BasicParamPreparer(paramProvider, levelPreparer, cache);
    }

    @Test
    public void shouldReturnPreparedParameterWithIndexForCacheableParameter() {
        // given
        Level[] levels = new Level[]{
            level().withName("level").withMatcher("matcher").withLevelCreator("creator").withType("type").build(),
            level().withName("outputLevel").withType("type").build()
        };
        Parameter parameter = parameter().withName("param").withInputLevels(1).withArraySeparator('^')
                .withEntries().withLevels(levels).build();
        when(paramProvider.load("param")).thenReturn(parameter);
        when(levelPreparer.prepare(any(Level.class))).thenReturn(preparedLevel().build()).thenReturn(preparedLevel().withName("outputLevel").build());

        // when
        PreparedParameter preparedParameter = paramPreparer.getPreparedParameter("param");

        // then
        assertThat(preparedParameter).hasName("param").hasInputLevels(1).hasArraySeparator('^').hasIndex()
                .hasLevelNameEntry("outputLevel", 0);
    }

    @Test
    public void shouldNotBuildIndexForNoncacheableParameter() {
        // given
        Parameter parameter = parameter().withName("param").withInputLevels(1).noncacheable().withEntries().withLevels().build();
        when(paramProvider.load("param")).thenReturn(parameter);

        // when
        PreparedParameter preparedParameter = paramPreparer.getPreparedParameter("param");

        // then
        assertThat(preparedParameter).hasName("param").hasNoIndex();
    }

    @Test
    public void shouldPrepareParameterOnlyOnceAndUseCacheInConsequentTries() {
        // given
        Parameter parameter = parameter().withEntries().build();
        when(cache.get("param")).thenReturn(null).thenReturn(preparedParameter().forParameter(parameter).build());
        when(paramProvider.load("param")).thenReturn(parameter);

        // when
        paramPreparer.getPreparedParameter("param");
        paramPreparer.getPreparedParameter("param");

        // then
        verify(cache, times(2)).get("param");
        verify(cache, times(1)).put(eq("param"), any(PreparedParameter.class));
    }

    @Test
    public void shouldReturnNullWhenParameterNotFound() {
        // given
        when(paramProvider.load("param")).thenReturn(null);

        // when
        PreparedParameter preparedParameter = paramPreparer.getPreparedParameter("param");

        // then
        assertThat(preparedParameter).isNull();
    }
}
