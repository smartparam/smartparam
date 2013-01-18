package org.smartparam.engine.core.config;

import org.smartparam.engine.core.config.AssemblerProvider;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.List;
import org.smartparam.engine.core.assembler.Assembler;
import org.smartparam.engine.core.assembler.AssemblerMethod;
import org.smartparam.engine.core.context.ParamContext;
import org.smartparam.engine.core.exception.ParamException;
import org.smartparam.engine.core.type.AbstractHolder;
import org.smartparam.engine.types.integer.IntegerHolder;
import org.smartparam.engine.types.string.StringHolder;
import org.smartparam.engine.util.EngineUtil;

/**
 * @author Przemek Hertel
 */
public class AssemblerProviderTest {

    @Test
    public void testRegisterAssemblerOwner() {

        // przygotowanie danych testowych
        Object owner1 = new AssemblerOwner();
        Object owner2 = new AssemblerOwner2();

        // kolejne testy
        // format: nazwa metody, obiekt ownera, klasa zrodlowa, klasa docelowa, czy uzywa ParamContext
        Object[][] cases = {
            {"complex", owner1, StringHolder.class, ComplexType.class, false},
            {"complex2", owner2, AbstractHolder.class, ComplexType2.class, false},
            {"complexDupl", owner2, StringHolder.class, ComplexType.class, false},
            {"num3", owner2, IntegerHolder.class, BigDecimal.class, false},
            {"obj", owner2, AbstractHolder.class, Object.class, false},
            {"str1", owner1, AbstractHolder.class, String.class, false},
            {"str2", owner1, AbstractHolder.class, char[].class, true}
        };

        // testowany obiekt
        AssemblerProvider ap = new AssemblerProvider();

        // przygotowanie obiektu
        ap.registerAssemblerOwner(owner1);
        ap.registerAssemblerOwner(owner2);

        // wykonanie testow
        List<AssemblerMethod> all = ap.getAssemblers();
        assertEquals(cases.length, all.size());
        sort(all);

        for (int i = 0; i < cases.length; i++) {

            // oczekiwana postac assemblera
            Object[] row = cases[i];
            String expectedMethodName = (String) row[0];
            Object expectedOwner = row[1];
            Class<?> expectedSource = (Class<?>) row[2];
            Class<?> expectedTarget = (Class<?>) row[3];
            boolean expectedPassContext = (Boolean) row[4];

            // assembler
            AssemblerMethod asm = all.get(i);

            // porownanie
            assertEquals(expectedMethodName, asm.getMethod().getName());
            assertSame(expectedOwner, asm.getOwner());
            assertSame(expectedSource, asm.getSource());
            assertSame(expectedTarget, asm.getTarget());
            assertEquals(expectedPassContext, asm.isPassingContext());
        }

        // wypisanie zarejestrowanych assemblerow
        ap.logAssemblers();
    }

    @Test
    public void testLookupAssembler() {

        // przygotowanie danych testowych
        Object owner1 = new AssemblerOwner();
        Object owner2 = new AssemblerOwner2();

        // testowany obiekt
        AssemblerProvider ap = new AssemblerProvider();
        ap.registerAssemblerOwner(owner1);
        ap.registerAssemblerOwner(owner2);

        // kolejne testy
        // format: klasa zrodlowa, klasa docelowa, oczekiwana metoda
        Object[][] cases = {
            {AbstractHolder.class, String.class, "str1"},
            {StringHolder.class, String.class, "str1"},
            {IntegerHolder.class, String.class, "str1"},
            {StringHolder.class, char[].class, "str2"}, // ~source =target
            {StringHolder.class, ComplexType.class, "complex"}, // =source =target
            {IntegerHolder.class, ComplexType2.class, "complex2"}, // ~source =target
            {IntegerHolder.class, ComplexType.class, "complex2"}, // ~source ~target
            {IntegerHolder.class, BigDecimal.class, "num3"}, // =source =target
            {IntegerHolder.class, Number.class, "num3"}, // =source ~target
            {IntegerHolder.class, Long.class, "obj"}, // ~source #target
            {BigDecimal.class, Long.class, null} // ~source #target
        };

        // wykonanie testow
        for (Object[] row : cases) {
            Class<?> source = (Class<?>) row[0];
            Class<?> target = (Class<?>) row[1];
            String expectedMethodName = (String) row[2];

            AssemblerMethod asm = ap.lookupAssembler(source, target);

            if (expectedMethodName != null) {

                assertNotNull(asm);
                assertEquals(expectedMethodName, asm.getMethod().getName());

            } else {
                assertNull(asm);
            }
        }
    }

    @Test
    public void testFindAssembler() {

        // przygotowanie danych testowych
        Object owner1 = new AssemblerOwner();

        // testowany obiekt
        AssemblerProvider apObj = new AssemblerProvider();
        apObj.registerAssemblerOwner(owner1);
        AssemblerProvider ap = spy(apObj);

        // wykonanie testu - uzycie cache'a
        ap.findAssembler(IntegerHolder.class, String.class);      // miss -> lookupAssembler
        ap.findAssembler(IntegerHolder.class, String.class);      // hit
        ap.findAssembler(IntegerHolder.class, String.class);      // hit

        // weryfikacja uzycia metod
        verify(ap, times(3)).findAssembler(any(Class.class), any(Class.class));
        verify(ap, times(1)).lookupAssembler(any(Class.class), any(Class.class));

        // wypisanie stanu cache'a
        ap.logAssemblers();
    }

    @Test(expected = ParamException.class)
    public void testFindAssembler__notFound() {

        // testowany obiekt
        AssemblerProvider ap = new AssemblerProvider();

        // wykonanie testu - assembler nie zostanie znaleziony
        ap.findAssembler(StringHolder.class, String.class);
    }

    /**
     * Sortuje assemblery - na potrzeby powtarzalnosci wynikow testu.
     */
    private void sort(List<AssemblerMethod> list) {
        Collections.sort(list, new Comparator<AssemblerMethod>() {

            @Override
            public int compare(AssemblerMethod o1, AssemblerMethod o2) {
                return o1.getMethod().getName().compareToIgnoreCase(o2.getMethod().getName());
            }
        });
    }

    public class AssemblerOwner {

        @Assembler
        String str1(AbstractHolder value) {
            return "[" + value.getString() + "]";
        }

        @Assembler
        ComplexType complex(StringHolder value) {
            String[] ab = EngineUtil.split2(value.getString(), ',');
            int a = Integer.parseInt(ab[0]);
            int b = Integer.parseInt(ab[1]);
            return new ComplexType(a, b);
        }

        @Assembler
        char[] str2(AbstractHolder value, ParamContext ctx) {
            String str = value.toString() + ":" + ctx.getResultClass().getSimpleName();
            char[] array = new char[1024];
            str.getChars(0, str.length(), array, 0);
            return array;
        }
    }

    public class AssemblerOwner2 {

        @Assembler
        ComplexType complexDupl(StringHolder value) {
            int a = Integer.parseInt(value.getString());
            return new ComplexType(a, 0);
        }

        @Assembler
        ComplexType2 complex2(AbstractHolder value) {
            int a = value.intValue();
            return new ComplexType2(a, 0);
        }

        @Assembler
        BigDecimal num3(IntegerHolder value) {
            return new BigDecimal(value.longValue());
        }

        @Assembler
        Object obj(AbstractHolder value) {
            return value.getValue();
        }

        ComplexType nonassemblerMethod(IntegerHolder value) {
            return null;
        }
    }

    class ComplexType {

        int a;

        int b;

        public ComplexType(int a, int b) {
            this.a = a;
            this.b = b;
        }

        public int getA() {
            return a;
        }

        public int getB() {
            return b;
        }
    }

    class ComplexType2 extends ComplexType {

        public ComplexType2(int a, int b) {
            super(a, b);
        }
    }

    @Test
    public void testSignatureKey__equals() {
        // zaleznosci
        Class<?> cls1 = Integer.class;
        Class<?> cls2 = String.class;
        Class<?> cls3 = Long.class;

        AssemblerProvider.SignatureKey key1 = new AssemblerProvider.SignatureKey(cls1, cls2);
        AssemblerProvider.SignatureKey key2 = new AssemblerProvider.SignatureKey(cls1, cls2);
        AssemblerProvider.SignatureKey key3 = new AssemblerProvider.SignatureKey(cls1, cls3);
        AssemblerProvider.SignatureKey key4 = new AssemblerProvider.SignatureKey(cls2, cls3);
        Object key5 = new Object();

        // testy
        assertTrue(key1.equals(key1));
        assertTrue(key1.equals(key2));
        assertTrue(key2.equals(key1));

        assertFalse(key1.equals(key3));
        assertFalse(key3.equals(key1));
        assertFalse(key1.equals(key4));
        assertFalse(key1.equals(key5));
    }
}
