package org.smartparam.engine.model.functions;

import org.smartparam.engine.model.functions.SpringFunction;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 * @author Przemek Hertel
 */
public class SpringFunctionTest {

    SpringFunction f;

    @Before
    public void init() {
        f = new SpringFunction();
    }

    @Test
    public void testConstructor() {

        // stworzenie obiektu
        SpringFunction sf = new SpringFunction();

        // weryfikacja
        assertNull(sf.getBeanName());
        assertNull(sf.getMethodName());
    }

    @Test
    public void testConstructor2() {

        // przykladowe dane
        String beanName = "hhPremiumCalculator";
        String methodName = "calc";

        // stworzenie obiektu
        SpringFunction sf = new SpringFunction(beanName, methodName);

        // weryfikacja
        assertEquals(beanName, sf.getBeanName());
        assertEquals(methodName, sf.getMethodName());
    }

    @Test
    public void testGetImplCode() {
        assertEquals("spring", f.getImplCode());
    }

    @Test
    public void testGetId() {

        // inicjalizacja zerem
        assertEquals(0, f.getId());

        // ustawienie id
        f.setId(9);

        // weryfikacja
        assertEquals(9, f.getId());
    }

    @Test
    public void testToString() {

        // inicjalizacja obiektu
        f.setId(17);
        f.setBeanName("hhCalculator");
        f.setMethodName("calc");

        // oczekiwany wynik
        String expected = "SpringFunction#17[bean=hhCalculator, method=calc]";

        // weryfikacja
        assertEquals(expected, f.toString());
    }
}
