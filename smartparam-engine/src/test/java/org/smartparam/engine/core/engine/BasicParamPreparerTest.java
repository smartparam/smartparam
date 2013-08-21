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
import static org.smartparam.engine.test.builder.LevelTestBuilder.level;
import static org.smartparam.engine.test.builder.ParameterTestBuilder.parameter;
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

        paramPreparer = new BasicParamPreparer();
        paramPreparer.setParamCache(cache);
        paramPreparer.setParameterProvider(paramProvider);
        paramPreparer.setLevelPreparer(levelPreparer);

//        typeProvider = new BasicTypeRepository();
//        typeProvider.register("string", type);
//
//        matcherProvider = new BasicMatcherRepository();
//        matcherProvider.register("between/ii", new BetweenMatcher(true, true, ":"));
//        matcherProvider.register("between/ie", new BetweenMatcher(true, false, ":"));
//
//        cache = mock(ParamCache.class);
//
//        repository = mock(ParamRepository.class);
//
//        paramPreparer = new BasicParamPreparer();
//        paramPreparer.setParamCache(cache);
//        paramPreparer.setTypeRepository(typeProvider);
//        paramPreparer.setMatcherRepository(matcherProvider);
//
//        BasicParameterProvider provider = new BasicParameterProvider();
//        provider.register(repository);
//        paramPreparer.setParameterProvider(provider);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnPreparedParameterWithIndexForCacheableParameter() {
        // given
        Level level = level().withName("level").withMatcher("matcher").withLevelCreator("creator").withType("type").build();
        Parameter parameter = parameter().withName("param").withInputLevels(1).withArraySeparator('^')
                .withEntries().withLevels(level).build();
        when(paramProvider.load("param")).thenReturn(parameter);
        when(levelPreparer.prepare(any(Level.class))).thenReturn(new PreparedLevel(null, null, false, null, null));

        // when
        PreparedParameter preparedParameter = paramPreparer.getPreparedParameter("param");

        // then
        assertThat(preparedParameter).hasName("param").hasInputLevels(1).hasArraySeparator('^').hasIndex();
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

//
//    @Test
//    public void testGetPreparedParameter__prepare_illegalMatcher() {
//
//        ParameterMockBuilder.parameter(p2).withName("par2")
//                .withLevels(
//                    LevelMockBuilder.level().withType("string").withMatcherCode("nonexisting").get()
//                ).withEntries(
//                    ParameterEntryMockBuilder.parameterEntry("A", "B", "value-AB")
//                );
//
//        // test
//        try {
//            paramPreparer.getPreparedParameter("par2");
//            fail();
//        } catch (SmartParamDefinitionException e) {
//            assertEquals(SmartParamErrorCode.UNKNOWN_MATCHER, e.getErrorCode());
//        }
//    }
//
//    @Test
//    public void testGetPreparedParameter__prepare_nullTypeLevel() {
//
//        ParameterMockBuilder.parameter(p2).withName("par2")
//                .withLevels(
//                    LevelMockBuilder.level().get(),
//                    LevelMockBuilder.level().withType("string").get()
//                ).withEntries(
//                    ParameterEntryMockBuilder.parameterEntry("A", "value-AB"),
//                    ParameterEntryMockBuilder.parameterEntry("B", "value-CD")
//                ).inputLevels(1);
//
//        // test
//        PreparedParameter result = paramPreparer.getPreparedParameter("par2");
//
//        // weryfikacja
//        assertEquals(2, result.getLevelCount());
//        assertEquals(1, result.getInputLevelsCount());
//        assertEquals("par2", result.getName());
//        assertNull(result.getLevels()[0].getType());
//    }
//
//    @Test
//    public void testGetPreparedParameter__prepare_illegalLevelType() {
//
//        ParameterMockBuilder.parameter(p2).withName("par2")
//                .withLevels(
//                    LevelMockBuilder.level("unknown-type")
//                ).withEntries(
//                    ParameterEntryMockBuilder.parameterEntry("A", "value-AB")
//                );
//
//        // test
//        try {
//            paramPreparer.getPreparedParameter("par2");
//            fail();
//        } catch (SmartParamDefinitionException e) {
//            assertEquals(SmartParamErrorCode.UNKNOWN_PARAM_TYPE, e.getErrorCode());
//        }
//    }
//
//    @Test
//    public void testFindEntries() {
//        // given
//        entries.add(ParameterEntryMockBuilder.parameterEntry("A", "A2", "A3", "A4"));
//
//        // test
//        List<PreparedEntry> result = paramPreparer.findEntries("param", new String[]{"A"});
//
//        // weryfikacja
//        assertEquals(1, result.size());
//        assertArrayEquals(new String[]{"A", "A2", "A3", "A4"}, result.get(0).getLevels());
//    }
//
//    @Test
//    public void testGetPreparedParameter__prepare_cacheable() {
//
//        ParameterMockBuilder.parameter(p2).withName("par2")
//                .inputLevels(1).cacheable(false)
//                .withLevels(
//                    LevelMockBuilder.level("string"),
//                    LevelMockBuilder.level("string")
//                ).withEntries(
//                    ParameterEntryMockBuilder.parameterEntry("A", "B", "value-AB"),
//                    ParameterEntryMockBuilder.parameterEntry("C", "D", "value-CD")
//                );
//
//        // test
//        PreparedParameter result = paramPreparer.getPreparedParameter("par2");
//
//        // weryfikacja
//        assertEquals(2, result.getLevelCount());
//        assertEquals(1, result.getInputLevelsCount());
//        assertFalse(result.isCacheable());
//        assertNull(result.getIndex());
//    }
//
//    @Test
//    public void testGetFirstLevels() {
//
//        // preparing big entry: 14 parameterEntry
//        ParameterEntry pe = ParameterEntryMockBuilder.parameterEntryCsv("1;2;3;4;5;6;7;8;9;10;11;12;13;14");
//
//        // test cases
//        Object[][] tests = {
//            new Object[]{0, new String[]{}},
//            new Object[]{1, new String[]{"1"}},
//            new Object[]{4, new String[]{"1", "2", "3", "4"}},
//            new Object[]{8, new String[]{"1", "2", "3", "4", "5", "6", "7", "8"}},
//            new Object[]{11, new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"}},
//            new Object[]{14, new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14"}},
//            new Object[]{15, new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", null}}
//        };
//
//        // run test cases
//        for (Object[] test : tests) {
//            Integer n = (Integer) test[0];
//            String[] expectedLevels = (String[]) test[1];
//
//            // when
//            String[] result = paramPreparer.getFirstLevels(pe, n);
//
//            // then
//            assertArrayEquals(expectedLevels, result);
//        }
//    }
}
