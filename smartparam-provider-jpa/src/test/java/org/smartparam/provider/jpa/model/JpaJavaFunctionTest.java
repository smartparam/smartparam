package org.smartparam.provider.jpa.model;

import org.smartparam.provider.jpa.model.JpaJavaFunction;
import org.junit.Before;
import org.junit.Test;
import org.smartparam.engine.model.functions.JavaFunction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Przemek Hertel
 */
public class JpaJavaFunctionTest {

    private JpaJavaFunction f;

    @Before
    public void init() {
        f = new JpaJavaFunction();
    }

    @Test
    public void testConstructor() {

        // stworzenie obiektu
        JavaFunction f = new JpaJavaFunction();

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
        JavaFunction f = new JpaJavaFunction(className, methodName);

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
        JavaFunction f = new JpaJavaFunction(clazz, methodName);

        // weryfikacja
        assertEquals(clazz.getName(), f.getClassName());
        assertEquals(methodName, f.getMethodName());
    }

    @Test
    public void testGetImplCode() {
        assertEquals("java", f.getTypeCode());
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
