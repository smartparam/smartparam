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
package org.smartparam.engine.core.prepared;

import org.testng.annotations.BeforeMethod;

import org.smartparam.engine.core.parameter.Parameter;
import static org.mockito.Mockito.*;
import org.smartparam.engine.core.parameter.entry.ParameterEntry;
import org.smartparam.engine.core.parameter.level.Level;
import org.smartparam.engine.core.index.FastLevelIndexWalker;
import org.smartparam.engine.core.parameter.ParameterFromRepository;
import org.smartparam.engine.core.repository.RepositoryName;
import org.testng.annotations.Test;
import static org.smartparam.engine.core.parameter.ParameterTestBuilder.parameter;
import static org.smartparam.engine.core.parameter.entry.ParameterEntryTestBuilder.parameterEntry;
import static org.smartparam.engine.core.parameter.level.LevelTestBuilder.level;
import static org.smartparam.engine.core.prepared.PreparedLevelTestBuilder.preparedLevel;
import static org.smartparam.engine.test.ParamEngineAssertions.*;

/**
 * @author Przemek Hertel
 */
public class BasicParamPreparerTest {

    private BasicParamPreparer paramPreparer;

    private LevelPreparer levelPreparer;

    @BeforeMethod
    public void initialize() {
        levelPreparer = mock(LevelPreparer.class);

        paramPreparer = new BasicParamPreparer(levelPreparer);
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
        when(levelPreparer.prepare(any(Level.class))).thenReturn(preparedLevel().build()).thenReturn(preparedLevel().withName("outputLevel").build());

        // when
        PreparedParameter preparedParameter = paramPreparer.prepare(new ParameterFromRepository(parameter, RepositoryName.from("test")));

        // then
        assertThat(preparedParameter).hasName("param").hasInputLevels(1).hasArraySeparator('^').hasIndex()
                .hasLevelNameEntry("outputLevel", 0);
    }

    @Test
    public void shouldInsertLightParameterEntriesIntoIndexWhenParameterHasNoIdentifyEntriesFlagSet() {
        // given
        Level[] levels = new Level[]{
            level().withName("outputLevel").withType("type").build()
        };
        ParameterEntry[] entries = new ParameterEntry[]{
            parameterEntry().withLevels("hello").build()
        };
        Parameter parameter = parameter().withName("param").withInputLevels(0)
                .withLevels(levels).withEntries(entries).build();
        when(levelPreparer.prepare(any(Level.class))).thenReturn(preparedLevel().build()).thenReturn(preparedLevel().withName("outputLevel").build());

        // when
        PreparedParameter preparedParameter = paramPreparer.prepare(new ParameterFromRepository(parameter, RepositoryName.from("test")));

        // then
        FastLevelIndexWalker<PreparedEntry> walker = new FastLevelIndexWalker<PreparedEntry>(preparedParameter.getIndex());
        assertThat(walker.find().get(0)).isExactlyInstanceOf(PreparedEntry.class);
    }

    @Test
    public void shouldInsertIdentifiableParameterEntriesIntoIndexWhenParameterHasIdentifyEntriesFlagSet() {
        // given
        Level[] levels = new Level[]{
            level().withName("outputLevel").withType("type").build()
        };
        ParameterEntry[] entries = new ParameterEntry[]{
            parameterEntry().withLevels("hello").build()
        };
        Parameter parameter = parameter().withName("param").identifyEntries().withInputLevels(0)
                .withLevels(levels).withEntries(entries).build();
        when(levelPreparer.prepare(any(Level.class))).thenReturn(preparedLevel().build()).thenReturn(preparedLevel().withName("outputLevel").build());

        // when
        PreparedParameter preparedParameter = paramPreparer.prepare(new ParameterFromRepository(parameter, RepositoryName.from("test")));

        // then
        FastLevelIndexWalker<PreparedEntry> walker = new FastLevelIndexWalker<PreparedEntry>(preparedParameter.getIndex());
        assertThat(walker.find().get(0)).isInstanceOf(IdentifiablePreparedEntry.class);
    }

    @Test
    public void shouldNotBuildIndexForNoncacheableParameter() {
        // given
        Parameter parameter = parameter().withName("param").withInputLevels(1).noncacheable().withEntries().withLevels().build();

        // when
        PreparedParameter preparedParameter = paramPreparer.prepare(new ParameterFromRepository(parameter, RepositoryName.from("test")));

        // then
        assertThat(preparedParameter).hasName("param").hasNoIndex();
    }
}
