package org.smartparam.engine.core.engine;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.smartparam.engine.core.cache.ParamCache;
import org.smartparam.engine.core.provider.SmartMatcherProvider;
import org.smartparam.engine.core.provider.TypeProvider;
import org.smartparam.engine.core.exception.SmartParamDefinitionException;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.core.loader.ParamProvider;
import org.smartparam.engine.matchers.BetweenMatcher;
import org.smartparam.engine.test.builder.LevelMockBuilder;
import org.smartparam.engine.test.builder.ParameterEntryMockBuilder;
import org.smartparam.engine.test.builder.ParameterMockBuilder;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.engine.types.string.StringType;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.smartparam.engine.core.provider.SmartTypeProvider;
import org.smartparam.engine.core.exception.SmartParamErrorCode;

/**
 * @author Przemek Hertel
 */
public class SmartParamPreparerTest {

    private ParamCache cache;

    private ParamProvider loader;

    private PreparedParameter pp1;

    private Parameter p2;

    private SmartParamPreparer instance;

    private TypeProvider typeProvider = new SmartTypeProvider();

    private SmartMatcherProvider matcherProvider = new SmartMatcherProvider();

    private StringType type = new StringType();

    private List<ParameterEntry> entries;

    @Test
    public void noop() {

    }
    @Before
    public void init() {
        pp1 = new PreparedParameter();
        p2 = ParameterMockBuilder.parameter().get();
        entries = new ArrayList<ParameterEntry>();

        typeProvider.registerType("string", type);

        matcherProvider.registerMatcher("between/ii", new BetweenMatcher(true, true, ":"));
        matcherProvider.registerMatcher("between/ie", new BetweenMatcher(true, false, ":"));

        cache = mock(ParamCache.class);
        when(cache.get("par1")).thenReturn(pp1);
        when(cache.get("par2")).thenReturn(null);

        loader = mock(ParamProvider.class);
        when(loader.load("par2")).thenReturn(p2);
        when(loader.load("par3")).thenReturn(null);
        when(loader.findEntries(any(String.class), any(String[].class))).thenReturn(entries);

        instance = new SmartParamPreparer();
        instance.setCache(cache);
        instance.setLoader(loader);
        instance.setTypeProvider(typeProvider);
        instance.setMatcherProvider(matcherProvider);
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
        Level l1 = LevelMockBuilder.level().withType("string").withLabel("L1").get();

        ParameterMockBuilder.parameter(p2).withName("par2").withType("string").withLevels(l1).withEntries(
                ParameterEntryMockBuilder.parameterEntry("A", "value-A"),
                ParameterEntryMockBuilder.parameterEntry("*", "value-*")
                );

        // test
        PreparedParameter result = instance.getPreparedParameter("par2");

        // weryfikacja
        assertEquals(1, result.getLevelCount());
        assertEquals(1, result.getInputLevelsCount());
        assertEquals("par2", result.getName());
        assertSame(type, result.getType());

        assertEquals(1, result.getLevels().length);
        assertEquals(type, result.getLevels()[0].getType());
    }

    @Test
    public void testGetPreparedParameter__prepare_singleValue_nullType() {

        // konfiguracja
        ParameterMockBuilder.parameter(p2).withName("par2").withType(null)
                .withLevels(
                    LevelMockBuilder.level("string")
                ).withEntries(
                    ParameterEntryMockBuilder.parameterEntry("A", "value-A")
                );

        // test
        try {
            instance.getPreparedParameter("par2");
            fail();

        } catch (SmartParamException e) {
            assertEquals(SmartParamErrorCode.UNKNOWN_PARAM_TYPE, e.getErrorCode());
        }
    }

    @Test
    public void testGetPreparedParameter__prepare_multiValue_nullType() {

        ParameterMockBuilder.parameter(p2).withName("par2").withType(null)
                .withMultivalue(true).withInputLevels(1)
                .withLevels(
                    LevelMockBuilder.level("string"),
                    LevelMockBuilder.level("string")
                ).withEntries(
                    ParameterEntryMockBuilder.parameterEntry("A;B", "value-AB"),
                    ParameterEntryMockBuilder.parameterEntry("C;D", "value-CD")
                );

        // test
        PreparedParameter result = instance.getPreparedParameter("par2");

        // weryfikacja
        assertEquals(2, result.getLevelCount());
        assertEquals(1, result.getInputLevelsCount());
        assertEquals("par2", result.getName());
        assertNull(result.getType());
    }

    @Test
    public void testGetPreparedParameter__prepare_multiValue_notnullType() {

        ParameterMockBuilder.parameter(p2).withName("par2").withType("string")
                .withMultivalue(true).withInputLevels(1)
                .withLevels(
                    LevelMockBuilder.level("string"),
                    LevelMockBuilder.level("string")
                ).withEntries(
                    ParameterEntryMockBuilder.parameterEntry("A;B", "value-AB"),
                    ParameterEntryMockBuilder.parameterEntry("C;D", "value-CD")
                );

        // test
        PreparedParameter result = instance.getPreparedParameter("par2");

        // weryfikacja
        assertEquals(2, result.getLevelCount());
        assertEquals(1, result.getInputLevelsCount());
        assertEquals("par2", result.getName());
        assertSame(type, result.getType());
    }

    @Test
    public void testGetPreparedParameter__paramNotFound() {

        // test
        PreparedParameter result = instance.getPreparedParameter("par3");

        // weryfikacja
        assertNull(result);
    }

    @Test
    public void testGetPreparedParameter__array() {

        ParameterMockBuilder.parameter(p2).withName("par2").withType("string")
                .withArray(true)
                .withLevels(
                    LevelMockBuilder.level().withType("string").withLabel("L1").get()
                ).withEntries(
                    ParameterEntryMockBuilder.parameterEntry("*", "value")
                );

        // test
        PreparedParameter result = instance.getPreparedParameter("par2");

        // weryfikacja
        assertEquals(1, result.getLevelCount());
        assertTrue(result.isArray());
        assertEquals(',', result.getArraySeparator());

        // test 2
        when(p2.getArraySeparator()).thenReturn('/');
        result = instance.getPreparedParameter("par2");

        // weryfikacja 2
        assertTrue(result.isArray());
        assertEquals('/', result.getArraySeparator());
    }

    @Test
    public void testGetPreparedParameter__prepare_withMatchers() {

        ParameterMockBuilder.parameter(p2).withName("par2").withType("string")
                .withLevels(
                    LevelMockBuilder.level().withType("string").withMatcherCode("between/ii").get(),
                    LevelMockBuilder.level().withType("string").withMatcherCode("between/ie").get()
                ).withEntries(
                    ParameterEntryMockBuilder.parameterEntry("A;B", "value-AB")
                );

        // test
        PreparedParameter result = instance.getPreparedParameter("par2");

        // weryfikacja
        assertEquals(2, result.getLevelCount());
        assertEquals(2, result.getInputLevelsCount());
        assertEquals("par2", result.getName());
        assertSame(type, result.getType());

        assertEquals(2, result.getLevels().length);
        for (int i = 0; i < 2; i++) {
            PreparedLevel pl = result.getLevels()[i];
            assertEquals(type, pl.getType());
            assertTrue(pl.getMatcher() instanceof BetweenMatcher);
        }
    }

    @Test
    public void testGetPreparedParameter__prepare_illegalMatcher() {

        ParameterMockBuilder.parameter(p2).withName("par2").withType("string")
                .withLevels(
                    LevelMockBuilder.level().withType("string").withMatcherCode("nonexisting").get()
                ).withEntries(
                    ParameterEntryMockBuilder.parameterEntry("A;B", "value-AB")
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

        ParameterMockBuilder.parameter(p2).withName("par2").withType("string")
                .withLevels(
                    LevelMockBuilder.level().get()
                ).withEntries(
                    ParameterEntryMockBuilder.parameterEntry("A", "value-AB"),
                    ParameterEntryMockBuilder.parameterEntry("B", "value-CD")
                );

        // test
        PreparedParameter result = instance.getPreparedParameter("par2");

        // weryfikacja
        assertEquals(1, result.getLevelCount());
        assertEquals(1, result.getInputLevelsCount());
        assertEquals("par2", result.getName());
        assertNull(result.getLevels()[0].getType());
    }

    @Test
    public void testGetPreparedParameter__prepare_illegalLevelType() {

        ParameterMockBuilder.parameter(p2).withName("par2").withType("string")
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
        entries.add(ParameterEntryMockBuilder.parameterEntry(new String[] { "A", "A2", "A3", "A4"}, null ));
        entries.add(ParameterEntryMockBuilder.parameterEntry(new String[] { "A", "B2", "B3", "B4"}, null ));

        // test
        List<PreparedEntry> result = instance.findEntries("param", new String[]{"A"});

        // weryfikacja
        assertEquals(2, result.size());
        assertArrayEquals(new String[]{"A", "A2", "A3", "A4"}, result.get(0).getLevels());
        assertArrayEquals(new String[]{"A", "B2", "B3", "B4"}, result.get(1).getLevels());
    }

    @Test
    public void testGetPreparedParameter__prepare_cacheable() {

        ParameterMockBuilder.parameter(p2).withName("par2").withType("string")
                .withMultivalue(true).withInputLevels(1).withCacheable(false)
                .withLevels(
                    LevelMockBuilder.level("string"),
                    LevelMockBuilder.level("string")
                ).withEntries(
                    ParameterEntryMockBuilder.parameterEntry("A;B", "value-AB"),
                    ParameterEntryMockBuilder.parameterEntry("C;D", "value-CD")
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
        
        // preparing big entry: 14 levels
        ParameterEntry pe = ParameterEntryMockBuilder.parameterEntry("1;2;3;4;5;6;7;8;9;10;11;12;13;14", "value");

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
