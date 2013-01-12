package pl.generali.merkury.param.core.function;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Undefined;
import pl.generali.merkury.param.core.context.DefaultContext;
import pl.generali.merkury.param.core.context.ParamContext;
import pl.generali.merkury.param.core.exception.ParamException;
import pl.generali.merkury.param.model.functions.RhinoFunction;

/**
 * @author Przemek Hertel
 */
public class RhinoFunctionInvokerTest {

    RhinoFunctionInvoker invoker;

    @Before
    public void init() {
        invoker = new RhinoFunctionInvoker();
    }

    @Test
    public void testInternalName() {

        // konfiguracja testu
        RhinoFunction f = new RhinoFunction("a", "return a;");
        f.setId(117);

        // test
        String fname = invoker.internalName(f);

        // sprawdzenie wynikow testu
        assertEquals("F_117", fname);
    }

    @Test
    public void testCreateFullBody() {

        // przypadki testowe
        RhinoFunction[] cases = {
            new RhinoFunction("a,b, c", "return a+b+c;"),
            new RhinoFunction("", "return 7;"),
            new RhinoFunction(null, "return 'aaa';")
        };

        // oczekiwane wyniki
        String[] expected = {
            "function F_0(a,b, c) {return a+b+c;}",
            "function F_0() {return 7;}",
            "function F_0(ctx) {return 'aaa';}",};

        // wykonanie testow
        for (int i = 0; i < cases.length; i++) {
            RhinoFunction f = cases[i];
            String expectedBody = expected[i];

            String fullBody = invoker.createFullBody(f);
            assertEquals(expectedBody, fullBody);
        }
    }

    @Test
    public void testUnwrap() {

        // konfiguracja zaleznosci
        NativeJavaObject njo = mock(NativeJavaObject.class);
        when(njo.unwrap()).thenReturn(BigDecimal.ONE);
        Undefined undef = mock(Undefined.class);

        // przypadki testowe
        Object[] scriptReturned = {
            new Integer(17),
            "text",
            new NativeArray(new Integer[]{1, 2, 3, 4}),
            new NativeArray(new String[]{"a", "b", "cc"}),
            njo,
            undef
        };

        // oczekiwane wyniki
        Object[] expected = {
            new Integer(17),
            "text",
            new Object[]{1, 2, 3, 4},
            new String[]{"a", "b", "cc"},
            BigDecimal.ONE,
            null
        };

        for (int i = 0; i < scriptReturned.length; i++) {
            Object ret = scriptReturned[i];
            Object expectedValue = expected[i];

            Object unwrapped = invoker.unwrap(ret);

            if (expectedValue == null) {
                assertNull(unwrapped);

            } else if (expectedValue.getClass().isArray()) {
                assertTrue(unwrapped instanceof Object[]);
                assertArrayEquals((Object[]) expectedValue, (Object[]) unwrapped);

            } else {
                assertEquals(expectedValue, unwrapped);
            }
        }
    }

    @Test
    public void testInvoke__anyArgs() {

        // przypadki testowe
        RhinoFunction[] functions = {
            rf(1, "a,b", "return a + b;"),
            rf(2, "a,b, c", "return (a + b*c)/2;"),
            rf(3, "", "return 'test';"),
            rf(4, "", "return ['a','b','c'];")
        };

        // argumenty do funkcji
        Object[][] functionsArgs = {
            new Object[]{2, 3},
            new Object[]{2, 3, 4},
            new Object[]{},
            new Object[]{}
        };

        // oczekiwane wyniki
        Object[] expected = {
            new Double(5),
            new Double(7),
            "test",
            new String[]{"a", "b", "c"}
        };

        // 2-krotne wykonanie testow
        for (int k = 0; k < 2; k++) {

            for (int i = 0; i < functions.length; i++) {
                RhinoFunction f = functions[i];
                Object[] args = functionsArgs[i];

                Object result = invoker.invoke(f, args);
                if (result instanceof Object[]) {
                    assertArrayEquals((Object[]) expected[i], (Object[]) result);
                } else {
                    assertEquals(expected[i], result);
                }
            }

        }
    }

    @Test
    public void testInvoke__ctx() {

        // konfiguracja zaleznosci
        String code = "A12345";
        BigDecimal number = new BigDecimal("12345");
        Integer integer = 123;

        // przypadki testowe
        RhinoFunction[] functions = {
            rf(1, null, "return ctx.get('string');"),
            rf(2, null, "return ctx.get('bigdecimal');"),
            rf(3, null, "return ctx.get('int');"),};

        // argumenty do funkcji
        ParamContext[] functionsArgs = {
            new DefaultContext("string", code),
            new DefaultContext().set(number),
            new DefaultContext().set("int", integer)
        };

        // oczekiwane wyniki
        Object[] expected = {
            code,
            number,
            integer
        };

        // wykonanie testow
        for (int i = 0; i < functions.length; i++) {
            RhinoFunction f = functions[i];
            ParamContext ctx = functionsArgs[i];

            Object result = invoker.invoke(f, ctx);
            assertEquals(expected[i], result);                  // zgodne co do wartosci
            assertSame(expected[i], result);                    // dodatkowo (w tym przypadku) zgodnosc co do referencji
        }
    }

    @Test
    public void testGlobalJavaObjects() {

        // przygotowanie zaleznosci
        Map<String, Object> globals = new HashMap<String, Object>();
        globals.put("test", new TestJavaObject());
        globals.put("str", new StringUtils());

        // ustawienie globalnych obiektow
        invoker.setGlobalJavaObjects(globals);

        // wykonanie skryptow uzywajacych global java objects
        assertEquals("M1", invoker.invoke(rf(1, "", "return test.method1();")));                    // wykonuje method1() na obiekcie test
        assertEquals(new Integer(22), invoker.invoke(rf(2, "", "return test.method2();")));
        assertEquals("abcd", invoker.invoke(rf(3, "x", "return str.left(x, 4);"), "abcdefghijkl")); // wykonuje left(x, 4) na obiekcie str
        assertEquals("jkl", invoker.invoke(rf(4, "x", "return str.right(x, 3);"), "abcdefghijkl"));
    }

    @Test
    public void testInvalidate() {

        // obie funkcje maja takie samo ID
        RhinoFunction fa = rf(1, "", "return 'AA';");
        RhinoFunction fb = rf(1, "", "return 'BB';");

        // pierwsze uzycie powoduje umieszczenie fa w cache'u
        assertEquals("AA", invoker.invoke(fa));  // umieszczenie w cache'u pod kluczem: 1
        assertEquals("AA", invoker.invoke(fb));  // pobranie z cacha'e fa spod klucza: 1

        // invalidate czysci cache
        invoker.invalidate(1);
        assertEquals("BB", invoker.invoke(fb));
    }

    @Test
    public void testInvalidate__all() {

        // cache'owane funkcje
        RhinoFunction fa = rf(1, "", "return 'AA';");
        RhinoFunction fb = rf(2, "", "return 'BB';");

        // pierwsze uzycie powoduje umieszczenie fa w cache'u
        assertEquals("AA", invoker.invoke(fa));  // umieszczenie w cache'u pod kluczem: 1
        assertEquals("BB", invoker.invoke(fb));  // umieszczenie w cache'u pod kluczem: 2

        // zmiana tresci funkcji fa
        fa = rf(1, "", "return 'CC';");
        assertEquals("AA", invoker.invoke(fa));  // w cache'u jest nadal kompilat poprzedniej wersji fa

        // invalidate czysci cache
        invoker.invalidate();

        // weryfikacja
        assertEquals("CC", invoker.invoke(fa));
    }

    @Test
    public void invoke__error() {

        // przykladowe funkcje, ktore sie nie skompiluja lub wykonaja z wyjatkiem
        RhinoFunction[] badFunctions = {
            rf(1, "", "bad statement"),
            rf(2, "", "if (a==2) {{}"),
            rf(2, "", "return xx.unknownMethod();")
        };

        // wykonanie testow
        for (int i = 0; i < badFunctions.length; i++) {
            RhinoFunction f = badFunctions[i];
            try {
                invoker.invoke(f);
            } catch (ParamException e) {
                assertEquals(ParamException.ErrorCode.FUNCTION_INVOKE_ERROR, e.getErrorCode());
            }
        }
    }

    private RhinoFunction rf(int id, String args, String body) {
        RhinoFunction rf = new RhinoFunction(args, body);
        rf.setId(id);
        return rf;
    }

    public class TestJavaObject {

        public String method1() {
            return "M1";
        }

        public int method2() {
            return 22;
        }
    }
}
