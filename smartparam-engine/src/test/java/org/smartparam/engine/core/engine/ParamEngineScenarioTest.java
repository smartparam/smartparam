package org.smartparam.engine.core.engine;

import org.smartparam.engine.core.service.BasicFunctionManager;
import java.util.ArrayList;
import org.smartparam.engine.core.repository.ParamRepository;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.smartparam.engine.core.cache.MapFunctionCache;
import org.smartparam.engine.core.cache.MapParamCache;
import org.smartparam.engine.core.repository.BasicInvokerRepository;
import org.smartparam.engine.core.repository.BasicTypeRepository;
import org.smartparam.engine.core.repository.TypeRepository;
import org.smartparam.engine.core.context.DefaultContext;
import org.smartparam.engine.core.context.LevelValues;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.core.exception.SmartParamUsageException;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.repository.FunctionRepository;
import org.smartparam.engine.core.invoker.JavaFunctionInvoker;
import org.smartparam.engine.core.service.BasicFunctionProvider;
import org.smartparam.engine.core.service.BasicParameterProvider;
import org.smartparam.engine.core.type.AbstractHolder;
import org.smartparam.engine.test.builder.FunctionMockBuilder;
import org.smartparam.engine.test.builder.LevelMockBuilder;
import org.smartparam.engine.test.builder.ParameterEntryMockBuilder;
import org.smartparam.engine.test.builder.ParameterMockBuilder;
import org.smartparam.engine.model.function.Function;
import org.smartparam.engine.model.Level;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.engine.types.integer.IntegerHolder;
import org.smartparam.engine.types.integer.IntegerType;
import org.smartparam.engine.types.plugin.PluginType;
import org.smartparam.engine.types.string.StringHolder;
import org.smartparam.engine.types.string.StringType;

/**
 * @author Przemek Hertel
 */
public class ParamEngineScenarioTest {

    private SmartParamEngine engine;

    private ParamRepository paramRepository;

    private FunctionRepository functionRepository;

    @Before
    public void init() {
        TypeRepository typeRepository = new BasicTypeRepository();
        typeRepository.register("string", new StringType());
        typeRepository.register("integer", new IntegerType());
        typeRepository.register("plugin", new PluginType());

        paramRepository = mock(ParamRepository.class);

        BasicInvokerRepository invokerRepository = new BasicInvokerRepository();
        invokerRepository.register("java", new JavaFunctionInvoker());

        functionRepository = mock(FunctionRepository.class);

        BasicFunctionProvider functionProvider = new BasicFunctionProvider();
        functionProvider.setFunctionCache(new MapFunctionCache());
        functionProvider.register("java", 0, functionRepository);

        SmartParamPreparer paramPreparer = new SmartParamPreparer();
        paramPreparer.setTypeRepository(typeRepository);
        paramPreparer.setParamCache(new MapParamCache());
        paramPreparer.setFunctionProvider(functionProvider);

        BasicParameterProvider paramProvider = new BasicParameterProvider();
        paramProvider.register("test", 0, paramRepository);
        paramPreparer.setParameterProvider(paramProvider);

        BasicFunctionManager functionManager = new BasicFunctionManager();
        functionManager.setFunctionProvider(functionProvider);
        functionManager.setInvokerRepository(invokerRepository);

        engine = new SmartParamEngine();
        engine.setParamPreparer(paramPreparer);
        engine.setFunctionManager(functionManager);
    }

    @Test
    public void testGetValue__nullable() {

        // zaleznosci
        Level l1 = LevelMockBuilder.level("string");    // input
        Level l2 = LevelMockBuilder.level("string");    // input
        Level l3 = LevelMockBuilder.level("integer");   // output

        Function f = FunctionMockBuilder.function().withType("integer").withJavaImplementation(this.getClass(), "calculate").get();

        Function f2 = FunctionMockBuilder.function().withType("integer").withJavaImplementation(this.getClass(), "calculate2").get();

        Set<ParameterEntry> entries = new HashSet<ParameterEntry>();
        entries.add(ParameterEntryMockBuilder.parameterEntry("A", "F", "11"));
        entries.add(ParameterEntryMockBuilder.parameterEntry("A", "G", "12"));
        entries.add(ParameterEntryMockBuilder.parameterEntry("A", "*", "13"));
        entries.add(ParameterEntryMockBuilder.parameterEntry("B", "F", "21"));
        entries.add(ParameterEntryMockBuilder.parameterEntry("B", "*", "22"));
        entries.add(ParameterEntryMockBuilder.parameterEntry("*", "F", "31"));
//        entries.add(ParameterEntryMockBuilder.parameterEntry0("*;X", f));
//        entries.add(ParameterEntryMockBuilder.parameterEntry0("*;Y", f2));

        Parameter par = ParameterMockBuilder.parameter().withName("par").nullable(true)
                .withEntries(entries).withLevels(l1, l2, l3).inputLevels(2).get();

        // konfiguracja
        when(paramRepository.load("par")).thenReturn(par);
//        when(functionRepository.loadFunction("calculate")).thenReturn(f);
//        when(functionRepository.loadFunction("calculate2")).thenReturn(f2);

        // testy - wartosci poziomow
        String[][] tests = {
            {"A", "F"}, //AF 11
            {"A", "G"}, //AG 12
            {"A", "Z"}, //A* 13
            {"B", "F"}, //BF 21
            {"B", "B"}, //B* 22
            {"C", "F"}, //*F 31
//            {"S", "X"}, //*X f  = 78
//            {"S", "Y"}, //*Y f2 = null
            {"Z", "Z"} //null
        };

        // oczekiwane wyniki
        Integer[] expected = {11, 12, 13, 21, 22, 31, null};

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
        Level l1 = LevelMockBuilder.level("string");    // input
        Level l2 = LevelMockBuilder.level("string");    // output
        Level l3 = LevelMockBuilder.level("string");    // output

        Set<ParameterEntry> entries = new HashSet<ParameterEntry>();
        entries.add(ParameterEntryMockBuilder.parameterEntry("A", null, "A-null"));
        entries.add(ParameterEntryMockBuilder.parameterEntry("A", "B", "A-B"));
        entries.add(ParameterEntryMockBuilder.parameterEntry(null, "B", "null-B"));
        entries.add(ParameterEntryMockBuilder.parameterEntry(null, null, "null-null"));
        entries.add(ParameterEntryMockBuilder.parameterEntry("*", "*", "default"));

        Parameter par = ParameterMockBuilder.parameter().withName("par")
                .withEntries(entries)
                .withLevels(l1, l2, l3).inputLevels(2)
                .get();

        // konfiguracja
        when(paramRepository.load("par")).thenReturn(par);

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

//    @Test
//    public void testGetValue__notnull() {
//
//        // zaleznosci
//        Level l1 = LevelMockBuilder.level("string");
//
//        Function f2 = FunctionMockBuilder.function().withType("integer").withJavaImplementation(this.getClass(), "calculate2").get();
//
//        Set<ParameterEntry> entries = new HashSet<ParameterEntry>();
//        entries.add(ParameterEntryMockBuilder.parameterEntry0("A", "11"));
//        entries.add(ParameterEntryMockBuilder.parameterEntry0("B", f2));
//
//        Parameter par = ParameterMockBuilder.parameter().withName("par").withType("integer").nullable(false)
//                .withEntries(entries).withLevels(l1).get();
//
//        // konfiguracja
//        when(paramRepository.load("par")).thenReturn(par);
//        when(functionRepository.loadFunction("calculate2")).thenReturn(f2);
//
//        // testy - wartosci poziomow
//        String[] tests = {
//            "A", //11
//            "B", //null
//            "C" //ex
//        };
//
//        // oczekiwane wyniki
//        Object[] expected = {
//            11,
//            null,
//            new SmartParamException(SmartParamErrorCode.PARAM_VALUE_NOT_FOUND, "")
//        };
//
//        // test i weryfikacja
//        for (int i = 0; i < tests.length; i++) {
//            String lv = tests[i];
//            Object expectedObject = expected[i];
//
//            if (expectedObject instanceof Exception) {
//                try {
//                    engine.getValue("par", lv).getInteger();
//                    fail();
//                } catch (SmartParamException e) {
//                    assertEquals(expectedObject.getClass(), e.getClass());
//                }
//
//            } else {
//                Integer result = engine.getValue("par", lv).getInteger();
//                assertEquals(expectedObject, result);
//            }
//        }
//    }

    /**
     * Getting multivalue from notnull parameter.
     */
    @Test
    public void testGetMultiValue__notnull() {

        // zaleznosci
        Level l1 = LevelMockBuilder.level("string");
        Level l2 = LevelMockBuilder.level("string");
        Level l3 = LevelMockBuilder.level("integer");

        Set<ParameterEntry> entries = new HashSet<ParameterEntry>();
        entries.add(ParameterEntryMockBuilder.parameterEntry().withLevels("A", "X", "1").get());
        entries.add(ParameterEntryMockBuilder.parameterEntry().withLevels("B", "Y", "2").get());
        entries.add(ParameterEntryMockBuilder.parameterEntry().withLevels("C", "Z", "3").get());

        Parameter par = ParameterMockBuilder.parameter().withName("par").nullable(false)
                .withEntries(entries).withLevels(l1, l2, l3).inputLevels(1).get();

        // konfiguracja
        when(paramRepository.load("par")).thenReturn(par);

        // testy - wartosci poziomow
        String[] tests = {
            "A", //MV: X 1
            "B", //MV: Y 2
            "C", //MV: Z 3
            "F"  //ex
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

            } catch (SmartParamException e) {

                assertTrue(expectedEx);     // wyjatek jest oczekiwany
                assertEquals(SmartParamErrorCode.PARAM_VALUE_NOT_FOUND, e.getErrorCode());
            }
        }
    }

    @Test
    public void testGet__noInputLevelParam() {

        // given
        Set<ParameterEntry> entries = new HashSet<ParameterEntry>();
        entries.add(ParameterEntryMockBuilder.parameterEntry("123"));

        Level l1 = LevelMockBuilder.level("integer");
        Parameter par = ParameterMockBuilder.parameter().withName("par").
                withLevels(l1).inputLevels(0).
                withEntries(entries).get();

        when(paramRepository.load("par")).thenReturn(par);

        // when
        int result = engine.get("par", new DefaultContext()).get().intValue();

        // then
        assertEquals(123, result);
    }

    /**
     * Calling get() with wrong number of input values.
     * Parameter has 2 input parameterEntry.
     * Provided 4 input parameterEntry.
     * get() throws ILLEGAL_LEVEL_VALUES
     */
    @Test
    public void testGet__illegalLevelValues() {

        // zaleznosci
        Level l1 = LevelMockBuilder.level("string");
        Level l2 = LevelMockBuilder.level("string");
        Level l3 = LevelMockBuilder.level("integer");

        Set<ParameterEntry> entries = new HashSet<ParameterEntry>();
        entries.add(ParameterEntryMockBuilder.parameterEntry().withLevels("A", "B", "11").get());
        entries.add(ParameterEntryMockBuilder.parameterEntry().withLevels("B", "C", "12").get());

        Parameter par = ParameterMockBuilder.parameter().withName("par").withLevels(l1, l2, l3).inputLevels(2).withEntries(entries).get();

        // konfiguracja
        when(paramRepository.load("par")).thenReturn(par);

        // test
        try {
            engine.get("par", new LevelValues("A", "B", "C", "D"));
            fail();
        } catch (SmartParamUsageException e) {
            assertEquals(SmartParamErrorCode.ILLEGAL_LEVEL_VALUES, e.getErrorCode());
        }
    }

    @Test
    public void testGetValue__findParameterEntry__nocache() {

        // zaleznosci
        ParameterEntry pe1 = ParameterEntryMockBuilder.parameterEntry("A", "B", "11");
        ParameterEntry pe2 = ParameterEntryMockBuilder.parameterEntry("B", "C", "12");
        ParameterEntry pe3 = ParameterEntryMockBuilder.parameterEntry("C", "D", "13");

        Level l1 = LevelMockBuilder.level("string");
        Level l2 = LevelMockBuilder.level("string");
        Level l3 = LevelMockBuilder.level("integer");

        Set<ParameterEntry> entries = new HashSet<ParameterEntry>();
        entries.add(pe1);
        entries.add(pe2);
        entries.add(pe3);

        Parameter par = ParameterMockBuilder.parameter().withName("par").withLevels(l1, l2, l3).inputLevels(2)
                .cacheable(false).nullable(true).withEntries(entries).get();

        // konfiguracja
        List<ParameterEntry> resultAB = Arrays.asList(pe1);
        List<ParameterEntry> resultBC = Arrays.asList(pe2);
        List<ParameterEntry> resultCD = Arrays.asList(pe3);
        List<ParameterEntry> resultXY = new ArrayList<ParameterEntry>();

        when(paramRepository.load("par")).thenReturn(par);
        when(paramRepository.findEntries("par", new String[]{"A", "B"})).thenReturn(resultAB);
        when(paramRepository.findEntries("par", new String[]{"B", "C"})).thenReturn(resultBC);
        when(paramRepository.findEntries("par", new String[]{"C", "D"})).thenReturn(resultCD);
        when(paramRepository.findEntries("par", new String[]{"X", "Y"})).thenReturn(resultXY);

        // test
        assertEquals(11, engine.getValue("par", "A", "B").intValue());
        assertEquals(12, engine.getValue("par", "B", "C").intValue());
        assertEquals(13, engine.getValue("par", "C", "D").intValue());

        assertTrue(engine.getValue("par", "X", "Y").isNull());
    }

    @Test
    public void testGetMultiValue__nullable() {

        // zaleznosci
        Level l1 = LevelMockBuilder.level("string");
        Level l2 = LevelMockBuilder.level("string");
        Level l3 = LevelMockBuilder.level("integer");

        Set<ParameterEntry> entries = new HashSet<ParameterEntry>();
        entries.add(ParameterEntryMockBuilder.parameterEntry().withLevels("A", "X", "1").get());
        entries.add(ParameterEntryMockBuilder.parameterEntry().withLevels("B", "Y", "2").get());

        Parameter par = ParameterMockBuilder.parameter().withName("par").withLevels(l1, l2, l3).inputLevels(1).nullable(true).withEntries(entries).get();

        // konfiguracja
        when(paramRepository.load("par")).thenReturn(par);

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
        Level l1 = LevelMockBuilder.level("string");
        Level l2 = LevelMockBuilder.level("string");
        Level l3 = LevelMockBuilder.level("string");
        Level l4 = LevelMockBuilder.level("string");

        Set<ParameterEntry> entries = new HashSet<ParameterEntry>();
        entries.add(ParameterEntryMockBuilder.parameterEntryCsv("A;B;Z;1"));
        entries.add(ParameterEntryMockBuilder.parameterEntryCsv("A;C;Z;2"));
        entries.add(ParameterEntryMockBuilder.parameterEntryCsv("A;D;Y;3"));
        entries.add(ParameterEntryMockBuilder.parameterEntryCsv("B;F;3;4"));
        entries.add(ParameterEntryMockBuilder.parameterEntryCsv("B;G;3;5"));
        entries.add(ParameterEntryMockBuilder.parameterEntryCsv("B;H;3;6"));

        Parameter par = ParameterMockBuilder.parameter().withName("par").withLevels(l1, l2, l3, l4).inputLevels(2)
                .withEntries(entries).get();

        when(paramRepository.load("par")).thenReturn(par);

        // test 1
        MultiValue mv = engine.get("par", new LevelValues("A", "B")).row();
        assertEquals(StringHolder.class, mv.getValue(1).getClass());
        assertEquals(StringHolder.class, mv.getValue(2).getClass());
        assertEquals("Z", mv.getString(1));
        assertEquals("1", mv.getString(2));

        // test 2
        mv = engine.get("par", new LevelValues("B", "H")).row();
        assertArrayEquals(new String[]{"3", "6"}, mv.asStrings());
    }

    @Test
    public void testGetMultiValue__withArray() {
        Level l1 = LevelMockBuilder.level("string");
        Level l2 = LevelMockBuilder.level("string");
        Level l3 = LevelMockBuilder.level("integer", true);

        Set<ParameterEntry> entries = new HashSet<ParameterEntry>();
        entries.add(ParameterEntryMockBuilder.parameterEntry().withLevels("A", "XX", "1,2,3").get());
        entries.add(ParameterEntryMockBuilder.parameterEntry().withLevels("B", "YY", "4").get());
        entries.add(ParameterEntryMockBuilder.parameterEntry().withLevels("*", "ZZ", "").get());

        Parameter par = ParameterMockBuilder.parameter().withName("par").withLevels(l1, l2, l3).inputLevels(1)
                .withEntries(entries).get();

        when(paramRepository.load("par")).thenReturn(par);

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
    public void testGetMultiRow() {

        Level l1 = LevelMockBuilder.level("string");
        Level l2 = LevelMockBuilder.level("string");
        Level l3 = LevelMockBuilder.level("string");

        Set<ParameterEntry> entries = new HashSet<ParameterEntry>();
        entries.add(ParameterEntryMockBuilder.parameterEntryCsv("A;B;Z"));
        entries.add(ParameterEntryMockBuilder.parameterEntryCsv("A;C;Z"));
        entries.add(ParameterEntryMockBuilder.parameterEntryCsv("A;D;Y"));
        entries.add(ParameterEntryMockBuilder.parameterEntryCsv("B;F;3"));
        entries.add(ParameterEntryMockBuilder.parameterEntryCsv("B;G;3"));
        entries.add(ParameterEntryMockBuilder.parameterEntryCsv("B;H;3"));

        Parameter par = ParameterMockBuilder.parameter().withName("par").withLevels(l1, l2, l3).nullable(true)
                .inputLevels(1)
                .withEntries(entries).get();

        when(paramRepository.load("par")).thenReturn(par);

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
    public void testGet__withArray() {

        Level l1 = LevelMockBuilder.level("string");
        Level l2 = LevelMockBuilder.level("string");
        Level l3 = LevelMockBuilder.level("integer", true);

        Set<ParameterEntry> entries = new HashSet<ParameterEntry>();
        entries.add(ParameterEntryMockBuilder.parameterEntry("A", "B", "1"));
        entries.add(ParameterEntryMockBuilder.parameterEntry("A", "C", "2,3"));
        entries.add(ParameterEntryMockBuilder.parameterEntry("A", "D", "4,5,6"));
        entries.add(ParameterEntryMockBuilder.parameterEntry("A", "E", ""));

        Parameter par = ParameterMockBuilder.parameter().withName("par").withLevels(l1, l2, l3)
                .inputLevels(1)
                .withEntries(entries).get();

        when(paramRepository.load("par")).thenReturn(par);

        // test
        ParamValue value = engine.get("par", new LevelValues("A"));

        // weryfikacja
        for (MultiValue row : value.rows()) {
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
        ParameterEntry pe1 = ParameterEntryMockBuilder.parameterEntryCsv("A;B;1;2");
        ParameterEntry pe2 = ParameterEntryMockBuilder.parameterEntryCsv("A;B;2;3");
        ParameterEntry pe3 = ParameterEntryMockBuilder.parameterEntryCsv("C;D;7;8");

        Level l1 = LevelMockBuilder.level("string");
        Level l2 = LevelMockBuilder.level("string");
        Level l3 = LevelMockBuilder.level("integer");
        Level l4 = LevelMockBuilder.level("integer");

        Set<ParameterEntry> entries = new HashSet<ParameterEntry>();
        entries.add(pe1);
        entries.add(pe2);
        entries.add(pe3);

        Parameter par = ParameterMockBuilder.parameter().withName("par").withLevels(l1, l2, l3, l4)
                .nullable(true).cacheable(false)
                .inputLevels(2)
                .withEntries(entries).get();

        // konfiguracja
        List<ParameterEntry> resultAB = Arrays.asList(pe1, pe2);
        List<ParameterEntry> resultCD = Arrays.asList(pe3);
        List<ParameterEntry> resultXY = Arrays.asList();

        when(paramRepository.load("par")).thenReturn(par);
        when(paramRepository.findEntries("par", new String[]{"A", "B"})).thenReturn(resultAB);
        when(paramRepository.findEntries("par", new String[]{"C", "D"})).thenReturn(resultCD);
        when(paramRepository.findEntries("par", new String[]{"X", "Y"})).thenReturn(resultXY);

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

        Level l1 = LevelMockBuilder.level("string");
        Level l2 = LevelMockBuilder.level("string");
        Level l3 = LevelMockBuilder.level("string");

        Set<ParameterEntry> entries = new HashSet<ParameterEntry>();
        entries.add(ParameterEntryMockBuilder.parameterEntry("A", "B", "Z"));

        Parameter par = ParameterMockBuilder.parameter().withName("par").withLevels(l1, l2, l3).inputLevels(1)
                .nullable(false)
                .withEntries(entries).get();

        when(paramRepository.load("par")).thenReturn(par);

        // test
        try {
            engine.get("par", new LevelValues("NON_EXISTING"));
            fail();

        } catch (SmartParamException e) {
            assertEquals(SmartParamErrorCode.PARAM_VALUE_NOT_FOUND, e.getErrorCode());
        }
    }

    @Test
    public void testGet__illegalUsage() {

        Level l1 = LevelMockBuilder.level("string");
        Level l2 = LevelMockBuilder.level("string");
        Level l3 = LevelMockBuilder.level("string");

        Set<ParameterEntry> entries = new HashSet<ParameterEntry>();
        entries.add(ParameterEntryMockBuilder.parameterEntry("A", "B", "Z"));

        Parameter par = ParameterMockBuilder.parameter().withName("par").withLevels(l1, l2, l3)
                .inputLevels(1)
                .withEntries(entries).get();

        when(paramRepository.load("par")).thenReturn(par);

        // when
		ParamValue value = engine.get("par", new LevelValues("A"));

		// then
		assertEquals("B", value.row(1).nextString());
        assertEquals("Z", value.row(1).nextString());
    }

    @Test
    public void testGetMultiRow__illegalLevelValues() {

        Level l1 = LevelMockBuilder.level("string");
        Level l2 = LevelMockBuilder.level("string");

        Set<ParameterEntry> entries = new HashSet<ParameterEntry>();
        entries.add(ParameterEntryMockBuilder.parameterEntry("A", "value"));

        Parameter par = ParameterMockBuilder.parameter().withName("par").withLevels(l1, l2)
                .inputLevels(1)
                .withEntries(entries).get();

        when(paramRepository.load("par")).thenReturn(par);

        // test
        try {
            // uzytkownik podaje 3 levelValues, w parametrze jest tylko 1 level wejsciowy
            engine.get("par", new LevelValues("A", "B", "C"));
            fail();

        } catch (SmartParamException e) {
            assertEquals(SmartParamErrorCode.ILLEGAL_LEVEL_VALUES, e.getErrorCode());
        }
    }

    @Test
    public void testGetMultiRow__noInputLevelParam() {

        Level l1 = LevelMockBuilder.level("string");    // output
        Level l2 = LevelMockBuilder.level("string");    // output

        Set<ParameterEntry> entries = new HashSet<ParameterEntry>();
        entries.add(ParameterEntryMockBuilder.parameterEntry("A", "1"));
        entries.add(ParameterEntryMockBuilder.parameterEntry("B", "2"));
        entries.add(ParameterEntryMockBuilder.parameterEntry("C", "2"));

        Parameter par = ParameterMockBuilder.parameter().withName("par").withLevels(l1, l2).inputLevels(0)
                .withEntries(entries).get();

        // zaleznosci
        when(paramRepository.load("par")).thenReturn(par);

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

    /**
     * Should return first output level as array.
     * Verifies ParamEngine.getArray() method.
     */
    @Test
    public void testGetArray() {

        Level l1 = LevelMockBuilder.level("string");
        Level l2 = LevelMockBuilder.level().withType("integer").withArray(true).get();

        Set<ParameterEntry> entries = new HashSet<ParameterEntry>();
        entries.add(ParameterEntryMockBuilder.parameterEntry("A", "1,2,3"));
        entries.add(ParameterEntryMockBuilder.parameterEntry("B", "4,5,6, 7,8,"));
        entries.add(ParameterEntryMockBuilder.parameterEntry("C", "9"));
        entries.add(ParameterEntryMockBuilder.parameterEntry("D", ""));

        Parameter par = ParameterMockBuilder.parameter().withName("par")
                .withLevels(l1, l2)
                .inputLevels(1)
                .nullable(true).arraySeparator(',')
                .withEntries(entries)
                .get();

        when(paramRepository.load("par")).thenReturn(par);

        // test cases for input parameterEntry
        String[] levels = {
            "A",
            "B",
            "C",
            "D",
            "NONEXISTING"
        };

        // expected arrays
        AbstractHolder[][] expected = {
            {inth(1), inth(2), inth(3)},
            {inth(4), inth(5), inth(6), inth(7), inth(8), inth(null)},
            {inth(9)},
            {},
            {}
        };

        // run tests and verify
        for (int i = 0; i < expected.length; i++) {
            String lev = levels[i];
            AbstractHolder[] expectedArray = expected[i];

            // test
            AbstractHolder[] array = engine.getArray("par", new LevelValues(lev));

            // weryfikacja
            assertArrayEquals(expectedArray, array);
        }
    }

    /**
     * Trying to read integer array from output level
     * but output level is not defined as array (array flag is not set).
     */
    @Test
    public void testGetArray__illegalUsage() {

        // konfiguracja
        Level l1 = LevelMockBuilder.level("string");        // input
        Level l2 = LevelMockBuilder.level("integer");       // output

        Set<ParameterEntry> entries = new HashSet<ParameterEntry>();
        entries.add(ParameterEntryMockBuilder.parameterEntry("A", "1,2,3"));

        Parameter par = ParameterMockBuilder.parameter().withName("par").withLevels(l1, l2).inputLevels(1)
                .withEntries(entries).get();

        // zaleznosci
        when(paramRepository.load("par")).thenReturn(par);

        //test
        try {
            engine.getArray("par", new LevelValues("A"));
            fail();
        } catch (SmartParamUsageException e) {
            assertEquals(SmartParamErrorCode.ILLEGAL_API_USAGE, e.getErrorCode());
        }
    }

    @Test
    public void testGetArray__notnull() {

        Level l1 = LevelMockBuilder.level("string");
        Level l2 = LevelMockBuilder.level().withType("integer").withArray(true).get();

        Set<ParameterEntry> entries = new HashSet<ParameterEntry>();
        entries.add(ParameterEntryMockBuilder.parameterEntry("A", "1,2,3"));

        Parameter par = ParameterMockBuilder.parameter().withName("par").withLevels(l1, l2).inputLevels(1)
                .nullable(false).arraySeparator(',')
                .withEntries(entries).get();

        // zaleznosci
        when(paramRepository.load("par")).thenReturn(par);

        //test
        try {
            engine.getArray("par", new LevelValues("NONEXISTING"));
            fail();
        } catch (SmartParamException e) {
            assertEquals(SmartParamErrorCode.PARAM_VALUE_NOT_FOUND, e.getErrorCode());
        }
    }

    private IntegerHolder inth(Integer v) {
        return new IntegerHolder(v != null ? v.longValue() : null);
    }

    @Test
    public void testCall() {
        // zaleznosci
        Level l1 = LevelMockBuilder.level("string"); // input
        Level l2 = LevelMockBuilder.level("string"); // input
        Level l3 = LevelMockBuilder.level("string"); // output (plugin name)

        Function f = FunctionMockBuilder.function().withName("calc.1").withType("plugin").withJavaImplementation(this.getClass(), "calculate").get();
        Set<ParameterEntry> entries = new HashSet<ParameterEntry>();
        entries.add(ParameterEntryMockBuilder.parameterEntry("A", "F", "calc.1"));

        Parameter par = ParameterMockBuilder.parameter().withName("par")
                .withLevels(l1, l2, l3).inputLevels(2)
                .nullable(true)
                .withEntries(entries).get();

        // konfiguracja
        when(paramRepository.load("par")).thenReturn(par);
        when(functionRepository.loadFunction("calc.1")).thenReturn(f);

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
        Level l1 = LevelMockBuilder.level("string");
        Level l2 = LevelMockBuilder.level("string");
        Level l3 = LevelMockBuilder.level("string");

        Function f = FunctionMockBuilder.function().withName("calc.1").withType("string").withJavaImplementation(this.getClass(), "calculate").get();
        Set<ParameterEntry> entries = new HashSet<ParameterEntry>();
        entries.add(ParameterEntryMockBuilder.parameterEntry("A", "F", "calc.1"));

        Parameter par = ParameterMockBuilder.parameter().withName("par").withLevels(l1, l2, l3).inputLevels(2)
                .nullable(true)
                .withEntries(entries).get();

        // konfiguracja
        when(paramRepository.load("par")).thenReturn(par);
        when(functionRepository.loadFunction("calc.1")).thenReturn(f);

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
        Level l1 = LevelMockBuilder.level("string");
        Level l2 = LevelMockBuilder.level("string");
        Level l3 = LevelMockBuilder.level("string");    // output

        Set<ParameterEntry> entries = new HashSet<ParameterEntry>();
        entries.add(ParameterEntryMockBuilder.parameterEntry("A", "B", "value"));

        Parameter par = ParameterMockBuilder.parameter().withName("par").withLevels(l1, l2, l3).inputLevels(2)
                .withEntries(entries).get();

        // zaleznosci
        when(paramRepository.load("par")).thenReturn(par);

        // test
        try {
            engine.get("par", new DefaultContext());
            fail();
        } catch (SmartParamException e) {
            assertEquals(SmartParamErrorCode.UNDEFINED_LEVEL_CREATOR, e.getErrorCode());
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
