package org.smartparam.engine.core.assembler;

import org.smartparam.engine.core.assembler.AssemblerMethod;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import org.junit.Test;
import static org.junit.Assert.*;
import org.smartparam.engine.core.context.DefaultContext;

import org.smartparam.engine.core.context.ParamContext;
import org.smartparam.engine.core.exception.ParamException;
import org.smartparam.engine.core.exception.ParamException.ErrorCode;
import org.smartparam.engine.core.type.AbstractHolder;
import org.smartparam.engine.types.integer.IntegerHolder;
import org.smartparam.engine.types.number.NumberHolder;
import org.smartparam.engine.types.string.StringHolder;

/**
 * @author Przemek Hertel
 */
public class AssemblerMethodTest {

    @Test
    public void testConstructor() throws NoSuchMethodException {

        // przygotowanie danych
        Method m1 = loadMethod("str1");
        Method m2 = loadMethod("str2");
        Method m3 = loadMethod("int1");

        // oczekiwane wyniki
        // format: metoda, source, target, passingContext
        Object[][] tests = {
            {m1, IntegerHolder.class, String.class, false},
            {m2, StringHolder.class, String.class, true},
            {m3, IntegerHolder.class, Number.class, false}
        };

        // wykonanie testow
        for (Object[] test : tests) {
            Method m = (Method) test[0];
            Class<?> source = (Class<?>) test[1];
            Class<?> target = (Class<?>) test[2];
            boolean passingContext = (Boolean) test[3];

            // stworzenie assemblera na podstawie metody
            AssemblerMethod asm = new AssemblerMethod(this, m);

            // weryfikacja assemblera
            assertSame(source, asm.getSource());
            assertSame(target, asm.getTarget());
            assertEquals(passingContext, asm.isPassingContext());
            assertTrue(asm.getMethod().isAccessible());
        }
    }

    @Test
    public void testConstructor__illegalArgument() throws NoSuchMethodException {

        // przygotowanie danych
        Method m1 = loadMethod("bad1");
        Method m2 = loadMethod("bad2a");
        Method m3 = loadMethod("bad3");

        // testy
        Method[] methods = {m1, m2, m3};

        // wykonanie testow
        for (Method m : methods) {

            try {
                AssemblerMethod asm = new AssemblerMethod(this, m);
                fail();
            } catch (ParamException e) {
                assertEquals(ErrorCode.ILLEGAL_ASSEMBLER_DEFINITION, e.getErrorCode());
            }
        }
    }

    @Test
    public void testAssemble() {

        // obiekty pomocnicze
        ParamContext ctx1 = new DefaultContext().withResultClass(String.class);
        ParamContext ctx2 = new DefaultContext().withResultClass(BigDecimal.class);
        ParamContext ctx3 = new DefaultContext().withResultClass(LetterType.class);

        // dane testowe
        // format: nazwa metody, argument, kontekst, oczekiwany wynik
        Object[][] tests = {
            {"str1", new IntegerHolder(17L), null, "str:17"},
            {"str2", new StringHolder("AA"), ctx1, "str:AA:java.lang.String"},
            {"str2", new StringHolder("BB"), ctx2, "str:BB:java.math.BigDecimal"},
            {"int1", new IntegerHolder(22L), null, 2200L},
            {"enum1", new StringHolder("A4"), ctx3, LetterType.A4}
        };

        // wykonanie testow
        for (Object[] test : tests) {
            Method m = loadMethod((String) test[0]);
            AbstractHolder arg1 = (AbstractHolder) test[1];
            ParamContext arg2 = (ParamContext) test[2];
            Object expectedResult = test[3];

            // stworzenie assemblera
            AssemblerMethod asm = new AssemblerMethod(this, m);

            // uruchomienie assemblera
            Object result = asm.assemble(arg1, arg2);

            // weryfikacja wyniku
            assertEquals(expectedResult, result);
        }
    }

    @Test
    public void testAssemble__exception() {

        // zaleznosci
        Method m1 = loadMethod("div0");         // InvocationTargetException
        Method m2 = loadMethod("privMethod");   // IllegalAccessException
        Method m3 = loadMethod("str1");         // IllegalArgumentException
        BadOwner badOwner = new BadOwner();

        // assemblery
        AssemblerMethod[] asmArray = {
            new AssemblerMethod(this, m1),
            new AssemblerMethod(this, m2),
            new AssemblerMethod(badOwner, m3)
        };

        // przywracam flage niedostepnosci metodzie
        m2.setAccessible(false);

        // testy
        for (int i = 0; i < asmArray.length; i++) {
            AssemblerMethod asm = asmArray[i];

            try {
                asm.assemble(new IntegerHolder(7L), new DefaultContext());
                fail();
            } catch (ParamException e) {
                assertEquals(ParamException.ErrorCode.ASSEMBLER_INVOKE_ERROR, e.getErrorCode());
            }
        }
    }

    public String str1(IntegerHolder value) {
        return "str:" + value.getString();
    }

    @SuppressWarnings("unused")
    private String str2(StringHolder value, ParamContext ctx) {
        return "str:" + value.getString() + ":" + ctx.getResultClass().getName();
    }

    private static Number int1(IntegerHolder value) {
        return new Long(value.intValue() * 100);
    }

    public static int div0(IntegerHolder value) {
        int a = 1, b = 1;
        return value.intValue() / (a - b);
    }

    private int bad1(Integer a) {
        return 1;
    }

    private int privMethod(IntegerHolder value) {
        return 1;
    }

    public int bad2(NumberHolder value) {
        return value.intValue();
    }

    protected String bad2a(IntegerHolder v1, IntegerHolder v2) {
        return null;
    }

    public String bad3(ParamContext ctx, IntegerHolder v1) {
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T extends Enum<T>> T enum1(StringHolder code, ParamContext ctx) {
        return Enum.valueOf((Class<T>) ctx.getResultClass(), code.getString());
    }

    private Method loadMethod(String name) {
        for (Method m : this.getClass().getDeclaredMethods()) {
            if (m.getName().equals(name)) {
                return m;
            }
        }
        return null;
    }

    private static enum LetterType {

        A3,
        A4,
        A5

    }

    private static class BadOwner {
    }
}
