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
package org.smartparam.editor.core.entry;

import org.smartparam.engine.core.index.Star;
import java.text.ParseException;
import java.util.Date;
import org.smartparam.editor.matcher.EmptyMatcherEncoder;
import org.smartparam.editor.core.matcher.MatcherEncoderRepository;
import org.smartparam.editor.model.simple.SimpleLevel;
import org.smartparam.editor.model.simple.SimpleParameter;
import org.smartparam.engine.config.ParamEngineConfig;
import org.smartparam.engine.config.ParamEngineConfigBuilder;
import org.smartparam.engine.config.ParamEngineFactory;
import org.smartparam.engine.core.output.entry.MapEntry;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.engine.core.parameter.entry.ParameterEntry;
import org.smartparam.engine.matchers.BetweenMatcher;
import org.smartparam.engine.types.date.DateType;
import org.smartparam.engine.types.date.SimpleDateFormatPool;
import org.smartparam.engine.types.string.StringType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.smartparam.engine.test.ParamEngineAssertions.assertThat;

/**
 *
 * @author Adam Dubiel
 */
public class MapToEntryConverterTest {

    private MapToEntryConverter converter;

    @BeforeMethod
    public void setUp() {
        ParamEngineConfig config = ParamEngineConfigBuilder.paramEngineConfig()
                .withAnnotationScanDisabled()
                .withType("string", new StringType())
                .withType("date", new DateType())
                .withMatcher("between/ie", new BetweenMatcher())
                .build();
        MatcherEncoderRepository converterRepository = mock(MatcherEncoderRepository.class);
        doReturn(new EmptyMatcherEncoder()).when(converterRepository).getEncoder(anyString());

        converter = new MapToEntryConverter(ParamEngineFactory.paramEngine(config).runtimeConfiguration(), converterRepository);
    }

    @Test
    public void shouldReturnStarStringWhenMapValueStarObject() {
        // given
        Parameter metadata = new SimpleParameter().withLevel(new SimpleLevel().withName("star"));
        MapEntry map = new MapEntry().put("star", Star.star());

        // when
        ParameterEntry entry = converter.asEntry(metadata, map);

        // then
        assertThat(entry).hasLevels("*");
    }

    @Test
    public void shouldReturnEmptyStringWhenNullLevelValueAndNoTypeRegistered() {
        // given
        Parameter metadata = new SimpleParameter().withLevel(new SimpleLevel().withName("null"));
        MapEntry map = new MapEntry().put("null", null);

        // when
        ParameterEntry entry = converter.asEntry(metadata, map);

        // then
        assertThat(entry).hasLevels("");
    }

    @Test
    public void shouldReturnObjectToStringWhenNoTypeRegistered() {
        // given
        Parameter metadata = new SimpleParameter().withLevel(new SimpleLevel().withName("string"));
        MapEntry map = new MapEntry().put("string", "something");

        // when
        ParameterEntry entry = converter.asEntry(metadata, map);

        // then
        assertThat(entry).hasLevels("something");
    }

    @Test
    public void shouldReturnStringEncodedByTypeHandlerWhenTypeRegistered() throws ParseException {
        // given
        Date date = SimpleDateFormatPool.get("yyyy-MM-dd").parse("2013-12-04");

        Parameter metadata = new SimpleParameter().withLevel(new SimpleLevel().withName("date").withType("date"));
        MapEntry map = new MapEntry().put("date", date);

        // when
        ParameterEntry entry = converter.asEntry(metadata, map);

        // then
        assertThat(entry).hasLevels("2013-12-04");
    }

    @Test
    public void shouldWorkForMultipleLevelValues() {
        // given
        Parameter metadata = new SimpleParameter().withLevel(new SimpleLevel().withName("string"))
                .withLevel(new SimpleLevel().withName("string2"));
        MapEntry map = new MapEntry().put("string", "something")
                .put("string2", "else");

        // when
        ParameterEntry entry = converter.asEntry(metadata, map);

        // then
        assertThat(entry).hasLevels("something", "else");
    }
}
