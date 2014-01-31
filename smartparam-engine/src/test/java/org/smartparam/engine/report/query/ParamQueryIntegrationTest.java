/*
 * Copyright 2014 Adam Dubiel.
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
package org.smartparam.engine.report.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.config.ParamEngineConfig;
import org.smartparam.engine.config.ParamEngineConfigBuilder;
import org.smartparam.engine.config.ParamEngineFactory;
import org.smartparam.engine.core.ParamEngine;
import org.smartparam.engine.core.context.LevelValues;
import org.smartparam.engine.core.output.DetailedParamValue;
import org.smartparam.engine.core.parameter.ParamRepository;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.engine.core.parameter.ParameterTestBuilder;
import org.smartparam.engine.core.parameter.entry.ParameterEntry;
import org.smartparam.engine.core.parameter.level.Level;
import org.smartparam.engine.matchers.MatchAllMatcher;
import org.smartparam.engine.matchers.decoder.Range;
import org.smartparam.engine.report.sekleton.ReportLevel;
import org.smartparam.engine.report.sekleton.ReportSkeleton;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.smartparam.engine.core.parameter.entry.ParameterEntryTestBuilder.parameterEntry;
import static org.smartparam.engine.core.parameter.level.LevelTestBuilder.level;

/**
 *
 * @author Adam Dubiel
 */
public class ParamQueryIntegrationTest {

    private ParamEngine paramEngine;

    private ParamRepository repository;

    @BeforeMethod
    public void setUp() {
        repository = mock(ParamRepository.class);
        ParamEngineConfig config = ParamEngineConfigBuilder.paramEngineConfig()
                .withParameterRepository(repository).build();
        paramEngine = ParamEngineFactory.paramEngine(config);
    }

    @Test
    public void shouldFindDetailedValueWithModifiedRangeAfterPerformigQueryWithAmbiguousValueAtLastLevel() {
        // given
        Level[] levels = new Level[]{
            level().withName("first").withType("string").build(),
            level().withName("second").withType("string").build(),
            level().withName("third").withType("string").build(),
            level().withName("ambigous").withMatcher("between/ie").withType("integer").build(),
            level().withName("value").withType("string").build()
        };
        ParameterEntry[] entries = new ParameterEntry[]{
            parameterEntry().withLevels("FIRST", "SECOND_A", "*", "0-10", "VALUE_A").build(),
            parameterEntry().withLevels("FIRST", "SECOND_A", "*", "5-15", "VALUE_B").build()
        };

        Parameter parameter = ParameterTestBuilder.parameter()
                .withName("test")
                .withInputLevels(4)
                .withEntries(entries)
                .withLevels(levels).build();
        when(repository.load("test")).thenReturn(parameter);

        ReportSkeleton skeleton = ReportSkeleton.reportSkeleton();
        skeleton.withLevel(
                "FIRST", ReportLevel.level().withChild(
                        "SECOND_A", ReportLevel.level().withChild(
                                ReportLevel.level().withChild(
                                        ReportLevel.level()
                                )
                        )
                )
                .withChild(
                        "SECOND_B", ReportLevel.level().withChild(
                                ReportLevel.level().withChild(
                                        ReportLevel.level()
                                )
                        )
                )
        );
        skeleton.levelWithAmbigousChildren("ambigous");

        // when
        DetailedParamValue paramValue = ParamQuery.select(paramEngine)
                .fromParameter("test")
                .fillingIn(skeleton)
                .usingMatcher("first", MatchAllMatcher.MATCH_ALL)
                .usingMatcher("second", MatchAllMatcher.MATCH_ALL)
                .usingMatcher("third", MatchAllMatcher.MATCH_ALL)
                .withGreedyLevels("first", "second", "third", "ambigous")
                .askingFor(new LevelValues("FIRST", "SECOND_A", "SOMETHING", "5"))
                .execute();

        // then
        assertThat(paramValue).hasSize(1);
        assertThat(paramValue.detailedEntry().getAs("ambigous", Range.class)).isEqualTo(new Range(5L, 10L));
    }

}
