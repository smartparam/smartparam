package org.smartparam.engine.core.engine;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.smartparam.engine.core.cache.ParamCache;
import org.smartparam.engine.core.repository.BasicMatcherRepository;
import org.smartparam.engine.core.repository.TypeRepository;
import org.smartparam.engine.core.exception.SmartParamDefinitionException;
import org.smartparam.engine.core.repository.ParamRepository;
import org.smartparam.engine.matchers.BetweenMatcher;
import org.smartparam.engine.test.builder.LevelMockBuilder;
import org.smartparam.engine.test.builder.ParameterEntryMockBuilder;
import org.smartparam.engine.test.builder.ParameterMockBuilder;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.engine.types.string.StringType;

import static org.testng.AssertJUnit.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.smartparam.engine.core.repository.BasicTypeRepository;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.service.BasicParameterProvider;

/**
 * @author Przemek Hertel
 */
public class SmartParamPreparerTest {

    private ParamCache cache;

    private ParamRepository loader;

    private PreparedParameter pp1;

    private Parameter p2;

    private SmartParamPreparer instance;

    private TypeRepository typeProvider;

    private BasicMatcherRepository matcherProvider;

    private StringType type = new StringType();

    private Set<ParameterEntry> entries;


    @BeforeMethod
    public void initialize() {
        pp1 = new PreparedParameter();
        p2 = ParameterMockBuilder.parameter().get();
        entries = new HashSet<ParameterEntry>();

        typeProvider = new BasicTypeRepository();
        typeProvider.register("string", type);

        matcherProvider = new BasicMatcherRepository();
        matcherProvider.register("between/ii", new BetweenMatcher(true, true, ":"));
        matcherProvider.register("between/ie", new BetweenMatcher(true, false, ":"));

        cache = mock(ParamCache.class);
        when(cache.get("par1")).thenReturn(pp1);
        when(cache.get("par2")).thenReturn(null);

        loader = mock(ParamRepository.class);
        when(loader.load("par2")).thenReturn(p2);
        when(loader.load("par3")).thenReturn(null);
        when(loader.findEntries(any(String.class), any(String[].class))).thenReturn(entries);

        instance = new SmartParamPreparer();
        instance.setParamCache(cache);
        instance.setTypeRepository(typeProvider);
        instance.setMatcherRepository(matcherProvider);

        BasicParameterProvider provider = new BasicParameterProvider();
        provider.register("test", 0, loader);
        instance.setParameterProvider(provider);
    }

    @Test
    public void testGetPreparedParameter__fromCache() {

        // test
        PreparedParameter result = instance.getPreparedParameter("par1");

        // weryfikacja
        assertSame(pp1, result);
    }

    @Test
    public void testGetPreparedParameter__prepare() {

        // konfiguracja
        Level l1 = LevelMockBuilder.level().withType("string").get();
        Level l2 = LevelMockBuilder.level().withType("string").get();

        ParameterMockBuilder.parameter(p2).withName("par2").withLevels(l1, l2).inputLevels(1).withEntries(
                ParameterEntryMockBuilder.parameterEntry("A", "value-A"),
                ParameterEntryMockBuilder.parameterEntry("*", "value-*")
                );

        // test
        PreparedParameter result = instance.getPreparedParameter("par2");

        // weryfikacja
        assertEquals(2, result.getLevelCount());
        assertEquals(1, result.getInputLevelsCount());
        assertEquals("par2", result.getName());

        assertEquals(2, result.getLevels().length);
        assertEquals(type, result.getLevels()[0].getType());
        assertEquals(type, result.getLevels()[1].getType());
    }

//todo ph 0 finish shouldFailWhenOutputLevelHasNoType
//    @Test
//    public void shouldFailWhenOutputLevelHasNoType() {
//
//        // konfiguracja
//        ParameterMockBuilder.parameter(p2).withName("par2")
//                .withLevels(
//                    LevelMockBuilder.level().withType("string").get(),
//                    LevelMockBuilder.level().get()
//                ).withEntries(
//                    ParameterEntryMockBuilder.parameterEntry("A", "value-A")
//                );
//
//        // test
//        try {
//            instance.getPreparedParameter("par2");
//            fail();
//
//        } catch (SmartParamException e) {
//            assertEquals(SmartParamErrorCode.UNKNOWN_PARAM_TYPE, e.getErrorCode());
//        }
//    }

    @Test
    public void testGetPreparedParameter__paramNotFound() {

        // test
        PreparedParameter result = instance.getPreparedParameter("par3");

        // weryfikacja
        assertNull(result);
    }

    @Test
    public void testGetPreparedParameter__array() {

        ParameterMockBuilder.parameter(p2).withName("par2")
                .withLevels(
                    LevelMockBuilder.level().withType("string").get(),
                    LevelMockBuilder.level().withType("string").get()
                )
                .inputLevels(1)
                .withEntries(
                    ParameterEntryMockBuilder.parameterEntry("*", "value")
                );

        // test
        PreparedParameter result = instance.getPreparedParameter("par2");

        // weryfikacja
        assertEquals(2, result.getLevelCount());
        assertEquals(1, result.getInputLevelsCount());
        assertEquals(',', result.getArraySeparator());

        // test 2
        when(p2.getArraySeparator()).thenReturn('/');
        result = instance.getPreparedParameter("par2");

        // weryfikacja 2
        assertEquals('/', result.getArraySeparator());
    }

    @Test
    public void testGetPreparedParameter__prepare_withMatchers() {

        ParameterMockBuilder.parameter(p2).withName("par2")
                .withLevels(
                    LevelMockBuilder.level().withType("string").withMatcherCode("between/ii").get(),
                    LevelMockBuilder.level().withType("string").withMatcherCode("between/ie").get(),
                    LevelMockBuilder.level().withType("string").get()
                )
                .inputLevels(2)
                .withEntries(
                    ParameterEntryMockBuilder.parameterEntry("A", "B", "value-AB")
                );

        // test
        PreparedParameter result = instance.getPreparedParameter("par2");

        // weryfikacja
        assertEquals(3, result.getLevelCount());
        assertEquals(2, result.getInputLevelsCount());
        assertEquals("par2", result.getName());

        assertEquals(3, result.getLevels().length);
        for (int i = 0; i < 2; i++) {
            PreparedLevel pl = result.getLevels()[i];
            assertEquals(type, pl.getType());
            assertTrue(pl.getMatcher() instanceof BetweenMatcher);
        }
    }

    @Test
    public void testGetPreparedParameter__prepare_illegalMatcher() {

        ParameterMockBuilder.parameter(p2).withName("par2")
                .withLevels(
                    LevelMockBuilder.level().withType("string").withMatcherCode("nonexisting").get()
                ).withEntries(
                    ParameterEntryMockBuilder.parameterEntry("A", "B", "value-AB")
                );

        // test
        try {
            instance.getPreparedParameter("par2");
            fail();
        } catch (SmartParamDefinitionException e) {
            assertEquals(SmartParamErrorCode.UNKNOWN_MATCHER, e.getErrorCode());
        }
    }

    @Test
    public void testGetPreparedParameter__prepare_nullTypeLevel() {

        ParameterMockBuilder.parameter(p2).withName("par2")
                .withLevels(
                    LevelMockBuilder.level().get(),
                    LevelMockBuilder.level().withType("string").get()
                ).withEntries(
                    ParameterEntryMockBuilder.parameterEntry("A", "value-AB"),
                    ParameterEntryMockBuilder.parameterEntry("B", "value-CD")
                ).inputLevels(1);

        // test
        PreparedParameter result = instance.getPreparedParameter("par2");

        // weryfikacja
        assertEquals(2, result.getLevelCount());
        assertEquals(1, result.getInputLevelsCount());
        assertEquals("par2", result.getName());
        assertNull(result.getLevels()[0].getType());
    }

    @Test
    public void testGetPreparedParameter__prepare_illegalLevelType() {

        ParameterMockBuilder.parameter(p2).withName("par2")
                .withLevels(
                    LevelMockBuilder.level("unknown-type")
                ).withEntries(
                    ParameterEntryMockBuilder.parameterEntry("A", "value-AB")
                );

        // test
        try {
            instance.getPreparedParameter("par2");
            fail();
        } catch (SmartParamDefinitionException e) {
            assertEquals(SmartParamErrorCode.UNKNOWN_PARAM_TYPE, e.getErrorCode());
        }
    }

    @Test
    public void testFindEntries() {

        // konfiguracja
        entries.add(ParameterEntryMockBuilder.parameterEntry("A", "A2", "A3", "A4"));
        //entries.add(ParameterEntryMockBuilder.parameterEntry("A", "B2", "B3", "B4"));

        // test
        List<PreparedEntry> result = instance.findEntries("param", new String[]{"A"});

        // weryfikacja
        assertEquals(1, result.size());
        assertArrayEquals(new String[]{"A", "A2", "A3", "A4"}, result.get(0).getLevels());
        //assertArrayEquals(new String[]{"A", "B2", "B3", "B4"}, result.get(1).getLevels());
    }

    @Test
    public void testGetPreparedParameter__prepare_cacheable() {

        ParameterMockBuilder.parameter(p2).withName("par2")
                .inputLevels(1).cacheable(false)
                .withLevels(
                    LevelMockBuilder.level("string"),
                    LevelMockBuilder.level("string")
                ).withEntries(
                    ParameterEntryMockBuilder.parameterEntry("A", "B", "value-AB"),
                    ParameterEntryMockBuilder.parameterEntry("C", "D", "value-CD")
                );

        // test
        PreparedParameter result = instance.getPreparedParameter("par2");

        // weryfikacja
        assertEquals(2, result.getLevelCount());
        assertEquals(1, result.getInputLevelsCount());
        assertFalse(result.isCacheable());
        assertNull(result.getIndex());
    }

    @Test
    public void testGetFirstLevels() {

        // preparing big entry: 14 parameterEntry
        ParameterEntry pe = ParameterEntryMockBuilder.parameterEntryCsv("1;2;3;4;5;6;7;8;9;10;11;12;13;14");

        // test cases
        Object[][] tests = {
            new Object[]{0, new String[]{}},
            new Object[]{1, new String[]{"1"}},
            new Object[]{4, new String[]{"1", "2", "3", "4"}},
            new Object[]{8, new String[]{"1", "2", "3", "4", "5", "6", "7", "8"}},
            new Object[]{11, new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"}},
            new Object[]{14, new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14"}},
            new Object[]{15, new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", null}}
        };

        // run test cases
        for (Object[] test : tests) {
            Integer n = (Integer) test[0];
            String[] expectedLevels = (String[]) test[1];

            // when
            String[] result = instance.getFirstLevels(pe, n);

            // then
            assertArrayEquals(expectedLevels, result);
        }
    }
}
