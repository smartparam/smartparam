package org.smartparam.engine.core.engine;

import org.smartparam.engine.core.engine.PreparedLevel;
import org.smartparam.engine.core.engine.PreparedEntry;
import org.smartparam.engine.core.engine.PreparedParameter;
import org.smartparam.engine.core.engine.ParamProviderImpl;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.smartparam.engine.core.cache.ParamCache;
import org.smartparam.engine.core.config.MatcherProvider;
import org.smartparam.engine.core.config.TypeProvider;
import org.smartparam.engine.core.exception.ParamDefinitionException;
import org.smartparam.engine.core.exception.ParamException;
import org.smartparam.engine.core.loader.ParamLoader;
import org.smartparam.engine.matchers.BetweenMatcher;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.engine.types.string.StringType;

/**
 * @author Przemek Hertel
 */
public class ParamProviderImplTest {

    private ParamCache cache;

    private ParamLoader loader;

    private PreparedParameter pp1;

    private Parameter p2;

    private ParamProviderImpl instance;

    private TypeProvider typeProvider = new TypeProvider();

    private MatcherProvider matcherProvider = new MatcherProvider();

    private StringType type = new StringType();

    private List<ParameterEntry> entries;

    @Before
    public void init() {
        pp1 = new PreparedParameter();
        p2 = new Parameter();
        entries = new ArrayList<ParameterEntry>();

        typeProvider.registerType("string", type);

        matcherProvider.registerMatcher("between/ii", new BetweenMatcher(true, true, ":"));
        matcherProvider.registerMatcher("between/ie", new BetweenMatcher(true, false, ":"));

        cache = mock(ParamCache.class);
        when(cache.get("par1")).thenReturn(pp1);
        when(cache.get("par2")).thenReturn(null);

        loader = mock(ParamLoader.class);
        when(loader.load("par2")).thenReturn(p2);
        when(loader.load("par3")).thenReturn(null);
        when(loader.findEntries(any(String.class), any(String[].class))).thenReturn(entries);

        instance = new ParamProviderImpl();
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
        Level l1 = new Level("string");
        l1.setLabel("L1");

        p2.setName("par2");
        p2.setType("string");
        p2.addLevel(l1);
        p2.addEntry(new ParameterEntry("A", "value-A"));
        p2.addEntry(new ParameterEntry("*", "value-*"));

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
        p2.setName("par2");
        p2.setType((String) null);
        p2.addLevel(new Level("string"));
        p2.addEntry(new ParameterEntry("A", "value-A"));

        // test
        try {
            instance.getPreparedParameter("par2");
            fail();

        } catch (ParamException e) {
            assertEquals(ParamException.ErrorCode.UNKNOWN_PARAM_TYPE, e.getErrorCode());
        }
    }

    @Test
    public void testGetPreparedParameter__prepare_multiValue_nullType() {

        // konfiguracja
        p2.setName("par2");
        p2.addLevel(new Level("string"), new Level("string"));
        p2.setMultivalue(true);
        p2.setInputLevels(1);
        p2.addEntry(new ParameterEntry("A;B", "value-AB"));
        p2.addEntry(new ParameterEntry("C;D", "value-CD"));

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

        // konfiguracja
        p2.setName("par2");
        p2.setType("string");
        p2.addLevel(new Level("string"), new Level("string"));
        p2.setMultivalue(true);
        p2.setInputLevels(1);
        p2.addEntry(new ParameterEntry("A;B", "value-AB"));
        p2.addEntry(new ParameterEntry("C;D", "value-CD"));

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

        // konfiguracja
        Level l1 = new Level("string");
        l1.setLabel("L1");

        p2.setName("par2");
        p2.setType("string");
        p2.addLevel(l1);
        p2.setArray(true);
        p2.addEntry(new ParameterEntry("*", "value"));

        // test
        PreparedParameter result = instance.getPreparedParameter("par2");

        // weryfikacja
        assertEquals(1, result.getLevelCount());
        assertTrue(result.isArray());
        assertEquals(',', result.getArraySeparator());

        // test 2
        p2.setArraySeparator('/');
        result = instance.getPreparedParameter("par2");

        // weryfikacja 2
        assertTrue(result.isArray());
        assertEquals('/', result.getArraySeparator());
    }

    @Test
    public void testGetPreparedParameter__prepare_withMatchers() {

        // konfiguracja
        Level l1 = new Level("string");
        l1.setMatcherCode("between/ii");
        Level l2 = new Level("string");
        l2.setMatcherCode("between/ie");

        p2.setName("par2");
        p2.setType("string");
        p2.addLevel(l1, l2);

        p2.addEntry(new ParameterEntry("A;B", "value-AB"));

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

        // konfiguracja
        Level l1 = new Level("string");
        l1.setMatcherCode("nonexisting");

        p2.setName("par2");
        p2.setType("string");
        p2.addLevel(l1);

        p2.addEntry(new ParameterEntry("A;B", "value-AB"));

        // test
        try {
            instance.getPreparedParameter("par2");
            fail();
        } catch (ParamDefinitionException e) {
            assertEquals(ParamException.ErrorCode.UNKNOWN_MATCHER, e.getErrorCode());
        }
    }

    @Test
    public void testGetPreparedParameter__prepare_nullTypeLevel() {

        // konfiguracja
        p2.setName("par2");
        p2.setType("string");
        p2.addLevel(new Level());
        p2.addEntry(new ParameterEntry("A", "value-AB"));
        p2.addEntry(new ParameterEntry("B", "value-CD"));

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

        // konfiguracja
        p2.setName("par2");
        p2.setType("string");
        p2.addLevel(new Level("unknown-type"));
        p2.addEntry(new ParameterEntry("A", "value-AB"));

        // test
        try {
            instance.getPreparedParameter("par2");
            fail();
        } catch (ParamDefinitionException e) {
            assertEquals(ParamException.ErrorCode.UNKNOWN_PARAM_TYPE, e.getErrorCode());
        }
    }

    @Test
    public void testFindEntries() {

        // konfiguracja
        entries.add(new ParameterEntry("A", "A2", "A3", "A4"));
        entries.add(new ParameterEntry("A", "B2", "B3", "B4"));

        // test
        List<PreparedEntry> result = instance.findEntries("param", new String[]{"A"});

        // weryfikacja
        assertEquals(2, result.size());
        assertArrayEquals(new String[]{"A", "A2", "A3", "A4"}, result.get(0).getLevels());
        assertArrayEquals(new String[]{"A", "B2", "B3", "B4"}, result.get(1).getLevels());
    }

    @Test
    public void testGetPreparedParameter__prepare_cacheable() {

        // konfiguracja
        p2.setName("par2");
        p2.addLevel(new Level("string"), new Level("string"));
        p2.setMultivalue(true);
        p2.setInputLevels(1);
        p2.addEntry(new ParameterEntry("A;B", "value-AB"));
        p2.addEntry(new ParameterEntry("C;D", "value-CD"));
        p2.setCacheable(false);

        // test
        PreparedParameter result = instance.getPreparedParameter("par2");

        // weryfikacja
        assertEquals(2, result.getLevelCount());
        assertEquals(1, result.getInputLevelsCount());
        assertFalse(result.isCacheable());
        assertNull(result.getIndex());
    }

}
