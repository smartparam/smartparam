package org.smartparam.engine.core.engine;

import org.smartparam.engine.core.provider.FunctionProvider;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.slf4j.Logger;
import org.smartparam.engine.core.cache.MapParamCache;
import org.smartparam.engine.core.provider.SmartInvokerProvider;
import org.smartparam.engine.core.provider.SmartTypeProvider;
import org.smartparam.engine.core.provider.TypeProvider;
import org.smartparam.engine.core.context.DefaultContext;
import org.smartparam.engine.core.context.ParamContext;
import org.smartparam.engine.core.exception.SmartParamException;
import org.smartparam.engine.core.exception.SmartParamErrorCode;
import org.smartparam.engine.core.function.FunctionInvoker;
import org.smartparam.engine.core.function.JavaFunctionInvoker;
import org.smartparam.engine.core.loader.ParamLoader;
import org.smartparam.engine.core.type.AbstractHolder;
import org.smartparam.engine.core.type.AbstractType;
import org.smartparam.engine.test.builder.FunctionMockBuilder;
import org.smartparam.engine.test.builder.ParameterEntryMockBuilder;
import org.smartparam.engine.test.builder.ParameterMockBuilder;
import org.smartparam.engine.model.Function;
import org.smartparam.engine.model.FunctionImpl;
import org.smartparam.engine.model.Parameter;
import org.smartparam.engine.model.ParameterEntry;
import org.smartparam.engine.types.integer.IntegerHolder;
import org.smartparam.engine.types.integer.IntegerType;
import org.smartparam.engine.types.number.NumberHolder;
import org.smartparam.engine.types.number.NumberType;
import org.smartparam.engine.types.plugin.PluginType;
import org.smartparam.engine.types.string.StringHolder;
import org.smartparam.engine.types.string.StringType;

/**
 * @author Przemek Hertel
 */
public class ParamEngineTest {

    Function levelCreator;

    @Before
    public void init() {
        levelCreator = mock(Function.class);
    }

    @Test
    public void testEvaluateStringAsArray() {

        // values[i] - string wejsciowy
        String[] values = {
            "A, B,  ,D",
            " 1,  2,3 ",
            " "
        };

        // types[i] - typ parametru
        AbstractType<?>[] types = {
            new StringType(),
            new IntegerType(),
            new IntegerType()
        };

        // expectations[i] - tablica wynikowa okreslonego typu
        Object[][] expectations = {
            new StringHolder[]{new StringHolder("A"), new StringHolder("B"), new StringHolder(""), new StringHolder("D")},
            new IntegerHolder[]{new IntegerHolder(1L), new IntegerHolder(2L), new IntegerHolder(3L)},
            new IntegerHolder[]{}
        };

        // wykonanie testow
        for (int i = 0; i < values.length; i++) {
            String value = values[i];
            AbstractType<?> type = types[i];

            AbstractHolder[] expected = (AbstractHolder[]) expectations[i];
            AbstractHolder[] result = new SmartParamEngine().evaluateStringAsArray(value, type, ',');
            checkArrays(expected, result);
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testEvaluateParameterEntry() {

        // obiekty pomocnicze
        ParamContext ctx = new DefaultContext();
        FunctionImpl fimpl = mock(FunctionImpl.class);
        Function f = mock(Function.class);
        when(f.getImplementation()).thenReturn(fimpl);

        // zaleznosci
        SmartInvokerProvider invokerProvider = mock(SmartInvokerProvider.class);
        FunctionInvoker<FunctionImpl> functionInvoker = mock(FunctionInvoker.class);

        // konfiguracja zaleznosci
        when(functionInvoker.invoke(fimpl, new Object[]{ctx})).thenReturn("str", 200, BigDecimal.TEN);
        when(invokerProvider.getInvoker(fimpl)).thenReturn(functionInvoker);

        // testowany obiekt
        SmartParamEngine engine = new SmartParamEngine();
        engine.setInvokerProvider(invokerProvider);

        // dane testowe
        PreparedEntry[] tests = {
            pe("abc", null),
            pe("100", null),
            pe("1.5", null),
            pe(null, f), // ret: "str"
            pe(null, f), // ret: 200
            pe(null, f), // ret: BigDecimal.TEN
            pe(null, null) // ret: null
        };

        // typy parametrow
        AbstractType<?>[] types = {
            new StringType(),
            new IntegerType(),
            new NumberType(),
            new StringType(),
            new IntegerType(),
            new NumberType(),
            new IntegerType()
        };

        // wartosci oczekiwane
        AbstractHolder[] expectedValues = {
            new StringHolder("abc"),
            new IntegerHolder(100L),
            new NumberHolder(new BigDecimal("1.5")),
            new StringHolder("str"),
            new IntegerHolder(200L),
            new NumberHolder(BigDecimal.TEN),
            new IntegerHolder(null)
        };

        // testy
        for (int i = 0; i < tests.length; i++) {
            PreparedEntry pe = tests[i];
            AbstractType<?> type = types[i];
            AbstractHolder expectedValue = expectedValues[i];

            // test
            AbstractHolder result = engine.evaluateParameterEntry(pe, ctx, type);

            // weryfikacja
            assertEquals(expectedValue, result);
        }
    }

    @Test
    public void testEvaluateParameterEntryAsArray() {

        // obiekty pomocnicze
        ParamContext ctx = new DefaultContext();
        FunctionImpl fimpl = mock(FunctionImpl.class);
        Function f = mock(Function.class);
        when(f.getImplementation()).thenReturn(fimpl);

        // zaleznosci
        SmartInvokerProvider invokerProvider = mock(SmartInvokerProvider.class);
        @SuppressWarnings("unchecked")
        FunctionInvoker<FunctionImpl> functionInvoker = mock(FunctionInvoker.class);

        Object[] ret = {
            " A, B ",
            new Integer[]{1, 2},
            new int[]{5, 6, 7},
            Arrays.asList(11, 22, 33),
            null,
            12345L
        };

        // konfiguracja zaleznosci
        when(functionInvoker.invoke(fimpl, new Object[]{ctx})).thenReturn(ret[0], ret[1], ret[2], ret[3], ret[4], ret[5]);
        when(invokerProvider.getInvoker(fimpl)).thenReturn(functionInvoker);

        // dane testowe
        PreparedEntry[] tests = {
            pe("a,b,c", null),
            pe("1,2, 3 ", null),
            pe(" 1.2, 2.3", null),
            pe(null, f), // ret: "A,B"
            pe(null, f), // ret: Integer[] {1,2}
            pe(null, f), // ret: int[] {5,6,7}
            pe(null, f), // ret: List {11,22,33}
            pe(null, f), // ret: null
            pe(null, f), // ret: long
            pe(null, null)
        };

        // typy parametrow
        AbstractType<?>[] types = {
            new StringType(),
            new IntegerType(),
            new NumberType(),
            new StringType(),
            new IntegerType(),
            new IntegerType(),
            new IntegerType(),
            new IntegerType(),
            new IntegerType(),
            new IntegerType()
        };

        // wartosci oczekiwane
        AbstractHolder[][] expectedValues = {
            new StringHolder[]{new StringHolder("a"), new StringHolder("b"), new StringHolder("c")},
            new IntegerHolder[]{new IntegerHolder(1L), new IntegerHolder(2L), new IntegerHolder(3L)},
            new NumberHolder[]{new NumberHolder(new BigDecimal("1.2")), new NumberHolder(new BigDecimal("2.3"))},
            new StringHolder[]{new StringHolder("A"), new StringHolder("B")},
            new IntegerHolder[]{new IntegerHolder(1L), new IntegerHolder(2L)},
            new IntegerHolder[]{new IntegerHolder(5L), new IntegerHolder(6L), new IntegerHolder(7L)},
            new IntegerHolder[]{new IntegerHolder(11L), new IntegerHolder(22L), new IntegerHolder(33L)},
            new IntegerHolder[]{},
            new IntegerHolder[]{new IntegerHolder(12345L)},
            new IntegerHolder[]{}
        };

        // testowany obiekt
        SmartParamEngine engine = new SmartParamEngine();
        engine.setInvokerProvider(invokerProvider);

        // testy
        for (int i = 0; i < tests.length; i++) {
            PreparedEntry pe = tests[i];
            AbstractType<?> type = types[i];
            AbstractHolder[] expectedArray = expectedValues[i];

            // test
            AbstractHolder[] result = engine.evaluateParameterEntryAsArray(pe, ctx, type, ',');

            // weryfikacja
            assertArrayEquals(expectedArray, result);
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testEvaluateLevelValues() {

        // kolejne wyniki zwracane przez levelCratory
        Object[] lcResults = {
            "A", "B",
            "C", null,
            100, 200,
            new BigDecimal("1.2"), 300
        };
        int resultsLen = lcResults.length;

        // obiekty pomocnicze
        FunctionImpl fimpl = mock(FunctionImpl.class);
        when(levelCreator.getImplementation()).thenReturn(fimpl);

        StringType strType = new StringType();
        IntegerType intType = new IntegerType();

        ParamContext ctx = new DefaultContext();

        // zaleznosci
        SmartInvokerProvider invokerProvider = mock(SmartInvokerProvider.class);
        FunctionInvoker<FunctionImpl> functionInvoker = mock(FunctionInvoker.class);
        Logger logger = mock(Logger.class);

        // konfiguracja zaleznosci
        when(functionInvoker.invoke(fimpl, new Object[]{ctx})).thenReturn(lcResults[0], Arrays.copyOfRange(lcResults, 1, resultsLen));
        when(invokerProvider.getInvoker(fimpl)).thenReturn(functionInvoker);
        when(logger.isDebugEnabled()).thenReturn(true, false, true, false);

        // testowany obiekt
        SmartParamEngine engine = new SmartParamEngine();
        engine.setInvokerProvider(invokerProvider);
        engine.setLogger(logger);

        // parametery
        PreparedParameter[] params = {
            pp(pl(strType), pl(strType)), // L1(str) L2(str) : A     B
            pp(pl(strType), pl(strType)), // L1(str) L2(str) : C     null
            pp(pl(intType), pl(intType)), // L1(int) L2(int) : 100   200
            pp(pl(null), pl(null)) // L1(null) L2(null) : 1.2   300
        };

        // oczekiwane wartosci wyznaczonych dynamicznie poziomow
        String[][] expected = {
            {"A", "B"},
            {"C", null},
            {"100", "200"},
            {"1.2", "300"}
        };

        // testy
        for (int i = 0; i < params.length; i++) {
            PreparedParameter param = params[i];
            String[] expectedResult = expected[i];

            // test - metoda wypelnia ctx
            engine.evaluateLevelValues(param, ctx);

            // weryfikacja
            assertArrayEquals(expectedResult, ctx.getLevelValues());
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCall() {

        // obiekty pomocnicze
        FunctionImpl fimpl = mock(FunctionImpl.class);
        FunctionImpl fimplPlug = mock(FunctionImpl.class);
        ParamContext ctx = new DefaultContext();
        Function fPlug = mock(Function.class);
        when(fPlug.getImplementation()).thenReturn(fimplPlug);

        // dane testowe
        Set<ParameterEntry> entries = new HashSet<ParameterEntry>();
        entries.add(ParameterEntryMockBuilder.parameterEntry("", "plugin.fun"));
        Parameter par = ParameterMockBuilder.parameter().withName("par").withType("plugin").withEntries(entries).get();

        TypeProvider tp = new SmartTypeProvider();
        tp.registerType("plugin", new PluginType());
        tp.registerType("string", new StringType());

        ParamLoader loader = mock(ParamLoader.class);
        when(loader.load("par")).thenReturn(par);

        SmartParamPreparer provider = new SmartParamPreparer();
        provider.setTypeProvider(tp);
        provider.setLoader(loader);
        provider.setCache(new MapParamCache());

        // zaleznosci
        SmartInvokerProvider invokerProvider = mock(SmartInvokerProvider.class);
        FunctionInvoker<FunctionImpl> functionInvoker = mock(FunctionInvoker.class);
        FunctionProvider functionProvider = mock(FunctionProvider.class);

        // konfiguracja zaleznosci
        when(functionInvoker.invoke(fimpl, new Object[]{ctx})).thenReturn("plugin.fun");
        when(functionInvoker.invoke(fimplPlug, new Object[]{1, 2})).thenReturn(new Integer(99));
        when(invokerProvider.getInvoker(any(FunctionImpl.class))).thenReturn(functionInvoker);
        when(functionProvider.getFunction("plugin.fun")).thenReturn(fPlug);

        // testowany obiekt
        SmartParamEngine engine = new SmartParamEngine();
        engine.setInvokerProvider(invokerProvider);
        engine.setParamProvider(provider);
        engine.setFunctionProvider(functionProvider);

        // test
        Object result = engine.call("par", ctx, 1, 2);

        // weryfikacja
        assertEquals(new Integer(99), result);

        // test 2
        par = ParameterMockBuilder.parameter().withName("par").withType("plugin").withEntries(entries).get();
        result = engine.call("par", ctx, 1, 2);

        // weryfikacja 2
        assertEquals(new Integer(99), result);
    }

    @Test
    public void testUnwrap() {

        // zaleznosci
        AbstractHolder[] array = {
            new StringHolder("ABC"),
            new IntegerHolder(12L),
            new IntegerHolder(null)
        };

        // oczekiwany wynik
        Object[] expectedResult = {
            "ABC",
            new Long(12L),
            null
        };

        // test
        Object[] result = new SmartParamEngine().unwrap(array);

        // weryfikacja
        assertArrayEquals(expectedResult, result);
    }

    @Test
    public void testInvokeFunction__undefinedInvoker() {

        // zaleznosci
        SmartInvokerProvider ip = new SmartInvokerProvider();
        Function f = FunctionMockBuilder.function().withJavaImplementation(this.getClass(), null).get();

        // konfiguracja
        SmartParamEngine engine = new SmartParamEngine();
        engine.setInvokerProvider(ip);

        // test
        try {
            engine.invokeFunction(f, 1, 2, 3);
            fail();
        } catch (SmartParamException e) {
            assertEquals(SmartParamErrorCode.UNDEFINED_FUNCTION_INVOKER, e.getErrorCode());
        }
    }

    @Test
    public void testInvokeFunction__invokeException() {

        // zaleznosci
        JavaFunctionInvoker javaInvoker = new JavaFunctionInvoker();
        SmartInvokerProvider ip = new SmartInvokerProvider();
        ip.registerInvoker("java", javaInvoker);

        Function f = FunctionMockBuilder.function().withJavaImplementation(this.getClass(), "badMethod").get();

        // konfiguracja
        SmartParamEngine engine = new SmartParamEngine();
        engine.setInvokerProvider(ip);

        // test
        try {
            engine.invokeFunction(f, 13);
            fail();
        } catch (SmartParamException e) {
            assertEquals(SmartParamErrorCode.FUNCTION_INVOKE_ERROR, e.getErrorCode());
        }
    }

    @Test
    public void testIsDebug() {

        // zaleznosci
        Logger logger1 = mock(Logger.class); // info
        Logger logger2 = mock(Logger.class); // debug

        // konfiguracja zaleznosci
        when(logger1.isDebugEnabled()).thenReturn(false);
        when(logger2.isDebugEnabled()).thenReturn(true);

        // testowany obiekt
        SmartParamEngine engine = new SmartParamEngine();

        // test 1
        engine.setLogger(logger1);
        assertFalse(engine.isDebug());

        // test 2
        engine.setLogger(logger2);
        assertTrue(engine.isDebug());
    }

    @Test
    public void testGetPreparedParameter() {

        // zaleznosci
        ParamPreparer paramProvider = mock(ParamPreparer.class);

        // konfiguracja zaleznosci
        when(paramProvider.getPreparedParameter("par")).thenReturn(null);

        // testowany obiekt
        SmartParamEngine engine = new SmartParamEngine();
        engine.setParamProvider(paramProvider);

        // test
        try {
            engine.getValue("par", new DefaultContext());
            fail();
        } catch (SmartParamException e) {
            assertEquals(SmartParamErrorCode.UNKNOWN_PARAMETER, e.getErrorCode());
        }
    }

    /**
     * rzuca: java.lang.ArithmeticException: / by zero
     */
    public int badMethod(int a) {
        return 1 / (a / a - 1);
    }

    private PreparedParameter pp(PreparedLevel... levels) {
        PreparedParameter pp = new PreparedParameter();
        pp.setLevels(levels);
        return pp;
    }

    private PreparedLevel pl(AbstractType<?> type) {
        return new PreparedLevel(type, false, null, levelCreator);
    }

    private PreparedEntry pe(String value, Function f) {
        PreparedEntry pe = new PreparedEntry();
        pe.setValue(value);
        pe.setFunction(f);
        return pe;
    }

    private void checkArrays(AbstractHolder[] expected, AbstractHolder[] result) {
        assertEquals(expected.length, result.length);
        assertTrue(expected.getClass() == result.getClass());

        for (int i = 0; i < result.length; i++) {
            AbstractHolder e = expected[i];
            AbstractHolder r = result[i];
            assertEquals(e.getValue(), r.getValue());
        }
    }
}
