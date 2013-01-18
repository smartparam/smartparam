package org.smartparam.engine.model.functions;

import org.smartparam.engine.model.functions.JavaFunction;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 * @author Przemek Hertel
 */
public class JavaFunctionTest {

    JavaFunction f;

    @Before
    public void init() {
        f = new JavaFunction();
    }

    @Test
    public void testConstructor() {

        // stworzenie obiektu
        JavaFunction f = new JavaFunction();

        // weryfikacja
        assertNull(f.getClassName());
        assertNull(f.getMethodName());
    }

    @Test
    public void testConstructor2() {

        // przykladowe dane
        String className = "org.smartparam.engine.model.functions.JavaFunctionTest";
        String methodName = "testConstructor2";

        // stworzenie obiektu
        JavaFunction f = new JavaFunction(className, methodName);

        // weryfikacja
        assertEquals(className, f.getClassName());
        assertEquals(methodName, f.getMethodName());
    }

    @Test
    public void testConstructor3() {

        // przykladowe dane
        Class<?> clazz = this.getClass();
        String methodName = "testConstructor3";

        // stworzenie obiektu
        JavaFunction f = new JavaFunction(clazz, methodName);

        // weryfikacja
        assertEquals(clazz.getName(), f.getClassName());
        assertEquals(methodName, f.getMethodName());
    }

    @Test
    public void testGetImplCode() {
        assertEquals("java", f.getImplCode());
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
        f.setClassName("pl.generali.merkury.TestClass");
        f.setMethodName("testMethod");

        // oczekiwany wynik
        String expected = "JavaFunction#17[class=pl.generali.merkury.TestClass, method=testMethod]";

        // weryfikacja
        assertEquals(expected, f.toString());
    }
}
