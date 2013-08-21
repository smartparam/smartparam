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
import org.smartparam.engine.core.repository.BasicMatcherRepository;
import org.smartparam.engine.core.repository.TypeRepository;
import org.smartparam.engine.core.repository.ParamRepository;
import org.smartparam.engine.matchers.BetweenMatcher;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.types.string.StringType;

import static org.mockito.Mockito.mock;
import org.smartparam.engine.core.repository.BasicTypeRepository;
import org.smartparam.engine.core.service.BasicParameterProvider;

/**
 * @author Przemek Hertel
 */
public class SmartParamPreparerTest {

    private ParamCache cache;

    private ParamRepository repository;

    private PreparedParameter pp1;

    private Parameter p2;

    private SmartParamPreparer paramPreparer;

    private TypeRepository typeProvider;

    private BasicMatcherRepository matcherProvider;

    private StringType type = new StringType();


    @BeforeMethod
    public void initialize() {
        typeProvider = new BasicTypeRepository();
        typeProvider.register("string", type);

        matcherProvider = new BasicMatcherRepository();
        matcherProvider.register("between/ii", new BetweenMatcher(true, true, ":"));
        matcherProvider.register("between/ie", new BetweenMatcher(true, false, ":"));

        cache = mock(ParamCache.class);

        repository = mock(ParamRepository.class);

        paramPreparer = new SmartParamPreparer();
        paramPreparer.setParamCache(cache);
        paramPreparer.setTypeRepository(typeProvider);
        paramPreparer.setMatcherRepository(matcherProvider);

        BasicParameterProvider provider = new BasicParameterProvider();
        provider.register(repository);
        paramPreparer.setParameterProvider(provider);
    }

// FIXME #ad needs rewrite
//    @Test
//    public void testGetPreparedParameter__fromCache() {
//
//        // test
//        PreparedParameter result = paramPreparer.getPreparedParameter("par1");
//
//        // weryfikacja
//        assertSame(pp1, result);
//    }
//
//    @Test
//    public void testGetPreparedParameter__prepare() {
//        // given
//        Level[] levels = new Level[] {
//            level().withType("string").build(),
//            level().withType("string").build()
//        };
//        ParameterEntry[] entries = new ParameterEntry[] {
//            parameterEntry().withLevels("A", "value-A").build(),
//            parameterEntry().withLevels("*", "value-*").build()
//        };
//
//        Parameter parameter = parameter().withName("param").withLevels(levels).withInputLevels(1).withEntries(entries).build();
//        when(repository.load("param")).thenReturn(parameter);
//
//        // test
//        PreparedParameter preparedParameter = paramPreparer.getPreparedParameter("param");
//
//        // weryfikacja
//        assertEquals(2, preparedParameter.getLevelCount());
//        assertEquals(1, preparedParameter.getInputLevelsCount());
//        assertEquals("param", preparedParameter.getName());
//
//        assertEquals(2, preparedParameter.getLevels().length);
//        assertEquals(type, preparedParameter.getLevels()[0].getType());
//        assertEquals(type, preparedParameter.getLevels()[1].getType());
//    }
//
//    @Test
//    public void testGetPreparedParameter__paramNotFound() {
//
//        // test
//        PreparedParameter result = paramPreparer.getPreparedParameter("par3");
//
//        // weryfikacja
//        assertNull(result);
//    }
//
//    @Test
//    public void testGetPreparedParameter__array() {
//
//        ParameterMockBuilder.parameter(p2).withName("par2")
//                .withLevels(
//                    LevelMockBuilder.level().withType("string").get(),
//                    LevelMockBuilder.level().withType("string").get()
//                )
//                .inputLevels(1)
//                .withEntries(
//                    ParameterEntryMockBuilder.parameterEntry("*", "value")
//                );
//
//        // test
//        PreparedParameter result = paramPreparer.getPreparedParameter("par2");
//
//        // weryfikacja
//        assertEquals(2, result.getLevelCount());
//        assertEquals(1, result.getInputLevelsCount());
//        assertEquals(',', result.getArraySeparator());
//
//        // test 2
//        when(p2.getArraySeparator()).thenReturn('/');
//        result = paramPreparer.getPreparedParameter("par2");
//
//        // weryfikacja 2
//        assertEquals('/', result.getArraySeparator());
//    }
//
//    @Test
//    public void testGetPreparedParameter__prepare_withMatchers() {
//
//        ParameterMockBuilder.parameter(p2).withName("par2")
//                .withLevels(
//                    LevelMockBuilder.level().withType("string").withMatcherCode("between/ii").get(),
//                    LevelMockBuilder.level().withType("string").withMatcherCode("between/ie").get(),
//                    LevelMockBuilder.level().withType("string").get()
//                )
//                .inputLevels(2)
//                .withEntries(
//                    ParameterEntryMockBuilder.parameterEntry("A", "B", "value-AB")
//                );
//
//        // test
//        PreparedParameter result = paramPreparer.getPreparedParameter("par2");
//
//        // weryfikacja
//        assertEquals(3, result.getLevelCount());
//        assertEquals(2, result.getInputLevelsCount());
//        assertEquals("par2", result.getName());
//
//        assertEquals(3, result.getLevels().length);
//        for (int i = 0; i < 2; i++) {
//            PreparedLevel pl = result.getLevels()[i];
//            assertEquals(type, pl.getType());
//            assertTrue(pl.getMatcher() instanceof BetweenMatcher);
//        }
//    }
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
