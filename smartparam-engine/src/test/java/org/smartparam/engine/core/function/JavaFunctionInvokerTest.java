package org.smartparam.engine.core.function;

import org.smartparam.engine.core.function.JavaFunctionInvoker;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.core.context.DefaultContext;
import org.smartparam.engine.core.context.ParamContext;
import org.smartparam.engine.core.exception.ParamDefinitionException;
import org.smartparam.engine.core.exception.ParamException;
import org.smartparam.engine.model.functions.JavaFunction;
import org.smartparam.engine.util.EngineUtil;

/**
 * @author Przemek Hertel
 */
public class JavaFunctionInvokerTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void testLoadMethod() throws Exception {

        // przypadki testowe - tablica przykladowych wywolan metody
        // format: expectedResult, methodName, args...
        Object[][] callCases = {
            {"method1,i", "method1", new Integer(1)},
            {"method1,str,i", "method1", "anystring", 7},
            {"method1,str,d", "method1", "anystring", 7.1},
            {"method1,str,d", "method1", null, 7.1},
            {"method1,str,n", "method1", "str", BigDecimal.ZERO},
            {"method1,str,n", "method1", null, BigDecimal.ZERO},
            {"method2,i", "method2", 17},
            {"method2,n", "method2", new Float(1)},
            {"method2,n", "method2", null}
        };

        // testowany obiekt
        JavaFunctionInvoker inv = new JavaFunctionInvoker();

        // wykonanie testow
        for (Object[] call : callCases) {

            String expectedResult = (String) call[0];
            String methodName = (String) call[1];
            Object[] args = Arrays.copyOfRange(call, 2, call.length);

            // znalezienie metody dla podanych argumentow
            Method m = inv.loadMethod(this.getClass(), methodName, args);
            assertNotNull(m);

            // sprawdzenie, czy zostala zwrocona wlasciwa metoda - kazda metoda zwraca unikalny string
            Object result = m.invoke(this, args);
            assertEquals(expectedResult, result);
        }
    }

    @Test
    public void testLoadMethod__methodNotFound() {

        // przypadki testowe - wywolania, dla ktorych nie ma metody
        // format: methodName, args...
        Object[][] callCases = {
            {"method9", new Integer(1)},
            {"method9", "anystring", 7},
            {"method1", new Long(8)},
            {"method1", "str1", "str2"},
            {"method1", "str", 1, 2},
            {"method1", 1, 2}
        };

        // testowany obiekt
        JavaFunctionInvoker inv = new JavaFunctionInvoker();

        // wykonanie testow
        for (Object[] call : callCases) {

            String methodName = (String) call[0];
            Object[] args = Arrays.copyOfRange(call, 1, call.length);

            try {
                inv.findMethod(this.getClass(), methodName, args);
                fail();
            } catch (ParamException e) {
                //ok
            }
        }
    }

    @Test
    public void testLoadClass() {

        // testowany obiekt
        JavaFunctionInvoker inv = new JavaFunctionInvoker();

        // wykonanie testu
        Class<?> clazz = inv.loadClass("org.smartparam.engine.core.function.JavaFunctionInvokerTest");
        assertSame(this.getClass(), clazz);
    }

    @Test(expected = ParamException.class)
    public void testLoadClass__classNotFound() {

        // testowany obiekt
        JavaFunctionInvoker inv = new JavaFunctionInvoker();

        // wykonanie testu
        inv.loadClass("nonexisting.package.NonExistingClass");
    }

    @Test
    public void testFindMethod() throws Exception {

        // testowany obiekt
        JavaFunctionInvoker inv = new JavaFunctionInvoker();

        // wykonanie testu
        assertTrue(inv.getMethodMap().isEmpty());                       // cache metod jest pusty
        Method m = inv.findMethod(this.getClass(), "method1", 17);      // findMethod zwraca metode i umieszcza ja w cache
        assertEquals("method1,i", m.invoke(this, 17));                  // metoda zwrocona przez findMethod
        assertEquals(1, inv.getMethodMap().size());                     // po zawolaniu findMethod, cache zawiera 1 element
        assertEquals(m, inv.getMethodMap().values().iterator().next()); // ten jedyny element w cache'u to metoda m

        Method m2 = inv.findMethod(this.getClass(), "method1", 9999);   // kolejne pobranie metody, dla innej wartosci argumentu
        assertSame(m, m2);
    }

    @Test
    public void testFindClass() {

        // testowany obiekt
        JavaFunctionInvoker inv = new JavaFunctionInvoker();

        // wykonanie testu
        assertTrue(inv.getClassMap().isEmpty());                        // cache klas jest pusty
        Class<?> clazz = inv.findClass("org.smartparam.engine.core.function.JavaFunctionInvokerTest");
        assertSame(this.getClass(), clazz);
        assertEquals(1, inv.getClassMap().size());
        assertTrue(inv.getClassMap().containsValue(clazz));             // klasa jest juz w cache'u

        Class<?> clazz2 = inv.findClass(this.getClass().getName());
        assertSame(clazz, clazz2);
    }

    @Test
    public void testCreateInstance() {

        // testowany obiekt
        JavaFunctionInvoker inv = new JavaFunctionInvoker();

        // wykonanie testu
        Object obj = inv.createInstance(this.getClass());
        assertEquals(this.getClass(), obj.getClass());
        assertNotSame(this, obj);
    }

    @Test(expected = ParamDefinitionException.class)
    public void testCreateInstance__cannotInstantiate() {

        // testowany obiekt
        JavaFunctionInvoker inv = new JavaFunctionInvoker();

        // wykonanie testu
        inv.createInstance(NoDefaultContstructorClass.class);
    }

    @Test
    public void testFindInstance() {

        // testowany obiekt
        JavaFunctionInvoker inv = new JavaFunctionInvoker();

        // wykonanie testu
        assertEquals(0, inv.getInstanceMap().size());                       // cache instancji pusty
        Object obj = inv.findInstance(DefaultContstructorClass.class);      // pobranie instancji, umieszczenie jej w cache'u
        assertNotNull(obj);
        assertEquals(1, inv.getInstanceMap().size());
        Object obj2 = inv.findInstance(DefaultContstructorClass.class);     // ponowne pobranie instancji, zwraca z cache'a
        assertSame(obj, obj2);
    }

    /**
     * Test invoke dla metody niestatycznej.
     */
    @Test
    public void testInvoke__nonstatic() {
        // testowany obiekt
        JavaFunctionInvoker inv = new JavaFunctionInvoker();

        // wykonanie testu
        Method m = inv.findMethod(this.getClass(), "method3", "AA");
        assertEquals("method3,str:AA", inv.invoke(this, m, "AA"));
    }

    /**
     * Test invoke dla metody statycznej.
     */
    @Test
    public void testInvoke__static() {
        // testowany obiekt
        JavaFunctionInvoker inv = new JavaFunctionInvoker();

        // wykonanie testu
        Method m = inv.findMethod(this.getClass(), "method4", "BB");
        assertEquals("method4,str:BB", inv.invoke(null, m, new Object[]{"BB"}));
    }

    @Test
    public void testInvoke() {

        // zaleznosci
        DefaultContext ctx = new DefaultContext();
        ctx.setLevelValues("X", "Y", "Z");

        // przypadki testowe - tablica przykladowych wywolan metody
        // format: expectedResult, methodName, args...
        Object[][] callCases = {
            {"method1,i", f(this.getClass(), "method1"), 100},
            {"method3,str:B", f(this.getClass(), "method3"), "B"},
            {"method4,str:C", f(this.getClass(), "method4"), "C"},
            {"method4,str:null", f(this.getClass(), "method4"), "null"},
            {"method5,ctx=X", f(this.getClass(), "method5"), ctx},
            {"method6,1,2,3", f(this.getClass(), "method6"), new Integer(1), 2, 3},
            {"abcnull", f(this.getClass(), "method7"), "abc", null}
        };

        // testowany obiekt
        JavaFunctionInvoker inv = new JavaFunctionInvoker();

        // wykonanie testow
        for (Object[] call : callCases) {

            String expectedResult = (String) call[0];
            JavaFunction f = (JavaFunction) call[1];
            Object[] args = Arrays.copyOfRange(call, 2, call.length);

            if (args.length == 1 && args[0] instanceof ParamContext) {
                Object result = inv.invoke(f, (ParamContext) args[0]);
                assertEquals(expectedResult, result);
            } else {
                Object result = inv.invoke(f, args);
                assertEquals(expectedResult, result);
            }
        }

        // weryfikacja cache'a
        assertEquals(1, inv.getInstanceMap().size());
        assertEquals(6, inv.getMethodMap().size());
        assertEquals(1, inv.getClassMap().size());
    }

    @Test
    public void testInvoke__invokeError() {

        // testowany obiekt
        JavaFunctionInvoker inv = new JavaFunctionInvoker();

        // funkcja
        JavaFunction f = f(this.getClass(), "div");

        // test
        try {
            inv.invoke(f, 5, 0);
            fail();
        } catch (ParamException e) {
            assertEquals(ParamException.ErrorCode.FUNCTION_INVOKE_ERROR, e.getErrorCode());
        }
    }

    /**
     * Test porownawczy,
     * potwierdzajacy, ze metoda cache'owana (findMethod)
     * jest szybsza od metody niecache'owanej (loadMethod)
     */
    @Test
    public void testCachePerformance() {

        // testowany obiekt
        JavaFunctionInvoker inv = new JavaFunctionInvoker();

        // 1000-krotne uzycie metody loadMethod (nie uzywa cache'a)
        long t = System.nanoTime();
        for (int i = 0; i < 1000; ++i) {
            inv.loadMethod(this.getClass(), "method1", 1);
        }
        long timeNoCache = System.nanoTime() - t;

        // 1000-krotne uzycie metody findMethod (uzywa cache'a)
        t = System.nanoTime();
        for (int i = 0; i < 1000; ++i) {
            inv.findMethod(this.getClass(), "method1", 1);
        }
        long timeCache = System.nanoTime() - t;

        // metoda findMethod powinna byc szybsza od loadMethod
        logger.info("timeNoCache : {}", timeNoCache);
        logger.info("timeCache   : {}", timeCache);
        assertTrue(timeCache < timeNoCache);
    }

    @Test
    public void testDumpStatistics() {

        // testowany obiekt
        JavaFunctionInvoker inv = new JavaFunctionInvoker();

        // przygotowanie obiektu (wypelnienie cache'y)
        inv.invoke(f(this.getClass(), "method1"), 10);
        inv.invoke(f(this.getClass(), "method1"), "A", 10);
        inv.invoke(f(this.getClass(), "method2"), BigDecimal.ONE);
        inv.invoke(f(EngineUtil.class, "split2"), "X,Y", ',');

        // test
        String dump = inv.dumpStatistics();

        // weryfikacja zawartosci dumpa

        // 1. cache klas
        assertTrue(dump.contains("cached classes (2)"));
        assertTrue(dump.contains("class org.smartparam.engine.core.function.JavaFunctionInvokerTest"));
        assertTrue(dump.contains("class org.smartparam.engine.util.EngineUtil"));

        // 2. cache metod
        assertTrue(dump.contains("cached methods (4)"));
        assertTrue(dump.contains("JavaFunctionInvokerTest.method1(int)"));
        assertTrue(dump.contains("JavaFunctionInvokerTest.method1(java.lang.String,int)"));
        assertTrue(dump.contains("JavaFunctionInvokerTest.method2(java.lang.Number)"));
        assertTrue(dump.contains("EngineUtil.split2(java.lang.String,char)"));

        // 3. cache instancji
        assertTrue(dump.contains("cached instances (1)"));
        assertTrue(dump.contains("class org.smartparam.engine.core.function.JavaFunctionInvokerTest"));
    }

    private JavaFunction f(Class<?> clazz, String methodName) {
        return new JavaFunction(clazz, methodName);
    }

    public String method1(int i) {
        return "method1,i";
    }

    public String method1(String str, int i) {
        return "method1,str,i";
    }

    public static String method1(String str, double d) {
        return "method1,str,d";
    }

    public String method1(String str, Number n) {
        return "method1,str,n";
    }

    public String method2(int i) {
        return "method2,i";
    }

    public static String method2(Number n) {
        return "method2,n";
    }

    public String method3(String str) {
        return "method3,str:" + str;
    }

    public static String method4(String str) {
        return "method4,str:" + str;
    }

    public static String method5(ParamContext ctx) {
        return "method5,ctx=" + (ctx != null ? ctx.getLevelValues()[0] : null);
    }

    public String method6(int a, int b, int c) {
        return "method6," + a + "," + b + "," + c;
    }

    public String method7(String a, String b) {
        return a + b;
    }

    public int div(int a, int b) {
        return a / b;
    }

    /**
     * Przykladowa klasa, ktora nie moze byc uzyta w JavaFunction.
     * Ta klasa <b> nie </b> jest instancjonowalna.
     */
    public static class NoDefaultContstructorClass {

        public NoDefaultContstructorClass(int i) {
        }
    }

    /**
     * Przykladowa klasa, ktora moze byc uzyta w JavaFunction.
     * Ta klasa <b> jest </b> instancjonowalna.
     */
    public static class DefaultContstructorClass {

        public DefaultContstructorClass() {
        }
    }
}
