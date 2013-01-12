package pl.generali.merkury.param.core.engine;

import java.util.ArrayList;
import pl.generali.merkury.param.core.loader.ParamLoader;
import java.util.Arrays;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import pl.generali.merkury.param.assemblers.GenericEnumAssembler;
import pl.generali.merkury.param.core.cache.MapFunctionCache;
import pl.generali.merkury.param.core.cache.MapParamCache;
import pl.generali.merkury.param.core.config.AssemblerProvider;
import pl.generali.merkury.param.core.config.InvokerProvider;
import pl.generali.merkury.param.core.config.TypeProvider;
import pl.generali.merkury.param.core.context.DefaultContext;
import pl.generali.merkury.param.core.context.LevelValues;
import pl.generali.merkury.param.core.exception.ParamException;
import pl.generali.merkury.param.core.exception.ParamUsageException;
import pl.generali.merkury.param.core.function.JavaFunctionInvoker;
import pl.generali.merkury.param.core.loader.FunctionLoader;
import pl.generali.merkury.param.core.type.AbstractHolder;
import pl.generali.merkury.param.model.Function;
import pl.generali.merkury.param.model.Level;
import pl.generali.merkury.param.model.Parameter;
import pl.generali.merkury.param.model.ParameterEntry;
import pl.generali.merkury.param.model.functions.JavaFunction;
import pl.generali.merkury.param.types.integer.IntegerHolder;
import pl.generali.merkury.param.types.integer.IntegerType;
import pl.generali.merkury.param.types.plugin.PluginType;
import pl.generali.merkury.param.types.string.StringHolder;
import pl.generali.merkury.param.types.string.StringType;

/**
 * @author Przemek Hertel
 */
public class ParamEngineScenarioTest {

    TypeProvider typeProvider;

    ParamLoader loader;

    ParamProviderImpl paramProvider;

    AssemblerProvider assemblerProvider;

    InvokerProvider invokerProvider;

    ParamEngine engine;

    FunctionLoader functionLoader;

    @Before
    public void init() {
        typeProvider = new TypeProvider();
        typeProvider.registerType("string", new StringType());
        typeProvider.registerType("integer", new IntegerType());
        typeProvider.registerType("plugin", new PluginType());

        loader = mock(ParamLoader.class);

        paramProvider = new ParamProviderImpl();
        paramProvider.setTypeProvider(typeProvider);
        paramProvider.setLoader(loader);
        paramProvider.setCache(new MapParamCache());

        invokerProvider = new InvokerProvider();
        invokerProvider.registerInvoker("java", new JavaFunctionInvoker());

        assemblerProvider = new AssemblerProvider();

        functionLoader = mock(FunctionLoader.class);

        FunctionProviderImpl fp = new FunctionProviderImpl();
        fp.setLoader(functionLoader);
        fp.setCache(new MapFunctionCache());

        engine = new ParamEngine();
        engine.setParamProvider(paramProvider);
        engine.setInvokerProvider(invokerProvider);
        engine.setAssemblerProvider(assemblerProvider);
        engine.setFunctionProvider(fp);
    }

    @Test
    public void testGetValue__nullable() {

        // zaleznosci
        Level l1 = new Level("string");
        Level l2 = new Level("string");

        Parameter par = new Parameter();
        par.setName("par");
        par.setType("integer");
        par.addLevel(l1, l2);
        par.setNullable(true);

        Function f = new Function();
        f.setType(par.getType());
        f.setImplementation(new JavaFunction(this.getClass(), "calculate"));

        Function f2 = new Function();
        f2.setType(par.getType());
        f2.setImplementation(new JavaFunction(this.getClass(), "calculate2"));

        par.addEntry(new ParameterEntry("A;F", "11"));
        par.addEntry(new ParameterEntry("A;G", "12"));
        par.addEntry(new ParameterEntry("A;*", "13"));
        par.addEntry(new ParameterEntry("B;F", "21"));
        par.addEntry(new ParameterEntry("B;*", "22"));
        par.addEntry(new ParameterEntry("*;F", "31"));
        par.addEntry(new ParameterEntry("*;X", f));
        par.addEntry(new ParameterEntry("*;Y", f2));

        // konfiguracja
        when(loader.load("par")).thenReturn(par);

        // testy - wartosci poziomow
        String[][] tests = {
            {"A", "F"}, //AF 11
            {"A", "G"}, //AG 12
            {"A", "Z"}, //A* 13
            {"B", "F"}, //BF 21
            {"B", "B"}, //B* 22
            {"C", "F"}, //*F 31
            {"S", "X"}, //*X f  = 78
            {"S", "Y"}, //*Y f2 = null
            {"Z", "Z"} //null
        };

        // oczekiwane wyniki
        Integer[] expected = {11, 12, 13, 21, 22, 31, 78, null, null};

        // test i weryfikacja
        for (int i = 0; i < tests.length; i++) {
            Object[] lvalues = tests[i];
            Integer expectedResult = expected[i];

            // przekazanie wartosci leveli wprost
            Integer result = engine.getValue("par", lvalues).getInteger();
            assertEquals(expectedResult, result);

            // przekazanie wartosci leveli w kontekscie
            result = engine.getValue("par", new LevelValues(lvalues)).getInteger();
            assertEquals(expectedResult, result);
        }
    }

    @Test
    public void testGetValue__nullLevelValue() {

        // zaleznosci
        Level l1 = new Level("string");
        Level l2 = new Level("string");

        Parameter par = new Parameter();
        par.setName("par");
        par.setType("string");
        par.addLevel(l1, l2);

        par.addEntry(new ParameterEntry(new String[]{"A", null}, "A-null"));
        par.addEntry(new ParameterEntry(new String[]{"A", "B"}, "A-B"));
        par.addEntry(new ParameterEntry(new String[]{null, "B"}, "null-B"));
        par.addEntry(new ParameterEntry(new String[]{null, null}, "null-null"));
        par.addEntry(new ParameterEntry(new String[]{"*", "*"}, "default"));

        // konfiguracja
        when(loader.load("par")).thenReturn(par);

        // testy - wartosci poziomow
        String[][] tests = {
            {"A", null},
            {"A", "B"},
            {null, "B"},
            {null, null},
            {null, "Z"}
        };

        // oczekiwane wyniki
        String[] expected = {
            "A-null",
            "A-B",
            "null-B",
            "null-null",
            "default"
        };

        // test i weryfikacja
        for (int i = 0; i < tests.length; i++) {
            Object[] lvalues = tests[i];
            String expectedResult = expected[i];

            // przekazanie wartosci leveli wprost
            String result = engine.getValue("par", lvalues).getString();
            assertEquals(expectedResult, result);

            // przekazanie wartosci leveli w kontekscie
            result = engine.getValue("par", new LevelValues(lvalues)).getString();
            assertEquals(expectedResult, result);
        }
    }

    @Test
    public void testGetValue__notnull() {

        // zaleznosci
        Level l1 = new Level("string");

        Parameter par = new Parameter();
        par.setName("par");
        par.setType("integer");
        par.addLevel(l1);
        par.setNullable(false);

        Function f2 = new Function();
        f2.setType(par.getType());
        f2.setImplementation(new JavaFunction(this.getClass(), "calculate2"));

        par.addEntry(new ParameterEntry("A", "11"));
        par.addEntry(new ParameterEntry("B", f2));

        // konfiguracja
        when(loader.load("par")).thenReturn(par);

        // testy - wartosci poziomow
        String[] tests = {
            "A", //11
            "B", //null
            "C" //ex
        };

        // oczekiwane wyniki
        Object[] expected = {
            11,
            null,
            new ParamException(ParamException.ErrorCode.PARAM_VALUE_NOT_FOUND, "")
        };

        // test i weryfikacja
        for (int i = 0; i < tests.length; i++) {
            String lv = tests[i];
            Object expectedObject = expected[i];

            if (expectedObject instanceof Exception) {
                try {
                    engine.getValue("par", lv).getInteger();
                    fail();
                } catch (ParamException e) {
                    assertEquals(expectedObject.getClass(), e.getClass());
                }

            } else {
                Integer result = engine.getValue("par", lv).getInteger();
                assertEquals(expectedObject, result);
            }
        }
    }

    @Test
    public void testGetMultiValue__notnull() {

        // zaleznosci
        Level l1 = new Level("string");
        Level l2 = new Level("string");
        Level l3 = new Level("integer");

        Parameter par = new Parameter();
        par.setName("par");
        par.setType("string");
        par.addLevel(l1, l2, l3);
        par.setMultivalue(true);
        par.setInputLevels(1);
        par.setNullable(false);

        par.addEntry(new ParameterEntry("A", "X", "1"));
        par.addEntry(new ParameterEntry("B", "Y", "2"));
        par.addEntry(new ParameterEntry("C", "Z", "3"));

        // konfiguracja
        when(loader.load("par")).thenReturn(par);

        // testy - wartosci poziomow
        String[] tests = {
            "A", //MV: X 1
            "B", //MV: Y 2
            "C", //MV: Z 3
            "F" //ex
        };

        // oczekiwane wyniki
        boolean[] expectedException = {
            false,
            false,
            false,
            true
        };

        // test i weryfikacja
        for (int i = 0; i < tests.length; i++) {
            String lv = tests[i];
            boolean expectedEx = expectedException[i];

            // przekazanie wartosci leveli wprost
            try {
                MultiValue result = engine.getMultiValue("par", new DefaultContext().withLevelValues(lv));

                assertFalse(expectedEx);    // wyjatek nie jest oczekiwany
                assertNotNull(result);

            } catch (ParamException e) {

                assertTrue(expectedEx);     // wyjatek jest oczekiwany
                assertEquals(ParamException.ErrorCode.PARAM_VALUE_NOT_FOUND, e.getErrorCode());
            }
        }
    }

    @Test
    public void testGetValue__noLevelParam() {

        // zaleznosci
        Parameter par = new Parameter();
        par.setName("par");
        par.setType("integer");
        par.addEntry(new ParameterEntry("", "123"));

        // konfiguracja
        when(loader.load("par")).thenReturn(par);

        // test
        Integer result = engine.getValue("par", new DefaultContext()).getInteger();

        // weryfikacja
        assertEquals(new Integer(123), result);
    }

    @Test
    public void testGetValue__illegalLevelValues() {

        // zaleznosci
        Level l1 = new Level("string");
        Level l2 = new Level("string");

        Parameter par = new Parameter();
        par.setName("par");
        par.setType("integer");
        par.addLevel(l1, l2);

        par.addEntry(new ParameterEntry("A;B", "11"));
        par.addEntry(new ParameterEntry("B;C", "12"));

        // konfiguracja
        when(loader.load("par")).thenReturn(par);

        // test
        try {
            engine.getValue("par", "A", "B", "C", "D");
            fail();
        } catch (ParamUsageException e) {
            assertEquals(ParamException.ErrorCode.ILLEGAL_LEVEL_VALUES, e.getErrorCode());
        }
    }

    @Test
    public void testGetValue__findParameterEntry__nocache() {

        // zaleznosci
        ParameterEntry pe1 = new ParameterEntry("A;B", "11");
        ParameterEntry pe2 = new ParameterEntry("B;C", "12");
        ParameterEntry pe3 = new ParameterEntry("C;D", "13");

        Level l1 = new Level("string");
        Level l2 = new Level("string");

        Parameter par = new Parameter();
        par.setName("par");
        par.setType("integer");
        par.addLevel(l1, l2);
        par.setCacheable(false);
        par.setNullable(true);

        par.addEntry(pe1);
        par.addEntry(pe2);
        par.addEntry(pe3);

        // konfiguracja
        List<ParameterEntry> resultAB = Arrays.asList(pe1);
        List<ParameterEntry> resultBC = Arrays.asList(pe2);
        List<ParameterEntry> resultCD = Arrays.asList(pe3);
        List<ParameterEntry> resultXY = new ArrayList<ParameterEntry>();

        when(loader.load("par")).thenReturn(par);
        when(loader.findEntries("par", new String[]{"A", "B"})).thenReturn(resultAB);
        when(loader.findEntries("par", new String[]{"B", "C"})).thenReturn(resultBC);
        when(loader.findEntries("par", new String[]{"C", "D"})).thenReturn(resultCD);
        when(loader.findEntries("par", new String[]{"X", "Y"})).thenReturn(resultXY);

        // test
        assertEquals(11, engine.getValue("par", "A", "B").intValue());
        assertEquals(12, engine.getValue("par", "B", "C").intValue());
        assertEquals(13, engine.getValue("par", "C", "D").intValue());
        
        assertTrue(engine.getValue("par", "X", "Y").isNull());
    }


    @Test
    public void testGetMultiValue__nullable() {

        // zaleznosci
        Level l1 = new Level("string");
        Level l2 = new Level("string");
        Level l3 = new Level("integer");

        Parameter par = new Parameter();
        par.setName("par");
        par.setType("string");
        par.addLevel(l1, l2, l3);
        par.setMultivalue(true);
        par.setInputLevels(1);
        par.setNullable(true);

        par.addEntry(new ParameterEntry("A", "X", "1"));
        par.addEntry(new ParameterEntry("B", "Y", "2"));

        // konfiguracja
        when(loader.load("par")).thenReturn(par);

        // testy - wartosci poziomow
        String[] tests = {
            "A", //MV: X 1
            "B", //MV: Y 2
            "F" //null
        };

        // oczekiwane wyniki
        MultiValue[] expected = {
            new MultiValue(new Object[]{new StringHolder("X"), new IntegerHolder(1L)}),
            new MultiValue(new Object[]{new StringHolder("Y"), new IntegerHolder(2L)}),
            null
        };

        // test i weryfikacja
        for (int i = 0; i < tests.length; i++) {
            String lv = tests[i];
            MultiValue expectedResult = expected[i];

            // przekazanie wartosci leveli wprost
            MultiValue result = engine.getMultiValue("par", new DefaultContext().withLevelValues(lv));

            if (expectedResult != null) {
                assertEquals(expectedResult.getValue(1), result.getValue(1));
                assertEquals(expectedResult.getValue(2), result.getValue(2));
            } else {
                assertNull(result);
            }
        }
    }

    public int calculate(DefaultContext ctx) {
        return 78;
    }

    public Integer calculate2(DefaultContext ctx) {
        return null;
    }

    @Test
    public void testGetMultiValue__2() {
        Level l1 = new Level("string");
        Level l2 = new Level("string");
        Level l3 = new Level("string");
        Level l4 = new Level("string");

        Parameter par = new Parameter();
        par.setType("string");
        par.setName("par");
        par.addLevel(l1, l2, l3, l4);

        par.setMultivalue(true);
        par.setInputLevels(2);

        par.addEntry(new ParameterEntry("A;B;Z;1", (String) null));
        par.addEntry(new ParameterEntry("A;C;Z;2", (String) null));
        par.addEntry(new ParameterEntry("A;D;Y;3", (String) null));
        par.addEntry(new ParameterEntry("B;F;3;4", (String) null));
        par.addEntry(new ParameterEntry("B;G;3;5", (String) null));
        par.addEntry(new ParameterEntry("B;H;3;6", (String) null));

        when(loader.load("par")).thenReturn(par);

        // test 1
        MultiValue mv = engine.getMultiValue("par", new LevelValues("A", "B"));
        assertEquals(StringHolder.class, mv.getValue(1).getClass());
        assertEquals(StringHolder.class, mv.getValue(2).getClass());
        assertEquals("Z", mv.getString(1));
        assertEquals("1", mv.getString(2));

        // test 2
        mv = engine.getMultiValue("par", new LevelValues("B", "H"));
        assertArrayEquals(new String[]{"3", "6"}, mv.asStrings());
    }

    @Test
    public void testGetMultiValue__withArray() {
        Level l1 = new Level("string");
        Level l2 = new Level("string");
        Level l3 = new Level("integer");
        l3.setArray(true);

        Parameter par = new Parameter();
        par.setType("string");
        par.setName("par");
        par.addLevel(l1, l2, l3);

        par.setMultivalue(true);
        par.setInputLevels(1);

        par.addEntry(new ParameterEntry("A", "XX", "1,2,3"));
        par.addEntry(new ParameterEntry("B", "YY", "4"));
        par.addEntry(new ParameterEntry("*", "ZZ", ""));

        when(loader.load("par")).thenReturn(par);

        // test 1
        MultiValue mv = engine.getMultiValue("par", new LevelValues("A"));
        assertTrue(mv.getValue(1) instanceof StringHolder);
        assertTrue(mv.getArray(2) instanceof IntegerHolder[]);
        assertEquals("XX", mv.getValue(1).getString());
        assertArrayEquals(new IntegerHolder[]{new IntegerHolder(1L), new IntegerHolder(2L), new IntegerHolder(3L)}, mv.getArray(2));

        // test 2
        mv = engine.getMultiValue("par", new LevelValues("B"));
        assertEquals("YY", mv.getValue(1).getString());
        assertEquals(4, mv.getArray(2)[0].intValue());

        // test 3
        mv = engine.getMultiValue("par", new LevelValues("Z"));
        assertEquals("ZZ", mv.getValue(1).getString());
        assertEquals(0, mv.getArray(2).length);
    }

    @Test
    public void testGetMultiValue__illegalUsage() {
        Parameter par = new Parameter();
        par.setType("string");
        par.setName("par");
        par.addLevel(new Level("string"));
        par.setMultivalue(false);       // nie mozna uzywac metody getMultiValue
        par.addEntry(new ParameterEntry("A"));

        when(loader.load("par")).thenReturn(par);

        // test
        try {
            engine.getMultiValue("par", new DefaultContext());
            fail();
        } catch (ParamException e) {
            assertEquals(ParamException.ErrorCode.ILLEGAL_API_USAGE, e.getErrorCode());
        }
    }

    @Test
    public void testGetResult() {

        // zaleznosci
        Parameter par = new Parameter();
        par.setName("par");
        par.setType("string");
        par.addLevel(new Level("string"));
        par.setNullable(true);
        par.addEntry(new ParameterEntry("C", "A3"));
        par.addEntry(new ParameterEntry("D", "A4"));

        // konfiguracja zaleznosci
        when(loader.load("par")).thenReturn(par);
        assemblerProvider.registerAssemblerOwner(new GenericEnumAssembler());

        // testy
        String[] tests = {
            "C",
            "D",
            "X"
        };

        // oczekiwane obiekty
        LetterType[] expected = {
            LetterType.A3,
            LetterType.A4,
            null
        };

        // testy
        for (int i = 0; i < tests.length; i++) {
            String level = tests[i];
            LetterType expectedResult = expected[i];

            // test 1
            LetterType result = engine.getResult("par", LetterType.class, new DefaultContext().withLevelValues(level));
            assertEquals(expectedResult, result);

            // test 2
            result = engine.getResult("par", LetterType.class, new DefaultContext().withLevelValues(level).withResultClass(LetterType.class));
            assertEquals(expectedResult, result);

            // test 3
            result = (LetterType) engine.getResult("par", new LevelValues(level).withResultClass(LetterType.class));
            assertEquals(expectedResult, result);
        }
    }

    @Test
    public void testGetResult__illegalArgument() {

        // zaleznosci
        Parameter par = new Parameter();
        par.setName("par");
        par.setType("string");
        par.addLevel(new Level("string"));
        par.addEntry(new ParameterEntry("C", "A3"));

        // konfiguracja zaleznosci
        when(loader.load("par")).thenReturn(par);
        assemblerProvider.registerAssemblerOwner(new GenericEnumAssembler());

        // test 1 - rozbieznosc miedzy podanym resultClass a przechowywanym w kontekscie
        try {
            engine.getResult("par", LetterType.class, new LevelValues("C").withResultClass(Integer.class));
            fail();
        } catch (ParamUsageException e) {
            assertEquals(ParamException.ErrorCode.ILLEGAL_API_USAGE, e.getErrorCode());
        }

        // test 2 - brak resultClass w kontekscie
        try {
            engine.getResult("par", new LevelValues("C"));
            fail();
        } catch (ParamUsageException e) {
            assertEquals(ParamException.ErrorCode.ILLEGAL_API_USAGE, e.getErrorCode());
        }
    }

    @Test
    public void testGetResultArray() {
        Level l1 = new Level("string");

        Parameter par = new Parameter();
        par.setName("par");
        par.setType("string");
        par.addLevel(l1);
        par.setArray(true);

        par.addEntry(new ParameterEntry("A", "A3,A4"));
        par.addEntry(new ParameterEntry("B", "A3, A4, A5"));
        par.addEntry(new ParameterEntry("C", ""));
        par.addEntry(new ParameterEntry("*", "A1"));

        // konfiguracja zaleznosci
        when(loader.load("par")).thenReturn(par);
        assemblerProvider.registerAssemblerOwner(new GenericEnumAssembler());

        // testy
        String[] tests = {
            "A",
            "B",
            "C",
            "OTHER"
        };

        // oczekiwane wyniki
        LetterType[][] expected = {
            {LetterType.A3, LetterType.A4},
            {LetterType.A3, LetterType.A4, LetterType.A5},
            {},
            {LetterType.A1}
        };

        // testy
        for (int i = 0; i < tests.length; i++) {
            String level = tests[i];
            LetterType[] expectedArray = expected[i];

            // test 1 - typowe uzycie
            LetterType[] array = engine.getResultArray("par", LetterType.class, new LevelValues(level));
            assertArrayEquals(expectedArray, array);

            // test 2 - kontekst zawiera juz resultClass
            array = engine.getResultArray("par", LetterType.class, new LevelValues(level).withResultClass(LetterType.class));
            assertArrayEquals(expectedArray, array);
        }
    }

    @Test
    public void testGetResultArray__illegalArgument() {
        Level l1 = new Level("string");

        Parameter par = new Parameter();
        par.setName("par");
        par.setType("string");
        par.addLevel(l1);
        par.setArray(true);

        par.addEntry(new ParameterEntry("A", "A3,A4"));

        // konfiguracja zaleznosci
        when(loader.load("par")).thenReturn(par);
        assemblerProvider.registerAssemblerOwner(new GenericEnumAssembler());

        // test
        try {
            engine.getResultArray("par", LetterType.class, new LevelValues("A").withResultClass(Integer.class));
            fail();
        } catch (ParamUsageException e) {
            assertEquals(ParamException.ErrorCode.ILLEGAL_API_USAGE, e.getErrorCode());
        }
    }

    @Test
    public void testGetMultiRow() {

        Level l1 = new Level("string");
        Level l2 = new Level("string");
        Level l3 = new Level("string");

        Parameter par = new Parameter();
        par.setType("string");
        par.setName("par");
        par.addLevel(l1, l2, l3);
        par.setNullable(true);

        par.setMultivalue(true);
        par.setInputLevels(1);

        par.addEntry(new ParameterEntry("A;B;Z", (String) null));
        par.addEntry(new ParameterEntry("A;C;Z", (String) null));
        par.addEntry(new ParameterEntry("A;D;Y", (String) null));
        par.addEntry(new ParameterEntry("B;F;3", (String) null));
        par.addEntry(new ParameterEntry("B;G;3", (String) null));
        par.addEntry(new ParameterEntry("B;H;3", (String) null));

        when(loader.load("par")).thenReturn(par);

        // test 1
        MultiRow mr = engine.getMultiRow("par", new LevelValues("A"));
        assertTrue(contains(mr, new Object[]{"B", "Z"}));
        assertTrue(contains(mr, new Object[]{"C", "Z"}));
        assertTrue(contains(mr, new Object[]{"D", "Y"}));

        // test 2
        mr = engine.getMultiRow("par", new LevelValues("B"));
        assertTrue(contains(mr, new Object[]{"F", "3"}));
        assertTrue(contains(mr, new Object[]{"G", "3"}));
        assertTrue(contains(mr, new Object[]{"H", "3"}));

        // test 3
        mr = engine.getMultiRow("par", new LevelValues("NON_EXISTING"));
        assertNull(mr);
    }

    @Test
    public void testGetMultiRow__withArray() {

        Level l1 = new Level("string");
        Level l2 = new Level("string");
        Level l3 = new Level("integer");
        l3.setArray(true);

        Parameter par = new Parameter();
        par.setType("string");
        par.setName("par");
        par.addLevel(l1, l2, l3);

        par.setMultivalue(true);
        par.setInputLevels(1);

        par.addEntry(new ParameterEntry("A;B;1", (String) null));
        par.addEntry(new ParameterEntry("A;C;2,3", (String) null));
        par.addEntry(new ParameterEntry("A;D;4,5,6", (String) null));
        par.addEntry(new ParameterEntry("A;E;", (String) null));

        when(loader.load("par")).thenReturn(par);

        // test
        MultiRow mr = engine.getMultiRow("par", new LevelValues("A"));

        // weryfikacja
        for (MultiValue row : mr.getRows()) {
            String v1 = row.getString(1);
            if (v1.equals("B")) {
                Integer[] array = row.getIntegerArray(2);
                assertArrayEquals(new Integer[]{1}, array);
            }
            if (v1.equals("C")) {
                Integer[] array = row.getIntegerArray(2);
                assertArrayEquals(new Integer[]{2, 3}, array);
            }
            if (v1.equals("D")) {
                Integer[] array = row.getIntegerArray(2);
                assertArrayEquals(new Integer[]{4, 5, 6}, array);
            }
            if (v1.equals("E")) {
                Integer[] array = row.getIntegerArray(2);
                assertArrayEquals(new Integer[]{}, array);
            }
        }
    }

    @Test
    public void testGetMultiRow__findParameterEntries__nocache() {

        // zaleznosci
        ParameterEntry pe1 = new ParameterEntry("A;B;1;2", "");
        ParameterEntry pe2 = new ParameterEntry("A;B;2;3", "");
        ParameterEntry pe3 = new ParameterEntry("C;D;7;8", "");

        Level l1 = new Level("string");
        Level l2 = new Level("string");
        Level l3 = new Level("integer");
        Level l4 = new Level("integer");

        Parameter par = new Parameter();
        par.setName("par");
        par.setType("integer");
        par.addLevel(l1, l2, l3, l4);
        par.setCacheable(false);
        par.setNullable(true);
        par.setMultivalue(true);
        par.setInputLevels(2);

        par.addEntry(pe1);
        par.addEntry(pe2);
        par.addEntry(pe3);

        // konfiguracja
        List<ParameterEntry> resultAB = Arrays.asList(pe1, pe2);
        List<ParameterEntry> resultCD = Arrays.asList(pe3);
        List<ParameterEntry> resultXY = Arrays.asList();

        when(loader.load("par")).thenReturn(par);
        when(loader.findEntries("par", new String[]{"A", "B"})).thenReturn(resultAB);
        when(loader.findEntries("par", new String[]{"C", "D"})).thenReturn(resultCD);
        when(loader.findEntries("par", new String[]{"X", "Y"})).thenReturn(resultXY);

        // test
        MultiRow mr;

        mr = engine.getMultiRow("par", new LevelValues("A", "B"));
        assertEquals(2, mr.length());
        assertTrue(contains(mr, new Long[]{1L, 2L}));
        assertTrue(contains(mr, new Long[]{2L, 3L}));

        mr = engine.getMultiRow("par", new LevelValues("C", "D"));
        assertEquals(1, mr.length());
        assertTrue(contains(mr, new Long[]{7L, 8L}));

        mr = engine.getMultiRow("par", new LevelValues("X", "Y"));
        assertNull(mr);
    }

    @Test
    public void testGetMultiRow__notFound() {

        Level l1 = new Level("string");
        Level l2 = new Level("string");
        Level l3 = new Level("string");

        Parameter par = new Parameter();
        par.setType("string");
        par.setName("par");
        par.addLevel(l1, l2, l3);
        par.setNullable(false);

        par.setMultivalue(true);
        par.setInputLevels(1);

        par.addEntry(new ParameterEntry("A;B;Z", (String) null));

        when(loader.load("par")).thenReturn(par);

        // test
        try {
            engine.getMultiRow("par", new LevelValues("NON_EXISTING"));
            fail();

        } catch (ParamException e) {
            assertEquals(ParamException.ErrorCode.PARAM_VALUE_NOT_FOUND, e.getErrorCode());
        }
    }

    @Test
    public void testGetMultiRow__illegalUsage() {

        Level l1 = new Level("string");
        Level l2 = new Level("string");
        Level l3 = new Level("string");

        Parameter par = new Parameter();
        par.setType("string");
        par.setName("par");
        par.addLevel(l1, l2, l3);

        par.setMultivalue(false);       // non multivalue
        par.setInputLevels(1);

        par.addEntry(new ParameterEntry("A;B;Z", (String) null));

        when(loader.load("par")).thenReturn(par);

        // test
        try {
            engine.getMultiRow("par", new LevelValues("A"));
            fail();

        } catch (ParamException e) {
            assertEquals(ParamException.ErrorCode.ILLEGAL_API_USAGE, e.getErrorCode());
        }
    }

    @Test
    public void testGetMultiRow__illegalLevelValues() {

        Level l1 = new Level("string");
        Level l2 = new Level("string");

        Parameter par = new Parameter();
        par.setType("string");
        par.setName("par");
        par.addLevel(l1, l2);

        par.setMultivalue(true);
        par.setInputLevels(1);

        par.addEntry(new ParameterEntry("A;value", (String) null));

        when(loader.load("par")).thenReturn(par);

        // test
        try {
            // uzytkownik podaje 3 levelValues, w parametrze jest tylko 1 level wejsciowy
            engine.getMultiRow("par", new LevelValues("A", "B", "C"));
            fail();

        } catch (ParamException e) {
            assertEquals(ParamException.ErrorCode.ILLEGAL_LEVEL_VALUES, e.getErrorCode());
        }
    }

    @Test
    public void testGetMultiRow__noLevelParam() {

        // konfiguracja
        Parameter par = new Parameter();
        par.setName("par");
        par.setType("string");
        par.addLevel(new Level("string"));
        par.addLevel(new Level("string"));

        par.addEntry(new ParameterEntry("A;1", ""));
        par.addEntry(new ParameterEntry("B;2", ""));
        par.addEntry(new ParameterEntry("C;2", ""));

        par.setMultivalue(true);        // input = 0, output = 2

        // zaleznosci
        when(loader.load("par")).thenReturn(par);

        // test
        MultiRow mr = engine.getMultiRow("par", new DefaultContext());

        // weryfikacja
        assertTrue(contains(mr, new Object[]{"A", "1"}));
        assertTrue(contains(mr, new Object[]{"B", "2"}));
        assertTrue(contains(mr, new Object[]{"C", "2"}));
    }

    private boolean contains(MultiRow mr, Object[] array) {
        for (MultiValue mv : mr.getRows()) {
            if (Arrays.equals(array, mv.unwrap())) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void testGetArray() {

        Level l1 = new Level("string");

        Parameter par = new Parameter();
        par.setName("par");
        par.addLevel(l1);
        par.setType("integer");
        par.setArray(true);
        par.setArraySeparator(',');
        par.setNullable(true);

        par.addEntry(new ParameterEntry("A", "1,2,3"));
        par.addEntry(new ParameterEntry("B", "4,5,6, 7,8,"));
        par.addEntry(new ParameterEntry("C", "9"));
        par.addEntry(new ParameterEntry("D", ""));

        when(loader.load("par")).thenReturn(par);

        // testy
        String[] levels = {
            "A",
            "B",
            "C",
            "D",
            "NONEXISTING"
        };

        // oczekiwane wartosci
        AbstractHolder[][] expected = {
            {inth(1), inth(2), inth(3)},
            {inth(4), inth(5), inth(6), inth(7), inth(8), inth(null)},
            {inth(9)},
            {},
            {}
        };

        // testy i weryfikacja
        for (int i = 0; i < expected.length; i++) {
            String lev = levels[i];
            AbstractHolder[] expectedArray = expected[i];

            // test
            AbstractHolder[] array = engine.getArray("par", new LevelValues(lev));

            // weryfikacja
            assertArrayEquals(expectedArray, array);
        }
    }

    @Test
    public void testGetArray__illegalUsage() {

        // konfiguracja
        Level l1 = new Level("string");
        Parameter par = new Parameter();
        par.setName("par");
        par.addLevel(l1);
        par.setType("integer");
        par.addEntry(new ParameterEntry("A", "1,2,3"));

        // zaleznosci
        when(loader.load("par")).thenReturn(par);

        //test
        try {
            engine.getArray("par", new LevelValues("A"));
            fail();
        } catch (ParamUsageException e) {
            assertEquals(ParamException.ErrorCode.ILLEGAL_API_USAGE, e.getErrorCode());
        }
    }

    @Test
    public void testGetArray__notnull() {

        Level l1 = new Level("string");

        Parameter par = new Parameter();
        par.setName("par");
        par.addLevel(l1);
        par.setType("integer");
        par.setArray(true);
        par.setArraySeparator(',');

        // not null
        par.setNullable(false);

        par.addEntry(new ParameterEntry("A", "1,2,3"));

        // zaleznosci
        when(loader.load("par")).thenReturn(par);

        //test
        try {
            engine.getArray("par", new LevelValues("NONEXISTING"));
            fail();
        } catch (ParamException e) {
            assertEquals(ParamException.ErrorCode.PARAM_VALUE_NOT_FOUND, e.getErrorCode());
        }
    }

    private IntegerHolder inth(Integer v) {
        return new IntegerHolder(v != null ? v.longValue() : null);
    }

    @Test
    public void testCall() {
        // zaleznosci
        Level l1 = new Level("string");
        Level l2 = new Level("string");

        Parameter par = new Parameter();
        par.setName("par");
        par.setType("plugin");
        par.addLevel(l1, l2);
        par.setNullable(true);

        Function f = new Function();
        f.setName("calc.1");
        f.setType(par.getType());
        f.setImplementation(new JavaFunction(this.getClass(), "calculate"));

        par.addEntry(new ParameterEntry("A;F", "calc.1"));

        // konfiguracja
        when(loader.load("par")).thenReturn(par);
        when(functionLoader.load("calc.1")).thenReturn(f);

        // testy
        String[][] tests = {
            {"A", "F"},
            {"Z", "Z"}
        };

        // oczekiwane wyniki
        Object[] expectedResults = {
            78,
            null
        };

        // testy i weryfikacja
        for (int i = 0; i < tests.length; i++) {
            String[] lev = tests[i];
            Object expectedResult = expectedResults[i];

            // test
            Object result = engine.call("par", new DefaultContext().withLevelValues(lev), new DefaultContext());

            // weryfikacja
            assertEquals(expectedResult, result);
        }
    }

    @Test
    public void testCall__typeNotPlugin() {
        // zaleznosci
        Level l1 = new Level("string");
        Level l2 = new Level("string");

        Parameter par = new Parameter();
        par.setName("par");
        par.setType("string");
        par.addLevel(l1, l2);
        par.setNullable(true);

        Function f = new Function();
        f.setName("calc.1");
        f.setType(par.getType());
        f.setImplementation(new JavaFunction(this.getClass(), "calculate"));

        par.addEntry(new ParameterEntry("A;F", "calc.1"));

        // konfiguracja
        when(loader.load("par")).thenReturn(par);
        when(functionLoader.load("calc.1")).thenReturn(f);

        // testy
        String[][] tests = {
            {"A", "F"},
            {"Z", "Z"}
        };

        // oczekiwane wyniki
        Object[] expectedResults = {
            78,
            null
        };

        // testy i weryfikacja
        for (int i = 0; i < tests.length; i++) {
            String[] lev = tests[i];
            Object expectedResult = expectedResults[i];

            // test
            Object result = engine.call("par", new DefaultContext().withLevelValues(lev), new DefaultContext());

            // weryfikacja
            assertEquals(expectedResult, result);
        }
    }

    @Test
    public void testEvaluateLevelValues__illegalState() {

        // konfiguracja
        Level l1 = new Level("string");
        Level l2 = new Level("string");

        Parameter par = new Parameter();
        par.setName("par");
        par.setType("string");
        par.addLevel(l1, l2);
        par.addEntry(new ParameterEntry("A;B", "value"));

        // zaleznosci
        when(loader.load("par")).thenReturn(par);

        // test
        try {
            engine.getValue("par", new DefaultContext());
            fail();
        } catch (ParamException e) {
            assertEquals(ParamException.ErrorCode.UNDEFINED_LEVEL_CREATOR, e.getErrorCode());
        }
    }

    private enum LetterType {

        A1,
        A2,
        A3,
        A4,
        A5

    }
}
