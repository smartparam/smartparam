package org.smartparam.provider.hibernate.model;

import org.smartparam.provider.hibernate.model.HibernateJavaFunction;
import org.junit.Before;
import org.junit.Test;
import org.smartparam.engine.model.functions.JavaFunction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Przemek Hertel
 */
public class HibernateJavaFunctionTest {

    private HibernateJavaFunction f;

    @Before
    public void init() {
        f = new HibernateJavaFunction();
    }

    @Test
    public void testConstructor() {

        // stworzenie obiektu
        JavaFunction f = new HibernateJavaFunction();

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
        JavaFunction f = new HibernateJavaFunction(className, methodName);

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
        JavaFunction f = new HibernateJavaFunction(clazz, methodName);

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
